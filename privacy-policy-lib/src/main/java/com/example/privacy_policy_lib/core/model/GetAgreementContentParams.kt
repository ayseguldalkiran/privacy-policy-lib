package com.example.privacy_policy_lib.core.model

import android.os.Parcel
import android.os.Parcelable

data class GetAgreementContentParams(
    val isProduction: Boolean,
    val contractor: String,
    val itemCode: String?,
    val language: String,
    val agreementType: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isProduction) 1 else 0)
        parcel.writeString(contractor)
        parcel.writeString(itemCode)
        parcel.writeString(language)
        parcel.writeString(agreementType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetAgreementContentParams> {
        override fun createFromParcel(parcel: Parcel): GetAgreementContentParams {
            return GetAgreementContentParams(parcel)
        }

        override fun newArray(size: Int): Array<GetAgreementContentParams?> {
            return arrayOfNulls(size)
        }
    }
}