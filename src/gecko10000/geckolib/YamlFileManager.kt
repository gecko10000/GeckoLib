package gecko10000.geckolib

import com.charleskorn.kaml.*
import kotlinx.serialization.KSerializer
import java.io.File

class YamlFileManager<T : Any>(
    private val configFile: File,
    private val backupDirectory: File,
    private val initialValue: T,
    private val serializer: KSerializer<T>,
) {

    constructor(
        configDirectory: File,
        configName: String = "config.yml",
        initialValue: T,
        serializer: KSerializer<T>,
    ) : this(configDirectory.resolve(configName), configDirectory.resolve("backups"), initialValue, serializer)

    private val yaml = Yaml(
        configuration = YamlConfiguration(
            yamlNamingStrategy = YamlNamingStrategy.KebabCase,
            multiLineStringStyle = MultiLineStringStyle.Literal,
            strictMode = false,
            polymorphismStyle = PolymorphismStyle.Property,
        )
    )
    private val fileName = configFile.name
    var value: T = initialValue
        private set

    init {
        reload()
    }

    private fun createIfNotExists() {
        if (!configFile.exists()) {
            value = initialValue
            saveInternal()
        }
    }

    private fun saveInternal() {
        // Do this separately so any encoding
        // errors don't clear the output file.
        // Not ideal -- config is loaded into memory
        // (though it's already in memory in the first place I guess)
        val contents = yaml.encodeToString(serializer, value)
        configFile.writeText("$contents\n") // lol
    }

    fun save() {
        backup()
        saveInternal()
    }

    private fun loadInternalUncaught() {
        value = yaml.decodeFromStream(serializer, configFile.inputStream())
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

    companion object {
        private const val backupAmount = 5
        private const val backupStart = 0
    }


    private val backupScheme = "$fileName.%d.bak"

    private fun resolveBackupFile(index: Int) = backupDirectory.resolve(backupScheme.format(index))

    private fun shiftBackupDown(index: Int) {
        val sourceFile = resolveBackupFile(index)
        val targetFile = resolveBackupFile(index + 1)
        sourceFile.renameTo(targetFile)
    }

    private fun backup() {
        backupDirectory.mkdirs()
        // 4 -> 5, 3 -> 4, etc.
        for (i in backupAmount - 1 downTo backupStart) {
            shiftBackupDown(i)
        }
        configFile.copyTo(resolveBackupFile(backupStart), overwrite = true)
    }

}
