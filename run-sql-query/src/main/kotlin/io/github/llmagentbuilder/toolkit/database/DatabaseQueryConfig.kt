package io.github.llmagentbuilder.toolkit.database

data class DatabaseQueryConfig(
    val jdbcUrl: String,
    val username: String? = null,
    val password: String? = null,
)

enum class QueryResultFormat {
    JSON,
    CSV,
}

data class DatabaseQueryRequest(
    val query: String,
    val format: QueryResultFormat? = QueryResultFormat.CSV,
)

enum class ErrorType {
    SYNTAX,
    EXECUTION,
}

data class DatabaseQueryResponse(
    val result: String?,
    val errorType: ErrorType? = null,
    val error: String? = null,
)