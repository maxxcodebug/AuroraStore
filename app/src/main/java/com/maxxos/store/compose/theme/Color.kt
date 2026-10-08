/*
 * SPDX-FileCopyrightText: 2025 The Calyx Institute
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


/*
 * Copyright (C) 2026 MaxxOS. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maxxos.store.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Whether the active [MaterialTheme] is dark.
 *
 * Derived from the resolved color scheme rather than [androidx.compose.foundation.isSystemInDarkTheme]
 * so it stays correct when the user forces a light/dark theme that differs from the system setting.
 */
@Composable
@ReadOnlyComposable
private fun isAppInDarkTheme(): Boolean = MaterialTheme.colorScheme.surface.luminance() < 0.5f

/**
 * Amber used to flag warnings/caveats. Lightened in dark theme for adequate contrast.
 */
val warningColor: Color
    @Composable @ReadOnlyComposable
    get() = if (isAppInDarkTheme()) Color(0xFFFFB74D) else Color(0xFFFF7600)

/**
 * Green used to flag positive/success states. Lightened in dark theme for adequate contrast.
 */
val successColor: Color
    @Composable @ReadOnlyComposable
    get() = if (isAppInDarkTheme()) Color(0xFF5BD27A) else Color(0xFF1B8738)

val colorGreen: Color
    @Composable @ReadOnlyComposable
    get() = if (isAppInDarkTheme()) Color(0xFF81C784) else Color(0xFF388E3C)

val colorRed: Color
    @Composable @ReadOnlyComposable
    get() = if (isAppInDarkTheme()) Color(0xFFE57373) else Color(0xFFD32F2F)

/**
 * Brand color schemes seeded from Aurora's accent (#6C63FF), used on devices that don't support
 * dynamic color (Android 11 and below) so the full palette stays on-brand instead of falling back
 * to Material's default purple baseline.
 */
val BrandLightColorScheme = lightColorScheme(
    primary = Color(0xFF245A50),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFCDE9E0),
    onPrimaryContainer = Color(0xFF07372F),
    secondary = Color(0xFF4F7770),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD8ECE7),
    onSecondaryContainer = Color(0xFF123C35),
    tertiary = Color(0xFF4E7C73),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFCAE7DF),
    onTertiaryContainer = Color(0xFF123B34),
    background = Color(0xFFF5FAF7),
    onBackground = Color(0xFF18201E),
    surface = Color(0xFFF5FAF7),
    onSurface = Color(0xFF18201E),
    surfaceVariant = Color(0xFFE0ECE8),
    onSurfaceVariant = Color(0xFF4A5A56),
    outline = Color(0xFF74837F),
    outlineVariant = Color(0xFFC3D1CD),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

val BrandDarkColorScheme = darkColorScheme(
    primary = Color(0xFF9BD7C8),
    onPrimary = Color(0xFF00382F),
    primaryContainer = Color(0xFF145247),
    onPrimaryContainer = Color(0xFFB9F1E2),
    secondary = Color(0xFFA5CCC3),
    onSecondary = Color(0xFF0C3831),
    secondaryContainer = Color(0xFF294D46),
    onSecondaryContainer = Color(0xFFC1E9E0),
    tertiary = Color(0xFFA4D5CB),
    onTertiary = Color(0xFF0B3932),
    tertiaryContainer = Color(0xFF2A5049),
    onTertiaryContainer = Color(0xFFC0EDE4),
    background = Color(0xFF0D1513),
    onBackground = Color(0xFFE0EAE6),
    surface = Color(0xFF0D1513),
    onSurface = Color(0xFFE0EAE6),
    surfaceVariant = Color(0xFF3B4945),
    onSurfaceVariant = Color(0xFFBECBC7),
    outline = Color(0xFF899691),
    outlineVariant = Color(0xFF3B4945),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)
