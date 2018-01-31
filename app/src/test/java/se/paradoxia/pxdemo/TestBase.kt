package se.paradoxia.pxdemo

import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@RunWith(BlockJUnit4ClassRunner::class)
abstract class TestBase {

    @Before
    abstract fun setUp()

    @After
    abstract fun tearDown()

}