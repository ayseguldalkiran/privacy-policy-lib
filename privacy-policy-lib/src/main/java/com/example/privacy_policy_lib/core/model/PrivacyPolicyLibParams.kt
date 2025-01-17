package com.example.privacy_policy_lib.core.model

import android.os.Parcel
import android.os.Parcelable
import com.example.privacy_policy_lib.core.AgreementTypes

data class PrivacyPolicyLibParams(
    val isProduction: Boolean,
    val contractor: String,
    val itemCode: String,
    val language: String,
    val agreementTypes: List<AgreementTypes>,
    val server: String,
    val erpType: String,
    val userName: String,
    val password: String,
    var contentHashList: MutableList<Pair<AgreementTypes, String>> = mutableListOf(),
    var agreementTokenList: MutableList<Pair<AgreementTypes, String>> = mutableListOf(),
    var endDateList: MutableList<Pair<AgreementTypes, String>> = mutableListOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createIntArray()?.map { AgreementTypes.fromValue(it) } ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        mutableListOf<Pair<AgreementTypes, String>>().apply {
            parcel.readList(this as List<*>, Pair::class.java.classLoader)
        },
        mutableListOf<Pair<AgreementTypes, String>>().apply {
            parcel.readList(this as List<*>, Pair::class.java.classLoader)
        },
        mutableListOf<Pair<AgreementTypes, String>>().apply {
            parcel.readList(this as List<*>, Pair::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isProduction) 1 else 0)
        parcel.writeString(contractor)
        parcel.writeString(itemCode)
        parcel.writeString(language)
        parcel.writeIntArray(agreementTypes.map { it.value }.toIntArray())
        parcel.writeString(server)
        parcel.writeString(erpType)
        parcel.writeString(userName)
        parcel.writeString(password)
        parcel.writeList(contentHashList)
        parcel.writeList(agreementTokenList)
        parcel.writeList(endDateList)
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

object PrivacyPolicyState {
    // Bunun default'unu böyle bırakmamalıyız.
    var params: PrivacyPolicyLibParams = PrivacyPolicyLibParams(
            isProduction = true,
            contractor = "MOBILE",
            itemCode = "wmsmobile_privacyPolicy",
            language = "TR",
            agreementTypes = arrayListOf(
                AgreementTypes.GENERALAGREEMENT,
                AgreementTypes.TERMSOFUSE
            ),
            server = "10.122.122.143",
            erpType = "Tiger",
            userName = "LN1",
            password = "1"
    )
    const val PARAMS_TO_GET_FROM_APP = "privacyPolicyParamsFromApp"
}
