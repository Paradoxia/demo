package se.paradoxia.pxdemo.service

/**
 * Created by mikael on 2018-01-22.
 */
interface SharedPreferencesService {

    fun getString(key: String, groupKey: String? = null, defaultValue: String? = null): String?
    fun getBoolean(key: String, groupKey: String? = null, defaultValue: Boolean? = null): Boolean?
    fun getInt(key: String, groupKey: String? = null, defaultValue: Int? = null): Int?

    fun putString(key: String, value: String?, groupKey: String? = null)
    fun putBoolean(key: String, value: Boolean?, groupKey: String? = null)
    fun putInt(key: String, value: Int?, groupKey: String? = null)

    fun removeString(key: String, groupKey: String? = null)
    fun removeBoolean(key: String, groupKey: String? = null)
    fun removeInt(key: String, groupKey: String? = null)
    fun removeAll(groupKey: String? = null)

}