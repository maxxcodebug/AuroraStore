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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aurora.extensions.toast
import com.maxxos.store.R
import com.maxxos.store.compose.composable.MicroG
import com.maxxos.store.compose.composable.ScrollHint
import com.maxxos.store.compose.preview.ThemePreviewProvider
import com.maxxos.store.data.model.PermissionType
import com.maxxos.store.data.providers.PermissionProvider
import com.maxxos.store.viewmodel.onboarding.MicroGUIState
import com.maxxos.store.viewmodel.onboarding.MicroGViewModel

@Composable
fun MicroGPage(
    onMicrogTOSChecked: (Boolean) -> Unit = {},
    viewModel: MicroGViewModel = hiltViewModel()
) {
    ScreenContent(
        uiState = viewModel.uiState,
        onInstall = { viewModel.downloadMicroG() },
        onRetry = { viewModel.retryDownload() },
        onMicrogTOSChecked = onMicrogTOSChecked
    )
}

@Composable
private fun ScreenContent(
    uiState: MicroGUIState,
    onInstall: () -> Unit = {},
    onRetry: () -> Unit = {},
    onMicrogTOSChecked: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
            state = listState
        ) {
            item {
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_medium))
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_title_gsf),
                        style = MaterialTheme.typography.headlineLargeEmphasized,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(R.string.onboarding_title_gsf_desc),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                MicroG(
                    onInstall = {
                        when {
                            PermissionProvider.isGranted(
                                context,
                                PermissionType.INSTALL_UNKNOWN_APPS
                            ) -> {
                                onInstall()
                            }

                            else -> context.toast(R.string.permissions_denied)
                        }
                    },
                    onRetry = onRetry,
                    uiState = uiState,
                    onTOSChecked = onMicrogTOSChecked
                )
            }
        }

        ScrollHint(
            listState = listState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun MicroGPagePreview() {
    ScreenContent(
        uiState = MicroGUIState()
    )
}
