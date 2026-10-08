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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.aurora.gplayapi.data.models.Category
import com.maxxos.store.CategoryStash
import com.maxxos.store.R
import com.maxxos.store.compose.composable.CategoryItem
import com.maxxos.store.compose.composable.Placeholder
import com.maxxos.store.compose.composable.ShimmerCategoryRow
import com.maxxos.store.compose.preview.ThemePreviewProvider
import com.maxxos.store.data.model.ViewState
import com.maxxos.store.viewmodel.category.CategoryViewModel

@Composable
internal fun CategoriesContent(
    pageType: Int,
    viewModel: CategoryViewModel,
    onCategoryClick: (Category) -> Unit
) {
    val categoryType = if (pageType == 1) Category.Type.GAME else Category.Type.APPLICATION
    val state by viewModel.liveData.observeAsState()

    LaunchedEffect(categoryType) {
        viewModel.getCategoryList(categoryType)
    }

    if (state is ViewState.Error) {
        Placeholder(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.ic_refresh),
            message = stringResource(R.string.error),
            actionLabel = stringResource(R.string.action_retry),
            onAction = { viewModel.getCategoryList(categoryType) }
        )
        return
    }

    @Suppress("UNCHECKED_CAST")
    val categories = (state as? ViewState.Success<*>)?.data as? CategoryStash
    val list = categories?.get(categoryType)

    CategoriesBody(list = list, onCategoryClick = onCategoryClick)
}

@Composable
private fun CategoriesBody(list: List<Category>?, onCategoryClick: (Category) -> Unit = {}) {
    if (list.isNullOrEmpty()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(10) { ShimmerCategoryRow() }
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = list.size, key = { list[it].title }) { index ->
                CategoryItem(
                    category = list[index],
                    onClick = { onCategoryClick(list[index]) }
                )
            }
        }
    }
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun CategoriesBodyLoadingPreview() {
    CategoriesBody(list = null)
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun CategoriesBodyLoadedPreview() {
    val categories = listOf(
        "Art & Design",
        "Auto & Vehicles",
        "Beauty",
        "Books & Reference",
        "Business",
        "Comics",
        "Communication",
        "Dating",
        "Education",
        "Entertainment"
    ).map { Category(title = it, imageUrl = "") }
    CategoriesBody(list = categories)
}
