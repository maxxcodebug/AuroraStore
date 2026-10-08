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

package com.aurora.store.viewmodel.topchart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurora.gplayapi.data.models.StreamCluster
import com.aurora.gplayapi.helpers.contracts.TopChartsContract
import com.aurora.gplayapi.helpers.web.WebTopChartsHelper
import com.aurora.store.TopChartStash
import com.aurora.store.data.model.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TopChartViewModel @Inject constructor(
    private val webTopChartsHelper: WebTopChartsHelper
) : ViewModel() {

    private var stash: TopChartStash = mutableMapOf()

    private val _state = MutableStateFlow<ViewState>(ViewState.Loading)
    val state: StateFlow<ViewState> = _state.asStateFlow()

    private val topChartsContract: TopChartsContract
        get() = webTopChartsHelper

    fun getStreamCluster(type: TopChartsContract.Type, chart: TopChartsContract.Chart) {
        viewModelScope.launch(Dispatchers.IO) {
            if (targetCluster(type, chart).clusterAppList.isNotEmpty()) {
                _state.value = ViewState.Success(targetCluster(type, chart))
                return@launch
            }

            _state.value = ViewState.Loading

            try {
                val cluster = topChartsContract.getCluster(type.value, chart.value)
                updateCluster(type, chart, cluster)
                _state.value = ViewState.Success(targetCluster(type, chart))
            } catch (e: Exception) {
                _state.value = ViewState.Error(e.message)
            }
        }
    }

    fun nextCluster(type: TopChartsContract.Type, chart: TopChartsContract.Chart) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val target = targetCluster(type, chart)
                if (target.hasNext()) {
                    val newCluster = topChartsContract.getNextStreamCluster(
                        target.id,
                        target.clusterNextPageUrl
                    )

                    updateCluster(type, chart, newCluster)

                    _state.value = ViewState.Success(targetCluster(type, chart))
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun updateCluster(
        type: TopChartsContract.Type,
        chart: TopChartsContract.Chart,
        newCluster: StreamCluster
    ) {
        val streamCluster = targetCluster(type, chart)
        val mergedCluster = streamCluster.copy(
            clusterNextPageUrl = newCluster.clusterNextPageUrl,
            clusterAppList = streamCluster.clusterAppList + newCluster.clusterAppList
        )

        stash[type]?.set(chart, mergedCluster)
    }

    private fun targetCluster(
        type: TopChartsContract.Type,
        chart: TopChartsContract.Chart
    ): StreamCluster {
        val cluster = stash
            .getOrPut(type) { mutableMapOf() }
            .getOrPut(chart) { StreamCluster.EMPTY }
        return cluster
    }
}
