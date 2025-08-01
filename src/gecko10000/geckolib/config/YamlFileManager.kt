package gecko10000.geckolib.config

import com.charleskorn.kaml.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import java.io.File
import java.nio.file.Files

class YamlFileManager<T : Any>(
    private val configFile: File,
    private val backupDirectory: File,
    givenFormat: StringFormat? = null,
    private val initialValue: T,
    private val serializer: KSerializer<T>,
) {

    companion object {
        val defaultConfiguration = YamlConfiguration(
            yamlNamingStrategy = YamlNamingStrategy.KebabCase,
            multiLineStringStyle = MultiLineStringStyle.Literal,
            strictMode = false,
            polymorphismStyle = PolymorphismStyle.Property,
            breakScalarsAt = 10000,
        )
        private const val backupAmount = 5
        private const val backupStart = 0
    }

    constructor(
        configDirectory: File,
        configName: String = "config.yml",
        stringFormat: StringFormat? = null,
        initialValue: T,
        serializer: KSerializer<T>,
    ) : this(
        configDirectory.resolve(configName),
        configDirectory.resolve("backups"),
        stringFormat,
        initialValue,
        serializer
    )

    private val fileName = configFile.name
    private val stringFormat = givenFormat ?: Yaml(configuration = defaultConfiguration)
    var value: T = initialValue
        private set

    init {
        reload()
    }

    private fun createIfNotExists() {
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            value = initialValue
            saveInternal()
        }
    }

    private fun saveInternal() {
        // Do this separately so any encoding
        // errors don't clear the output file.
        // Not ideal -- config is loaded into memory
        // (though it's already in memory in the first place I guess)
        val contents = stringFormat.encodeToString(serializer, value)
        configFile.writeText("$contents\n") // lol
    }

    fun save() {
        backup()
        saveInternal()
    }

    private fun loadInternalUncaught() {
        val string = configFile.readText()
        value = stringFormat.decodeFromString(serializer, string)
    }

    private fun loadInternal() {
        try {
            loadInternalUncaught()
        } catch (_: EmptyYamlDocumentException) {
            saveInternal()
            loadInternalUncaught() // no chance of infinite recursion
        }
        saveInternal()
    }

    private fun load() {
        backup()
        loadInternal()
    }

    fun reload() {
        createIfNotExists()
        loadInternal()
    }


    private val backupScheme = "$fileName.%d.bak"

    private fun resolveBackupFile(index: Int) = backupDirectory.resolve(backupScheme.format(index))

    private fun shiftBackupDown(index: Int) {
        val sourceFile = resolveBackupFile(index)
        val targetFile = resolveBackupFile(index + 1)
        sourceFile.renameTo(targetFile)
    }

    private fun backup() {
        // Don't back up duplicates.
        val firstBackup = resolveBackupFile(backupStart)
        if (firstBackup.exists() && Files.mismatch(firstBackup.toPath(), configFile.toPath()) == -1L) {
            return
        }
        backupDirectory.mkdirs()
        // 4 -> 5, 3 -> 4, etc.
        for (i in backupAmount - 1 downTo backupStart) {
            shiftBackupDown(i)
        }
        configFile.copyTo(resolveBackupFile(backupStart), overwrite = true)
    }

}
