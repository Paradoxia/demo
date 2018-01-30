package se.paradoxia.pxdemo

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by mikael on 2018-01-30.
 */
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("se.paradoxia.pxdemo", appContext.packageName)
    }

}