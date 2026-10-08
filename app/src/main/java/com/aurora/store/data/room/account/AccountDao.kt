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

package com.aurora.store.data.room.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aurora.store.data.model.AccountType
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(account: Account)

    @Query("SELECT * FROM account ORDER BY addedAt ASC")
    fun accounts(): Flow<List<Account>>

    @Query("SELECT * FROM account ORDER BY addedAt ASC")
    suspend fun getAll(): List<Account>

    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getById(id: String): Account?

    @Query("SELECT * FROM account WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefault(): Account?

    @Query("SELECT * FROM account WHERE isDefault = 1 LIMIT 1")
    fun observeDefault(): Flow<Account?>

    @Query("SELECT COUNT(*) FROM account WHERE type = :type")
    suspend fun countByType(type: AccountType): Int

    @Query("DELETE FROM account WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE account SET isDefault = (id = :id)")
    suspend fun setDefaultOnly(id: String)

    @Transaction
    suspend fun replaceDefault(id: String) = setDefaultOnly(id)

    /**
     * Deletes [id] and, only if it was the default, promotes a new default (anonymous preferred,
     * otherwise the oldest remaining account). Runs in one transaction so observers never see a
     * transient null default.
     */
    @Transaction
    suspend fun deleteAndEnsureDefault(id: String) {
        val wasDefault = getById(id)?.isDefault == true
        delete(id)
        if (wasDefault) {
            val remaining = getAll()
            val next = remaining.firstOrNull { it.isAnonymous } ?: remaining.firstOrNull()
            next?.let { setDefaultOnly(it.id) }
        }
    }
}
