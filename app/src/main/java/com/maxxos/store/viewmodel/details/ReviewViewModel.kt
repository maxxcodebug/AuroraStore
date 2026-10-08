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

package com.maxxos.store.viewmodel.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aurora.extensions.TAG
import com.aurora.gplayapi.data.models.Review
import com.aurora.gplayapi.exceptions.GooglePlayException
import com.aurora.gplayapi.helpers.ReviewsHelper
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

@HiltViewModel(assistedFactory = ReviewViewModel.Factory::class)
class ReviewViewModel @AssistedInject constructor(
    @Assisted private val packageName: String,
    private val reviewsHelper: ReviewsHelper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(packageName: String): ReviewViewModel
    }

    private val _reviews = MutableStateFlow<PagingData<Review>>(PagingData.Companion.empty())
    val reviews = _reviews.asStateFlow()

    init {
        fetchReviews()
    }

    fun fetchReviews(filter: Review.Filter = Review.Filter.ALL) {
        var reviewsNextPageUrl: String? = null

        manualPager { page ->
            val items = try {
                when (page) {
                    1 -> reviewsHelper.getReviews(packageName, filter).also {
                        reviewsNextPageUrl = it.nextPageUrl
                    }.reviewList

                    else -> {
                        if (!reviewsNextPageUrl.isNullOrBlank()) {
                            reviewsHelper.next(reviewsNextPageUrl!!).also {
                                reviewsNextPageUrl = it.nextPageUrl
                            }.reviewList
                        } else {
                            emptyList()
                        }
                    }
                }
            } catch (exception: GooglePlayException.AuthException) {
                Log.w(TAG, "Reviews fetch returned ${exception.code}, redirecting to Splash")
                MaxxStoreApp.events.send(AuthEvent.SessionExpired(packageName))
                emptyList()
            }
            PageResult(items)
        }.flow.distinctUntilChanged()
            .cachedIn(viewModelScope)
            .onEach { _reviews.value = it }
            .launchIn(viewModelScope)
    }
}
