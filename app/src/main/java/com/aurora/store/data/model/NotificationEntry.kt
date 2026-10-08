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

package com.aurora.store.data.model

import com.aurora.store.data.room.download.Download
import com.aurora.store.data.room.notification.AppNotification

sealed class NotificationEntry {

    abstract val id: String
    abstract val timestamp: Long

    data class PendingInstall(val download: Download) : NotificationEntry() {
        override val id get() = "install:${download.packageName}"
        override val timestamp get() = download.downloadedAt
    }

    data class Remote(val notification: AppNotification) : NotificationEntry() {
        override val id get() = notification.id
        override val timestamp get() = notification.timestamp
    }
}
