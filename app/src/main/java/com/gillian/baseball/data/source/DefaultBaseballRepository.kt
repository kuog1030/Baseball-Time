package com.gillian.baseball.data.source

class DefaultBaseballRepository(private val remoteDataSource: BaseballDataSource,
                                private val localDataSource: BaseballDataSource
) : BaseballRepository {

    override fun temp(){
        return remoteDataSource.temp()
    }
}