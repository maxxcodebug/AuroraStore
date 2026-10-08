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

package com.aurora.store.viewmodel.spoof

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.aurora.extensions.TAG
import com.aurora.store.data.providers.AuthProvider
import com.aurora.store.data.providers.NativeDeviceInfoProvider
import com.aurora.store.data.providers.SpoofProvider
import com.aurora.store.util.PathUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import java.util.Properties
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SpoofViewModel @Inject constructor(
    private val spoofProvider: SpoofProvider,
    private val authProvider: AuthProvider,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /** Full sign-out, clearing both the account DB rows and the legacy prefs. */
    fun logout() = authProvider.logout()

    val defaultLocale: Locale = Locale.getDefault()
    val defaultProperties = NativeDeviceInfoProvider.getNativeDeviceProperties(context)

    private val _currentLocale = MutableStateFlow(spoofProvider.locale)
    val currentLocale = _currentLocale.asStateFlow()

    private val _availableLocales = MutableStateFlow(spoofProvider.availableSpoofLocales)
    val availableLocales = _availableLocales.asStateFlow()

    private val _currentDevice = MutableStateFlow(spoofProvider.deviceProperties)
    val currentDevice = _currentDevice.asStateFlow()

    private val _availableDevices = MutableStateFlow(spoofProvider.availableSpoofDeviceProperties)
    val availableDevices = _availableDevices.asStateFlow()

    fun onDeviceSelected(properties: Properties) {
        _currentDevice.value = properties

        if (properties == defaultProperties) {
            spoofProvider.removeSpoofDeviceProperties()
        } else {
            spoofProvider.setSpoofDeviceProperties(properties)
        }
    }

    fun onLocaleSelected(locale: Locale) {
        _currentLocale.value = locale

        if (locale == defaultLocale) {
            spoofProvider.removeSpoofLocale()
        } else {
            spoofProvider.setSpoofLocale(locale)
        }
    }

    fun importDeviceSpoof(uri: Uri) {
        try {
            context.contentResolver?.openInputStream(uri)?.use { input ->
                PathUtil.getNewEmptySpoofConfig(context).outputStream().use {
                    input.copyTo(it)
                }
            }
            _availableDevices.value = spoofProvider.availableSpoofDeviceProperties
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to import device config", exception)
        }
    }

    fun exportDeviceSpoof(uri: Uri) {
        try {
            NativeDeviceInfoProvider.getNativeDeviceProperties(context, true)
                .store(context.contentResolver?.openOutputStream(uri), "DEVICE_CONFIG")
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to export device config", exception)
        }
    }
}
