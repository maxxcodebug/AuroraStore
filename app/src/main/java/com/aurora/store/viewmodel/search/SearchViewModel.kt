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
 * SPDX-FileCopyrightText: 2021 Rahul Kumar Patel <whyorean@gmail.com>
 * SPDX-FileCopyrightText: 2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.maxxos.store.viewmodel.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.aurora.extensions.TAG
import com.aurora.extensions.requiresGMS
import com.aurora.gplayapi.SearchSuggestEntry
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.exceptions.GooglePlayException
import com.aurora.gplayapi.helpers.contracts.SearchContract
import com.aurora.gplayapi.helpers.web.WebSearchHelper
import com.maxxos.store.AuroraApp
import com.maxxos.store.data.PageResult
import com.maxxos.store.data.event.AuthEvent
import com.maxxos.store.data.model.SearchFilter
import com.maxxos.store.data.paging.GenericPagingSource.Companion.manualPager
import com.maxxos.store.data.providers.AuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    val authProvider: AuthProvider,
    private val webSearchHelper: WebSearchHelper
) : ViewModel() {

    private val contract: SearchContract
        get() = webSearchHelper

    private val _suggestions = MutableStateFlow<List<SearchSuggestEntry>>(emptyList())
    val suggestions = _suggestions.asStateFlow()

    private val searchFilter = MutableStateFlow(SearchFilter())
    private val _apps = MutableStateFlow<PagingData<App>>(PagingData.empty())
    val apps = combine(searchFilter, _apps) { filter, pagingData ->
        pagingData.filter { app ->
            when {
                filter.noAds && app.containsAds -> false
                filter.isFree && !app.isFree -> false
                filter.noGMS && app.requiresGMS() -> false
                app.rating.average < filter.minRating -> false
                app.installs < filter.minInstalls -> false
                else -> true
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PagingData.empty())

    fun filterResults(filter: SearchFilter) {
        searchFilter.value = filter
    }

    fun search(query: String) {
        var nextBundleUrl: String? = null
        val nextStreamUrls = mutableSetOf<String>()

        fun Collection<StreamCluster>.flatClusters(): List<App> = this.flatMap { streamCluster ->
            if (streamCluster.hasNext()) {
                nextStreamUrls.add(streamCluster.clusterNextPageUrl)
            }
            streamCluster.clusterAppList
        }.distinctBy { app -> app.packageName }

        manualPager { page ->
            val items = try {
                when (page) {
                    1 -> contract.searchResults(query)
                        .also { nextBundleUrl = it.streamNextPageUrl }
                        .streamClusters.values
                        .flatClusters()

                    else -> {
                        when {
                            nextStreamUrls.isNotEmpty() -> {
                                nextStreamUrls.map { nextPageStreamUrl ->
                                    contract.nextStreamCluster(query, nextPageStreamUrl)
                                }.also { nextStreamUrls.clear() }.flatClusters()
                            }

                            !nextBundleUrl.isNullOrBlank() -> {
                                contract.nextStreamBundle(query, nextBundleUrl!!)
                                    .also { nextBundleUrl = it.streamNextPageUrl }
                                    .streamClusters.values
                                    .flatClusters()
                            }

                            else -> emptyList()
                        }
                    }
                }
            } catch (exception: GooglePlayException.AuthException) {
                Log.w(TAG, "Search returned ${exception.code}, redirecting to Splash")
                AuroraApp.events.send(AuthEvent.SessionExpired())
                emptyList()
            }
            PageResult(items)
        }.flow.distinctUntilChanged()
            .cachedIn(viewModelScope)
            .onEach { _apps.value = it }
            .launchIn(viewModelScope)
    }

    fun fetchSuggestions(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _suggestions.value = contract.searchSuggestions(query)
                .filter { it.title.isNotBlank() }
                .take(5)
        }
    }
}
