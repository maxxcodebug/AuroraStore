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

package com.maxxos.store.data.providers

import androidx.test.ext.junit.runners.AndroidJUnit4
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
class SpoofProviderTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var spoofProvider: SpoofProvider

    @Before
    fun setup() {
        hiltAndroidRule.inject()
    }

    @After
    fun tearDown() {
        spoofProvider.removeSpoofLocale()
        spoofProvider.removeSpoofDeviceProperties()
    }

    @Test
    fun testSpoofingDeviceLocale() {
        assertThat(spoofProvider.isLocaleSpoofEnabled).isFalse()

        spoofProvider.setSpoofLocale(Locale.JAPAN)
        assertThat(spoofProvider.isLocaleSpoofEnabled).isTrue()
        assertThat(spoofProvider.locale == Locale.JAPAN).isTrue()
    }

    @Test
    fun testSpoofingDeviceProperties() {
        assertThat(spoofProvider.isDeviceSpoofEnabled).isFalse()

        val properties = Properties().apply {
            setProperty("UserReadableName", "Test")
        }
        spoofProvider.setSpoofDeviceProperties(properties)
        assertThat(spoofProvider.isDeviceSpoofEnabled).isTrue()
        assertThat(spoofProvider.deviceProperties == properties).isTrue()
    }
}
