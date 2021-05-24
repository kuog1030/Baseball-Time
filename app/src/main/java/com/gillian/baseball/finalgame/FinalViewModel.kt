package com.gillian.baseball.finalgame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gillian.baseball.R
import com.gillian.baseball.data.BoxView
import com.gillian.baseball.data.LoadStatus
import com.gillian.baseball.data.MyGame
import com.gillian.baseball.data.Result
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.Util
import kotlinx.coroutines.launch

class FinalViewModel(private val repository: BaseballRepository, private val myGame: MyGame) : ViewModel() {

    var box = myGame.game.box
    var boxViewList = mutableListOf<BoxView>()

    private val _submitAdapter = MutableLiveData<List<BoxView>>()
    val submitAdapter : LiveData<List<BoxView>>
        get() = _submitAdapter

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        uploadBox()
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

    fun uploadBox () {
        Log.i("gillian", "box is $box")
//        viewModelScope.launch {
//            val result = repository.updateGameBox(myGame.game.id, box)
//            when (result) {
//                is Result.Success -> {
//                }
//                is Result.Fail -> {
//                    _errorMessage.value = result.error
//                }
//                is Result.Error -> {
//                    _errorMessage.value = result.exception.toString()
//                }
//                else -> {
//                    _errorMessage.value = Util.getString(R.string.return_nothing)
//                }
//            }
//        }
    }

}