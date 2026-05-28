package com.task.modelViewer.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Build

/**
 * Utility to detect device tier so we can tune rendering quality.
 *
 * Tier 1 = Low-end  : < 2 GB RAM, API < 28
 * Tier 2 = Mid-range: 2-4 GB RAM
 * Tier 3 = High-end : > 4 GB RAM, API 29+
 */
object DeviceCapability {

    enum class Tier { LOW, MID, HIGH }

    fun getTier(context: Context): Tier {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)
        val totalRamMb = info.totalMem / (1024 * 1024)

        return when {
            totalRamMb < 2048 || Build.VERSION.SDK_INT < 28 -> Tier.LOW
            totalRamMb < 4096 -> Tier.MID
            else -> Tier.HIGH
        }
    }

    /**
     * Max models we allow simultaneously based on device tier.
     * Low-end devices get fewer to avoid OOM crashes.
     */
    fun maxConcurrentModels(context: Context): Int = when (getTier(context)) {
        Tier.LOW  -> 3
        Tier.MID  -> 5
        Tier.HIGH -> 8
    }
}
