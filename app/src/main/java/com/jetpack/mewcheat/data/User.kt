package com.jetpack.mewcheat.data

import androidx.annotation.DrawableRes
import com.jetpack.mewcheat.R

class User(
    val id: String,
    val name: String,
    @DrawableRes val avatar: Int
) {
    companion object {
        val Me: User = User("xiaofei", "渣渣辉", R.mipmap.avatar)
    }
}