package se.paradoxia.pxdemo.home

import android.Manifest
import android.app.DownloadManager
import android.app.Fragment
import android.content.ActivityNotFoundException
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_home.*
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.permission.FragmentPermissionReceiver
import se.paradoxia.pxdemo.permission.PermissionHelper
import se.paradoxia.pxdemo.permission.PermissionViewModel
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by mikael on 2018-01-24.
 */
class HomeFragment : Fragment(), HomeViewAction, FragmentPermissionReceiver {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    private var permissionHelper: PermissionHelper? = null

    private var saveToStorageUrl: String? = null

    companion object {
        fun newInstance(): Fragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        AndroidInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return activity.layoutInflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.init(this)
        val layoutManager = LinearLayoutManager(activity)
        recViewHome.layoutManager = layoutManager
        recViewHome.adapter = HomeRecyclerViewAdapter(homeViewModel.getCards())
        permissionHelper = PermissionHelper(this.activity as AppCompatActivity)

    }

    override fun saveToStorage(url: String) {
        this.saveToStorageUrl = url
        val permissionViewModel = PermissionViewModel()
        val permitted = permissionHelper?.havePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionViewModel).let {
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
            this.startActivity(externalSiteIntent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e(ex)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionHelper?.permissionToRequestCode(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
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

    private fun download(url: String) {
        val uri = Uri.parse(url)
        val fileName = uri.lastPathSegment
        val r = DownloadManager.Request(uri)
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        r.allowScanningByMediaScanner()
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val dm = activity.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(r)
    }

}