package com.task.modelViewer.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.task.modelViewer.components.ModelPickerBottomSheet
import com.task.modelViewer.components.ModelContainer
import com.task.modelViewer.components.TopActionBar
import com.task.modelViewer.utils.DeviceCapability
import com.task.modelViewer.viewmodel.ModelViewerViewModel
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader

/**
 * Root screen composable - the single "page" of the app.
 *
 * PERFORMANCE OPTIMIZATIONS:
 *  1. Shared Engine: A single Filament engine is created here and passed down.
 *     Creating multiple engines is extremely memory-intensive and will crash low-end devices.
 *  2. Model Limit: Max concurrent models are capped based on DeviceCapability (RAM/API).
 */
@Composable
fun ModelViewerScreen(
    viewModel: ModelViewerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val placedModels by viewModel.placedModels.collectAsStateWithLifecycle()
    var showPicker by remember { mutableStateOf(false) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    // ---- Shared Filament Resources ----
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F))  // Deep dark background
            .onGloballyPositioned { canvasSize = it.size }
    ) {
        // ---- 3D model containers (rendered in z-order: first = bottom, last = top) ----
        placedModels.forEach { model ->
            key(model.id) {
                ModelContainer(
                    model = model,
                    engine = engine,
                    modelLoader = modelLoader,
                    canvasWidth = canvasSize.width.toFloat(),
                    canvasHeight = canvasSize.height.toFloat(),
                    onDrag = { delta ->
                        viewModel.updatePosition(
                            id = model.id,
                            position = model.position + delta
                        )
                    },
                    onResize = { scaleFactor ->
                        viewModel.updateSize(
                            id = model.id,
                            size = model.size * scaleFactor
                        )
                    },
                    onToggleInteract = {
                        viewModel.toggleInteraction(model.id)
                    },
                    onClose = {
                        viewModel.removeModel(model.id)
                    },
                    onTouchDown = {
                        viewModel.bringToFront(model.id)
                    }
                )
            }
        }

        // ---- Top bar: model count + add button ----
        TopActionBar(
            modelCount = placedModels.size,
            onAddModel = {
                val limit = DeviceCapability.maxConcurrentModels(context)
                if (placedModels.size >= limit) {
                    Toast.makeText(context, "Max $limit models allowed on this device", Toast.LENGTH_SHORT).show()
                } else {
                    showPicker = true
                }
            }
        )
    }

    // ---- Bottom sheet picker ----
    if (showPicker) {
        ModelPickerBottomSheet(
            catalog = viewModel.catalog,
            onDismiss = { showPicker = false },
            onModelSelected = { entry ->
                viewModel.addModel(
                    entry = entry,
                    screenWidth = canvasSize.width.toFloat(),
                    screenHeight = canvasSize.height.toFloat()
                )
                showPicker = false
            }
        )
    }
}
