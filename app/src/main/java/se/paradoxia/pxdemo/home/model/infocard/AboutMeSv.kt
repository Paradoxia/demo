package se.paradoxia.pxdemo.home.model.infocard

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class AboutMeSv : RealmObject() {

        @field:SerializedName("text")
        var text: String? = null

        @field:SerializedName("title")
        var title: String? = null

        @field:SerializedName("headline")
        var headline: String? = null
}