package il.ac.huji.todolist;

import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "todo_db";
	private static final String TABLE_NAME = "todo";

	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_TITLE = "title";
	private static final String COLUMN_DUE = "due";

	private static final String CREATE_DB = "create table " + TABLE_NAME + "("
			+ COLUMN_ID + " integer primary key autoincrement, " + COLUMN_TITLE
			+ " string, " + COLUMN_DUE + " long);";

	private static DBHelper _instance;

	private DBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	

	}

	// Singelton inteface
	public static DBHelper getInstance(Context context) {
		if (_instance == null) {
			_instance = new DBHelper(context);
		}
		return _instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	
		db.execSQL(CREATE_DB);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void insert(String taskName, Date due) {
		ContentValues task = new ContentValues();
		task.put(COLUMN_TITLE, taskName);
		task.put(COLUMN_DUE, due.getTime());
		getWritableDatabase().insert(TABLE_NAME, null, task);

	
	}

	public void remove(int id) {
		// remove from SQLite
		getWritableDatabase().delete(TABLE_NAME, "_id = ?",
				new String[] { String.valueOf(id) });

		}

	public Cursor getCursor() {
		return getWritableDatabase().query(TABLE_NAME,
				new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DUE }, null,
				null, null, null, "_id desc");
	}

}