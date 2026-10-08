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

package com.aurora.store.data.model

import android.graphics.Bitmap
import com.aurora.store.compose.ui.commons.InstalledAppMeta

data class BlacklistAppItem(
    override val packageName: String,
    val displayName: String,
    val versionName: String,
    val versionCode: Long,
    val icon: Bitmap,
    val isFiltered: Boolean,
    override val firstInstallTime: Long = 0L,
    override val lastUpdateTime: Long = 0L,
    override val sizeBytes: Long = 0L,
    override val isSystem: Boolean = false,
    override val installer: String? = null
) : InstalledAppMeta {
    override val label: String get() = displayName
}
