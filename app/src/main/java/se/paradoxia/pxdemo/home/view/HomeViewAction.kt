package se.paradoxia.pxdemo.home.view

/**
 * Created by mikael on 2018-01-29.
 */
interface HomeViewAction {

    fun openExternalSite(url: String)

    fun saveToStorage(url: String, language: String)

}