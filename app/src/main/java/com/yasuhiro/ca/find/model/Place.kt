package com.yasuhiro.ca.find.model

import com.google.firebase.database.Exclude

data class Place(val placeName: String = "", val discription: String = "", val address: String?) {
    // [START post_to_map]
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "placeName" to placeName,
                "discription" to discription,
                "starCount" to address
        )
    }
}