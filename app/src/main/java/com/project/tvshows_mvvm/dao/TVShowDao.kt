package com.project.tvshows_mvvm.dao

import androidx.room.*
import com.project.tvshows_mvvm.models.TVShow
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TVShowDao {

    @Query("SELECT * FROM TVSHOWS")
    fun getWatchlist(): Flowable<List<TVShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToWatchlist(tvShow: TVShow): Completable

    @Delete
    fun removeFromWatchlist(tvShow: TVShow): Completable

    @Query("SELECT * FROM TVSHOWS WHERE id = :tvShowId")
    fun getTVShowFromWatchlist(tvShowId: String): Flowable<TVShow>
}