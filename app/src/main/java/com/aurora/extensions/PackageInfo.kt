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

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import androidx.core.content.pm.PackageInfoCompat

fun PackageInfo.isValidApp(packageManager: PackageManager): Boolean {
    if (this.applicationInfo == null || this.packageName.isEmpty()) return false

    // Filter out core AOSP system apps
    if (this.applicationInfo!!.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
        if (this.packageName.endsWith(".resources")) return false
        if (this.applicationInfo!!.loadLabel(packageManager).startsWith(this.packageName)) {
            return false
        }
        if (this.versionName?.endsWith("system image") == true) return false
        if (this.versionName?.endsWith("-initial") == true) return false
        if (this.versionName == Build.VERSION.RELEASE &&
            PackageInfoCompat.getLongVersionCode(this) == Build.VERSION.SDK_INT.toLong()
        ) {
            return false
        }
    }

    return when {
        isQAndAbove -> {
            Process.isApplicationUid(this.applicationInfo!!.uid) &&
                !this.applicationInfo!!.isResourceOverlay &&
                !this.isApex
        }

        isNAndAbove -> Process.isApplicationUid(this.applicationInfo!!.uid)

        else -> this.versionName != null
    }
}
