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

package com.maxxos.store.data.event

abstract class Event

sealed class BusEvent : Event() {
    lateinit var extra: String
    lateinit var error: String

    data class Blacklisted(val packageName: String) : BusEvent()
}

sealed class AuthEvent : Event() {
    data class GoogleLogin(val success: Boolean, val email: String, val token: String) : AuthEvent()
    data class SessionExpired(val packageName: String? = null) : AuthEvent()
}

open class InstallerEvent(open val packageName: String) : Event() {
    data class Installed(override val packageName: String) : InstallerEvent(packageName)
    data class Uninstalled(override val packageName: String) : InstallerEvent(packageName)

    data class Installing(
        override val packageName: String,
        val progress: Float = 0.0F
    ) : InstallerEvent(packageName)

    data class PendingUserAction(override val packageName: String) : InstallerEvent(packageName)

    data class Failed(
        override val packageName: String,
        val error: String? = null,
        val extra: String? = null
    ) : InstallerEvent(packageName)
}
