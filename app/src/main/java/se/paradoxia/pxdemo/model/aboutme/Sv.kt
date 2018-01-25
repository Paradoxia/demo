package se.paradoxia.pxdemo.model.aboutme

import com.google.gson.annotations.SerializedName

data class Sv(

        @field:SerializedName("text")
        val text: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("headline")
        val headline: String? = null
)