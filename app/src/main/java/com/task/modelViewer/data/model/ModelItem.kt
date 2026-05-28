package com.task.modelViewer.data.model

import androidx.compose.ui.geometry.Offset

/**
 * Represents a 3D model placed on the canvas.
 *
 * @param id           Unique identifier for this instance
 * @param assetPath    Path to the .glb file inside assets/models/
 * @param displayName  Human-readable name shown in the UI
 * @param position     Position (x, y) of the container top-left corner on screen
 * @param size         Width and height of the draggable container in px
 * @param isInteracting Whether the model is in "interaction mode" (rotate/zoom)
 */
data class ModelItem(
    val id: String,
    val assetPath: String,
    val displayName: String,
    val position: Offset = Offset(100f, 200f),
    val size: Float = 320f,           // Container size in dp (square)
    val isInteracting: Boolean = false
)

/**
 * Catalog of bundled .glb models the user can pick from.
 */
data class ModelCatalogEntry(
    val assetPath: String,   // e.g. "models/helmet.glb"
    val displayName: String,
    val thumbnailRes: Int = 0
)
