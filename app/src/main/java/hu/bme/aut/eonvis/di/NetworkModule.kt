package hu.bme.aut.eonvis.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.eonvis.network.EonVisService
import hu.bme.aut.eonvis.persistence.AppDatabase
import hu.bme.aut.eonvis.persistence.EonVisDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideEonVisNetworkService(): EonVisService {
        return EonVisService(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    }
}