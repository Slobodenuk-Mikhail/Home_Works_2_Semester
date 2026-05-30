package ru.itis.home_works_2_semester.data.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class CharacterDataModel(
    val remoteId: Long,
    val remoteName: String,
    val remoteStatus: String,
    val remoteSpecies: String,
    val remoteGender: String,
    val remoteImage: String? = "https://i.pinimg.com/736x/da/74/5a/da745af04b0727387590ab1813efd912.jpg"
) : Parcelable {
    companion object {
        val EMPTY = CharacterDataModel(
            remoteId = 0L,
            remoteName = "",
            remoteStatus = "",
            remoteSpecies = "",
            remoteGender = ""
        )
    }
}
