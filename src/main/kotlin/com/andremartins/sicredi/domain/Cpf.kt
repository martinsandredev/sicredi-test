package com.andremartins.sicredi.domain

private fun validateCPF(cpf: String): Boolean {
    var cpf = cpf
    cpf = cpf.replace("[^\\d]".toRegex(), "")
    if (cpf.length != 11 || cpf.matches("(\\d)\\1{10}".toRegex())) {
        return false
    }
    val digits = IntArray(11)
    for (i in 0..10) {
        digits[i] = Character.getNumericValue(cpf[i])
    }
    var sum = 0
    for (i in 0..8) {
        sum += digits[i] * (10 - i)
    }
    var remainder = sum % 11
    val expectedDigit1 = if (remainder < 2) 0 else 11 - remainder
    if (digits[9] != expectedDigit1) {
        return false
    }
    sum = 0
    for (i in 0..9) {
        sum += digits[i] * (11 - i)
    }
    remainder = sum % 11
    val expectedDigit2 = if (remainder < 2) 0 else 11 - remainder
    return digits[10] == expectedDigit2
}

@JvmInline
value class Cpf(private val value: String) {
    init {
        if (!validateCPF(value)) {
            throw ValidationError.InvalidCpf(value)
        }
    }

    override fun toString() = value
}
