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

package com.maxxos.store.data.model

import androidx.annotation.StringRes

/**
 * Class holding information on a supported installer
 */
data class InstallerInfo(
    val id: Int,
    val installer: Installer,
    val installerPackageNames: List<String>,
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    @StringRes val description: Int,
    /** Label of the app providing this installer, where more than one app can serve it. */
    val provider: String? = null
) {
    override fun equals(other: Any?): Boolean = when (other) {
        is InstallerInfo -> other.id == id
        else -> false
    }

    override fun hashCode(): Int = id.hashCode()
}
