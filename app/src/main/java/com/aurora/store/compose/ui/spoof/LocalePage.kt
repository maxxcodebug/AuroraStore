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

package com.aurora.store.compose.ui.spoof

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aurora.store.R
import com.aurora.store.compose.composable.LocaleListItem
import com.aurora.store.compose.composable.TextDividerComposable
import com.aurora.store.compose.preview.ThemePreviewProvider
import com.aurora.store.viewmodel.spoof.SpoofViewModel
import java.util.Locale

@Composable
fun LocalePage(onRequestNavigateToSplash: () -> Unit, viewModel: SpoofViewModel = hiltViewModel()) {
    val availableLocales by viewModel.availableLocales.collectAsStateWithLifecycle()
    val currentLocale by viewModel.currentLocale.collectAsStateWithLifecycle()

    PageContent(
        defaultLocale = viewModel.defaultLocale,
        locales = availableLocales,
        isLocaleSelected = { locale -> currentLocale == locale },
        onLocaleSelected = { locale ->
            viewModel.onLocaleSelected(locale)
            onRequestNavigateToSplash()
        }
    )
}

@Composable
private fun PageContent(
    defaultLocale: Locale = Locale.getDefault(),
    locales: List<Locale> = emptyList(),
    isLocaleSelected: (locale: Locale) -> Boolean = { false },
    onLocaleSelected: (locale: Locale) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xsmall))
    ) {
        stickyHeader {
            Surface(modifier = Modifier.fillMaxWidth()) {
                TextDividerComposable(
                    title = stringResource(R.string.default_spoof)
                )
            }
        }

        item {
            LocaleListItem(
                displayName = defaultLocale.displayName,
                displayLanguage = defaultLocale.getDisplayLanguage(defaultLocale),
                isChecked = isLocaleSelected(defaultLocale),
                onClick = { onLocaleSelected(defaultLocale) }
            )
        }

        stickyHeader {
            Surface(modifier = Modifier.fillMaxWidth()) {
                TextDividerComposable(
                    title = stringResource(R.string.available_spoof)
                )
            }
        }

        items(items = locales, key = { locale -> locale.hashCode() }) { locale ->
            LocaleListItem(
                displayName = locale.displayName,
                displayLanguage = locale.getDisplayLanguage(locale),
                isChecked = isLocaleSelected(locale),
                onClick = { onLocaleSelected(locale) }
            )
        }
    }
}

@PreviewWrapper(ThemePreviewProvider::class)
@Preview(showBackground = true)
@Composable
private fun LocalePagePreview() {
    PageContent(
        locales = Locale.getAvailableLocales().toList().filter { it.displayName.isNotBlank() },
        isLocaleSelected = { locale -> locale == Locale.getDefault() }
    )
}
