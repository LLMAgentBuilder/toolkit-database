package io.github.llmagentbuilder.toolkit.database

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.util.CollectionUtils
import java.util.*

class DatabaseHelper(config: DatabaseQueryConfig) {
    private val dataSource: HikariDataSource
    private val jdbcClient: JdbcClient
    private val objectMapper =
        ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    init {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = config.jdbcUrl
        hikariConfig.username = config.username
        hikariConfig.password = config.password
        dataSource = HikariDataSource(hikariConfig)
        jdbcClient = JdbcClient.create(dataSource)
    }

    fun runQuery(request: DatabaseQueryRequest): DatabaseQueryResponse {
        try {
            val rows = jdbcClient.sql(request.query)
                .query().listOfRows()
            val result = when (request.format) {
                QueryResultFormat.JSON -> toJson(rows)
                else -> toCsv(rows)
            }
            return DatabaseQueryResponse(result)
        } catch (e: BadSqlGrammarException) {
            return DatabaseQueryResponse(null, ErrorType.SYNTAX, e.message)
        } catch (e: DataAccessException) {
            return DatabaseQueryResponse(null, ErrorType.EXECUTION, e.message)
        }
    }

    private fun toCsv(rows: List<Map<String, Any?>>): String {
        if (CollectionUtils.isEmpty(rows)) {
            return ""
        }
        val fields = rows.first().keys.sorted()
        val builder = StringBuilder()
        builder.append(fields.joinToString(","))
        rows.forEach { row ->
            fields.map { row[it] ?: "" }.joinToString(",").let {
                builder.append(it)
            }
        }
        return builder.toString()
    }

    private fun toJson(rows: List<Map<String, Any?>>): String {
        return try {
            objectMapper.writeValueAsString(rows)
        } catch (e: JsonProcessingException) {
            Objects.toString(rows)
        }
    }
}