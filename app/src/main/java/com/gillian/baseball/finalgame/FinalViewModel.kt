package com.gillian.baseball.finalgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillian.baseball.data.BoxView
import com.gillian.baseball.data.MyGame
import com.gillian.baseball.data.source.BaseballRepository

class FinalViewModel(private val repository: BaseballRepository, private val myGame: MyGame) : ViewModel() {

    var box = myGame.game.box
    var boxViewList = mutableListOf<BoxView>()

    private val _submitAdapter = MutableLiveData<List<BoxView>>()
    val submitAdapter : LiveData<List<BoxView>>
        get() = _submitAdapter

    init {
        createBoxViewList()
    }

    fun createBoxViewList() {
        // TODO 處理沒有九下之類的情況
        var inning = 1

        for ( i in 1 .. box.score.size step 2 ) {
            boxViewList.add ( BoxView(type = "$inning",
                    guest = box.score[i-1].toString(),
                    home = box.score[i].toString()) )
            inning += 1
        }

        if ( box.score.size < 9 ) {
            var end = inning
            for (i in end .. 9) {
                boxViewList.add( BoxView(type = "$inning", guest = "-", home = "-"))
                inning += 1
            }
        }

        boxViewList.add( BoxView(type = "R", guest = box.run[0].toString(), home = box.run[1].toString()))
        boxViewList.add( BoxView(type = "H", guest = box.hit[0].toString(), home = box.hit[1].toString()))
        boxViewList.add( BoxView(type = "E", guest = box.error[0].toString(), home = box.error[1].toString()))

        _submitAdapter.value = boxViewList
    }

}