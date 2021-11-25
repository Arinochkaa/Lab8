# Lab8
our cool lab 8 yooooooo
Лабораторная работа № 8
Тема: Работа с базой данных SQLite.
Цель: Научиться работать с основными командами SQL.
Задание:
1. Разработали мобильное приложение на основе базы данных для
реализации команд добавить, удалить, обновить данные;
Порядок выполнения:
1. Создаем новый проект;
2. Добавили на форму 3 компонента «EditText*», а также 5
компонентов «Button» с соответствующими именами «ID»,
«Name», «E-mail», «Добавить», «Удалить», «Очистить»,
«Считать», «Обновить».
3. Добавили новый класс для работы с базой данных и назвали его
«DBHelper»;
4. Расширили этот класс методами «onCreate» и «onUpdate»;
5. Выбрали конструктор суперкласса «SQLLiteOpenHelper» с
параметрами – «Context», «Name», «Factory», «Version» и удалили
лишние параметры:
6. Расширили класс «MainActivity» и добавили наши переменные:
7. Создали 1 свой обработчик на все кнопки.
8. Нашли созданную нами базу данных через путь.
9. Проверили работоспособность всех кнопок через окно «Logcat»;


``Kotlin
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
                val cursor = database!!.query(
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
                database!!.delete(DBHelper.TABLE_PERSONS, null, null);
            }

            R.id.delete -> {
                if (id != "") {
                    val read = database!!.delete(
                        DBHelper.TABLE_PERSONS,
                        DBHelper.KEY_ID + "= " + id, null
                    )
                    Log.d("mLog", "Удалено строк = " + read)
                }
            }

            R.id.refresh -> {
                if (id != "") {

                    contentValues.put(DBHelper.KEY_MAIL, email);
                    contentValues.put(DBHelper.KEY_NAME, name);
                    val read = database!!.update(DBHelper.TABLE_PERSONS,
                        contentValues, DBHelper.KEY_ID + "= ?", Array<String>(10) { id });
                    Log.d("mLog", "Обновлено строк = " + read);
                }
            }
        }
        dbHelper?.close(); // закрываем соединение с БД
    }
}
``


``Java
package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myBase";
    public static final String TABLE_PERSONS = "persons";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_MAIL = "email";
    public DBHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + TABLE_PERSONS + "(" + KEY_ID + " integer primary key," + KEY_NAME + " text," + KEY_MAIL + " text" + ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion)
    {
        db.execSQL("drop table if exists " + TABLE_PERSONS);
        onCreate(db);
    }
}
``

