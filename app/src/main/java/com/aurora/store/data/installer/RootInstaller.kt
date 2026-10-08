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
 * SPDX-FileCopyrightText: 2021 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.maxxos.store.data.installer

import android.content.Context
import android.os.Process
import android.util.Log
import com.aurora.extensions.TAG
import com.maxxos.store.AuroraApp
import com.maxxos.store.R
import com.maxxos.store.data.event.InstallerEvent
import com.maxxos.store.data.installer.base.InstallerBase
import com.maxxos.store.data.model.Installer
import com.maxxos.store.data.model.InstallerInfo
import com.maxxos.store.data.room.download.Download
import com.maxxos.store.util.PackageUtil.isSharedLibraryInstalled
import com.topjohnwu.superuser.Shell
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RootInstaller @Inject constructor(
    @ApplicationContext private val context: Context
) : InstallerBase(context) {

    companion object {
        const val PLAY_PACKAGE_NAME = "com.android.vending"

        val installerInfo: InstallerInfo
            get() = InstallerInfo(
                id = 2,
                installer = Installer.ROOT,
                installerPackageNames = listOf(PLAY_PACKAGE_NAME),
                title = R.string.pref_install_mode_root,
                subtitle = R.string.root_installer_subtitle,
                description = R.string.root_installer_desc
            )
    }

    override fun install(download: Download) {
        if (isAlreadyQueued(download.packageName)) {
            Log.i(TAG, "${download.packageName} already queued")
        } else {
            if (Shell.getShell().isRoot) {
                download.sharedLibs.forEach {
                    // Shared library packages cannot be updated
                    if (!isSharedLibraryInstalled(context, it.packageName, it.versionCode)) {
                        xInstall(download.packageName, download.versionCode, it.packageName)
                    }
                }
                xInstall(download.packageName, download.versionCode)
            } else {
                postError(
                    download.packageName,
                    context.getString(R.string.installer_status_failure),
                    context.getString(R.string.installer_root_unavailable)
                )
                Log.e(
                    TAG,
                    " >>>>>>>>>>>>>>>>>>>>>>>>>> NO ROOT ACCESS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                )
            }
        }
    }

    private fun xInstall(packageName: String, versionCode: Long, sharedLibPkgName: String = "") {
        var totalSize = 0

        for (file in getFiles(packageName, versionCode, sharedLibPkgName)) {
            totalSize += file.length().toInt()
        }

        val userId = Process.myUid() / 100_000
        val result: Shell.Result =
            Shell.cmd("pm install-create -i $PLAY_PACKAGE_NAME --user $userId -r -S $totalSize")
                .exec()

        val response = result.out

        val sessionIdPattern = Pattern.compile("(\\d+)")
        val sessionIdMatcher = sessionIdPattern.matcher(response[0])
        val found = sessionIdMatcher.find()

        if (found) {
            val sessionId = sessionIdMatcher.group(1)?.toInt()
            if (Shell.getShell().isRoot && sessionId != null) {
                for (file in getFiles(packageName, versionCode, sharedLibPkgName)) {
                    Shell.cmd(
                        "cat \"${file.absoluteFile}\" | pm install-write -S ${file.length()} $sessionId \"${file.name}\""
                    )
                        .exec()
                }

                val shellResult = Shell.cmd("pm install-commit $sessionId").exec()

                if (shellResult.isSuccess) {
                    // Installation is not yet finished if this is a shared library
                    if (packageName == download?.packageName) onInstallationSuccess()
                } else {
                    removeFromInstallQueue(packageName)
                    AuroraApp.events.send(
                        InstallerEvent.Failed(
                            packageName = packageName,
                            error = parseError(shellResult)
                        )
                    )
                }
            } else {
                removeFromInstallQueue(packageName)
                postError(
                    packageName,
                    context.getString(R.string.installer_status_failure),
                    context.getString(R.string.installer_root_unavailable)
                )
            }
        } else {
            removeFromInstallQueue(packageName)
            postError(
                packageName,
                context.getString(R.string.installer_status_failure),
                context.getString(R.string.installer_status_failure_session)
            )
        }
    }

    private fun parseError(result: Shell.Result): String = result.err.joinToString(separator = "\n")
}
