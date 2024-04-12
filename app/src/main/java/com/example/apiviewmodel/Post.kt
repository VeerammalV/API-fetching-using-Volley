package com.example.apiviewmodel

import android.os.Parcel
import android.os.Parcelable

// Define a data class Post with four properties: userId, id, title, and body
data class Post(
    val userId: Int,
    val id: Int,
    val title: String?,
    val body: String?
) : Parcelable { // Implement Parcelable interface to make instances of this class parcelable

    // Secondary constructor for Parcelable interface, used for parceling data
    constructor(parcel: Parcel) : this(
        parcel.readInt(),      // Read userId from parcel
        parcel.readInt(),      // Read id from parcel
        parcel.readString(),   // Read title from parcel
        parcel.readString()    // Read body from parcel
    )

    // Write object's data to a Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)      // Write userId to parcel
        parcel.writeInt(id)          // Write id to parcel
        parcel.writeString(title)    // Write title to parcel
        parcel.writeString(body)     // Write body to parcel
    }

    // Describe the kinds of special objects contained in this Parcelable instance's marshaled representation
    override fun describeContents(): Int {
        return 0
    }

    // Companion object providing Parcelable.Creator implementation
    companion object CREATOR : Parcelable.Creator<Post> {
        // Create a new instance of the Parcelable class, instantiating it from the given Parcel
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}
