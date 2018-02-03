package se.paradoxia.pxdemo.home.model.aboutme

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class ProfileImage : RealmObject() {

    @field:SerializedName("x1")
    var x1: String? = null

    @field:SerializedName("x2")
    var x2: String? = null
}