package se.paradoxia.pxdemo.model.infocard

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class InfoCardResponse : RealmObject() {

    @PrimaryKey
    @field:SerializedName("id")
    var id: String? = null

    @field:SerializedName("downloadFile")
    var downloadFile: DownloadFile? = null

    @field:SerializedName("twitter")
    var twitter: String? = null

    @field:SerializedName("role")
    var role: Role? = null

    @field:SerializedName("facebook")
    var facebook: String? = null

    @field:SerializedName("name")
    var name: String? = null

    @field:SerializedName("google")
    var google: String? = null

    @field:SerializedName("instagram")
    var instagram: String? = null

    @field:SerializedName("linkedin")
    var linkedin: String? = null

    @field:SerializedName("profileImage")
    var profileImage: ProfileImage? = null

    @field:SerializedName("downloadText")
    var downloadText: DownloadText? = null
}