package se.paradoxia.pxdemo.provider

import android.app.Application
import android.content.Context
import se.paradoxia.pxdemo.service.SharedPreferencesService
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-22.
 */

const val DEFAULT_GROUP_KEY = "default"

class SharedPreferencesProvider @Inject constructor(private val app: Application) : SharedPreferencesService {


    override fun getString(key: String, groupKey: String?, defaultValue: String?): String? {
        val sharedPref = app.baseContext.getSharedPreferences(
            groupKey
                    ?: DEFAULT_GROUP_KEY, Context.MODE_PRIVATE
        )
        return sharedPref.getString(key, defaultValue)
    }

    override fun getBoolean(key: String, groupKey: String?, defaultValue: Boolean?): Boolean? {
        val sharedPref = app.baseContext.getSharedPreferences(
            groupKey
                    ?: DEFAULT_GROUP_KEY, Context.MODE_PRIVATE
        )
        return if (sharedPref.contains(key)) {
            sharedPref.getBoolean(key, false)
        } else {
            defaultValue
        }
    }

    override fun getInt(key: String, groupKey: String?, defaultValue: Int?): Int? {
        val sharedPref = app.baseContext.getSharedPreferences(
            groupKey
                    ?: DEFAULT_GROUP_KEY, Context.MODE_PRIVATE
        )
        return if (sharedPref.contains(key)) {
            sharedPref.getInt(key, 0)
        } else {
            defaultValue
        }
    }

    override fun putString(key: String, value: String?, groupKey: String?) {
        val sharedPref = app.baseContext.getSharedPreferences(
            groupKey
                    ?: DEFAULT_GROUP_KEY, Context.MODE_PRIVATE
        )
        if (value != null) {
            with(sharedPref.edit()) {
                putString(key, value)
                commit()
            }
        } else {
            removeString(key)
        }
    }

    override fun putBoolean(key: String, value: Boolean?, groupKey: String?) {
        val sharedPref = app.baseContext.getSharedPreferences(
            groupKey
                    ?: DEFAULT_GROUP_KEY, Context.MODE_PRIVATE
        )
        if (value != null) {
            with(sharedPref.edit()) {
                putBoolean(key, value)
                commit()
            }
        } else {
            removeBoolean(key)
        }
    }

    override fun putInt(key: String, value: Int?, groupKey: String?) {
        val sharedPref = app.baseContext.getSharedPreferences(
            groupKey
                    ?: DEFAULT_GROUP_KEY, Context.MODE_PRIVATE
        )
        if (value != null) {
            with(sharedPref.edit()) {
                putInt(key, value)
                commit()
            }
        } else {
            removeInt(key)
        }
    }

    override fun removeString(key: String, groupKey: String?) {
        removeByKey(key, groupKey)
    }

    override fun removeBoolean(key: String, groupKey: String?) {
        removeByKey(key, groupKey)
    }

    override fun removeInt(key: String, groupKey: String?) {
        removeByKey(key, groupKey)
    }

    private fun removeByKey(key: String, groupKey: String?) {
        val sharedPref = app.baseContext.getSharedPreferences(
            groupKey
                    ?: DEFAULT_GROUP_KEY, Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            remove(key)
            commit()
        }
    }

    override fun removeAll(groupKey: String?) {
        val sharedPref = app.baseContext.getSharedPreferences(
            groupKey
                    ?: DEFAULT_GROUP_KEY, Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            clear()
            commit()
        }
    }

}