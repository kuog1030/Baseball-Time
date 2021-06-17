package com.gillian.baseball.util

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.gillian.baseball.BaseballApplication

object Util {

    fun getString(resourceId: Int): String {
        return BaseballApplication.instance.getString(resourceId)
    }

    fun getAnim(resourceId: Int): Animation {
        return AnimationUtils.loadAnimation(BaseballApplication.instance, resourceId)
    }
}