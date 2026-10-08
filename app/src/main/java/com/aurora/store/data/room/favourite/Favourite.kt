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

package com.maxxos.store.data.room.favourite

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.Artwork
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity(tableName = "favourite")
data class Favourite(
    @PrimaryKey
    val packageName: String,
    val displayName: String,
    val iconURL: String,
    val added: Long,
    val mode: Mode
) : Parcelable {

    companion object {
        fun fromApp(app: App, mode: Mode): Favourite = Favourite(
            packageName = app.packageName,
            displayName = app.displayName,
            iconURL = app.iconArtwork.url,
            added = System.currentTimeMillis(),
            mode = mode
        )

        fun Favourite.toApp(): App = App(
            packageName = packageName,
            displayName = displayName,
            iconArtwork = Artwork(url = iconURL)
        )
    }

    enum class Mode {
        MANUAL,
        IMPORT
    }
}
