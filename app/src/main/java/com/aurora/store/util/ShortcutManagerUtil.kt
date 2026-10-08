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

package com.maxxos.store.util

import android.content.Context
import android.util.Log
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap

object ShortcutManagerUtil {

    private const val TAG = "ShortcutManagerUtil"

    fun canPinShortcut(context: Context, packageName: String): Boolean =
        ShortcutManagerCompat.isRequestPinShortcutSupported(context) &&
            context.packageManager.getLaunchIntentForPackage(packageName) != null

    fun requestPinShortcut(context: Context, packageName: String) {
        val packageManager = context.packageManager
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName) ?: return
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            val shortcutInfo = ShortcutInfoCompat.Builder(context, packageName)
                .setShortLabel(appInfo.loadLabel(packageManager))
                .setIcon(
                    IconCompat.createWithBitmap(appInfo.loadIcon(packageManager).toBitmap())
                )
                .setIntent(launchIntent)
                .build()

            ShortcutManagerCompat.requestPinShortcut(context, shortcutInfo, null)
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to request shortcut pin!", exception)
        }
    }
}
