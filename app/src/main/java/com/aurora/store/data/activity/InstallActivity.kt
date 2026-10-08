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

package com.maxxos.store.data.activity

import android.content.pm.PackageInstaller.SessionCallback
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.aurora.Constants
import com.aurora.extensions.TAG
import com.maxxos.store.data.installer.SessionInstaller
import com.maxxos.store.data.room.download.Download
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InstallActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionInstaller: SessionInstaller

    private lateinit var sessionCallback: SessionCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val download =
            IntentCompat.getParcelableExtra(intent, Constants.PARCEL_DOWNLOAD, Download::class.java)

        if (download != null) {
            sessionCallback = object : SessionCallback() {
                override fun onCreated(sessionId: Int) {}

                override fun onBadgingChanged(sessionId: Int) {}

                override fun onActiveChanged(sessionId: Int, active: Boolean) {}

                override fun onProgressChanged(sessionId: Int, progress: Float) {}

                override fun onFinished(sessionId: Int, success: Boolean) {
                    if (sessionInstaller.currentSessionId == sessionId) {
                        Log.i(TAG, "Install finished with status code: $success")
                        finish()
                    }
                }
            }
            packageManager.packageInstaller.registerSessionCallback(sessionCallback)
            install(download)
        } else {
            Log.e(TAG, "InstallActivity triggered without a valid download, bailing out!")
            finish()
        }
    }

    override fun onDestroy() {
        packageManager.packageInstaller.unregisterSessionCallback(sessionCallback)
        super.onDestroy()
    }

    private fun install(download: Download) {
        try {
            sessionInstaller.install(download)
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to install $packageName")
            finish()
        }
    }
}
