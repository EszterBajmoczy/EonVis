package hu.bme.aut.eonvis.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.eonvis.converter.DataConverter
import hu.bme.aut.eonvis.network.EonVisService
import hu.bme.aut.eonvis.persistence.EonVisDao
import hu.bme.aut.eonvis.ui.details.DetailsRepository
import hu.bme.aut.eonvis.ui.login.LoginRepository
import hu.bme.aut.eonvis.ui.main.MainRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideDataConverter(): DataConverter {
        return DataConverter()
    }

    @Provides
    @Singleton
    fun provideLoginRepository(eonVisService: EonVisService): LoginRepository {
        return LoginRepository(eonVisService)
    }

    @Provides
    @Singleton
    fun provideMainRepository(eonVisDao: EonVisDao, eonVisService: EonVisService, dataConverter: DataConverter): MainRepository {
        return MainRepository(eonVisDao, eonVisService, dataConverter)
    }

    @Provides
    @Singleton
    fun provideDetailsRepository(eonVisDao: EonVisDao, eonVisService: EonVisService, dataConverter: DataConverter): DetailsRepository {
        return DetailsRepository(eonVisDao, eonVisService, dataConverter)
    }
}