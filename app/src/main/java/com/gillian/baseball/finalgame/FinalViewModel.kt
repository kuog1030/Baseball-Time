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

    // 1. 更新box
    // 2. 更新每個球員的成績
    // 3. ->所以我的stat要有player id有!我好棒XD
    // 4. -> 看過所有的stat的player id 一個一個執行repository上傳


    var box = myGame.game.box
    //var boxViewList = mutableListOf<BoxView>()

    val gameNote = MutableLiveData<String>()


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
        _submitAdapter.value = box.toBoxViewList()
    }

    fun uploadBox () {
        Log.i("gillian", "box is $box")
        viewModelScope.launch {
            val result = repository.updateGameBox(myGame.game.id, box)
            when (result) {
                is Result.Success -> {
                }
                is Result.Fail -> {
                    _errorMessage.value = result.error
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.toString()
                }
                else -> {
                    _errorMessage.value = Util.getString(R.string.return_nothing)
                }
            }
        }
    }

}