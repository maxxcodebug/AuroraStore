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

package com.maxxos.store.compose.composition

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Supported UI styles for different types of Android OS
 */
enum class UI {

    /**
     * Targets Phone, Foldable, Tablets, Desktop
     */
    DEFAULT,

    /**
     * Targets TV
     */
    TV
}

/**
 * CompositionLocal to provide information on which UI style should be used
 */
val LocalUI = staticCompositionLocalOf { UI.DEFAULT }
