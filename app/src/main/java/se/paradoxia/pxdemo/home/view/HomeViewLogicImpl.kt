package se.paradoxia.pxdemo.home.view

import android.Manifest
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Environment
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.di.ActivityContext
import se.paradoxia.pxdemo.permission.PermissionViewModel
import se.paradoxia.pxdemo.service.PermissionService
import se.paradoxia.pxdemo.util.AllOpen
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-31.
 */
@AllOpen
class HomeViewLogicImpl @Inject constructor(@ActivityContext val context: Context, val permissionService: PermissionService) :
    HomeViewLogic {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var saveToStorageUrl: String? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionService.permissionToRequestCode(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    Timber.d("%s granted", permissions[0])
                    if (saveToStorageUrl != null) {
                        download(saveToStorageUrl!!)
                    }
                } else {
                    Timber.d("%s denied", permissions[0])
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    override fun download(url: String) {

        val uri = Uri.parse(url)
        val fileName = uri.lastPathSegment
        val request = DownloadManager.Request(uri)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    override fun getLocalizedResources(context: Context, language: String): Resources {
        val locale = Locale(language)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        val localizedContext = context.createConfigurationContext(configuration)
        return localizedContext.resources
    }

    override fun saveToStorage(url: String, language: String) {
        this.saveToStorageUrl = url
        val permissionViewModel = PermissionViewModel()
        permissionViewModel.explanation.set(
            getLocalizedResources(
                context,
                language
            ).getString(R.string.download_permission_explanation)!!
        )
        val permitted = permissionService.havePermission(
            context as AppCompatActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            permissionViewModel
        ).let {
            it ?: false
        }
        if (permitted) {
            download(url)
        }

    }

    override fun openExternalSite(url: String) {
        try {
            val externalSiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            externalSiteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            context.startActivity(externalSiteIntent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e(ex)
        }
    }


}