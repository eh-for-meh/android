package com.kirinpatel.ehformeh.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable
import java.util.*

data class Deal(
        val id: String,
        val title: String,
        val features: String,
        val specifications: String,
        val theme: Theme
): Serializable {

    var date: Date? = null

    companion object {
        private fun getDealFromDataSnapshot(databaseSnapshot: DataSnapshot): Deal {
            return Deal(
                    databaseSnapshot.child("id").value.toString(),
                    databaseSnapshot.child("title").value.toString(),
                    databaseSnapshot.child("features").value.toString(),
                    databaseSnapshot.child("specifications").value.toString(),
                    Theme.getThemeFromDataSnapshot(databaseSnapshot.child("theme"))
            )
        }

        fun getCurrentDeal(loaded: (deal: Deal) -> Unit, error: (databaseError: DatabaseError) -> Unit) {
            FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("currentDeal")
                    .child("deal")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            loaded(getDealFromDataSnapshot(dataSnapshot))
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            error(databaseError)
                        }
                    })
        }

        fun watchCurrentDeal(loaded: (deal: Deal) -> Unit, error: (databaseError: DatabaseError) -> Unit) {
            FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("currentDeal")
                    .child("deal")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            loaded(getDealFromDataSnapshot(dataSnapshot))
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            error(databaseError)
                        }
                    })
        }
    }
}