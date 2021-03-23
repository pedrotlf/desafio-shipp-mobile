package br.com.pedrotlf.desafioshippmobile.data.card

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "card_table")
data class Card(
        val number: String = "1111111111111111",
        val cvv: String = "111",
        val expirationDate: String = "12/30",
        val ownerName: String = "",
        val ownerCpf: String = "",
        val selected: Boolean = false,
        @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable