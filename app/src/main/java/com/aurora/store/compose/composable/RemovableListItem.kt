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

package com.maxxos.store.compose.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val REMOVE_ANIM_DURATION_MS = 300L

/**
 * Wraps [content] in an AnimatedVisibility container that shrinks vertically and fades out
 * before invoking [onRemove]. The content lambda receives a trigger callback that, when
 * invoked, plays the exit animation and then calls [onRemove].
 */
@Composable
fun RemovableListItem(
    onRemove: () -> Unit,
    content: @Composable (triggerRemove: () -> Unit) -> Unit
) {
    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(true) }
    AnimatedVisibility(visible = visible, exit = shrinkVertically() + fadeOut()) {
        content {
            scope.launch {
                visible = false
                delay(REMOVE_ANIM_DURATION_MS)
                onRemove()
            }
        }
    }
}
