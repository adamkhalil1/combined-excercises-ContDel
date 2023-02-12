package com.docdate

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class User(
    val id: String? = "",
    val profession: String? = "",
    val title: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val address: String? = "",
    val phone: String? = "",
    val website: String? = "",
    val specialisation: String? = "",
    val insurance: String? = "",
    val email: String? = "",

    val uri: String? = "",
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(profession)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(title)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeString(website)
        parcel.writeString(specialisation)
        parcel.writeString(insurance)
        parcel.writeString(email)
        parcel.writeString(uri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}