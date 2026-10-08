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
 * SPDX-FileCopyrightText: 2025 Aurora OSS
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.maxxos.store.data.model

import androidx.annotation.StringRes
import com.maxxos.store.R

enum class InstallStatus(@StringRes val localized: Int) {
    PENDING(R.string.action_pending),
    DOWNLOADING(R.string.status_downloading),
    INSTALLING(R.string.action_installing),
    INSTALLED(R.string.title_installed),
    FAILED(R.string.status_failed)
}
