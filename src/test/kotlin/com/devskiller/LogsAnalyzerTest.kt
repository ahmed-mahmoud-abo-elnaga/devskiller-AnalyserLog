package com.devskiller

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.file.Paths
import java.time.LocalDate

class LogsAnalyzerTest {
    private val logsAnalyzer = LogsAnalyzer()
    private val zipPath = Paths.get(javaClass.getResource("/logs-27_02_2018-03_03_2018.zip").toURI()).toFile()

    @Test
    fun `contains entries for correct days`() {
        val counts: Map<String, Int> = logsAnalyzer.countEntriesInZipFile(
                "Mozilla", zipPath, LocalDate.of(2018, 2, 27), 3)

        assertThat(counts).hasSize(3)
        assertThat(counts).containsKey("logs_2018-03-01-access.log")
        assertThat(counts).containsKey("logs_2018-02-28-access.log")
        assertThat(counts).containsKey("logs_2018-02-27-access.log")
    }

    @Test
    fun `returns line counts for Mozilla`() {
        val counts = logsAnalyzer.countEntriesInZipFile(
                "Mozilla", zipPath, LocalDate.of(2018, 2, 27), 3)

        assertThat(counts).hasSize(3)
        assertThat(counts["logs_2018-03-01-access.log"]).`as`("for logs_2018-03-01-access.log").isEqualTo(23)
        assertThat(counts["logs_2018-02-28-access.log"]).`as`("for logs_2018-02-28-access.log").isEqualTo(18)
        assertThat(counts["logs_2018-02-27-access.log"]).`as`("for logs_2018-02-27-access.log").isEqualTo(40)
    }

    @Test
    fun `returns line counts for Safari`() {
        val counts = logsAnalyzer.countEntriesInZipFile(
                "Safari", zipPath, LocalDate.of(2018, 2, 27), 4)

        assertThat(counts).hasSize(4)
        assertThat(counts["logs_2018-03-02-access.log"]).`as`("for logs_2018-03-02-access.log").isEqualTo(6)
        assertThat(counts["logs_2018-03-01-access.log"]).`as`("for logs_2018-03-01-access.log").isEqualTo(16)
        assertThat(counts["logs_2018-02-28-access.log"]).`as`("for logs_2018-02-28-access.log").isEqualTo(14)
        assertThat(counts["logs_2018-02-27-access.log"]).`as`("for logs_2018-02-27-access.log").isEqualTo(25)
    }
}