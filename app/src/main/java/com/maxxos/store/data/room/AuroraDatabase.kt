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

package com.maxxos.store.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maxxos.store.data.room.account.Account
import com.maxxos.store.data.room.account.AccountConverter
import com.maxxos.store.data.room.account.AccountDao
import com.maxxos.store.data.room.account.AppAccountBinding
import com.maxxos.store.data.room.account.AppAccountBindingDao
import com.maxxos.store.data.room.download.Download
import com.maxxos.store.data.room.download.DownloadConverter
import com.maxxos.store.data.room.download.DownloadDao
import com.maxxos.store.data.room.exodus.TrackerDao
import com.maxxos.store.data.room.exodus.TrackerEntity
import com.maxxos.store.data.room.favourite.Favourite
import com.maxxos.store.data.room.favourite.FavouriteDao
import com.maxxos.store.data.room.notification.AppNotification
import com.maxxos.store.data.room.notification.NotificationDao
import com.maxxos.store.data.room.review.LocalReview
import com.maxxos.store.data.room.review.ReviewDao
import com.maxxos.store.data.room.update.IgnoredUpdate
import com.maxxos.store.data.room.update.IgnoredUpdateDao
import com.maxxos.store.data.room.update.Update
import com.maxxos.store.data.room.update.UpdateDao

@Database(
    entities = [
        Download::class,
        Favourite::class,
        Update::class,
        IgnoredUpdate::class,
        LocalReview::class,
        Account::class,
        AppAccountBinding::class,
        TrackerEntity::class,
        AppNotification::class
    ],
    version = 13,
    exportSchema = true
)
@TypeConverters(DownloadConverter::class, AccountConverter::class)
abstract class AuroraDatabase : RoomDatabase() {
    abstract fun downloadDao(): DownloadDao
    abstract fun favouriteDao(): FavouriteDao
    abstract fun updateDao(): UpdateDao
    abstract fun ignoredUpdateDao(): IgnoredUpdateDao
    abstract fun reviewDao(): ReviewDao
    abstract fun accountDao(): AccountDao
    abstract fun appAccountBindingDao(): AppAccountBindingDao
    abstract fun trackerDao(): TrackerDao
    abstract fun notificationDao(): NotificationDao
}
