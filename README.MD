# Count matching log file entries

Complete a `LogAnalyzer` class that counts the number of lines which include search strings for files contained in a ZIP file. Each file name contains a date. Check only files that fit within the range set by a given date specified as `startDate` and `numberOfDays`. All files contained in a given ZIP archive have the following naming pattern: `logs_<date>-access.log` (for example `logs_2018-02-27-access.log`)

Your implementation of `LogsAnalyzer#countEntriesInZipFile' method should:

1. Iterate over files, which have been extracted from a ZIP file and placed into a temporary directory.

2. Verify that the name of the file matches an expected date pattern.

3. For each file matching the date pattern, count the number of lines that include the search string.

4. Return a Map with the file name as a key, and the number of lines which include the search string, as a value.

## Example data


### ZIP file "logs-27_02_2018-03_03_2018.zip":
ZIP archive contains the following files:
```
logs_2018-02-27-access.log
logs_2018-02-28-access.log
logs_2018-03-01-access.log
logs_2018-03-02-access.log
logs_2018-03-03-access.log
```

### Example Approach
The following solution should check three files from a ZIP archive, starting with `logs_2018-02-27-access.log` and all files created over the next two following days. For each file count lines that contain the search string `"Mozilla"`.

```kotlin
val logsAnalyzer = LogsAnalyzer()
val counts: Map<String, Int> = logsAnalyzer.countEntriesInZipFile("Mozilla", zipPath, LocalDate.of(2018, 2, 27), 3)
```

### Example Approach result
```json
{
  "logs_2018-02-27-access.log" : 23,
  "logs_2018-02-28-access.log" : 18,
  "logs_2018-03-01-access.log" : 40
}
```

