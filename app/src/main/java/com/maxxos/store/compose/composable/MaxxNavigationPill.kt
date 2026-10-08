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

package com.maxxos.store.compose.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MaxxNavigationPill(
    selectedIndex: Int,
    updateCount: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dark = isSystemInDarkTheme()

    val tabs = listOf(
        com.maxxos.store.R.string.title_apps to com.maxxos.store.R.drawable.ic_apps,
        com.maxxos.store.R.string.title_games to com.maxxos.store.R.drawable.ic_games,
        com.maxxos.store.R.string.title_updates to com.maxxos.store.R.drawable.ic_updates
    )

    val outerColor =
        if (dark) Color(0xFF171817)
        else Color(0xFFF8FAF7)

    val selectedColor =
        if (dark) Color(0xFFF5F7F4)
        else Color(0xFF183F38)

    val selectedContent =
        if (dark) Color(0xFF101210)
        else Color.White

    val inactiveContent =
        if (dark) Color(0xFFE4E8E3)
        else Color(0xFF36504A)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(12.dp, RoundedCornerShape(34.dp)),
        shape = RoundedCornerShape(34.dp),
        color = outerColor.copy(alpha = 0.97f),
        border = BorderStroke(
            1.dp,
            if (dark) Color.White.copy(alpha = 0.10f)
            else Color(0xFF183F38).copy(alpha = 0.10f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, (labelRes, iconRes) ->
                val selected = selectedIndex == index

                Surface(
                    onClick = { onSelected(index) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(28.dp),
                    color = if (selected) selectedColor else Color.Transparent,
                    contentColor = if (selected) selectedContent else inactiveContent
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 10.dp,
                                vertical = 11.dp
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (index == 2 && updateCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge {
                                        Text(updateCount.coerceAtMost(99).toString())
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(iconRes),
                                    contentDescription = stringResource(labelRes),
                                    modifier = Modifier.size(21.dp)
                                )
                            }
                        } else {
                            Icon(
                                painter = painterResource(iconRes),
                                contentDescription = stringResource(labelRes),
                                modifier = Modifier.size(21.dp)
                            )
                        }

                        if (selected) {
                            Text(
                                text = stringResource(labelRes),
                                modifier = Modifier.padding(start = 7.dp),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

