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

package com.aurora.store.viewmodel.details

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = PermissionViewModel.Factory::class)
class PermissionViewModel @AssistedInject constructor(
    @Assisted private val permissions: List<String>,
    @ApplicationContext private val context: Context
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(permissions: List<String>): PermissionViewModel
    }

    private val _permissionsInfo = MutableStateFlow<Map<String, PermissionInfo>>(emptyMap())
    val permissionsInfo = _permissionsInfo.asStateFlow()

    init {
        fetchPermissions()
    }

    private fun fetchPermissions() {
        // Bail out if this is not a known permission for the OS
        _permissionsInfo.value = permissions.mapNotNull { permission ->
            getPermissionInfo(permission)?.let { info -> permission to info }
        }.toMap()
    }

    private fun getPermissionInfo(permissionName: String): PermissionInfo? = try {
        context.packageManager.getPermissionInfo(permissionName, 0)
    } catch (_: PackageManager.NameNotFoundException) {
        null
    }
}
