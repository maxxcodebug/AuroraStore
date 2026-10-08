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
 * SPDX-FileCopyrightText: 2025 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.maxxos.store.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.aurora.extensions.TAG
import com.maxxos.store.MaxxStoreApp
import com.maxxos.store.data.helper.DownloadHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadCancelReceiver : BroadcastReceiver() {

    @Inject
    lateinit var downloadHelper: DownloadHelper

    override fun onReceive(context: Context, intent: Intent?) {
        val packageName: String = intent?.getStringExtra("PACKAGE_NAME") ?: ""

        if (packageName.isNotBlank()) {
            Log.d(TAG, "Received cancel download request for $packageName")
            MaxxStoreApp.scope.launch(Dispatchers.IO) {
                downloadHelper.cancelDownload(packageName)
            }
        }
    }
}
