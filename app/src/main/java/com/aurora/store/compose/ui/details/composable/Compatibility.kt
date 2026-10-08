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

package com.maxxos.store.compose.ui.details.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.maxxos.store.R
import com.maxxos.store.compose.composable.Info
import com.maxxos.store.compose.composable.SectionHeader
import com.maxxos.store.compose.preview.ThemePreviewProvider
import com.maxxos.store.compose.theme.successColor
import com.maxxos.store.compose.theme.warningColor
import com.maxxos.store.data.model.Scores

/**
 * Composable to display app compatibility rating from Plexus, supposed to be used as a part
 * of the Column with proper vertical arrangement spacing in the AppDetailsScreen.
 * @param needsGms Whether this app needs Google Play Services to work properly
 * @param plexusScores Scores from the Plexus
 */
@Composable
fun Compatibility(needsGms: Boolean, plexusScores: Scores? = null) {
    SectionHeader(
        title = stringResource(R.string.details_compatibility_title),
        subtitle = stringResource(R.string.plexus_powered)
    )

    if (!needsGms) {
        Info(
            painter = painterResource(R.drawable.ic_menu_about),
            titleColor = successColor,
            title = AnnotatedString(
                text = stringResource(R.string.details_compatibility_gms_not_required_title)
            ),
            description = AnnotatedString(
                text = stringResource(R.string.details_compatibility_gms_not_required_subtitle)
            )
        )

        // Nothing more to show
        return
    }

    Info(
        painter = painterResource(R.drawable.ic_menu_about),
        titleColor = warningColor,
        title = AnnotatedString(
            text = stringResource(R.string.details_compatibility_gms_required_title)
        ),
        description = AnnotatedString(
            text = stringResource(R.string.details_compatibility_gms_required_subtitle)
        )
    )

    val scoresStatus = mapOf(
        R.string.details_compatibility_no_gms to plexusScores?.aosp?.status,
        R.string.details_compatibility_microg to plexusScores?.microG?.status
    )
    scoresStatus.forEach { (title, description) ->
        Info(
            painter = painterResource(R.drawable.ic_android),
            title = AnnotatedString(text = stringResource(title)),
            description = AnnotatedString(
                text = stringResource(description ?: R.string.details_compatibility_status_unknown)
            )
        )
    }
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun CompatibilityPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Compatibility(needsGms = true)
    }
}
