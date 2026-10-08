// Copyright (C) 2026 MaxxOS. All rights reserved.
// SPDX-FileCopyrightText: 2026 Aurora OSS
// SPDX-License-Identifier: GPL-3.0-or-later

package com.maxxos.store.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.StreamBundle
import com.aurora.gplayapi.data.models.StreamCluster
import com.maxxos.store.R
import com.maxxos.store.compose.composable.app.AppListItem
import kotlinx.coroutines.flow.distinctUntilChanged

private const val LOAD_MORE_THRESHOLD = 2

@Composable
fun StreamCarousel(
    modifier: Modifier = Modifier,
    streamBundle: StreamBundle?,
    filterSingleAppClusters: Boolean = true,
    lazyListState: LazyListState = rememberLazyListState(),
    onHeaderClick: (StreamCluster) -> Unit = {},
    onAppClick: (App) -> Unit = {},
    onClusterScrolled: (StreamCluster) -> Unit = {},
    onScrolledToEnd: () -> Unit = {}
) {
    val bundleLoaded = streamBundle != null
    LaunchedEffect(lazyListState, bundleLoaded) {
        snapshotFlow {
            val last = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val total = lazyListState.layoutInfo.totalItemsCount
            last >= total - LOAD_MORE_THRESHOLD
        }.distinctUntilChanged().collect { if (it && bundleLoaded) onScrolledToEnd() }
    }

    if (streamBundle == null) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) { items(5) { ShimmerCarouselSection() } }
        return
    }

    val clusters = streamBundle.streamClusters.values
        .map { it.copy(clusterAppList = it.clusterAppList.distinctBy { app -> app.packageName }) }
        .filter { it.clusterAppList.isNotEmpty() && it.clusterTitle.isNotBlank() && (!filterSingleAppClusters || it.clusterAppList.size > 1) }

    if (clusters.isEmpty()) {
        Placeholder(modifier = modifier, painter = painterResource(R.drawable.ic_apps), message = stringResource(R.string.no_apps_available))
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(top = 2.dp, bottom = 22.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item(key = "featured") { FeaturedClusterCard(clusters.first(), onAppClick) }
        clusters.drop(1).forEach { cluster ->
            item(key = "header_${cluster.id}") {
                MaxxSectionTitle(
                    title = cluster.clusterTitle,
                    action = if (cluster.clusterBrowseUrl.isNotBlank()) "See all" else null,
                    onAction = if (cluster.clusterBrowseUrl.isNotBlank()) ({ onHeaderClick(cluster) }) else null
                )
            }
            item(key = "row_${cluster.id}") {
                LazyRow(contentPadding = PaddingValues(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    itemsIndexed(cluster.clusterAppList, key = { _, app -> app.packageName }) { _, app ->
                        AppListItem(app = app, onClick = { onAppClick(app) })
                    }
                }
            }
        }
        if (streamBundle.hasNext()) item(key = "loading_footer") { ShimmerCarouselSection() }
    }
}

@Composable
private fun FeaturedClusterCard(cluster: StreamCluster, onAppClick: (App) -> Unit) {
    val app = cluster.clusterAppList.firstOrNull() ?: return
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(30.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.84f),
        tonalElevation = 2.dp
    ) {
        Box(Modifier.fillMaxWidth().height(188.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(app.iconArtwork.url).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(146.dp).align(Alignment.CenterEnd).padding(14.dp).clip(RoundedCornerShape(28.dp))
            )
            Column(
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 22.dp, end = 135.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text("Featured", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(app.displayName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(app.developerName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Button(onClick = { onAppClick(app) }, shape = RoundedCornerShape(50), contentPadding = PaddingValues(horizontal = 17.dp, vertical = 0.dp)) { Text("View app") }
            }
        }
    }
}

@Composable
internal fun ClusterRow(
    cluster: StreamCluster,
    onAppClick: (App) -> Unit = {},
    onClusterScrolled: (StreamCluster) -> Unit = {}
) {
    val rowState = rememberLazyListState()
    val reachedEnd by remember { derivedStateOf {
        val last = rowState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        val total = rowState.layoutInfo.totalItemsCount
        last >= total - LOAD_MORE_THRESHOLD
    } }
    LaunchedEffect(reachedEnd) { if (reachedEnd && cluster.hasNext()) onClusterScrolled(cluster) }
    LazyRow(state = rowState, contentPadding = PaddingValues(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        itemsIndexed(cluster.clusterAppList, key = { _, app -> app.packageName }) { _, app ->
            AppListItem(app = app, onClick = { onAppClick(app) })
        }
    }
}
