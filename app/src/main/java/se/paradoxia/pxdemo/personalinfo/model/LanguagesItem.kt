package se.paradoxia.pxdemo.personalinfo.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class LanguagesItem : RealmObject() {

    @field:SerializedName("title")
    var title: String? = null

    @field:SerializedName("value")
    var value: Int? = null
}