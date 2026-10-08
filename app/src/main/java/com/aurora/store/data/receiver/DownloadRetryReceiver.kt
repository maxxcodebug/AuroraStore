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

package com.aurora.store.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.aurora.extensions.TAG
import com.aurora.store.AuroraApp
import com.aurora.store.data.helper.DownloadHelper
import com.aurora.store.util.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Re-queues a previously failed download/install when the user taps "Retry" on its
 * (grouped) failure notification.
 */
@AndroidEntryPoint
class DownloadRetryReceiver : BroadcastReceiver() {

    @Inject
    lateinit var downloadHelper: DownloadHelper

    override fun onReceive(context: Context, intent: Intent?) {
        val packageName = intent?.getStringExtra(DownloadHelper.PACKAGE_NAME) ?: ""

        if (packageName.isNotBlank()) {
            Log.d(TAG, "Received retry request for $packageName")
            // Clear the failure notification immediately for responsive feedback; the
            // download worker re-posts progress once it runs.
            NotificationUtil.clearAppNotification(context, packageName)
            AuroraApp.scope.launch(Dispatchers.IO) {
                downloadHelper.retryDownload(packageName)
            }
        }
    }
}
