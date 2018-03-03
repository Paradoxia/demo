package se.paradoxia.pxdemo.personalinfo.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class PersonalInfoFields : RealmObject() {

    @field:SerializedName("fullName")
    var fullName: String? = null

    @field:SerializedName("dateOfBirth")
    var dateOfBirth: String? = null

    @field:SerializedName("residence")
    var residence: String? = null

    @field:SerializedName("email")
    var email: String? = null

    @field:SerializedName("phone")
    var phone: String? = null

    @field:SerializedName("status")
    var status: String? = null

    @field:SerializedName("piTitle")
    var piTitle: String? = null

    @field:SerializedName("langTitle")
    var langTitle: String? = null

}