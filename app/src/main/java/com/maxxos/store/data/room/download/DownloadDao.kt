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

package com.maxxos.store.data.room.download

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.aurora.gplayapi.data.models.PlayFile
import com.maxxos.store.data.model.DownloadStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(download: Download)

    @Query("UPDATE download SET downloadStatus=:downloadStatus WHERE packageName=:packageName")
    suspend fun updateStatus(packageName: String, downloadStatus: DownloadStatus)

    @Query("UPDATE download SET fileList=:fileList WHERE packageName=:packageName")
    suspend fun updateFiles(packageName: String, fileList: List<PlayFile>)

    @Query("UPDATE download SET sharedLibs=:sharedLibs WHERE packageName=:packageName")
    suspend fun updateSharedLibs(packageName: String, sharedLibs: List<SharedLib>)

    @Query(
        """
        UPDATE download
        SET progress=:progress, speed=:speed, timeRemaining=:timeRemaining
        WHERE packageName=:packageName
        """
    )
    suspend fun updateProgress(packageName: String, progress: Int, speed: Long, timeRemaining: Long)

    @Query("SELECT * FROM download")
    fun downloads(): Flow<List<Download>>

    @Query(
        """
        SELECT * FROM download
        WHERE downloadStatus = 'AWAITING_INSTALL'
        ORDER BY downloadedAt DESC
        """
    )
    fun pendingInstalls(): Flow<List<Download>>

    @RawQuery(observedEntities = [Download::class])
    fun pagedDownloads(query: SupportSQLiteQuery): PagingSource<Int, Download>

    @Query("SELECT * FROM download WHERE packageName = :packageName")
    suspend fun getDownload(packageName: String): Download

    @Query("DELETE FROM download WHERE packageName = :packageName")
    suspend fun delete(packageName: String)

    @Query("DELETE FROM download")
    suspend fun deleteAll()
}
