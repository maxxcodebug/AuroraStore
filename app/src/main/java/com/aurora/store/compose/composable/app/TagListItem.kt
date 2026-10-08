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

package com.aurora.store.compose.composable.app

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.aurora.store.R
import com.aurora.store.compose.preview.ThemePreviewProvider

/**
 * Composable to show a tag related to an app
 * @param modifier The modifier to be applied to the composable
 * @param label Label of the tag
 * @param painter Painter to draw the icon
 * @param onClick Callback when this composable is clicked
 */
@Composable
fun TagListItem(
    modifier: Modifier = Modifier,
    label: String,
    painter: Painter,
    onClick: () -> Unit = {}
) {
    FilterChip(
        modifier = modifier,
        onClick = onClick,
        label = { Text(text = label, style = MaterialTheme.typography.bodySmall) },
        leadingIcon = { Icon(painter = painter, contentDescription = label) },
        selected = true
    )
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun TagListItemPreview() {
    TagListItem(
        label = stringResource(R.string.details_free),
        painter = painterResource(R.drawable.ic_paid)
    )
}
