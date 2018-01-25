package se.paradoxia.pxdemo.model.aboutme

import com.google.gson.annotations.SerializedName

data class AboutMeResponse(

        @field:SerializedName("sv")
        val sv: Sv? = null,

        @field:SerializedName("en")
        val en: En? = null,

        @field:SerializedName("id")
        val id: String? = null
)