package se.paradoxia.pxdemo.home.model.aboutme

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class DownloadText : RealmObject() {

    @field:SerializedName("sv")
    var sv: String? = null

    @field:SerializedName("en")
    var en: String? = null

}