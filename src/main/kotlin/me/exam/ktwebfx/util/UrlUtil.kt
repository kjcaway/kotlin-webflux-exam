package me.exam.ktwebfx.util

class UrlUtil {
    companion object {
        fun getRegex(urlPath: String): Regex {
            val regex = StringBuilder("^") // start regex
            val parts = urlPath.split("/")

            parts.forEach { part ->
                if (part.isNotEmpty()) {
                    regex.append("/")
                    when {
                        part.startsWith(":") || (part.startsWith("{") && part.endsWith("}")) -> {
                            regex.append("([^/]+)")
                        }

                        part == "*" -> {
                            regex.append("(.*)")
                        }

                        else -> {
                            regex.append(part)
                        }
                    }
                }
            }

            regex.append("$") // end regex
            return regex.toString().toRegex()
        }

        fun match(urlPath: String, regex: Regex): Boolean {
            return regex.find(urlPath)?.groupValues?.isNotEmpty() ?: false
        }
    }
}