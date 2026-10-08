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

package com.aurora.store.data.providers

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.data.providers.DeviceInfoProvider
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import java.util.Locale
import java.util.Properties
import javax.inject.Inject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AuthProviderTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var authProvider: AuthProvider

    @Inject
    lateinit var spoofProvider: SpoofProvider

    private lateinit var devices: List<Properties>

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        spoofProvider.removeSpoofLocale()
        spoofProvider.removeSpoofDeviceProperties()
        devices = spoofProvider.availableSpoofDeviceProperties
        assertThat(devices.size).isAtLeast(2)
    }

    @After
    fun tearDown() {
        spoofProvider.removeSpoofLocale()
        spoofProvider.removeSpoofDeviceProperties()
    }

    private fun sessionFor(properties: Properties, locale: Locale = Locale.getDefault()): AuthData =
        AuthData(
            email = "test@aurora",
            locale = locale,
            deviceInfoProvider = DeviceInfoProvider(properties, locale.toString())
        )

    @Test
    fun sessionBuiltForTheActiveSpoofIsAccepted() {
        spoofProvider.setSpoofDeviceProperties(devices.first())

        assertThat(authProvider.matchesActiveSpoof(sessionFor(devices.first()))).isTrue()
    }

    @Test
    fun sessionBuiltForAnotherDeviceIsRejected() {
        spoofProvider.setSpoofDeviceProperties(devices.first())

        assertThat(authProvider.matchesActiveSpoof(sessionFor(devices.last()))).isFalse()
    }

    @Test
    fun sessionBuiltBeforeTheSpoofWasEnabledIsRejected() {
        val native = spoofProvider.deviceProperties
        val session = sessionFor(native)
        assertThat(authProvider.matchesActiveSpoof(session)).isTrue()

        spoofProvider.setSpoofDeviceProperties(devices.first())

        assertThat(authProvider.matchesActiveSpoof(session)).isFalse()
    }

    @Test
    fun sessionBuiltForAnotherLocaleIsRejected() {
        spoofProvider.setSpoofDeviceProperties(devices.first())
        spoofProvider.setSpoofLocale(Locale.JAPAN)

        assertThat(authProvider.matchesActiveSpoof(sessionFor(devices.first()))).isFalse()
        assertThat(
            authProvider.matchesActiveSpoof(sessionFor(devices.first(), Locale.JAPAN))
        ).isTrue()
    }

    @Test
    fun sessionWithoutDeviceInfoIsRejected() {
        assertThat(authProvider.matchesActiveSpoof(AuthData("BOGUS"))).isFalse()
    }
}
