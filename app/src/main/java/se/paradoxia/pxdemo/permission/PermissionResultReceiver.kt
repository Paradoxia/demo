package se.paradoxia.pxdemo.permission

/**
 * Created by mikael on 2018-01-29.
 */
interface PermissionResultReceiver {

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

}