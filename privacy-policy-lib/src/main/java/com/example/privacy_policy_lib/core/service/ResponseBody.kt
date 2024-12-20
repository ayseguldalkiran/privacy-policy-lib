package com.example.privacy_policy_lib.core.service

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementUnion
import org.simpleframework.xml.Root

@Root(name = "Body", strict = false)
class ResponseBody {
    @Element(name = "getValueResponse", required = false, type = GetValueResponse::class)
    var getValueResponse: GetValueResponse? = null
}
