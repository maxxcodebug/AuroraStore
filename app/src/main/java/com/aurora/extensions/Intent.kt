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

package com.aurora.extensions

import android.content.Intent
import android.net.UrlQuerySanitizer
import android.os.Bundle

fun Intent.getPackageName(fallbackBundle: Bundle? = null): String? = when (action) {
    Intent.ACTION_VIEW -> {
        data?.getQueryParameter("id")
    }

    Intent.ACTION_SEND -> {
        val clipData = getStringExtra(Intent.EXTRA_TEXT).orEmpty()
        UrlQuerySanitizer(clipData).getValue("id")
    }

    Intent.ACTION_SHOW_APP_INFO -> {
        extras?.getString(Intent.EXTRA_PACKAGE_NAME)
    }

    else -> {
        extras?.getString("packageName") ?: fallbackBundle?.getString("packageName")
    }
}
