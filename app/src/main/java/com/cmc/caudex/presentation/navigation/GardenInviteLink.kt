package com.cmc.caudex.presentation.navigation

object GardenInviteLink {
    const val HOST = "caudex.duckdns.org"

    private const val SCHEME = "https"
    private const val GARDEN_PATH = "garden"
    private const val LEGACY_PREFIX = "https://garden/"

    fun fromGardenId(gardenId: String): String {
        val trimmedGardenId = gardenId.trim()
        return if (trimmedGardenId.isBlank()) "" else "$SCHEME://$HOST/$GARDEN_PATH/$trimmedGardenId"
    }

    fun extractGardenId(linkOrId: String): String {
        val value = linkOrId.trim()
        if (value.isBlank()) return ""

        if (value.startsWith(LEGACY_PREFIX)) {
            return value.removePrefix(LEGACY_PREFIX).substringBefore("/").substringBefore("?")
        }

        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            return value
        }

        val path = value
            .substringAfter("://", "")
            .substringAfter("/", "")
            .substringBefore("?")
        val segments = path.split("/").filter { it.isNotBlank() }
        val gardenPathIndex = segments.indexOfFirst { it == GARDEN_PATH || it == "gardens" }
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
