package com.app.robusta.weather.di
import android.content.Context
import androidx.room.Room
import com.app.robusta.weather.db.WeatherDb

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDb(@ApplicationContext appContext: Context): WeatherDb {
        return Room.databaseBuilder(
            appContext,
            WeatherDb::class.java,
            "myDb"
        ).build()
    }
}