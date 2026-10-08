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

package com.aurora.store.data.installer.base

import com.aurora.store.data.room.download.Download

interface IInstaller {
    fun install(download: Download)
    fun clearQueue()
    fun isAlreadyQueued(packageName: String): Boolean
    fun removeFromInstallQueue(packageName: String)

    /**
     * Abandons any staged-but-uncommitted install session for [packageName] so cancelling
     * a download doesn't leak a [android.content.pm.PackageInstaller] session. Default no-op
     * for installers that don't stage sessions.
     */
    fun cancelInstall(packageName: String) {}
}
