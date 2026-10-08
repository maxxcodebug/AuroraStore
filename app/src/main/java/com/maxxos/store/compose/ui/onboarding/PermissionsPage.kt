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

package com.maxxos.store.compose.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxxos.store.R
import com.maxxos.store.compose.composable.PermissionList
import com.maxxos.store.compose.preview.ThemePreviewProvider
import com.maxxos.store.data.model.Permission
import com.maxxos.store.data.model.PermissionType
import com.maxxos.store.viewmodel.commons.PermissionRationaleViewModel
import kotlin.random.Random

@Composable
fun PermissionsPage(viewModel: PermissionRationaleViewModel = hiltViewModel()) {
    val permissions by viewModel.permissions.collectAsStateWithLifecycle()

    PageContent(
        permissions = permissions,
        onPermissionCallback = { viewModel.refreshPermissionsList() }
    )
}

@Composable
private fun PageContent(
    permissions: List<Permission> = emptyList(),
    onPermissionCallback: (type: PermissionType) -> Unit = {}
) {
    PermissionList(
        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        permissions = permissions,
        onPermissionCallback = onPermissionCallback,
        header = {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.spacing_medium))
            ) {
                Text(
                    text = stringResource(R.string.onboarding_title_permissions),
                    style = MaterialTheme.typography.headlineLargeEmphasized,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.onboarding_permission_select),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun PermissionsPagePreview() {
    val permissions = PermissionType.entries.map { type ->
        Permission(
            type = type,
            title = LoremIpsum(3).values.first(),
            subtitle = LoremIpsum(7).values.first(),
            optional = Random.nextBoolean(),
            isGranted = Random.nextBoolean()
        )
    }
    PageContent(
        permissions = permissions
    )
}
