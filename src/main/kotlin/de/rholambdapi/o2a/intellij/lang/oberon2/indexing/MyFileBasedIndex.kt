package de.rholambdapi.o2a.intellij.lang.oberon2.indexing

import com.intellij.util.indexing.*
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonFileType
import java.io.DataInput
import java.io.DataOutput

class MyFileBasedIndex : FileBasedIndexExtension<String, String>() {
    override fun getName(): ID<String, String> {
        return ID.create<String, String>(MyFileBasedIndex::class.qualifiedName!!)
    }

    override fun getIndexer(): DataIndexer<String, String, FileContent> {
        return MyDataIndexer()
    }

    class MyDataIndexer : DataIndexer<String, String, FileContent> {
        override fun map(inputData: FileContent): MutableMap<String, String> {
            println("indexer map; inputData: $inputData")
            return mutableMapOf(
                Pair("key1", "value1"),
                Pair("key2", "value2"),
                Pair("key3", "value3")
            )
        }

    }

    override fun getKeyDescriptor(): KeyDescriptor<String> {
        return EnumeratorStringDescriptor.INSTANCE
    }

    override fun getValueExternalizer(): DataExternalizer<String> {
        return MyDataExternalizer()
    }

    override fun getVersion(): Int {
        return 0
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return DefaultFileTypeSpecificInputFilter(OberonFileType.INSTANCE)
    }

    override fun dependsOnFileContent(): Boolean {
        return true
    }
}

class MyDataExternalizer : DataExternalizer<String> {
    override fun save(out: DataOutput, value: String?) {
//        println("externalizer save; value: $value")
        out.writeUTF(value!!)
    }

    override fun read(`in`: DataInput): String {
        val value = `in`.readUTF()
//        println("externalizer read; value: $value")
        return value
    }

}
