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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aurora.extensions.requiresGMS
import com.aurora.gplayapi.data.models.App
import com.maxxos.store.R
import com.maxxos.store.compose.preview.AppPreviewProvider
import com.maxxos.store.compose.preview.ThemePreviewProvider
import com.maxxos.store.util.CommonUtil

@Composable
fun LargeAppListItem(modifier: Modifier = Modifier, app: App, onClick: () -> Unit = {}) {
    Surface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 4.dp),
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
        tonalElevation = 1.dp
    ) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier.size(58.dp).clip(RoundedCornerShape(17.dp)),
                model = ImageRequest.Builder(LocalContext.current).data(app.iconArtwork.url).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(app.displayName, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(app.developerName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(buildAppExtras(app), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Text("↓", modifier = Modifier.padding(horizontal = 7.dp), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun buildAppExtras(app: App): String = buildList {
    add(if (app.size > 0) CommonUtil.addSiPrefix(app.size) else app.downloadString)
    add("${app.labeledRating}★")
    add(stringResource(if (app.isFree) R.string.details_free else R.string.details_paid))
    add(
        stringResource(
            if (app.containsAds) R.string.details_contains_ads else R.string.details_no_ads
        )
    )
    if (app.requiresGMS()) add(stringResource(R.string.details_gsf_dependent))
}.joinToString(separator = "  •  ")

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
fun LargeAppListItemPreview(@PreviewParameter(AppPreviewProvider::class) app: App) {
    LargeAppListItem(app = app)
}
