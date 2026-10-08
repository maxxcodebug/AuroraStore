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

import android.content.Context
import androidx.room.Room
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_10_11
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_11_12
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_12_13
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_1_2
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_2_3
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_3_4
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_4_5
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_5_6
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_6_7
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_7_8
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_8_9
import com.maxxos.store.data.room.MigrationHelper.MIGRATION_9_10
import com.maxxos.store.data.room.account.AccountConverter
import com.maxxos.store.data.room.account.AccountDao
import com.maxxos.store.data.room.account.AppAccountBindingDao
import com.maxxos.store.data.room.download.DownloadConverter
import com.maxxos.store.data.room.download.DownloadDao
import com.maxxos.store.data.room.exodus.TrackerDao
import com.maxxos.store.data.room.favourite.FavouriteDao
import com.maxxos.store.data.room.notification.NotificationDao
import com.maxxos.store.data.room.review.ReviewDao
import com.maxxos.store.data.room.update.IgnoredUpdateDao
import com.maxxos.store.data.room.update.UpdateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE = "aurora_database"

    @Singleton
    @Provides
    fun providesRoomInstance(
        @ApplicationContext context: Context,
        downloadConverter: DownloadConverter,
        accountConverter: AccountConverter
    ): AuroraDatabase = Room.databaseBuilder(context, AuroraDatabase::class.java, DATABASE)
        .addMigrations(
            MIGRATION_1_2,
            MIGRATION_2_3,
            MIGRATION_3_4,
            MIGRATION_4_5,
            MIGRATION_5_6,
            MIGRATION_6_7,
            MIGRATION_7_8,
            MIGRATION_8_9,
            MIGRATION_9_10,
            MIGRATION_10_11,
            MIGRATION_11_12,
            MIGRATION_12_13
        )
        .addTypeConverter(downloadConverter)
        .addTypeConverter(accountConverter)
        .build()

    @Provides
    fun providesDownloadDao(auroraDatabase: AuroraDatabase): DownloadDao =
        auroraDatabase.downloadDao()

    @Provides
    fun providesFavouriteDao(auroraDatabase: AuroraDatabase): FavouriteDao =
        auroraDatabase.favouriteDao()

    @Provides
    fun providesUpdateDao(auroraDatabase: AuroraDatabase): UpdateDao = auroraDatabase.updateDao()

    @Provides
    fun providesIgnoredUpdateDao(auroraDatabase: AuroraDatabase): IgnoredUpdateDao =
        auroraDatabase.ignoredUpdateDao()

    @Provides
    fun providesReviewDao(auroraDatabase: AuroraDatabase): ReviewDao = auroraDatabase.reviewDao()

    @Provides
    fun providesAccountDao(auroraDatabase: AuroraDatabase): AccountDao = auroraDatabase.accountDao()

    @Provides
    fun providesAppAccountBindingDao(auroraDatabase: AuroraDatabase): AppAccountBindingDao =
        auroraDatabase.appAccountBindingDao()

    @Provides
    fun providesTrackerDao(auroraDatabase: AuroraDatabase): TrackerDao = auroraDatabase.trackerDao()

    @Provides
    fun providesNotificationDao(auroraDatabase: AuroraDatabase): NotificationDao =
        auroraDatabase.notificationDao()
}
