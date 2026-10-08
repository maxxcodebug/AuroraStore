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

package com.maxxos.store.compose.ui.apps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.helpers.contracts.StreamContract
import com.maxxos.store.HomeStash
import com.maxxos.store.compose.composable.StreamCarousel
import com.maxxos.store.data.model.ViewState
import com.maxxos.store.viewmodel.homestream.StreamViewModel

@Composable
internal fun ForYouContent(
    pageType: Int,
    viewModel: StreamViewModel,
    onAppClick: (App) -> Unit,
    onHeaderClick: (StreamCluster) -> Unit,
    onClusterScrolled: (StreamCluster) -> Unit,
    onScrolledToEnd: () -> Unit
) {
    val category = category(pageType)
    val state by viewModel.liveData.observeAsState()

    LaunchedEffect(category) {
        viewModel.getStreamBundle(category, StreamContract.Type.HOME)
    }

    @Suppress("UNCHECKED_CAST")
    val streamBundle = (state as? ViewState.Success<*>)?.data as? HomeStash
    StreamCarousel(
        modifier = Modifier.fillMaxSize(),
        streamBundle = streamBundle?.get(category),
        onHeaderClick = onHeaderClick,
        onAppClick = onAppClick,
        onClusterScrolled = onClusterScrolled,
        onScrolledToEnd = onScrolledToEnd
    )
}
