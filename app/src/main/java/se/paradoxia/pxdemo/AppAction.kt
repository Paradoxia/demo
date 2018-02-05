package se.paradoxia.pxdemo

/**
 * Created by mikael on 2018-02-05.
 */
interface AppAction {

    fun selectLangEN()

    fun selectLangSV()

}

interface AppActionReceiver {

    fun registerAppActionReceiver(appAction: AppAction)

}