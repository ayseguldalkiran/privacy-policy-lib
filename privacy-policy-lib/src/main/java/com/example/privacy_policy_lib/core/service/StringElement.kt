package com.example.privacy_policy_lib.core.service

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Text


class StringElement {
    @Namespace(reference = "http://www.w3.org/2001/XMLSchema-sInstance", prefix = "i")
    @Attribute(name = "type", required = false)
    var type: String? = null

    @Namespace(reference = "http://www.w3.org/2001/XMLSchema-sInstance", prefix = "i")
    @Attribute(name = "nil", required = false)
    var isNil: Boolean = false

    @Text(required = false)
    var value: String = ""

    @Attribute(name = "xmlns:a", required = false)
    var xmlnsa: String? = null

    @Attribute(name = "xmlns:i", required = false)
    var xmlnsi: String? = null

}
