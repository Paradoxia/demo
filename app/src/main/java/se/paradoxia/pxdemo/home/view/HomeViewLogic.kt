package se.paradoxia.pxdemo.home.view

import android.content.Context
import android.content.res.Resources
import se.paradoxia.pxdemo.permission.PermissionResultReceiver

/**
 * Created by mikael on 2018-02-01.
 */
interface HomeViewLogic : HomeViewAction, PermissionResultReceiver {

    fun download(url: String)

    fun getLocalizedResources(context: Context, language: String): Resources

}


