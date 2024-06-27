package com.permissionxs.backbootdemo

import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream

object AdbUtils {
    val TAG = javaClass.simpleName

    /**
     * run commands with no root (don't need "adb shell")
     */
    fun execCmd(cmd: String?): String? {
        return if (cmd.isNullOrBlank()) {
            Log.d(TAG, "exexCmd: cmd is null")
            null
        } else {
            try {
                val sb = StringBuffer()
                val process = Runtime.getRuntime().exec(cmd)
                val inputStream = process.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val buff = CharArray(1024)
                var ch: Int
                while (true) {
                    ch = bufferedReader.read(buff)
                    if (ch == -1) break
                    sb.append(buff, 0, ch)
                }
                process.waitFor()
                bufferedReader.close()
                Log.d(TAG, "result: $sb")
                return sb.toString()
            } catch (e: IOException) {
                e.toString()
            }
        }
    }



    fun execCmdRoot(cmd: String?): String? {
        return if (cmd.isNullOrBlank()) {
            null
        } else {
            val successMsg = StringBuffer()
            val errorMsg = StringBuffer()
            var process: Process? = null
            var successResult: BufferedReader? = null
            var errorResult: BufferedReader? = null
            var os: DataOutputStream? = null
            try {
                process = Runtime.getRuntime().exec("su")
                os = DataOutputStream(process.outputStream)
                os.write(cmd.toByteArray())
                os.writeBytes("\n")
                os.flush()
                val result = process.waitFor()
                successResult = BufferedReader(InputStreamReader(process.inputStream))
                errorResult = BufferedReader(InputStreamReader(process.errorStream))
                var s: String?
                while (true) {
                    s = successResult.readLine()
                    if (s == null) break
                    successMsg.append(s)
                }
                while (true) {
                    s = errorResult.readLine()
                    if (s == null) break
                    successMsg.append(s)
                }
                Log.d(TAG, "success msg: $successMsg")
                Log.d(TAG, "error msg: $errorMsg")
                return successMsg.toString() + "\n\n" + errorMsg.toString()
            } catch (e: IOException) {
                e.toString()
            } finally {
                process?.destroy()
                os?.close()
                successResult?.close()
                errorResult?.close()
            }
        }
    }




//    fun runCmd(cmd: String): String {
//        Log.d(TAG, "runCmd: $cmd")
//        if (cmd.isEmpty() || cmd.length < 11) {
//            return ""
//        }
//        val cmdHead = cmd.substring(0, 9)
//        if (cmdHead != "adb shell"){
//            return ""
//        }
//        return execRootCmd(cmd)
//    }
//
//    private fun execRootCmd( cmd: String): String {
//        var content = ""
//        try {
//            val command = cmd.replace("adb shell", "")
//            val process = Runtime.getRuntime().exec(command)
//            val reader = BufferedReader(InputStreamReader(process.inputStream))
//            val output = StringBuilder()
//
//            reader.lineSequence().forEach {
//                output.append(it + '\n')
//            }
//            reader.close()
//            process.waitFor();
//            val result = output.toString()
//            Log.d(TAG, result)
//            content = process.toString()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return content
//    }
//
//    private var os: OutputStream? = null
//
//    fun exec(cmd: String) {
//        try {
//            if (os == null) {
//                os = Runtime.getRuntime().exec("su").outputStream
//            }
//            os?.write((cmd.toByteArray()))
//            os?.flush()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

}