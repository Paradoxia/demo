package se.paradoxia.pxdemo.provider

import android.annotation.SuppressLint
import android.support.annotation.IdRes
import com.google.gson.Gson
import se.paradoxia.pxdemo.di.App
import se.paradoxia.pxdemo.service.RawResourceService
import java.io.InputStreamReader
import javax.inject.Inject


/**
 * Created by mikael on 2018-01-28.
 */
class RawResourceProvider @Inject constructor(private val app: App) : RawResourceService {

    @SuppressLint("ResourceType")
    override fun <E> readJson(@IdRes resId: Int, clazz: Class<E>): E {
        val input = app.resources.openRawResource(resId)
        val reader = InputStreamReader(input, "UTF-8")
        return Gson().fromJson(reader, clazz)
    }
}