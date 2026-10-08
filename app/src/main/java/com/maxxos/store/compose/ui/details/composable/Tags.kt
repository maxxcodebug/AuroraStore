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

package com.maxxos.store.compose.ui.details.composable

import android.text.format.Formatter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.aurora.gplayapi.data.models.App
import com.maxxos.store.R
import com.maxxos.store.compose.composable.app.TagListItem
import com.maxxos.store.compose.preview.AppPreviewProvider
import com.maxxos.store.compose.preview.ThemePreviewProvider
import com.maxxos.store.util.CommonUtil

/**
 * Composable to display tags related to the app, supposed to be used as a part
 * of the Column with proper vertical arrangement spacing in the AppDetailsScreen.
 * @param app App to show tags
 */
@Composable
fun Tags(app: App) {
    val context = LocalContext.current

    val installsLabel = CommonUtil.addDiPrefix(app.installs)
    val averageRating = if (app.labeledRating == "0.0" || app.labeledRating.isBlank()) {
        null
    } else {
        app.labeledRating
    }
    val paidLabel = if (app.isFree) {
        stringResource(R.string.details_free)
    } else {
        stringResource(R.string.details_paid)
    }
    val adsLabel = if (app.containsAds) {
        stringResource(R.string.details_contains_ads)
    } else {
        stringResource(R.string.details_no_ads)
    }

    val tags = mapOf(
        averageRating to R.drawable.ic_star,
        installsLabel to R.drawable.ic_download_manager,
        Formatter.formatShortFileSize(context, app.size) to R.drawable.ic_apk_install,
        app.updatedOn to R.drawable.ic_updates,
        paidLabel to R.drawable.ic_paid,
        adsLabel to R.drawable.ic_campaign
    ).filterKeys { it != null }

    LazyRow(
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        items(items = tags.keys.toList()) { label ->
            TagListItem(label = label!!, painter = painterResource(tags.getValue(label)))
        }
    }
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun TagsPreview(@PreviewParameter(AppPreviewProvider::class) app: App) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Tags(app = app)
    }
}
