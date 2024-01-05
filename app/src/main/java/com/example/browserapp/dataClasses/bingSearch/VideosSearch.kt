package com.example.browserapp.dataClasses.bingSearch

data class VideosSearch(
    val _type: String?,
    val currentOffset: Int?,
    val instrumentation: Instrumentation?,
    val nextOffset: Int?,
    val pivotSuggestions: List<PivotSuggestion?>?,
    val queryContext: QueryContext?,
    val readLink: String?,
    val relatedSearches: List<RelatedSearche?>?,
    val totalEstimatedMatches: Int?,
    val value: List<Value?>?,
    val webSearchUrl: String?
) {
    data class Instrumentation(
        val _type: String?
    )

    data class PivotSuggestion(
        val pivot: String?,
        val suggestions: List<Suggestion?>?
    ) {
        data class Suggestion(
            val displayText: String?,
            val searchLink: String?,
            val text: String?,
            val thumbnail: Thumbnail?,
            val webSearchUrl: String?
        ) {
            data class Thumbnail(
                val thumbnailUrl: String?
            )
        }
    }

    data class QueryContext(
        val alterationDisplayQuery: String?,
        val alterationMethod: String?,
        val alterationOverrideQuery: String?,
        val alterationType: String?,
        val originalQuery: String?
    )

    data class RelatedSearche(
        val displayText: String?,
        val searchLink: String?,
        val text: String?,
        val thumbnail: Thumbnail?,
        val webSearchUrl: String?
    ) {
        data class Thumbnail(
            val thumbnailUrl: String?
        )
    }

    data class Value(
        val allowHttpsEmbed: Boolean?,
        val allowMobileEmbed: Boolean?,
        val contentUrl: String?,
        val creator: Creator?,
        val datePublished: String?,
        val description: String?,
        val duration: String?,
        val embedHtml: String?,
        val encodingFormat: String?,
        val height: Int?,
        val hostPageDisplayUrl: String?,
        val hostPageUrl: String?,
        val isAccessibleForFree: Boolean?,
        val isFamilyFriendly: Boolean?,
        val isSuperfresh: Boolean?,
        val motionThumbnailUrl: String?,
        val name: String?,
        val publisher: List<Publisher?>?,
        val thumbnail: Thumbnail?,
        val thumbnailUrl: String?,
        val videoId: String?,
        val viewCount: Int?,
        val webSearchUrl: String?,
        val width: Int?
    ) {
        data class Creator(
            val name: String?
        )

        data class Publisher(
            val name: String?
        )

        data class Thumbnail(
            val height: Int?,
            val width: Int?
        )
    }
}