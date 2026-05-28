package com.task.modelViewer.viewmodel

/*
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.task.modelViewer.model.ModelItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import kotlin.collections.emptyList

/**
 * ViewModel holds all mutable UI state so it survives recomposition.
 *
 * Performance note:
 *   • We use a List<ModelItem> inside a StateFlow. Updates create a new list
 *     (immutable snapshots) so Compose can diff cheaply with `key {}`.
 *   • Heavy SceneView/Filament objects are NOT stored here — they live in
 *     AndroidView's update lambda, keyed by ModelItem.id.
 */
class MainViewModel : ViewModel() {

    // ---- Placed models on canvas ----
    private val _models = MutableStateFlow<List<ModelItem>>(emptyList())
    val models: StateFlow<List<ModelItem>> = _models.asStateFlow()

    // ---- Picker sheet visibility ----
    private val _showPicker = MutableStateFlow(false)
    val showPicker: StateFlow<Boolean> = _showPicker.asStateFlow()

    // ---- Z-order: last touched model is on top ----
    private val _zOrder = MutableStateFlow<List<String>>(emptyList())
    val zOrder: StateFlow<List<String>> = _zOrder.asStateFlow()

    // -------- Public actions --------

    fun openPicker() { _showPicker.value = true }
    fun closePicker() { _showPicker.value = false }

    fun addModel(assetPath: String, displayName: String) {
        val id = UUID.randomUUID().toString()
        val currentCount = _models.value.size
        val startOffset = Offset(
            x = 60f + (currentCount % 4) * 60f,
            y = 160f + (currentCount % 3) * 80f
        )
        val newItem = ModelItem(
            id = id,
            assetPath = assetPath,
            displayName = displayName,
            position = startOffset
        )
        _models.value = _models.value + newItem
        bringToFront(id)
        closePicker()
    }

    fun removeModel(id: String) {
        _models.value = _models.value.filter { it.id != id }
        _zOrder.value = _zOrder.value.filter { it != id }
    }

    fun moveModel(id: String, newPosition: Offset) {
        _models.value = _models.value.map {
            if (it.id == id) it.copy(position = newPosition) else it
        }
    }

    fun resizeModel(id: String, newSize: Float) {
        val clamped = newSize.coerceIn(120f, 480f)
        _models.value = _models.value.map {
            if (it.id == id) it.copy(size = clamped) else it
        }
    }

    fun toggleInteractionMode(id: String) {
        _models.value = _models.value.map {
            if (it.id == id) it.copy(isInteractionMode = !it.isInteractionMode) else it
        }
        bringToFront(id)
    }

    fun bringToFront(id: String) {
        _zOrder.value = (_zOrder.value.filter { it != id }) + id
    }

    fun getModel(id: String): ModelItem? = _models.value.find { it.id == id }

    val catalog get() = ModelCatalog.assets
}
*/
