package se.paradoxia.pxdemo.personalinfo.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class PersonalInfoResponse : RealmObject() {

    @PrimaryKey
    @field:SerializedName("id")
    var id: String? = null

    @field:SerializedName("sv")
    var personalInfoSv: PersonalInfo? = null

    @field:SerializedName("en")
    var personalInfoEn: PersonalInfo? = null

}