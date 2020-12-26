package com.example.myapplication.ui.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class StudentContentProvider : ContentProvider() {

    private val studentItem = 1
    private val studentDir = 0

    lateinit var db: SQLiteDatabase
    val uriMatcher: UriMatcher
    val authority = "com.example.myapplication.ui.database.provider"

    init {
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(authority,"student",studentDir)
        uriMatcher.addURI(authority,"student/*",studentItem)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            studentItem -> "vnd.android.cursor.item/vnd.$authority.student"
            studentDir -> "vnd.android.cursor.dir/vnd.$authority.student"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        context?.let {
            val openHelper = MyOpenHelper(it, 1)
            db = openHelper.readableDatabase
            return true
        }
        return false
    }


    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor = when(uriMatcher.match(uri)) {
            studentDir -> db.query(Table_NAME,null,null,null,null,null,null,null)
            studentItem -> {
                val sname = uri.pathSegments[1]
                db.query(Table_NAME,null,"sname=?", arrayOf(sname),null,null,null)
            }
            else -> null
        }
        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        return 0
    }
}