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

package com.maxxos.store.data.providers

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.pm.PackageInfoCompat
import com.maxxos.store.util.PackageUtil.getPackageInfo

class NativeGsfVersionProvider(context: Context, isExport: Boolean = false) {

    companion object {
        private const val GOOGLE_SERVICES_PACKAGE_ID = "com.google.android.gms"
        private const val GOOGLE_VENDING_PACKAGE_ID = "com.android.vending"
    }

    // Preferred defaults, not any specific reason they just work fine.
    var gsfVersionCode = 203019037L
    var vendingVersionCode = 82151710L
    var vendingVersionString = "21.5.17-21 [0] [PR] 326734551"

    init {
        try {
            if (isExport) {
                getPackageInfo(context, GOOGLE_SERVICES_PACKAGE_ID).let {
                    gsfVersionCode = PackageInfoCompat.getLongVersionCode(it)
                }

                getPackageInfo(context, GOOGLE_VENDING_PACKAGE_ID).let {
                    vendingVersionCode = PackageInfoCompat.getLongVersionCode(it)
                    vendingVersionString = it.versionName ?: vendingVersionString
                }
            }
        } catch (_: PackageManager.NameNotFoundException) {
        }
    }
}
