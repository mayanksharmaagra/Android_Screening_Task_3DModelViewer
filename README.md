# 3D Model Viewer - Android Developer Task

A high-performance, single-activity Android application built with Jetpack Compose that allows users to place, move, resize, and interact with multiple 3D models simultaneously.

## 3D Library: SceneView (Filament)
I chose **[SceneView 4.x](https://github.com/SceneView/sceneview-android)** for this project.
- **Why:** It is a Kotlin-native, Compose-friendly successor to Sceneform, built directly on Google's **Filament** PBR engine.
- **Performance:** Filament is designed for mobile devices. It provides high-quality Physically Based Rendering (PBR) with a very small memory footprint and excellent performance on low-end hardware compared to heavier engines like Unity or older OpenGL wrappers.

## Performance Optimizations
Performance was treated as the primary requirement for this task:

1.  **Shared Rendering Engine:** Instead of each model container creating its own Filament engine (which would crash low-end devices due to GPU memory exhaustion), the app uses a **single shared Engine and ModelLoader** hoisted at the screen level.
2.  **Device-Tiered Constraints:** I implemented a `DeviceCapability` utility that detects the device tier (RAM/API level). The app dynamically caps the maximum number of concurrent models (e.g., 3 for Low-end, 5 for Mid-range) to prevent Out-Of-Memory (OOM) crashes.
3.  **Large Heap Support:** Enabled `android:largeHeap="true"` in the Manifest to allow the app more headroom when decoding multiple high-resolution 3D meshes.
4.  **Zero-Compression Assets:** Configured Gradle to disable compression for `.glb` files. This prevents the CPU from having to decompress models into RAM during loading, leading to faster "Add Model" actions.
5.  **State Optimization:** Used Compose `offset {}` and `size()` modifiers with `IntOffset` to ensure that dragging a model only updates its position without triggering a full recomposition of the 3D SceneView itself.

## Separation of Modes (Requirement 7)
To ensure that "Normal Mode" (Move/Resize) and "Interact Mode" (Rotate/Zoom) never mix:
- **Normal Mode:** A transparent overlay captures all gestures. A 1-finger drag moves the container; a 2-finger pinch resizes it. Touches never reach the 3D engine.
- **Interact Mode:** The overlay is removed, and touch events are forwarded directly to the `SceneView` camera manipulator, allowing for intuitive 3D rotation and zooming.

## Trade-offs
- **Z-Order Management:** I implemented a "Bring to Front" strategy where the most recently touched model is moved to the end of the list (rendered last/on top). This is efficient for Compose but doesn't handle complex 3D intersections between two separate containers.
- **Fixed Scaling:** Models are normalized using `scaleToUnits(1.0f)`. This ensures consistency across different model sizes but means very long/thin models might look small compared to a cube in the same container.

## Future Improvements
- **Environment Lighting:** Implementing Custom Image-Based Lighting (IBL) for more realistic surface reflections.
- **Persistent State:** Saving the canvas layout to a local database (Room) so it persists across app restarts.
- **Advanced Gestures:** Adding inertia/friction to the rotation and movement for a more "premium" feel.

## Known Limitations
- **Memory Pressure:** While capped, loading 5+ extremely complex 3D models (high vertex count) may still challenge devices with less than 2GB of available RAM.
- **Overlap:** There is no "collision detection" between containers; they can be placed directly on top of each other.
