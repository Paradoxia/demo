package se.paradoxia.pxdemo.model.infocard

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class Role : RealmObject() {

    @field:SerializedName("sv")
    var sv: String? = null

    @field:SerializedName("en")
    var en: String? = null
}