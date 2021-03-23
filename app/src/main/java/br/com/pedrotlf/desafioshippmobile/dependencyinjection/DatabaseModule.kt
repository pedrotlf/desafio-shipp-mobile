package br.com.pedrotlf.desafioshippmobile.dependencyinjection

import android.app.Application
import androidx.room.Room
import br.com.pedrotlf.desafioshippmobile.data.card.CardDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabse(
            app: Application,
            callback: CardDatabase.Callback
    ) = Room.databaseBuilder(app, CardDatabase::class.java, "card_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideCardDao(db: CardDatabase) = db.cardDao()
}