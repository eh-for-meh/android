package com.kirinpatel.ehformeh.utils

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

data class Story(val title: String, val body: String): Serializable {

    companion object {
        fun getStoryFromDataSnapshot(databaseSnapshot: DataSnapshot): Story {
            return Story(
                    databaseSnapshot.child("title").value.toString(),
                    databaseSnapshot.child("body").value.toString()
            )
        }
    }
}