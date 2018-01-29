package se.paradoxia.pxdemo.service

import android.support.annotation.IdRes

/**
 * Created by mikael on 2018-01-28.
 */
interface RawResourceService {

    fun <E> readJson(@IdRes resId: Int, clazz: Class<E>): E

}