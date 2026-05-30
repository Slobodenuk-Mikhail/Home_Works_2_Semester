package ru.itis.home_works_2_semester.data.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class CharacterModel(
    val id: Long,
    val name: String,
    val image: String? = "https://i.pinimg.com/736x/da/74/5a/da745af04b0727387590ab1813efd912.jpg"
) : Parcelable {
    companion object {
        val EMPTY = CharacterModel(
            id = 0L,
            name = ""
        )
    }
}
