package com.kirinpatel.ehformeh.utils

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

data class Item(val id: String, val condition: String, val photo: String, val price: Float): Serializable {

    companion object {
        fun getItemFromDataSnapshot(databaseSnapshot: DataSnapshot): Item {
            val price: Float = if (databaseSnapshot.child("price").value is Long) {
                (databaseSnapshot.child("price").value as Long).toFloat()
            } else {
                databaseSnapshot.child("price").value as Float
            }
            return Item(
                    databaseSnapshot.child("id").value.toString(),
                    databaseSnapshot.child("condition").value.toString(),
                    databaseSnapshot.child("photo").value.toString(),
                    price
            )
        }
    }
}