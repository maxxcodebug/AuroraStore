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

package com.maxxos.store.compose.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.maxxos.store.R
import com.maxxos.store.compose.preview.ThemePreviewProvider

/**
 * Pager indicator
 * Adapted from [Pager in Compose](https://developer.android.com/jetpack/compose/layouts/pager#add-page)
 * @param modifier The modifier to be applied to the composable
 * @param totalPages Total number of pages
 * @param currentPage Currently displayed page number
 */
@Composable
fun PageIndicator(modifier: Modifier = Modifier, totalPages: Int, currentPage: Int = 0) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.spacing_xsmall),
            Alignment.CenterHorizontally
        )
    ) {
        val selectedColor = MaterialTheme.colorScheme.primary
        val unselectedColor = MaterialTheme.colorScheme.outlineVariant
        repeat(totalPages) { iteration ->
            val page = stringResource(R.string.page, iteration)
            val isSelected = currentPage == iteration
            val color by animateColorAsState(
                targetValue = if (isSelected) selectedColor else unselectedColor,
                animationSpec = tween()
            )
            val size by animateDpAsState(
                targetValue = if (isSelected) {
                    dimensionResource(R.dimen.radius_medium)
                } else {
                    dimensionResource(R.dimen.radius_small)
                },
                animationSpec = tween()
            )

            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color = color)
                    .semantics { stateDescription = page }
            )
        }
    }
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun PageIndicatorPreview() {
    PageIndicator(totalPages = 5)
}
