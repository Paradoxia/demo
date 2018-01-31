package se.paradoxia.pxdemo

import android.content.Context
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric.flushBackgroundThreadScheduler
import org.robolectric.Robolectric.flushForegroundThreadScheduler
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication.runBackgroundTasks
import org.robolectric.shadows.ShadowLooper.runUiThreadTasksIncludingDelayedTasks

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
abstract class RobolectricTestBase  {

    @Before
    abstract fun setUp()

    @After
    abstract fun tearDown()

    fun finishThreads() {
        runBackgroundTasks()
        flushForegroundThreadScheduler()
        flushBackgroundThreadScheduler()
        runUiThreadTasksIncludingDelayedTasks()
    }

    companion object {
        protected val CONTEXT: Context = RuntimeEnvironment.application
        protected val APPLICATION = RuntimeEnvironment.application
    }
}