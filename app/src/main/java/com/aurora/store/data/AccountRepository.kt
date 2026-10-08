// Copyright (C) 2026 MaxxOS. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
/*
 * SPDX-FileCopyrightText: 2026 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.store.data

import com.aurora.store.data.room.account.Account
import com.aurora.store.data.room.account.AccountDao
import com.aurora.store.data.room.account.AppAccountBinding
import com.aurora.store.data.room.account.AppAccountBindingDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class AccountRepository @Inject constructor(
    private val accountDao: AccountDao,
    private val bindingDao: AppAccountBindingDao
) {
    val accounts: Flow<List<Account>> = accountDao.accounts()
    val defaultAccount: Flow<Account?> = accountDao.observeDefault()

    suspend fun getAll(): List<Account> = accountDao.getAll()

    suspend fun getDefault(): Account? = accountDao.getDefault()

    suspend fun getById(id: String): Account? = accountDao.getById(id)

    /** Inserts/updates an account. Enforces the single-anonymous invariant. */
    suspend fun upsertAccount(account: Account) {
        if (account.isAnonymous) {
            accountDao.upsert(account.copy(id = Account.ANONYMOUS_ID))
        } else {
            accountDao.upsert(account)
        }
    }

    suspend fun setDefault(id: String) = accountDao.replaceDefault(id)

    /**
     * Removes an account and its bindings. If it was the default, promotes a new default
     * (anonymous preferred, else the oldest remaining). The default promotion is atomic; the
     * binding cleanup runs first as a separate statement (FK cascade is not enabled).
     */
    suspend fun removeAccount(id: String) {
        // SQLite only cascades FK deletes when `PRAGMA foreign_keys = ON`, which this project
        // does not enable, so this explicit delete is the actual enforcer of the binding cleanup.
        bindingDao.deleteByAccountId(id)
        accountDao.deleteAndEnsureDefault(id)
    }

    /**
     * The account id to use for [packageName]: its binding if one exists, otherwise the current
     * default account's id. Falls back to [Account.ANONYMOUS_ID] when no default is set yet
     * (e.g. immediately after migration before login) — callers must treat this as "use the
     * anonymous account", and the anonymous account is expected to exist by the time a download
     * runs.
     */
    suspend fun resolveAccountId(packageName: String): String {
        bindingDao.getForPackage(packageName)?.let { return it.accountId }
        return accountDao.getDefault()?.id ?: Account.ANONYMOUS_ID
    }

    /**
     * Records the account an app was installed with. Binding to the current default clears any
     * existing binding (default-account installs are never tagged).
     */
    suspend fun bindApp(packageName: String, accountId: String) {
        val defaultId = accountDao.getDefault()?.id
        if (accountId == defaultId) {
            bindingDao.delete(packageName)
        } else {
            bindingDao.upsert(AppAccountBinding(packageName, accountId))
        }
    }

    suspend fun unbindApp(packageName: String) = bindingDao.delete(packageName)
}
