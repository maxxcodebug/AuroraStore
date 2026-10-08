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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aurora.gplayapi.helpers.contracts.StreamContract
import com.aurora.gplayapi.helpers.contracts.TopChartsContract
import com.maxxos.store.R
import com.maxxos.store.compose.composable.MaxxPill
import com.maxxos.store.compose.navigation.Destination
import com.maxxos.store.util.Preferences
import com.maxxos.store.viewmodel.category.CategoryViewModel
import com.maxxos.store.viewmodel.homestream.StreamViewModel
import com.maxxos.store.viewmodel.topchart.TopChartViewModel
import kotlinx.coroutines.launch

internal fun category(pageType: Int): StreamContract.Category =
    if (pageType == 1) StreamContract.Category.GAME else StreamContract.Category.APPLICATION

private enum class AppsTab(@StringRes val titleRes: Int) {
    FOR_YOU(R.string.tab_for_you),
    TOP_CHARTS(R.string.tab_top_charts),
    CATEGORIES(R.string.tab_categories)
}

@Composable
fun AppsGamesScreen(
    pageType: Int,
    streamViewModel: StreamViewModel = hiltViewModel(key = "stream_$pageType"),
    topChartViewModel: TopChartViewModel = hiltViewModel(key = "topChart_$pageType"),
    categoryViewModel: CategoryViewModel = hiltViewModel(key = "category_$pageType"),
    onNavigateTo: (Destination) -> Unit = {}
) {
    val context = LocalContext.current

    val chartType = if (pageType == 1) {
        TopChartsContract.Type.GAME
    } else {
        TopChartsContract.Type.APPLICATION
    }

    LaunchedEffect(Unit) {
        topChartViewModel.getStreamCluster(chartType, TopChartsContract.Chart.TOP_SELLING_FREE)
    }

    val isForYouEnabled = Preferences.getBoolean(context, Preferences.PREFERENCE_FOR_YOU)
    val tabs = buildList {
        if (isForYouEnabled) add(AppsTab.FOR_YOU)
        add(AppsTab.TOP_CHARTS)
        add(AppsTab.CATEGORIES)
    }
    val pagerState = rememberPagerState { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                MaxxPill(
                    text = stringResource(tab.titleRes),
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            verticalAlignment = Alignment.Top,
            userScrollEnabled = false
        ) { page ->
            when (tabs[page]) {
                AppsTab.FOR_YOU -> ForYouContent(
                    pageType = pageType,
                    viewModel = streamViewModel,
                    onAppClick = { onNavigateTo(Destination.AppDetails(it.packageName)) },
                    onHeaderClick = { onNavigateTo(Destination.StreamBrowse(it)) },
                    onClusterScrolled = { cluster ->
                        streamViewModel.observeCluster(category(pageType), cluster)
                    },
                    onScrolledToEnd = {
                        streamViewModel.observe(category(pageType), StreamContract.Type.HOME)
                    }
                )

                AppsTab.TOP_CHARTS -> TopChartsContent(
                    pageType = pageType,
                    viewModel = topChartViewModel,
                    onAppClick = { onNavigateTo(Destination.AppDetails(it.packageName)) }
                )

                AppsTab.CATEGORIES -> CategoriesContent(
                    pageType = pageType,
                    viewModel = categoryViewModel,
                    onCategoryClick = { onNavigateTo(Destination.CategoryBrowse(it)) }
                )
            }
        }
    }
}
