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

package com.aurora.store.compose.composition

import androidx.compose.runtime.compositionLocalOf
import com.aurora.store.data.model.NetworkStatus

/**
 * CompositionLocal carrying the current device network status. Provided once at the
 * activity root from a single [com.aurora.store.data.providers.NetworkProvider] subscription,
 * so any screen can read `LocalNetworkStatus.current` without injecting the provider
 * or duplicating the flow collection.
 *
 * Uses [compositionLocalOf] (not static) so only readers recompose on change.
 */
val LocalNetworkStatus = compositionLocalOf { NetworkStatus.AVAILABLE }
