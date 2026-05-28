package com.task.modelViewer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class - entry point for Hilt DI.
 * largeHeap=true in manifest helps with multiple 3D models in memory.
 */
@HiltAndroidApp
class ModelViewerApp : Application()
