package com.task.modelViewer.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.task.modelViewer.data.model.ModelCatalogEntry
import com.task.modelViewer.data.model.ModelItem
import com.task.modelViewer.data.repository.ModelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel for the single canvas screen.
 * Thin delegation layer — all business logic lives in ModelRepository.
 */
@HiltViewModel
class ModelViewerViewModel @Inject constructor(
    private val repository: ModelRepository
) : ViewModel() {

    val placedModels: StateFlow<List<ModelItem>> = repository.placedModels
    val catalog: List<ModelCatalogEntry>         = repository.catalog

    fun addModel(entry: ModelCatalogEntry, screenWidth: Float, screenHeight: Float) =
        repository.addModel(entry, screenWidth, screenHeight)

    fun removeModel(id: String) =
        repository.removeModel(id)

    fun updatePosition(id: String, position: Offset) =
        repository.updatePosition(id, position)

    fun moveModel(id: String, delta: Offset) =
        repository.moveModel(id, delta)

    fun updateSize(id: String, size: Float) =
        repository.updateSize(id, size)

    fun scaleModel(id: String, scaleFactor: Float) =
        repository.scaleModel(id, scaleFactor)

    fun toggleInteraction(id: String) =
        repository.toggleInteractionMode(id)

    fun bringToFront(id: String) =
        repository.bringToFront(id)
}
