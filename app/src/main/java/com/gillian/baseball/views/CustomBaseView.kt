package com.gillian.baseball.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R

class CustomBaseView : ConstraintLayout {

    private lateinit var first: ImageView
    private lateinit var second: ImageView
    private lateinit var third: ImageView

    var onBase: Int = 0
        set(value) {
            field = value
            setBaseList(value)
        }

    private var baseInt: Int = 0

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet? = null) {
        context.theme.obtainStyledAttributes(
                attrs, R.styleable.CustomBaseView, 0, 0).apply {
            try {
                baseInt = getInteger(R.styleable.CustomBaseView_onBase, 0)
            } finally {
                recycle()
            }
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_base, this, true)

        first = view.findViewById(R.id.base_first)
        second = view.findViewById(R.id.base_second)
        third = view.findViewById(R.id.base_third)

    }


    private fun setBaseList(base: Int = 0) {
        if (base / 100 != 0) {
            third.setColorFilter(BaseballApplication.instance.getColor(R.color.yellow_strike))
        } else {
            third.setColorFilter(BaseballApplication.instance.getColor(R.color.grey_ddd))
        }

        if ((base / 10 % 10) != 0) {
            second.setColorFilter(BaseballApplication.instance.getColor(R.color.yellow_strike))
        } else {
            second.setColorFilter(BaseballApplication.instance.getColor(R.color.grey_ddd))
        }

        if (base % 10 != 0) {
            first.setColorFilter(BaseballApplication.instance.getColor(R.color.yellow_strike))
        } else {
            first.setColorFilter(BaseballApplication.instance.getColor(R.color.grey_ddd))
        }
    }

}