package br.com.pedrotlf.desafioshippmobile.data.card

import androidx.room.*
import br.com.pedrotlf.desafioshippmobile.data.card.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Query("SELECT * FROM card_table ORDER By id Desc")
    fun getCards(): Flow<List<Card>>

    @Query("SELECT * FROM card_table WHERE selected = 1")
    fun getSelectedCard(): Flow<Card?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card)

    @Update
    suspend fun update(card: Card)

    @Delete
    suspend fun delete(card: Card)
}