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

package com.maxxos.store.data.room.exodus

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trackers: List<TrackerEntity>)

    @Query("DELETE FROM exodus_tracker")
    suspend fun deleteAll()

    @Query("SELECT * FROM exodus_tracker WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Int>): List<TrackerEntity>

    @Query("SELECT COUNT(*) FROM exodus_tracker")
    suspend fun count(): Int

    @Transaction
    suspend fun purgeAndInsert(trackers: List<TrackerEntity>) {
        deleteAll()
        insertAll(trackers)
    }
}
