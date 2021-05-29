package com.gillian.baseball.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gillian.baseball.BaseballApplication
import com.gillian.baseball.R

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



class CustomBaseView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var first : ImageView
    private var second : ImageView
    private var third : ImageView
    private var hitter : TextView

    private val attributes by lazy {
        context.obtainStyledAttributes(attrs, R.styleable.CustomBaseView)
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_base, this, true)

        first = view.findViewById(R.id.base_first)
        second = view.findViewById(R.id.base_second)
        third = view.findViewById(R.id.base_third)
        hitter = view.findViewById(R.id.base_hitter)


    }

    fun setFirst() {
        first.setImageDrawable(BaseballApplication.instance.getDrawable(R.drawable.diamond_field_occupied))
    }
}

//class CustomBaseView@JvmOverloads constructor(
//        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//) : ConstraintLayout(context, attrs, defStyleAttr) {
//
//    val view = View.inflate(context, R.layout.customview_editxt, this)
//
//    val binding = ViewBaseBinding.inflate(LayoutInflater.from(context), this, true)
//
//    init {
//        if (attrs != null) {
//            val attributes = context.theme.obtainStyledAttributes(
//                    attrs,
//                    R.styleable.CustomEditxtView,
//                    0, 0
//            )
//
//            //取得預設值
//            view.tv_title_custom_editxt.text = attributes.getString(R.styleable. CustomEditxtView_title_editxt) ?: ""
//            view.et_custom_editxt.hint = attributes.getString(R.styleable.CustomEditxtView_et_hint) ?:""
//
//            //getRescource的第二個參數是  如果到時候沒指定Image 則回傳R.drawable.blank
//            view.imgv_drawable_end.setImageResource(attributes.getResourceId(R.styleable.CustomEditxtView_drawable_end,R.drawable.blank))
//
//        }
//    }
//
//    //待會可以在code裡動態設定edittext的text
//    open fun setEtText(text: String){
//        view.et_custom_editxt.setText(text)
//    }
//}
//fun Customview(context: Context, attrs: AttributeSet?) {
//    super(context, attrs)
//    val a = context.obtainStyledAttributes(attrs, R.styleable.styleName, defStyle, 0)
//    try {
//        val styleAttr = a.getBoolean(R.styleable.StyleName_styleAttr, false)
//    } finally {
//        a.recycle()
//    }
//}