package com.gillian.baseball.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.gillian.baseball.data.source.BaseballDataSource
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.data.source.DefaultBaseballRepository
import com.gillian.baseball.data.source.local.BaseballLocalDataSource
import com.gillian.baseball.data.source.remote.BaseballRemoteDataSource

object ServiceLocator {

        @Volatile
        var repository: BaseballRepository? = null
            @VisibleForTesting set

        fun provideRepository(context: Context): BaseballRepository {
            synchronized(this) {
                return repository
                    ?: repository
                    ?: createBaseballRepository(context)
            }
        }

        private fun createBaseballRepository(context: Context): BaseballRepository {
            return DefaultBaseballRepository(
                BaseballRemoteDataSource,
                createLocalDataSource(context)
            )
        }

        private fun createLocalDataSource(context: Context): BaseballDataSource {
            return BaseballLocalDataSource(context)
        }
}