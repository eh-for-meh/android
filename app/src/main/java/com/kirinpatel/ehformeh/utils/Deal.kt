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
        val theme: Theme,
        val photos: List<String>,
        val story: Story,
        val items: List<Item>
): Serializable {

    var date: Date? = null

    val price: String
        get() {
            val min = items.minBy { it.price }
            val max = items.maxBy { it.price }

            if (min != null && max != null) {
                return if (min.price == max.price) {
                    "$${min.price}"
                } else {
                    "$${min.price} - $${max.price}"
                }
            } else if (min != null) {
                return "$${min.price}"
            } else if (max != null) {
                return "$${max.price}"
            }
            return ""
        }

    companion object {
        private fun getDealFromDataSnapshot(databaseSnapshot: DataSnapshot): Deal {
            val items: List<Item> = databaseSnapshot
                    .child("items")
                    .children
                    .map { Item.getItemFromDataSnapshot(it) }
            return Deal(
                    databaseSnapshot.child("id").value.toString(),
                    databaseSnapshot.child("title").value.toString(),
                    databaseSnapshot.child("features").value.toString(),
                    databaseSnapshot.child("specifications").value.toString(),
                    Theme.getThemeFromDataSnapshot(databaseSnapshot.child("theme")),
                    databaseSnapshot.child("photos").children.map { it.value.toString() },
                    Story.getStoryFromDataSnapshot(databaseSnapshot.child("story")),
                    items
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