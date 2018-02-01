package se.paradoxia.pxdemo.service

import android.support.v7.app.AppCompatActivity

/**
 * Created by mikael on 2018-02-01.
 */
interface RouterService {

    fun setCurrentActivityContext(activity: AppCompatActivity)

    fun getCurrentActivityContext(): AppCompatActivity?

}