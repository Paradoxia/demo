package se.paradoxia.pxdemo.provider

import android.content.Context
import android.support.v7.app.AppCompatActivity
import se.paradoxia.pxdemo.di.ActivityContext
import se.paradoxia.pxdemo.service.RouterService
import javax.inject.Inject

/**
 * Created by mikael on 2018-02-01.
 */
class RouterProvider @Inject constructor(@ActivityContext private val context: Context) : RouterService {

    private var currentActivity: AppCompatActivity? = null

    override fun setCurrentActivityContext(activity: AppCompatActivity) {
        this.currentActivity = activity
    }

    override fun getCurrentActivityContext(): AppCompatActivity? {
        return currentActivity
    }

}