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

package com.aurora.store.data.room.download

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.aurora.gplayapi.data.models.PlayFile
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Singleton
@ProvidedTypeConverter
class DownloadConverter @Inject constructor(private val json: Json) {

    @TypeConverter
    fun toSharedLibList(string: String): List<SharedLib> =
        json.decodeFromString<List<SharedLib>>(string)

    @TypeConverter
    fun fromSharedLibList(list: List<SharedLib>): String = json.encodeToString(list)

    @TypeConverter
    fun toGPlayFileList(string: String): List<PlayFile> =
        json.decodeFromString<List<PlayFile>>(string)

    @TypeConverter
    fun fromGPlayFileList(list: List<PlayFile>): String = json.encodeToString(list)
}
