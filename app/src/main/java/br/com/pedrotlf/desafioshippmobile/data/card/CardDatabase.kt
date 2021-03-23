package br.com.pedrotlf.desafioshippmobile.data.card

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.pedrotlf.desafioshippmobile.dependencyinjection.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Card::class], version = 1)
abstract class CardDatabase : RoomDatabase(){

    abstract fun cardDao(): CardDao

    class Callback @Inject constructor(
            private val database: Provider<CardDatabase>,
            @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().cardDao()

            applicationScope.launch {
                dao.insert(Card())
                dao.insert(Card("1212121212121212"))
                dao.insert(Card("1234123412341234"))
            }
        }
    }
}