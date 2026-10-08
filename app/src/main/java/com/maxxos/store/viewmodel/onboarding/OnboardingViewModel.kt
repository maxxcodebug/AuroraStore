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
 * SPDX-FileCopyrightText: 2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.maxxos.store.viewmodel.onboarding

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aurora.Constants.FLAVOUR_HUAWEI
import com.aurora.Constants.PACKAGE_NAME_GMS
import com.aurora.Constants.PACKAGE_NAME_PLAY_STORE
import com.aurora.extensions.TAG
import com.aurora.extensions.areNotificationsEnabled
import com.aurora.extensions.isIgnoringBatteryOptimizations
import com.maxxos.store.MaxxStoreApp
import com.maxxos.store.BuildConfig
import com.maxxos.store.data.event.InstallerEvent
import com.maxxos.store.data.helper.UpdateHelper
import com.maxxos.store.data.model.UpdateMode
import com.maxxos.store.data.providers.BlacklistProvider
import com.maxxos.store.data.work.CacheWorker
import com.maxxos.store.util.FlavouredUtil
import com.maxxos.store.util.PackageUtil
import com.maxxos.store.util.Preferences
import com.maxxos.store.util.Preferences.PREFERENCE_AUTO_DELETE
import com.maxxos.store.util.Preferences.PREFERENCE_DEFAULT_SELECTED_TAB
import com.maxxos.store.util.Preferences.PREFERENCE_DISPENSER_URLS
import com.maxxos.store.util.Preferences.PREFERENCE_FILTER_AURORA_ONLY
import com.maxxos.store.util.Preferences.PREFERENCE_FILTER_FDROID
import com.maxxos.store.util.Preferences.PREFERENCE_FOR_YOU
import com.maxxos.store.util.Preferences.PREFERENCE_INSTALLER_ID
import com.maxxos.store.util.Preferences.PREFERENCE_INTRO
import com.maxxos.store.util.Preferences.PREFERENCE_THEME_STYLE
import com.maxxos.store.util.Preferences.PREFERENCE_UPDATES_AUTO
import com.maxxos.store.util.Preferences.PREFERENCE_UPDATES_CHECK_INTERVAL
import com.maxxos.store.util.Preferences.PREFERENCE_UPDATES_EXTENDED
import com.maxxos.store.util.Preferences.PREFERENCE_VENDING_VERSION
import com.maxxos.store.util.save
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class OnboardingUiState(
    val isMicroBundleChecked: Boolean = false,
    val isMicroGBundleInstalled: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    val updateHelper: UpdateHelper,
    val blacklistProvider: BlacklistProvider,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val isMicroGPromptRequired = FlavouredUtil.promptMicroGInstall(context)

    var uiState by mutableStateOf(OnboardingUiState())
        private set

    init {
        MaxxStoreApp.events.installerEvent.onEach {
            when (it) {
                is InstallerEvent.Installed -> confirmBundleInstall()
                else -> {}
            }
        }.launchIn(MaxxStoreApp.scope)
    }

    fun onMicrogTOSChecked(value: Boolean) {
        uiState = uiState.copy(isMicroBundleChecked = value)
    }

    fun finishOnboarding() {
        Log.i(TAG, "Finishing onboarding with defaults")
        context.saveDefaultPreferences()

        if (BuildConfig.FLAVOR == FLAVOUR_HUAWEI) {
            blacklistProvider.blacklist(PACKAGE_NAME_GMS)
            blacklistProvider.blacklist(PACKAGE_NAME_PLAY_STORE)
        }

        setupAutoUpdates()
        CacheWorker.scheduleAutomatedCacheCleanup(context)
        Preferences.putBooleanNow(context, PREFERENCE_INTRO, true)

        // Restart the app to ensure all permissions are granted
        ProcessPhoenix.triggerRebirth(context)
    }

    private fun confirmBundleInstall() {
        if (PackageUtil.isMicroGBundleInstalled(context)) {
            uiState = uiState.copy(isMicroGBundleInstalled = true)
        }
    }

    private fun setupAutoUpdates() {
        val updateMode = when {
            context.isIgnoringBatteryOptimizations() -> UpdateMode.CHECK_AND_INSTALL
            context.areNotificationsEnabled() -> UpdateMode.CHECK_AND_NOTIFY
            else -> UpdateMode.DISABLED
        }

        context.save(PREFERENCE_UPDATES_AUTO, updateMode.ordinal)
        context.save(PREFERENCE_UPDATES_CHECK_INTERVAL, 3)
        updateHelper.scheduleAutomatedCheck()
    }

    private fun Context.saveDefaultPreferences() {
        /*Filters*/
        save(PREFERENCE_FILTER_AURORA_ONLY, false)
        save(PREFERENCE_FILTER_FDROID, true)

        /*Network*/
        save(PREFERENCE_DISPENSER_URLS, FlavouredUtil.defaultDispensers)
        save(PREFERENCE_VENDING_VERSION, 0)

        /*Customization*/
        save(PREFERENCE_THEME_STYLE, 0)
        save(PREFERENCE_DEFAULT_SELECTED_TAB, 0)
        save(PREFERENCE_FOR_YOU, true)

        /*Installer*/
        save(PREFERENCE_AUTO_DELETE, true)
        save(PREFERENCE_INSTALLER_ID, 0)

        /*Updates*/
        save(PREFERENCE_UPDATES_EXTENDED, false)
    }
}
