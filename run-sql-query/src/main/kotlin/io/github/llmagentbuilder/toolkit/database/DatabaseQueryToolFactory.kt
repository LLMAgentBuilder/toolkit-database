package io.github.llmagentbuilder.toolkit.database

import io.github.llmagentbuilder.core.tool.ConfigurableAgentToolFactory

class DatabaseQueryToolFactory :
    ConfigurableAgentToolFactory<DatabaseQueryConfig, DatabaseQueryTool> {
    override fun create(config: DatabaseQueryConfig?): DatabaseQueryTool {
        return DatabaseQueryTool(config)
    }

    override fun toolId(): String {
        return toolId
    }
}