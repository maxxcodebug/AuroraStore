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

package com.maxxos.store.compose.composable.app

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertRangeInfoEquals
import androidx.compose.ui.test.onNodeWithTag
import com.maxxos.store.IsolatedTest
import org.junit.Test

class AnimatedAppIconTest : IsolatedTest() {

    @Test
    fun testAnimatedAppIconNoProgress() {
        setContent {
            AnimatedAppIcon(
                iconUrl = "https://example.com/icon.png",
                inProgress = false
            )
        }

        composeTestRule.onNodeWithTag("progressIndicator")
            .assertIsNotDisplayed()
    }

    @Test
    fun testAnimatedAppIconProgressAt0() {
        setContent {
            AnimatedAppIcon(
                iconUrl = "https://example.com/icon.png",
                progress = 0F,
                inProgress = true
            )
        }

        composeTestRule.onNodeWithTag("progressIndicator")
            .assertIsDisplayed()
            .assertRangeInfoEquals(ProgressBarRangeInfo.Indeterminate)
    }

    @Test
    fun testAnimatedAppIconProgressAt50() {
        setContent {
            AnimatedAppIcon(
                iconUrl = "https://example.com/icon.png",
                progress = 50F,
                inProgress = true
            )
        }

        composeTestRule.onNodeWithTag("progressIndicator")
            .assertIsDisplayed()
            .assertRangeInfoEquals(ProgressBarRangeInfo(0.5F, 0.00F..1.00F))
    }
}
