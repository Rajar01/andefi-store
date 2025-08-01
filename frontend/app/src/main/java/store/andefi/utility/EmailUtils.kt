package store.andefi.utility

object EmailUtils {
    fun maskEmail(email: String): String {
        val atIndex = email.indexOf('@')
        if (atIndex <= 1) {
            return email
        }

        val username = email.substring(0, atIndex)
        val domain = email.substring(atIndex)

        val maskedUsername = when {
            username.length > 2 -> {
                val startChar = username.first()
                val endChar = username.last()
                val maskLength = username.length - 2
                "$startChar${"*".repeat(maskLength)}$endChar"
            }

            else -> "*".repeat(username.length)
        }

        return "$maskedUsername$domain"
    }
}