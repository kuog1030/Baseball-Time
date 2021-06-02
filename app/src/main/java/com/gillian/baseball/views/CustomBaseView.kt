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


    //val layout: ConstraintLayout = LayoutInflater.from(context).inflate(R.layout.like_button, this, true) as ConstraintLayout

    var onBase: Int = 0
        set(value) {
            field = value
            Log.i("gillian", "on base is set again $value")
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


    init {
        //context.obtainStyledAttributes(attrs, R.styleable.CustomBaseView)

    }

    fun setBaseList(base: Int = 0) {
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

//class CustomBaseView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
//
//    private val greyPaint = Paint().apply {
//        color = Color.GRAY
//    }
//
//    private val yellowPaint = Paint().apply {
//        color = Color.YELLOW
//    }
//
//    private val path = Path()
//
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        drawSecondBase(canvas)
//        drawFirstBase(canvas)
//        drawThirdBase(canvas)
//    }
//
//    private fun drawFirstBase(canvas: Canvas?) {
//        path.reset()
//        path.moveTo(290F, 90F)
//        path.lineTo(360F, 161F)
//        path.lineTo(290F, 232F)
//        path.lineTo(220F, 161F)
//        path.lineTo(290F, 90F)
//        canvas?.drawPath(path, greyPaint)
//    }
//
//    private fun drawSecondBase(canvas: Canvas?) {
//        path.reset()
//        path.moveTo(200F, 0F)
//        path.lineTo(270F, 70F)
//        path.lineTo(200F, 141F)
//        path.lineTo(130F, 70F)
//        path.lineTo(200F, 0F)
//        canvas?.drawPath(path, greyPaint)
//    }
//
//    private fun drawThirdBase(canvas: Canvas?) {
//        path.reset()
//        path.moveTo(110F, 90F)
//        path.lineTo(180F, 161F)
//        path.lineTo(110F, 232F)
//        path.lineTo(40F, 161F)
//        path.lineTo(110F, 90F)
//        canvas?.drawPath(path, greyPaint)
//    }
//}