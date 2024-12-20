package com.example.privacy_policy_lib.core.service

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

@Root(name = "Envelope")
@NamespaceList(
    Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "s")
)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "s")

class ResponseEnvelope {

    @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "s")
    @field:Element(name = "Body")
    var body: ResponseBody? = null
}

