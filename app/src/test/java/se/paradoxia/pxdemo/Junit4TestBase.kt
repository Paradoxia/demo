package se.paradoxia.pxdemo

import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@RunWith(BlockJUnit4ClassRunner::class)
abstract class Junit4TestBase {

    @Before
    abstract fun setUp()

    @After
    abstract fun tearDown()

    /*
     * Too prevent "getMainLooper" is not mocked we use the "InstantTaskExecutorRule"
     * which make sure each task execute synchronously
     */
    @Suppress("unused")
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

}