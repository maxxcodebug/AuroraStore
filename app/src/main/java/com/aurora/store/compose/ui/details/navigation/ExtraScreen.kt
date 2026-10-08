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

package com.maxxos.store.compose.ui.details.navigation

import android.os.Parcelable
import androidx.navigation3.runtime.NavKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Extra destinations for app detail's screen
 *
 * All of these destinations require and show information related to an app and thus aren't part of
 * the main navigation display class.
 */
@Parcelize
@Serializable
sealed class ExtraScreen : NavKey, Parcelable {

    @Serializable
    data object More : ExtraScreen()

    @Serializable
    data class Screenshot(val index: Int) : ExtraScreen()

    @Serializable
    data object Exodus : ExtraScreen()

    @Serializable
    data object Review : ExtraScreen()

    @Serializable
    data object Permission : ExtraScreen()

    @Serializable
    data object ManualDownload : ExtraScreen()

    @Serializable
    data object MicroG : ExtraScreen()
}
