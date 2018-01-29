package se.paradoxia.pxdemo.model.aboutme

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class AboutMeResponse : RealmObject() {

    @PrimaryKey
    @field:SerializedName("id")
    var id: String? = null

    @field:SerializedName("sv")
    var aboutMeSv: AboutMeSv? = null

    @field:SerializedName("en")
    var aboutMeEn: AboutMeEn? = null

}