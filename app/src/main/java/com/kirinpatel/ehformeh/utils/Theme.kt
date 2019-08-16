package com.kirinpatel.ehformeh.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Theme(var accentColor: String, var backgroundColor: String, var foregroundColor: String) {

    companion object {
        private fun getThemeFromDataSnapshot(dataSnapshot: DataSnapshot): Theme {
            return Theme(
                    dataSnapshot.child("accentColor").value.toString(),
                    dataSnapshot.child("backgroundColor").value.toString(),
                    dataSnapshot.child("foregroundColor").value.toString())
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