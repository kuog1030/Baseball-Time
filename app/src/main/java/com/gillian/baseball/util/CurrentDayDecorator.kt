package com.gillian.baseball.util

import android.R
import com.gillian.baseball.BaseballApplication
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class CurrentDayDecorator(currentDay: CalendarDay) : DayViewDecorator {

    var myDay = currentDay
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(10F, BaseballApplication.instance.getColor(R.color.black)))
    }

    init {
    }
}