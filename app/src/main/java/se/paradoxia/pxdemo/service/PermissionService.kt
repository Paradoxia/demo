package se.paradoxia.pxdemo.service

import android.support.v7.app.AppCompatActivity
import se.paradoxia.pxdemo.permission.PermissionViewModel

/**
 * Created by mikael on 2018-01-31.
 */

interface PermissionService {

    fun havePermission(activity: AppCompatActivity, permission: String, viewModel: PermissionViewModel?): Boolean

    fun permissionToRequestCode(permission: String): Int

}