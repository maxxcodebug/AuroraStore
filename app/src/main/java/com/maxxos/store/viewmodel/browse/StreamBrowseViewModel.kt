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
 * SPDX-FileCopyrightText: 2021 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.maxxos.store.viewmodel.browse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aurora.extensions.TAG
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.exceptions.GooglePlayException
import com.aurora.gplayapi.helpers.web.WebStreamHelper
import com.maxxos.store.MaxxStoreApp
import com.maxxos.store.data.PageResult
import com.maxxos.store.data.event.AuthEvent
import com.maxxos.store.data.paging.GenericPagingSource.Companion.manualPager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel(assistedFactory = StreamBrowseViewModel.Factory::class)
class StreamBrowseViewModel @AssistedInject constructor(
    @Assisted private val streamCluster: StreamCluster,
    private val streamHelper: WebStreamHelper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(streamCluster: StreamCluster): StreamBrowseViewModel
    }

    private val _apps = MutableStateFlow<PagingData<App>>(PagingData.empty())
    val apps = _apps.asStateFlow()

    init {
        fetch()
    }

    fun fetch() {
        var nextPageUrl: String = streamCluster.clusterNextPageUrl

        manualPager { page ->
            val items = try {
                when (page) {
                    1 -> streamCluster.clusterAppList

                    else -> {
                        if (nextPageUrl.isNotBlank()) {
                            streamHelper.nextStreamCluster(
                                nextPageUrl.hashCode(),
                                nextPageUrl
                            ).also {
                                nextPageUrl = it.clusterNextPageUrl
                            }.clusterAppList
                        } else {
                            emptyList()
                        }
                    }
                }
            } catch (exception: GooglePlayException.AuthException) {
                Log.w(TAG, "Stream returned ${exception.code}, redirecting to Splash")
                MaxxStoreApp.events.send(AuthEvent.SessionExpired())
                emptyList()
            }
            PageResult(items)
        }.flow.distinctUntilChanged()
            .cachedIn(viewModelScope)
            .onEach { _apps.value = it }
            .launchIn(viewModelScope)
    }
}
