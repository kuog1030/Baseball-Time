package com.gillian.baseball.data.source

import com.gillian.baseball.data.Event

class DefaultBaseballRepository(private val remoteDataSource: BaseballDataSource,
                                private val localDataSource: BaseballDataSource
) : BaseballRepository {

    override fun temp(){
        return remoteDataSource.temp()
    }

    override suspend fun sendEvent(event: Event) {
        return remoteDataSource.sendEvent(event)
    }
}