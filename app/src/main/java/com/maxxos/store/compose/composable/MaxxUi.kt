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

package com.maxxos.store.compose.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun maxxGreeting(name: String = "Anshuman"): String {
    var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = System.currentTimeMillis()
            val wait = 60_000L - (now % 60_000L)
            delay(wait.coerceAtLeast(1_000L))
            tick = System.currentTimeMillis()
        }
    }

    // Reading Calendar after the minute tick makes the greeting change without reopening the app.
    val calendar = Calendar.getInstance().apply { timeInMillis = tick }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..20 -> "Good evening"
        else -> "Good night"
    }
    return "$greeting, $name"
}

@Composable
fun MaxxBackground(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()

    val background =
        if (dark) Color(0xFF101110)
        else Color(0xFFF5F8F4)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Box(
            Modifier
                .size(190.dp)
                .align(Alignment.TopEnd)
                .offset(x = 28.dp, y = (-24).dp)
                .clip(CircleShape)
                .background(
                    if (dark) Color(0xFF34564D).copy(alpha = 0.22f)
                    else Color(0xFFB9DED2).copy(alpha = 0.45f)
                )
        )

        Box(
            Modifier
                .size(125.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-48).dp, y = 90.dp)
                .clip(CircleShape)
                .background(
                    if (dark) Color(0xFF46655C).copy(alpha = 0.15f)
                    else Color(0xFFC9E7DD).copy(alpha = 0.48f)
                )
        )

        Box(
            Modifier
                .size(75.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 22.dp, y = (-80).dp)
                .clip(CircleShape)
                .background(
                    if (dark) Color(0xFF5B746C).copy(alpha = 0.12f)
                    else Color(0xFFD9EEE7).copy(alpha = 0.55f)
                )
        )

        content()
    }
}

@Composable
fun MaxxGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
        content = content
    )
}

@Composable
fun MaxxPill(
    text: String,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val dark = isSystemInDarkTheme()

    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        shape = RoundedCornerShape(50),
        color = when {
            selected && dark -> Color(0xFFF4F6F2)
            selected -> Color(0xFF183F38)
            dark -> Color(0xFF2C2E2C)
            else -> Color(0xFFE1E7E3)
        },
        contentColor = when {
            selected && dark -> Color(0xFF101210)
            selected -> Color.White
            dark -> Color(0xFFE8ECE8)
            else -> Color(0xFF30453F)
        }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 10.dp
            ),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@Composable
fun MaxxSectionTitle(
    title: String,
    action: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge
        )
        if (action != null) {
            MaxxPill(action, onClick = onAction)
        }
    }
}
