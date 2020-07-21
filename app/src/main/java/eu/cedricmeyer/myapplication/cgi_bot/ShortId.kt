package eu.cedricmeyer.myapplication.cgi_bot

object ShortId {
    val allowedChars = ('A'..'Z') + ('a'..'z') + (0..9) + '_' + '-'
    fun generate(length: Int = 7): String {
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}