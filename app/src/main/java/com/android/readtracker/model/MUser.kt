package com.android.readtracker.model

data class MUser(
    val id: String?,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
    val quote: String,
    val profession: String){


    fun toMap():MutableMap<String,Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "first_name" to this.firstName,
            "last_name" to this.lastName,
            "avatar_url" to this.avatarUrl,
            "quote" to this.quote,
            "profession" to this.profession
        )
    }

}



