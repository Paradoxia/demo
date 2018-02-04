package se.paradoxia.pxdemo.permission

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.databinding.ExplainPermissionBinding
import se.paradoxia.pxdemo.service.PermissionService
import se.paradoxia.pxdemo.util.Crc16
import timber.log.Timber

/**
 * Created by mikael on 2018-01-29.
 */

class PermissionProvider : PermissionService {

    override fun havePermission(
        activity: AppCompatActivity,
        permission: String,
        viewModel: PermissionViewModel?
    ): Boolean {

        if (ContextCompat.checkSelfPermission(activity, permission)
            != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                val builder = AlertDialog.Builder(activity)
                val binding: ExplainPermissionBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(activity.baseContext),
                    R.layout.explain_permission,
                    null,
                    false
                )
                binding.explainPermission = viewModel
                builder.setView(binding.root)
                val dialog = builder.create()
                binding.explainPermissionEvent = object : ExplainDialogEvent {
                    override fun onClick(view: View) {
                        dialog.cancel()
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(permission),
                            permissionToRequestCode(permission)
                        )
                    }
                }
                dialog.show()
                return false
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), permissionToRequestCode(permission))
                return false
            }
        } else {
            Timber.d("%s already permitted", permission)
            return true
        }

    }

    override fun permissionToRequestCode(permission: String): Int {
        return Crc16().crc16(permission.toByteArray())
    }

}