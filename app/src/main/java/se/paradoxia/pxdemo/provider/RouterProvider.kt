package se.paradoxia.pxdemo.provider

import android.support.v7.app.AppCompatActivity
import se.paradoxia.pxdemo.service.RouterService

/**
 * Created by mikael on 2018-02-01.
 */
class RouterProvider : RouterService {

    private var currentActivity: AppCompatActivity? = null

    override fun setCurrentActivityContext(activity: AppCompatActivity) {
        this.currentActivity = activity
    }

    override fun getCurrentActivityContext(): AppCompatActivity? {
        return currentActivity
    }

}