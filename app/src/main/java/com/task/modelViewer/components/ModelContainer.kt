package com.task.modelViewer.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Vrpano
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.filament.Engine
import com.task.modelViewer.data.model.ModelItem
import io.github.sceneview.loaders.ModelLoader
import kotlin.math.roundToInt

/**
 * Draggable, resizable container for a single 3D model.
 */
@Composable
fun ModelContainer(
    model: ModelItem,
    engine: Engine,
    modelLoader: ModelLoader,
    canvasWidth: Float,
    canvasHeight: Float,
    onDrag: (Offset) -> Unit,
    onResize: (Float) -> Unit,
    onToggleInteract: () -> Unit,
    onClose: () -> Unit,
    onTouchDown: () -> Unit
) {
    val sizeDp = model.size.dp

    // Use rememberUpdatedState to ensure the pointerInput blocks always use the latest callbacks
    // without restarting the gesture detection (which would happen if we used them as keys).
    val currentOnDrag by rememberUpdatedState(onDrag)
    val currentOnResize by rememberUpdatedState(onResize)
    val currentOnTouchDown by rememberUpdatedState(onTouchDown)

    val borderColor by animateColorAsState(
        targetValue    = if (model.isInteracting) Color(0xFF00E5FF) else Color(0x33FFFFFF),
        animationSpec  = tween(300),
        label          = "borderColor"
    )

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = model.position.x.roundToInt(),
                    y = model.position.y.roundToInt()
                )
            }
            .size(sizeDp)
            .pointerInput(model.id) {
                // Always detect touch down to bring to front
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    currentOnTouchDown()
                }
            }
    ) {
        // ---- 3D scene view ----
        SceneViewWrapper(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = if (model.isInteracting) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            engine = engine,
            modelLoader = modelLoader,
            assetPath = model.assetPath,
            isInteracting = model.isInteracting
        )

        // ---- GESTURE OVERLAY ----
        // When NOT interacting, we place a transparent box on top to capture container moves/resizes.
        if (!model.isInteracting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(model.id) {
                        detectTransformGestures(panZoomLock = false) { _, pan, zoom, _ ->
                            currentOnDrag(pan)
                            if (zoom != 1f) currentOnResize(zoom)
                        }
                    }
            )
        }

        // ---- Interaction mode badge ----
        if (model.isInteracting) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp),
                color  = Color(0xCC00E5FF),
                shape  = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text     = "INTERACT",
                    fontSize = 9.sp,
                    color    = Color.Black,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
        }

        // ---- Model name label (top right) ----
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp),
            color  = Color(0x88000000),
            shape  = RoundedCornerShape(4.dp)
        ) {
            Text(
                text     = model.displayName,
                fontSize = 9.sp,
                color    = Color.White,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                maxLines = 1
            )
        }

        // ---- Control buttons (always visible at bottom) ----
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ModelControlButton(
                onClick        = onToggleInteract,
                containerColor = if (model.isInteracting) Color(0xFF00E5FF) else Color(0xBB1C1C1C),
                contentColor   = if (model.isInteracting) Color.Black else Color.White,
                icon = {
                    Icon(
                        imageVector     = if (model.isInteracting) Icons.Default.ZoomOutMap else Icons.Default.Vrpano,
                        contentDescription = if (model.isInteracting) "Exit Interact" else "Interact",
                        modifier        = Modifier.size(15.dp)
                    )
                },
                label = if (model.isInteracting) "Done" else "Interact"
            )

            ModelControlButton(
                onClick        = onClose,
                containerColor = Color(0xBBAA0020),
                contentColor   = Color.White,
                icon = {
                    Icon(
                        imageVector        = Icons.Default.Close,
                        contentDescription = "Remove model",
                        modifier           = Modifier.size(15.dp)
                    )
                },
                label = "Close"
            )
        }
    }
}

/** Pill-shaped control button used in model containers. */
@Composable
private fun ModelControlButton(
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    icon: @Composable () -> Unit,
    label: String
) {
    Surface(
        onClick        = onClick,
        color          = containerColor,
        contentColor   = contentColor,
        shape          = RoundedCornerShape(20.dp),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier            = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon()
            Text(text = label, fontSize = 11.sp)
        }
    }
}
