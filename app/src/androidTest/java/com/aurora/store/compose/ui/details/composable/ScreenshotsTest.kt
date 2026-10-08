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

package com.maxxos.store.compose.ui.details.composable

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.performSemanticsAction
import com.aurora.gplayapi.data.models.Artwork
import com.maxxos.store.IsolatedTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ScreenshotsTest : IsolatedTest() {

    private val artwork = Artwork(url = "https://example.com/screenshot.png")
    private val duplicateArtwork = Artwork(url = "https://example.com/screenshot.png")
    private val otherArtwork = Artwork(url = "https://example.com/other.png")

    @Test
    fun testDuplicateScreenshotsAreDisplayedOnce() {
        setContent {
            Screenshots(screenshots = listOf(artwork, duplicateArtwork, otherArtwork))
        }

        composeTestRule.onAllNodes(hasClickAction())
            .assertCountEquals(2)
    }

    @Test
    fun testNavigationIndicesMatchDedupedList() {
        val clickedIndices = mutableSetOf<Int>()
        setContent {
            Screenshots(
                screenshots = listOf(artwork, duplicateArtwork, otherArtwork),
                onNavigateToScreenshot = { index -> clickedIndices.add(index) }
            )
        }

        val screenshots = composeTestRule.onAllNodes(hasClickAction())
            .assertCountEquals(2)
        screenshots[0].performSemanticsAction(SemanticsActions.OnClick)
        screenshots[1].performSemanticsAction(SemanticsActions.OnClick)

        assertEquals(setOf(0, 1), clickedIndices)
    }
}
