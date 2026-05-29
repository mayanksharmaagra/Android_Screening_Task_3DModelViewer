package com.task.modelViewer.data.repository

import androidx.compose.ui.geometry.Offset
import com.task.modelViewer.data.model.ModelCatalogEntry
import com.task.modelViewer.data.model.ModelItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for all placed models on screen.
 *
 * Performance note: StateFlow emits a new list reference only when mutated.
 * Compose reads it via collectAsStateWithLifecycle() — lifecycle-aware, no leaks.
 */
@Singleton
class ModelRepository @Inject constructor() {

    // ---- Catalog of bundled models the user can choose ----
    val catalog: List<ModelCatalogEntry> = listOf(
        ModelCatalogEntry("models/DamagedHelmet.glb", "Damaged Helmet"),
        ModelCatalogEntry("models/Avocado.glb",       "Avocado"),
        ModelCatalogEntry("models/Box.glb",           "Box"),
        ModelCatalogEntry("models/Duck.glb",          "Duck")
    )

    // ---- Live list of models placed on the canvas ----
    private val _placedModels = MutableStateFlow<List<ModelItem>>(emptyList())
    val placedModels: StateFlow<List<ModelItem>> = _placedModels.asStateFlow()

    /** Add a new model to the canvas with a cascading start position. */
    fun addModel(entry: ModelCatalogEntry, screenWidth: Float, screenHeight: Float) {
        val current = _placedModels.value
        // Better cascading: avoid perfect overlap
        val count = current.size
        val offsetX = (count % 3) * 150f
        val offsetY = (count / 3) * 150f
        
        val newModel = ModelItem(
            id           = UUID.randomUUID().toString(),
            assetPath    = entry.assetPath,
            displayName  = entry.displayName,
            position     = Offset(
                x = 100f + offsetX,
                y = 200f + offsetY
            ),
            size         = 320f
        )
        _placedModels.value = current + newModel
    }

    /** Remove a model from the canvas by id. */
    fun removeModel(id: String) {
        _placedModels.value = _placedModels.value.filter { it.id != id }
    }

    /** Move a container by a relative delta. */
    fun moveModel(id: String, delta: Offset) {
        _placedModels.value = _placedModels.value.map {
            if (it.id == id) {
                it.copy(position = it.position + delta)
            } else it
        }
    }

    /** Move a container to a new absolute position. */
    fun updatePosition(id: String, newPosition: Offset) {
        _placedModels.value = _placedModels.value.map {
            if (it.id == id) it.copy(position = newPosition) else it
        }
    }

    /** Scale a container by a factor. Clamped between 120 dp and 700 dp. */
    fun scaleModel(id: String, scaleFactor: Float) {
        _placedModels.value = _placedModels.value.map {
            if (it.id == id) {
                val newSize = (it.size * scaleFactor).coerceIn(120f, 700f)
                it.copy(size = newSize)
            } else it
        }
    }

    /** Resize a container. Clamped between 120 dp and 700 dp. */
    fun updateSize(id: String, newSize: Float) {
        _placedModels.value = _placedModels.value.map {
            if (it.id == id) it.copy(size = newSize.coerceIn(120f, 700f)) else it
        }
    }

    /** Toggle between Normal mode and Interaction mode for a model. */
    fun toggleInteractionMode(id: String) {
        _placedModels.value = _placedModels.value.map {
            if (it.id == id) it.copy(isInteracting = !it.isInteracting) else it
        }
    }

    /**
     * Bring a model to the front of the z-stack.
     * Last item in the list is rendered on top.
     */
    fun bringToFront(id: String) {
        val list = _placedModels.value.toMutableList()
        val index = list.indexOfFirst { it.id == id }
        if (index >= 0 && index != list.lastIndex) {
            val item = list.removeAt(index)
            list.add(item)
            _placedModels.value = list
        }
    }
}
