package com.example.browserapp.dataClasses.bingSearch

data class BingSearch(
    val _type: String?,
    val errors: List<Error?>?,
    val queryContext: QueryContext?,
    val rankingResponse: RankingResponse?,
    val relatedSearches: RelatedSearches?,
    val videos: Videos?,
    val webPages: WebPages?
) {
    data class Error(
        val code: String?,
        val message: String?,
        val moreDetails: String?,
        val subCode: String?
    )

    data class QueryContext(
        val originalQuery: String?
    )

    data class RankingResponse(
        val mainline: Mainline?,
        val sidebar: Sidebar?
    ) {
        data class Mainline(
            val items: List<Item?>?
        ) {
            data class Item(
                val answerType: String?,
                val resultIndex: Int?,
                val value: Value?
            ) {
                data class Value(
                    val id: String?
                )
            }
        }

        data class Sidebar(
            val items: List<Item?>?
        ) {
            data class Item(
                val answerType: String?,
                val value: Value?
            ) {
                data class Value(
                    val id: String?
                )
            }
        }
    }

    data class RelatedSearches(
        val id: String?,
        val value: List<Value?>?
    ) {
        data class Value(
            val displayText: String?,
            val text: String?,
            val webSearchUrl: String?
        )
    }

    data class Videos(
        val id: String?,
        val isFamilyFriendly: Boolean?,
        val readLink: String?,
        val scenario: String?,
        val value: List<Value?>?,
        val webSearchUrl: String?
    ) {
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
            val isSuperfresh: Boolean?,
            val motionThumbnailUrl: String?,
            val name: String?,
            val publisher: List<Publisher?>?,
            val thumbnail: Thumbnail?,
            val thumbnailUrl: String?,
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

    data class WebPages(
        val totalEstimatedMatches: Int?,
        val value: List<Value?>?,
        val webSearchUrl: String?
    ) {
        data class Value(
            val about: List<About?>?,
            val cachedPageUrl: String?,
            val contractualRules: List<ContractualRule?>?,
            val dateLastCrawled: String?,
            val datePublished: String?,
            val datePublishedDisplayText: String?,
            val displayUrl: String?,
            val id: String?,
            val isFamilyFriendly: Boolean?,
            val isNavigational: Boolean?,
            val language: String?,
            val name: String?,
            val primaryImageOfPage: PrimaryImageOfPage?,
            val richFacts: List<RichFact?>?,
            val snippet: String?,
            val thumbnailUrl: String?,
            val url: String?,
            val video: Video?
        ) {
            data class About(
                val _type: String?,
                val aggregateRating: AggregateRating?
            ) {
                data class AggregateRating(
                    val ratingValue: Int?,
                    val reviewCount: Int?
                )
            }

            data class ContractualRule(
                val _type: String?,
                val license: License?,
                val licenseNotice: String?,
                val mustBeCloseToContent: Boolean?,
                val targetPropertyIndex: Int?,
                val targetPropertyName: String?
            ) {
                data class License(
                    val name: String?,
                    val url: String?
                )
            }

            data class PrimaryImageOfPage(
                val height: Int?,
                val imageId: String?,
                val thumbnailUrl: String?,
                val width: Int?
            )

            data class RichFact(
                val hint: Hint?,
                val items: List<Item?>?,
                val label: Label?
            ) {
                data class Hint(
                    val text: String?
                )

                data class Item(
                    val text: String?
                )

                data class Label(
                    val text: String?
                )
            }

            data class Video(
                val duration: String?,
                val thumbnailUrl: String?,
                val videoId: String?,
                val webSearchUrl: String?
            )
        }
    }
}