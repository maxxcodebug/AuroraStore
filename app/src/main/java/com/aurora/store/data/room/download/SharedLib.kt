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

package com.maxxos.store.data.room.download

import android.os.Parcelable
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.PlayFile
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class SharedLib(
    val packageName: String,
    val versionCode: Long,
    var fileList: List<PlayFile>
) : Parcelable {
    companion object {
        fun fromApp(app: App): SharedLib = SharedLib(
            app.packageName,
            app.versionCode,
            app.fileList.filterNot { it.url.isBlank() }
        )
    }
}
