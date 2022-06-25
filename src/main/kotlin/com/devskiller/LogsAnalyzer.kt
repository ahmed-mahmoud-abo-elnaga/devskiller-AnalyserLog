package com.devskiller

import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.zip.ZipFile
import kotlin.collections.HashMap

class LogsAnalyzer {
    companion object {
        val TEMP_DIR: String = System.getProperty("java.io.tmpdir")+"logs"+File.separator

        /**
         * Size of the buffer to read/write data
         */
        private const val BUFFER_SIZE = 4096
    }

    fun countEntriesInZipFile(
        searchQuery: String,
        zipFile: File,
        startDate: LocalDate,
        numberOfDays: Int
    ): Map<String, Int> {
        val dataMap = HashMap<String, Int>()
        unzip(zipFile, TEMP_DIR)
        val endDate = startDate.plusDays(numberOfDays.toLong())
        File(TEMP_DIR).walk().forEach {
            if (!it.isDirectory && fileIsInDateRange(it.name, startDate, endDate)) {
                dataMap[it.name] = countOfSearchQuery(searchQuery, it)
            }
        }
        println(dataMap)
        return dataMap
    }

    /**
     * @param searchQuery
     * @param file
     * @return Int
     */
    private fun countOfSearchQuery(searchQuery: String, file: File): Int {
        var count = 0
        for (line in file.readLines()) {
            val searchCount = line.split(searchQuery)
                .dropLastWhile { it.isEmpty() }
                .toTypedArray().size - 1
            if(searchCount>0)
            count += searchCount
        }
        return count
    }
    /**
     * @param name
     * @param startDate
     * @param endDate
     * @return Boolean
     */
    private fun fileIsInDateRange(name: String, startDate: LocalDate, endDate: LocalDate): Boolean {
        val dateStringIso = name.replace("logs_", "").replace("-access.log", "")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val fileDate = LocalDate.parse(dateStringIso, formatter)

        return fileDate.isAfter(startDate.minusDays(1)) && fileDate.isBefore(endDate)
    }

    /**
     * @param zipFilePath
     * @param unzipLocation
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun unzip(zipFilePath: File, destDirectory: String) {
        ZipFile(zipFilePath).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    val dir = File(destDirectory)
                    dir.mkdir()
                    val filePath = destDirectory  + entry.name
                    if (!entry.isDirectory) {
                        // if the entry is a file, extracts it
                        extractFile(input, filePath)
                    } else {
                        // if the entry is a directory, make the directory
                        val dir = File(filePath)
                        dir.mkdir()
                    }

                }

            }
        }
    }

    /**
     * Extracts a zip entry (file entry)
     * @param inputStream
     * @param destFilePath
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun extractFile(inputStream: InputStream, destFilePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(destFilePath))
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read = 0
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }
}