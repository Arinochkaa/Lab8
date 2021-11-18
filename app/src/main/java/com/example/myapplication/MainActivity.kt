package com.example.myapplication

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var dbHelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = DBHelper(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val add = findViewById<Button>(R.id.add)
        add.setOnClickListener(this)
        val read = findViewById<Button>(R.id.read)
        read.setOnClickListener(this)
        val clear = findViewById<Button>(R.id.clear);
        clear.setOnClickListener(this)
        val refresh = findViewById<Button>(R.id.refresh);
        refresh.setOnClickListener(this)
        val delete = findViewById<Button>(R.id.delete);
        delete.setOnClickListener(this)
        val id = findViewById<EditText>(R.id.id)
        findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
    }

    override fun onClick(v: View) {
        val id = findViewById<EditText>(R.id.id).getText().toString();
        val name = findViewById<EditText>(R.id.name).getText().toString();
        val email = findViewById<EditText>(R.id.email).getText().toString();
        val database = dbHelper?.getWritableDatabase();
        val contentValues = ContentValues() // класс для добавления новых строк в таблицу

        when (v.getId()) {
            R.id.add -> {
                contentValues.put(DBHelper.KEY_NAME, name)
                contentValues.put(DBHelper.KEY_MAIL, email)
                database?.insert(
                    DBHelper.TABLE_PERSONS, null,
                    contentValues
                );
            }

            R.id.read -> {
                val cursor = database?.query(
                    DBHelper.TABLE_PERSONS, null,
                    null, null,
                    null, null, null
                ); // все поля без сортировки и группировки
                if (cursor.moveToFirst()) {
                    val idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    val nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    val emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
                    do {
                        Log.d(
                            "mLog",
                            "ID =" + cursor.getInt(idIndex) + ", name = " + cursor.getString(
                                nameIndex
                            ) + ", email = " + cursor.getString(emailIndex)
                        );
                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog", "0 rows");
                cursor.close(); // освобождение памяти
            }

            R.id.clear -> {
                database.delete(DBHelper.TABLE_PERSONS, null, null);
            }

            R.id.delete -> {
                if (!id.equalsIgnoreCase("")) {
                    val read = database.delete(
                        DBHelper.TABLE_PERSONS,
                        DBHelper.KEY_ID + "= " + id, null
                    )
                    Log.d("mLog", "Удалено строк = " + read)
                }
            }

            R.id.refresh -> {
                if (!id.equalsIgnoreCase("")) {

                    contentValues.put(DBHelper.KEY_MAIL, email);
                    contentValues.put(DBHelper.KEY_NAME, name);
                    val read = database.update(DBHelper.TABLE_PERSONS,
                        contentValues, DBHelper.KEY_ID + "= ?", Array<String>(10) { id });
                    Log.d("mLog", "Обновлено строк = " + read);
                }
            }
        }
        dbHelper?.close(); // закрываем соединение с БД
    }
}