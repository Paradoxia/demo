package se.paradoxia.pxdemo.util

import android.util.Log
import timber.log.Timber

/**
 * Created by mikael on 2018-01-28.
 */
class ReleaseTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return !(priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isLoggable(tag, priority)) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message)
            } else {
                Log.println(priority, tag, message)
            }
        }
    }
}