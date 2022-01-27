package com.project.tvshows_mvvm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.tvshows_mvvm.dao.TVShowDao
import com.project.tvshows_mvvm.models.TVShow

@Database(entities = [TVShow::class], version = 1, exportSchema = false)
abstract class TVShowsDatabase : RoomDatabase() {


    companion object {
        private var tVShowsDatabase: TVShowsDatabase? = null

        fun getTVShowsDatabase(context: Context): TVShowsDatabase {
            if (tVShowsDatabase == null) {
                tVShowsDatabase =
                    Room.databaseBuilder(context, TVShowsDatabase::class.java, "tv_shows_db")
                        .build()
            }
            return tVShowsDatabase as TVShowsDatabase
        }

    }


    abstract fun tvShowDao(): TVShowDao

}