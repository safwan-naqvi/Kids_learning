package com.example.kidsappfyp.HelperClasses

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.kidsappfyp.Constants.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DatabaseHelper(internal var context: Context) : SQLiteOpenHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION) {

    val videoDetails: ArrayList<ModelVideo>
        get() {
            var arrPfVideoDetails = ArrayList<ModelVideo>()
            val db = this.writableDatabase
            val cursor = db.rawQuery("SELECT * FROM kids WHERE id=" + Constants.VIDEO_CATEGORY_ID, null)
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                var modelVideo = ModelVideo()
                var videoDescription = cursor.getString(cursor.getColumnIndexOrThrow(Constants.VIDEO))
                modelVideo.videoId = videoDescription.split("#")[0]
                modelVideo.videoTitle = videoDescription.split("#")[1]
                modelVideo.videoThumb = "https://i3.ytimg.com/vi/" + videoDescription.split("#")[0] + "/hqdefault.jpg"

                arrPfVideoDetails.add(modelVideo)
                cursor.moveToNext()
            }
            return arrPfVideoDetails
        }

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    @Throws(IOException::class)
    fun createDataBase() {
        val databaseExist = checkDataBase()
        if (databaseExist) {
        } else {
            this.writableDatabase
            copyDataBase()
        }

    }

    public fun checkDataBase(): Boolean {
        val databaseFile = File(Constants.DB_PATH + Constants.DATABASE_NAME)
        return databaseFile.exists()
    }

    @Throws(IOException::class)
    private fun copyDataBase() {
        val myInput = context.assets.open(Constants.DATABASE_NAME)
        val outFileName = Constants.DB_PATH + Constants.DATABASE_NAME
        val myOutput = FileOutputStream(outFileName)
        val buffer = ByteArray(1024)
        while (myInput.read(buffer) > 0) {
            myOutput.write(buffer)
        }
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }

}
