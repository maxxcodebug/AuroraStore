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

package com.aurora.store.data.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.content.IntentCompat
import com.aurora.Constants.PACKAGE_NAME_PLAY_STORE
import com.aurora.extensions.TAG
import com.aurora.store.BuildConfig
import com.aurora.store.data.installer.MicroGInstaller.Companion.buildMicroGInstallIntent
import java.io.File

class MicroGInstallerActivity : Activity() {

    companion object {
        private const val REQUEST_CODE = 1001
        const val EXTRA_FILES = "extra_files"
        const val EXTRA_PACKAGE_NAME = "extra_package_name"

        fun launch(context: Context, packageName: String, files: List<File>) {
            val uris = files.map { file ->
                val uri = FileProvider.getUriForFile(
                    context,
                    "${BuildConfig.APPLICATION_ID}.fileProvider",
                    file
                )

                context.grantUriPermission(
                    PACKAGE_NAME_PLAY_STORE,
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                uri
            }

            val intent = Intent(context, MicroGInstallerActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(EXTRA_PACKAGE_NAME, packageName)
                putExtra(EXTRA_FILES, ArrayList(uris))
            }

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val files: ArrayList<Uri>? =
            IntentCompat.getParcelableArrayListExtra(intent, EXTRA_FILES, Uri::class.java)

        if (files.isNullOrEmpty()) {
            Log.e(TAG, "No files provided, cannot proceed with MicroG installation")
            return finish()
        }

        startActivityForResult(
            buildMicroGInstallIntent(files),
            REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // TODO: Handle result if needed
        finish()
    }
}
