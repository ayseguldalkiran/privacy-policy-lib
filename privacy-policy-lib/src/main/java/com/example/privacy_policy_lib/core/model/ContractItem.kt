package com.example.privacy_policy_lib.core.model

import android.os.Parcel
import android.os.Parcelable

data class ContractItem(
    var contractItemText: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(contractItemText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContractItem> {
        override fun createFromParcel(parcel: Parcel): ContractItem {
            return ContractItem(parcel)
        }

        override fun newArray(size: Int): Array<ContractItem?> {
            return arrayOfNulls(size)
        }
    }
}