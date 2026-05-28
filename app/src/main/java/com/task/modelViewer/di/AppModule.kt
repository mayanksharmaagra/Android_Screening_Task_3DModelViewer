package com.task.modelViewer.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module placeholder.
 * ModelRepository uses @Singleton + @Inject constructor directly,
 * so Hilt generates its binding automatically — no @Provides needed here.
 * Add future non-injectable dependencies (e.g. Room, Retrofit) in this file.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule
