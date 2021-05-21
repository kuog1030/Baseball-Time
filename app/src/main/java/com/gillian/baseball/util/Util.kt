package com.gillian.baseball.util

import com.gillian.baseball.BaseballApplication

object Util {

    fun getString(resourceId: Int) : String{
        return BaseballApplication.instance.getString(resourceId)
    }
}