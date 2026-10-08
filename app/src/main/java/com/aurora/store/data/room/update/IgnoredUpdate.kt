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

package com.maxxos.store.data.room.update

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Marks an update as ignored by the user.
 *
 * @property packageName Package whose updates are being ignored.
 * @property ignoredVersionCode When `null`, all future updates for this package are hidden.
 *   When set, only that specific version is hidden — a newer version arriving will show up
 *   in the Updates list again.
 */
@Entity(tableName = "ignored_update")
data class IgnoredUpdate(
    @PrimaryKey
    val packageName: String,
    val ignoredVersionCode: Long? = null
) {
    fun hides(update: Update): Boolean =
        ignoredVersionCode == null || ignoredVersionCode == update.versionCode
}

/**
 * Whether [rules], keyed by package name, mute this update. Every path that surfaces an
 * update — list, notification or auto-install — must go through this, or an update the
 * user ignored comes back on one of the paths that skipped the check.
 */
fun Update.isIgnoredBy(rules: Map<String, IgnoredUpdate>): Boolean =
    rules[packageName]?.hides(this) == true
