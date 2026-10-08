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

package com.aurora.extensions

import android.content.pm.PackageManager
import com.maxxos.store.BuildConfig

/**
 * Gets the name of package responsible for installing/updating given package
 */
fun PackageManager.getUpdateOwnerPackageNameCompat(packageName: String): String? {
    // Self-updates can be managed by ourselves
    if (packageName == BuildConfig.APPLICATION_ID) return BuildConfig.APPLICATION_ID

    return when {
        isUAndAbove -> {
            // If update ownership is null, we can still silently update it if we installed it
            val installSourceInfo = getInstallSourceInfo(packageName)
            installSourceInfo.updateOwnerPackageName ?: installSourceInfo.installingPackageName
        }

        isRAndAbove -> {
            val installSourceInfo = getInstallSourceInfo(packageName)
            installSourceInfo.installingPackageName
        }

        else -> {
            @Suppress("DEPRECATION")
            getInstallerPackageName(packageName)
        }
    }
}
