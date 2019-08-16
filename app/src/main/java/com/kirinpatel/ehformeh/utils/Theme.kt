package com.kirinpatel.ehformeh.utils

import android.graphics.Color
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable

data class Theme(private var _accentColor: String, private var _backgroundColor: String, private var _foreground: String): Serializable {

    val accentColor: Int
        get() = Color.parseColor(this._accentColor)

    val backgroundColor: Int
        get() = Color.parseColor(this._backgroundColor)

    val isDark: Boolean
        get() = this._foreground == "dark"

    companion object {
        fun getThemeFromDataSnapshot(dataSnapshot: DataSnapshot): Theme {
            return Theme(
                    dataSnapshot.child("accentColor").value.toString(),
                    dataSnapshot.child("backgroundColor").value.toString(),
                    dataSnapshot.child("foreground").value.toString())
        }

        fun getCurrentTheme(loaded: (theme: Theme) -> Unit, error: (code: DatabaseError) -> Unit) {
            FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("currentDeal")
                    .child("deal")
                    .child("theme")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            loaded(getThemeFromDataSnapshot(dataSnapshot))
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            error(databaseError)
                        }
                    })
        }
    }
}