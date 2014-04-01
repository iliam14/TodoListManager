package il.ac.huji.todolist;

import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "todo_db";
	private static final String TABLE_NAME = "todo";

	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_TITLE = "title";
	private static final String COLUMN_DUE = "due";
	private static final String COLUMN_SYNC = "LocalId";
	private static final String PARSE_CLASS = "todo";

	private static final String CREATE_DB = "create table " + TABLE_NAME + "("
			+ COLUMN_ID + " integer primary key autoincrement, " + COLUMN_TITLE
			+ " string, " + COLUMN_DUE + " long);";

	private static DBHelper _instance;

	private DBHelper(Context context) {
		super(context, DB_NAME, null, 1);
		Parse.initialize(context, "8iZEZzycf1oE2yMHUMYYBccZ1QBBU4ch00bpNXJt",
				"VMg7Icyf2phMhUd7ZzW4I7kfyN1qvyNrIT0rJVCs");

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
		ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS);

		// Clear the entries in the Parse Table
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> resList, ParseException e) {
				if (e == null) {
					for (ParseObject object : resList) {
						object.deleteInBackground();
					}
				} else {
					Log.d("DBHelper", "Error: " + e.getMessage());
				}

			}
		});

		ParseUser.enableAutomaticUser();
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
		long KeyID = getWritableDatabase().insert(TABLE_NAME, null, task);

		// Inset task to parse
		ParseObject parseTask = new ParseObject(PARSE_CLASS);
		parseTask.put(COLUMN_TITLE, taskName);
		parseTask.put(COLUMN_DUE, due.getTime());
		parseTask.put(COLUMN_SYNC, String.valueOf(KeyID));
		parseTask.saveInBackground();

	}

	public void remove(int id) {
		// remove from SQLite
		getWritableDatabase().delete(TABLE_NAME, "_id = ?",
				new String[] { String.valueOf(id) });

		// remove from Parse
		ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS);
		query.whereEqualTo(COLUMN_SYNC, String.valueOf(id));
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> resList, ParseException e) {
				if (e == null) {
					if (resList.size() > 1)
						Log.d("DBHelper",
								"get List of length: " + resList.size());
					else {
						resList.get(0).deleteInBackground();
					}
				} else {
					Log.d("DBHelper", "Error: " + e.getMessage());
				}

			}
		});
	}

	public Cursor getCursor() {
		return getWritableDatabase().query(TABLE_NAME,
				new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DUE }, null,
				null, null, null, "_id desc");
	}

}
