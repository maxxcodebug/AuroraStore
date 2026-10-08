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

package com.aurora.store.viewmodel.dispenser

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurora.extensions.observeAsStateFlow
import com.aurora.store.util.Preferences
import com.aurora.store.util.Preferences.PREFERENCE_DISPENSER_URLS
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DispenserViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val sharedPreferences = Preferences.getPrefs(context)

    private var _dispensers: Set<String>
        set(value) = sharedPreferences.edit { putStringSet(PREFERENCE_DISPENSER_URLS, value) }
        get() = sharedPreferences.getStringSet(PREFERENCE_DISPENSER_URLS, emptySet()) ?: emptySet()

    val dispensers = sharedPreferences.observeAsStateFlow(
        key = PREFERENCE_DISPENSER_URLS,
        scope = viewModelScope,
        initial = emptySet(),
        valueProvider = { _dispensers }
    )

    fun addDispenser(url: String) {
        _dispensers = _dispensers.toMutableSet().apply {
            add(url)
        }
    }

    fun removeDispenser(url: String) {
        _dispensers = _dispensers.toMutableSet().apply {
            remove(url)
        }
    }
}
