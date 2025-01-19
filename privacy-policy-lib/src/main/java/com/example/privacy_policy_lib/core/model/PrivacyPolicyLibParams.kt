package com.example.privacy_policy_lib.core.model

import android.os.Parcel
import android.os.Parcelable
import com.example.privacy_policy_lib.core.AgreementTypes

data class PrivacyPolicyLibParams(
    var isProduction: Boolean= false,
    var contractor: String = "",
    var itemCode: String = "",
    var language: String = "",
    var agreementTypes: MutableList<AgreementTypes> = mutableListOf(),
    var server: String = "",
    var erpType: String = "",
    var userName: String = "",
    var password: String= "",
    var contentHashList: MutableList<Pair<AgreementTypes, String>> = mutableListOf(),
    var agreementTokenList: MutableList<Pair<AgreementTypes, String>> = mutableListOf(),
    var endDateList: MutableList<Pair<AgreementTypes, String>> = mutableListOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        mutableListOf<AgreementTypes>().apply {
            parcel.readList(this, AgreementTypes::class.java.classLoader)
        },
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
        parcel.writeList(agreementTypes)
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
    /*var params: PrivacyPolicyLibParams = PrivacyPolicyLibParams(
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
    )*/
    var params: PrivacyPolicyLibParams = PrivacyPolicyLibParams()
    const val PARAMS_TO_GET_FROM_APP = "privacyPolicyParamsFromApp"
    const val PRIVACY_POLICY = "privacy_policy"
    const val POSITION = "position"
    const val IS_APPROVED = "is_approved"
}
