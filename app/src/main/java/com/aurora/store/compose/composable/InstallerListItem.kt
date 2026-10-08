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

import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.maxxos.store.R
import com.maxxos.store.compose.preview.ThemePreviewProvider
import com.maxxos.store.data.installer.SessionInstaller
import com.maxxos.store.data.model.InstallerInfo

@Composable
fun InstallerListItem(
    modifier: Modifier = Modifier,
    installerInfo: InstallerInfo,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val description = stringResource(installerInfo.description)

    AuroraListItem(
        modifier = modifier,
        headline = stringResource(installerInfo.title),
        supporting = stringResource(installerInfo.subtitle),
        // Name the app providing this installer, where several can serve the same one
        tertiary = installerInfo.provider
            ?.let { stringResource(R.string.installer_provider, description, it) }
            ?: description,
        onClick = onClick,
        trailing = { RadioButton(selected = isSelected, onClick = onClick) }
    )
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun InstallerListItemPreview() {
    InstallerListItem(installerInfo = SessionInstaller.installerInfo, isSelected = true)
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun InstallerListItemProviderPreview() {
    InstallerListItem(
        installerInfo = SessionInstaller.installerInfo.copy(provider = "Shevery"),
        isSelected = true
    )
}
