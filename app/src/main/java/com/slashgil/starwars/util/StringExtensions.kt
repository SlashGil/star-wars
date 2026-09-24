package com.slashgil.starwars.util

fun String.capitalizeWords(): String {
    if (this.isBlank()) return this
    if (this.equals("n/a", ignoreCase = true)) return "N/A"
    return this.replace(Regex("[a-zA-Z]+")) { matchResult ->
        val word = matchResult.value
        when {
            word.equals("n/a", ignoreCase = true) -> "N/A"
            word.equals("bby", ignoreCase = true) -> "BBY"
            word.equals("aby", ignoreCase = true) -> "ABY"
            else -> word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}
