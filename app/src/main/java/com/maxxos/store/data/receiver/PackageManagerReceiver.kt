/*
 * SPDX-FileCopyrightText: 2021 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.maxxos.store.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxxos.store.MaxxStoreApp
import com.maxxos.store.data.event.InstallerEvent
import com.maxxos.store.data.installer.AppInstaller
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class PackageManagerReceiver : BroadcastReceiver() {

    @Inject
    lateinit var appInstaller: AppInstaller

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && intent.data != null) {
            val packageName = intent.data!!.encodedSchemeSpecificPart

            when (intent.action) {
                Intent.ACTION_PACKAGE_ADDED -> {
                    MaxxStoreApp.events.send(InstallerEvent.Installed(packageName))
                }

                Intent.ACTION_PACKAGE_REMOVED -> {
                    MaxxStoreApp.events.send(InstallerEvent.Uninstalled(packageName))
                }
            }

            // Clear installation queue
            appInstaller.getPreferredInstaller().removeFromInstallQueue(packageName)
        }
    }
}
