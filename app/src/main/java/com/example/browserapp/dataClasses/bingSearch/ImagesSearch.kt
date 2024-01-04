package com.example.browserapp.dataClasses.bingSearch

data class ImagesSearch(
    val _type: String?,
    val currentOffset: Int?,
    val instrumentation: Instrumentation?,
    val nextOffset: Int?,
    val pivotSuggestions: List<PivotSuggestion?>?,
    val queryContext: QueryContext?,
    val queryExpansions: List<QueryExpansion?>?,
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

    data class QueryExpansion(
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
        val accentColor: String?,
        val contentSize: String?,
        val contentUrl: String?,
        val datePublished: String?,
        val encodingFormat: String?,
        val height: Int?,
        val hostPageDiscoveredDate: String?,
        val hostPageDisplayUrl: String?,
        val hostPageDomainFriendlyName: String?,
        val hostPageFavIconUrl: String?,
        val hostPageUrl: String?,
        val imageId: String?,
        val imageInsightsToken: String?,
        val insightsMetadata: InsightsMetadata?,
        val isFamilyFriendly: Boolean?,
        val isTransparent: Boolean?,
        val name: String?,
        val thumbnail: Thumbnail?,
        val thumbnailUrl: String?,
        val webSearchUrl: String?,
        val width: Int?
    ) {
        data class InsightsMetadata(
            val availableSizesCount: Int?,
            val pagesIncludingCount: Int?,
            val recipeSourcesCount: Int?
        )

        data class Thumbnail(
            val height: Int?,
            val width: Int?
        )
    }
}