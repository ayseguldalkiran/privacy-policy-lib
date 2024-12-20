package com.example.privacy_policy_lib.core.service

import org.simpleframework.xml.Element


class GetValueResponse {
    @Element(name = "Value", required = false)
    var value: StringElement? = null
    var errorDesc: String? = null
    var isSuccess: Boolean = false
    var message: String? = null
}
