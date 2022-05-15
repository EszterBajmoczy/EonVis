package hu.bme.aut.eonvis.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.eonvis.R
import hu.bme.aut.eonvis.persistence.AppDatabase
import hu.bme.aut.eonvis.persistence.EonVisDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(
                application,
                AppDatabase::class.java,
                application.getString(R.string.database)
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideEonVisDao(appDatabase: AppDatabase): EonVisDao {
        return appDatabase.eonVisDao()
    }
}