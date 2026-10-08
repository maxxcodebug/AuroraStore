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

package com.maxxos.store.data.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.aurora.extensions.TAG
import com.maxxos.store.data.ExodusRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ExodusTrackerWorker @AssistedInject constructor(
    private val exodusRepository: ExodusRepository,
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = try {
        exodusRepository.syncTrackersIfStale()
        Result.success()
    } catch (exception: Exception) {
        Log.e(TAG, "Failed to sync exodus trackers", exception)
        Result.retry()
    }

    companion object {
        private const val EXODUS_TRACKER_WORKER = "EXODUS_TRACKER_WORKER"

        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val work = OneTimeWorkRequestBuilder<ExodusTrackerWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(EXODUS_TRACKER_WORKER, ExistingWorkPolicy.KEEP, work)
        }
    }
}
