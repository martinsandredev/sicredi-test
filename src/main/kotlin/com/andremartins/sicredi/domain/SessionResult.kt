package com.andremartins.sicredi.domain

import com.fasterxml.jackson.annotation.JsonCreator
import java.io.Serializable

data class SessionResult @JsonCreator constructor(
    val yes: Long,
    val no: Long
) : Serializable