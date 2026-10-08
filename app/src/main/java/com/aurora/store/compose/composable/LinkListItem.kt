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
 * SPDX-FileCopyrightText: 2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.store.compose.composable

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.aurora.store.R
import com.aurora.store.compose.preview.ThemePreviewProvider
import com.aurora.store.data.model.Link

@Composable
fun LinkListItem(
    modifier: Modifier = Modifier,
    link: Link,
    onClick: () -> Unit = {},
    iconTint: Color? = null
) {
    AuroraListItem(
        modifier = modifier,
        headline = link.title,
        supporting = link.subtitle,
        onClick = onClick,
        leading = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(link.icon)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(dimensionResource(R.dimen.icon_size_default))
                    .clip(CircleShape),
                colorFilter = if (iconTint != null) ColorFilter.tint(color = iconTint) else null
            )
        }
    )
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun LinkListItemPreview() {
    LinkListItem(
        link = Link(
            id = 0,
            title = stringResource(R.string.title_about),
            subtitle = stringResource(R.string.about_aurora_store_subtitle),
            url = "https://auroraoss.com/",
            icon = R.drawable.ic_menu_about
        )
    )
}
