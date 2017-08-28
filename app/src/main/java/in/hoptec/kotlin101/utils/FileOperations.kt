package `in`.hoptec.kotlin101.utils


import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import android.util.Log

class FileOperations {
    fun write(fname: String, fcontent: String): Boolean? {
        try {
            val fpath = fname
            val file = File(fpath)
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile()
            }
            val fw = FileWriter(file.absoluteFile)
            val bw = BufferedWriter(fw)
            bw.write(fcontent)
            bw.close()
            Log.d("Suceess", "Sucess")
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    fun read(fname: String): String? {
        var br: BufferedReader? = null
        var response: String? = null
        try {
            val output = StringBuffer()
            val fpath = fname
            br = BufferedReader(FileReader(fpath))
            var line = ""
            while ((line) != null) {
                output.append(line + "n")
                line= br.readLine()
            }
            response = output.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return response.replace("}n", "}")
    }
}