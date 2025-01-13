package com.example.privacy_policy_lib.core.model

import android.os.Parcel
import android.os.Parcelable
import com.example.privacy_policy_lib.core.AgreementTypes

data class PrivacyPolicyLibParams(
    val isProduction: Boolean,
    val contractor: String,
    val itemCode: String?,
    val language: String,
    val agreementType: AgreementTypes
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readParcelable(AgreementTypes::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isProduction) 1 else 0)
        parcel.writeString(contractor)
        parcel.writeString(itemCode)
        parcel.writeString(language)
        parcel.writeInt(agreementType.value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PrivacyPolicyLibParams> {
        override fun createFromParcel(parcel: Parcel): PrivacyPolicyLibParams {
            return PrivacyPolicyLibParams(parcel)
        }

        override fun newArray(size: Int): Array<PrivacyPolicyLibParams?> {
            return arrayOfNulls(size)
        }
    }
}