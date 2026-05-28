package com.task.modelViewer.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.filament.Engine
import io.github.sceneview.SceneView
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberModelInstance

/**
 * Wraps SceneView (Filament PBR renderer).
 * Using SceneView 4.x Compose-native API.
 */
@Composable
fun SceneViewWrapper(
    modifier: Modifier = Modifier,
    engine: Engine,
    modelLoader: ModelLoader,
    assetPath: String,
    isInteracting: Boolean
) {
    // Camera manipulator handles rotate/zoom in Interaction Mode.
    val cameraManipulator = if (isInteracting) rememberCameraManipulator() else null

    SceneView(
        modifier = modifier,
        engine = engine,
        modelLoader = modelLoader,
        cameraManipulator = cameraManipulator
    ) {
        val modelInstance = rememberModelInstance(
            modelLoader = modelLoader,
            assetFileLocation = assetPath
        )
        
        if (modelInstance != null) {
            ModelNode(
                modelInstance = modelInstance,
                scaleToUnits = 1.0f,
                autoAnimate = true
            )
        }
    }
}
