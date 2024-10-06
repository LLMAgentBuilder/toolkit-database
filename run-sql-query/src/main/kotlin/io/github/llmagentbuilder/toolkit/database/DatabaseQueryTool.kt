package io.github.llmagentbuilder.toolkit.database

import io.github.llmagentbuilder.core.tool.ConfigurableAgentTool

const val toolId = "runSqlQuery"

class DatabaseQueryTool(config: DatabaseQueryConfig?) :
    ConfigurableAgentTool<DatabaseQueryRequest, DatabaseQueryResponse, DatabaseQueryConfig> {

    private val helper =
        config?.run { DatabaseHelper(this) } ?: throw RuntimeException(
            "Database query config is required"
        )

    override fun apply(request: DatabaseQueryRequest): DatabaseQueryResponse {
        return helper.runQuery(request)
    }

    override fun description(): String {
        return "Query database using SQL"
    }

    override fun name(): String {
        return toolId
    }

    override fun id(): String {
        return toolId
    }
}