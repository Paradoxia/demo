package se.paradoxia.pxdemo

import com.google.gson.Gson
import se.paradoxia.pxdemo.home.HomeViewModelTest

/**
 * Created by mikael on 2018-01-31.
 */

/**
 * sourceSets defined to include "src/main/res/raw" in tests
 */
inline fun <reified E> rawResourceToInstance(rawFileName: String): E {
    val textAsJson = HomeViewModelTest::class.java.classLoader.getResource(rawFileName).readText()
    return Gson().fromJson(textAsJson, E::class.java)
}