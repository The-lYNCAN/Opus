package com.example.kaamkarlo.uI.components.View

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import coil3.compose.rememberAsyncImagePainter
import coil3.imageLoader
import coil3.memory.MemoryCache
import coil3.request.ImageRequest
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.engawapg.lib.zoomable.ScrollGesturePropagation
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import kotlin.math.sqrt

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PdfViewer(
    modifier: Modifier = Modifier,
    uri: Uri,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp)
) {
    val rendererScope = rememberCoroutineScope()
    val mutex = remember { Mutex() }
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    var renderer by remember { mutableStateOf<PdfRenderer?>(null) }

    DisposableEffect(uri) {
        val job = rendererScope.launch(Dispatchers.IO) {
            val input = ParcelFileDescriptor.open(uri.toFile(), ParcelFileDescriptor.MODE_READ_ONLY)
            renderer = PdfRenderer(input)
        }
        onDispose {
            rendererScope.launch(Dispatchers.IO) {
                mutex.withLock { renderer?.close() }
            }
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val width = with(LocalDensity.current) { maxWidth.toPx() }.toInt()
        val height = (width * sqrt(2f)).toInt()
        val pageCount = renderer?.pageCount ?: 0

        val pagerState = rememberPagerState(pageCount = { pageCount })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSpacing = 8.dp
        ) { index ->
            val cacheKey = MemoryCache.Key("$uri-$index")
            var bitmap by remember { mutableStateOf<Bitmap?>(imageLoader.memoryCache?.get(cacheKey)?.image?.toBitmap()) }
            val zoomState = rememberZoomState(
                contentSize = androidx.compose.ui.geometry.Size(width.toFloat(), height.toFloat())

            )

            if (bitmap == null) {
                DisposableEffect(uri, index) {
                    val job = rendererScope.launch(Dispatchers.IO) {
                        val destinationBitmap =
                            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(destinationBitmap)
                        canvas.drawColor(android.graphics.Color.WHITE) // Match PDF background
                        mutex.withLock {
                            if (!coroutineContext.isActive) return@launch
                            try {
                                renderer?.let {
                                    it.openPage(index).use { page ->
                                        page.render(
                                            destinationBitmap,
                                            null,
                                            null,
                                            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                        )
                                    }
                                }
                                bitmap = destinationBitmap
                            } catch (e: Exception) {
                                // Handle renderer closure
                            }
                        }
                    }
                    onDispose { job.cancel() }
                }
                Box(
                    modifier = Modifier
                        .background(Color.Red) // Container background
                        .fillMaxWidth()
                        .aspectRatio(1f / sqrt(2f))
                )
            } else {
                val request = ImageRequest.Builder(context)
                    .size(width, height)
                    .memoryCacheKey(cacheKey)
                    .data(bitmap)
                    .build()
                Image(
                    modifier = Modifier
                        .background(Color.White) // Image background
                        .fillMaxWidth()
                        .aspectRatio(1f / sqrt(2f))
                        .zoomable(
                            zoomState = zoomState,
                            enableOneFingerZoom = false,
                            scrollGesturePropagation = ScrollGesturePropagation.NotZoomed,

                            ),
                    contentScale = ContentScale.Fit,
                    painter = rememberAsyncImagePainter(request),
                    contentDescription = "Page ${index + 1} of $pageCount"
                )
            }
        }
    }
}