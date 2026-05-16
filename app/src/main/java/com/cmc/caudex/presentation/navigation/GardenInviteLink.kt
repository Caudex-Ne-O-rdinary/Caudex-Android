package com.cmc.caudex.presentation.navigation

object GardenInviteLink {
    const val HOST = "caudex.duckdns.org"

    private const val SCHEME = "https"
    private const val SCHEME_SEPARATOR = "://"
    private const val HTTP_PREFIX = "http://"
    private const val HTTPS_PREFIX = "https://"
    private const val GARDEN_PATH = "garden"
    private const val GARDENS_PATH = "gardens"
    private const val LEGACY_PREFIX = "$HTTPS_PREFIX$GARDEN_PATH/"

    fun fromGardenId(gardenId: String): String {
        val trimmedGardenId = gardenId.trim()
        return if (trimmedGardenId.isBlank()) "" else "$SCHEME$SCHEME_SEPARATOR$HOST/$GARDEN_PATH/$trimmedGardenId"
    }

    fun extractGardenId(linkOrId: String): String {
        val value = linkOrId.trim()
        if (value.isBlank()) return ""

        if (value.startsWith(LEGACY_PREFIX)) {
            return value.removePrefix(LEGACY_PREFIX).substringBefore("/").substringBefore("?")
        }

        if (!value.startsWith(HTTP_PREFIX) && !value.startsWith(HTTPS_PREFIX)) {
            return value
        }

        val path = value
            .substringAfter(SCHEME_SEPARATOR, "")
            .substringAfter("/", "")
            .substringBefore("?")
        val segments = path.split("/").filter { it.isNotBlank() }
        val gardenPathIndex = segments.indexOfFirst { it == GARDEN_PATH || it == GARDENS_PATH }
        return if (gardenPathIndex >= 0) {
            segments.getOrNull(gardenPathIndex + 1).orEmpty()
        } else {
            segments.firstOrNull().orEmpty()
        }
    }

    fun fromSavedLinkOrId(savedLink: String, gardenId: String): String {
        val targetGardenId = extractGardenId(savedLink).ifBlank { gardenId.trim() }
        return fromGardenId(targetGardenId)
    }
}
