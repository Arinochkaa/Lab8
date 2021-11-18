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
public DBHelper(@Nullable Context context)
{
super(context, DATABASE_NAME, null, DATABASE_VERSION);
}
6.

public class DBHelper extends SQLiteOpenHelper
{
public static final int DATABASE_VERSION = 1;
public static final String DATABASE_NAME = &quot;myBase&quot;;
public static final String TABLE_PERSONS = &quot;persons&quot;;
public static final String KEY_ID = &quot;_id&quot;;
public static final String KEY_NAME = &quot;name&quot;;
public static final String KEY_MAIL = &quot;mail&quot;;
public DBHelper(@Nullable Context context)
{
super(context, DATABASE_NAME, null, DATABASE_VERSION);
}
@Override
public void onCreate(SQLiteDatabase db)
{
db.execSQL(&quot;create table &quot; + TABLE_PERSONS + &quot;(&quot; + KEY_ID + &quot;
integer primary key,&quot; + KEY_NAME + &quot; text,&quot; + KEY_MAIL + &quot; text&quot; + &quot;)&quot;);
}
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int
newVersion)
{
db.execSQL(&quot;drop table if exists &quot; + TABLE_PERSONS);
onCreate(db);
}
}
7. Расширили класс «MainActivity» и добавили наши переменные:
8. public class MainActivity extends AppCompatActivity implements
View.OnClickListener
{
Button buttonAdd, buttonDelete, buttonClear, buttonRead,
buttonUpdate;
EditText etName, etEmail, etID;
DBHelper dbHelper;
@Override
protected void onCreate(Bundle savedInstanceState)
{
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
buttonAdd = (Button) findViewById(R.id.buttonAdd);
buttonAdd.setOnClickListener(this);
buttonRead = (Button) findViewById(R.id.buttonRead);
buttonRead.setOnClickListener(this);
buttonClear = (Button) findViewById(R.id.buttonClear);
buttonClear.setOnClickListener(this);
buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
buttonUpdate.setOnClickListener(this);
buttonDelete = (Button) findViewById(R.id.buttonDelete);
buttonDelete.setOnClickListener(this);
etID = (EditText) findViewById(R.id.etID);
etName = (EditText) findViewById(R.id.etName);
etEmail = (EditText) findViewById(R.id.etEmail);
dbHelper = new DBHelper(this);
}
9. Создали 1 свой обработчик на все кнопки:
@Override
public void onClick(View v)
{
String ID = etID.getText().toString();
String name = etName.getText().toString();
String email = etEmail.getText().toString();
SQLiteDatabase database = dbHelper.getWritableDatabase();
ContentValues contentValues = new ContentValues(); // класс для
добавления новых строк в таблицу
switch (v.getId())
{
case R.id.buttonAdd:
contentValues.put(DBHelper.KEY_NAME, name);
contentValues.put(DBHelper.KEY_MAIL, email);
database.insert(DBHelper.TABLE_PERSONS, null,
contentValues);
break;
case R.id.buttonRead:
Cursor cursor = database.query(DBHelper.TABLE_PERSONS, null,
null, null,
null, null, null); // все поля без сортировки и
группировки
if (cursor.moveToFirst())
{
int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
int nameIndex =
cursor.getColumnIndex(DBHelper.KEY_NAME);
int emailIndex =
cursor.getColumnIndex(DBHelper.KEY_MAIL);
do {
Log.d(&quot;mLog&quot;, &quot;ID =&quot; + cursor.getInt(idIndex) +
&quot;, name = &quot; + cursor.getString(nameIndex) +
&quot;, email = &quot; +
cursor.getString(emailIndex));
} while (cursor.moveToNext());
} else
Log.d(&quot;mLog&quot;, &quot;0 rows&quot;);
cursor.close(); // освобождение памяти
break;
case R.id.buttonClear:
database.delete(DBHelper.TABLE_PERSONS, null, null);
break;
case R.id.buttonDelete:
if (ID.equalsIgnoreCase(&quot;&quot;))
{
break;
}
int delCount = database.delete(DBHelper.TABLE_PERSONS,
DBHelper.KEY_ID + &quot;= &quot; + ID, null);
Log.d(&quot;mLog&quot;, &quot;Удалено строк = &quot; + delCount);
case R.id.buttonUpdate:
if (ID.equalsIgnoreCase(&quot;&quot;))
{
break;
}
contentValues.put(DBHelper.KEY_MAIL, email);
contentValues.put(DBHelper.KEY_NAME, name);
int updCount = database.update(DBHelper.TABLE_PERSONS,
contentValues, DBHelper.KEY_ID + &quot;= ?&quot;, new String[] {ID});
Log.d(&quot;mLog&quot;, &quot;Обновлено строк = &quot; + updCount);
}
dbHelper.close(); // закрываем соединение с БД
}
10. Нашли созданную нами базу данных через путь: View–&gt;Tool
Windows–&gt;Device File Explorer–&gt;…..databases–&gt;имя_БД;
11. Проверили работоспособность всех кнопок через окно «Logcat»;
