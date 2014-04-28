package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	
	//TODO Remove me.
	// private SimpleCursorAdapter _curAdapter;
	//private TodoListCursorAdapter _curAdapter;
	private TodoListArrayAdapter _curAdapter;
	private DBHelper helper;
		

	private class TaskCreater extends AsyncTask<TaskDesc, Void, TaskDesc>
	{
		@Override
		protected TaskDesc doInBackground(TaskDesc... tasks) {
			TaskDesc task = tasks[0];
			task.updateId(helper.insert(task.getName(), task.due()));
				
				
			
			return task;
		}

		@Override
		protected void onPostExecute(TaskDesc result) {
//			_curAdapter.changeCursor(helper.getCursor());
			_curAdapter.insert(result,0);
			_curAdapter.notifyDataSetChanged();
		}
		
	}
	
	private class DeleteTask extends AsyncTask<TaskDesc, Void, Void>{

		private int _pos;
		
		public DeleteTask(int position){
			_pos = position;
		}
		
		@Override
		protected Void doInBackground(TaskDesc... params) {
			// TODO Auto-generated method stub
			
			for (TaskDesc task : params){
				helper.remove((int)task.id());
				
				//helper.remove(task.id());
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			_curAdapter.remove(_curAdapter.getItem(_pos));
			super.onPostExecute(result);
			
		}
		
	}
	
	private class AsyncTaskLoader extends AsyncTask<Cursor, TaskDesc, Void>
	{

		@Override
		protected Void doInBackground(Cursor... cursors) {
			
			Cursor cur = cursors[0];
			while (cur.moveToNext()) {
				int id = 	cur.getInt(cur.getColumnIndex(DBHelper.COLUMN_ID));
				Long date = cur.getLong(cur.getColumnIndex(DBHelper.COLUMN_DUE));
				String title = cur.getString(cur.getColumnIndex(DBHelper.COLUMN_TITLE));
				
				TaskDesc task = new TaskDesc(id, title, new Date(date));
				publishProgress(task);
				
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(TaskDesc... values) {
			for (TaskDesc task : values){
				_curAdapter.add(task);
			}
			_curAdapter.notifyDataSetChanged();
		}
		
		
		
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		helper = DBHelper.getInstance(this);

		setContentView(R.layout.activity_todo_list_manager);

		final ListView lstToDoItems = (ListView) findViewById(R.id.lstTodoItems);

		_curAdapter = new TodoListArrayAdapter(
				TodoListManagerActivity.this, R.layout.single_list_item);
		lstToDoItems.setAdapter(_curAdapter);

		new AsyncTaskLoader().execute(helper.getCursor());
		
//		new Handler().post(new Runnable() {
//			
//			@Override
//			public void run() {
//				_curAdapter = new TodoListArrayAdapter(
//						TodoListManagerActivity.this, R.layout.single_list_item);
//				lstToDoItems.setAdapter(_curAdapter);
//			}
//		});
		
		
		lstToDoItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				Builder alertDialogBuilder = new AlertDialog.Builder(
						TodoListManagerActivity.this);

				ArrayList<String> items = new ArrayList<String>();
				items.add("Delete item");
				String telnumber1 = null;
				//final Cursor cur = (Cursor) _curAdapter.getItem(position);
				final TaskDesc curTask = _curAdapter.getItem(position);
				String taskName = curTask.getName();
				if (taskName.startsWith("Call ")) {
					items.add(taskName);
					telnumber1 = taskName.substring("Call ".length());
				}
				final String telnumber = telnumber1;
				final int pos = position;
				alertDialogBuilder
						.setTitle(taskName)
						.setItems(items.toArray(new String[items.size()]),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											//helper.remove((int)curTask.id());
											//_curAdapter.remove(_curAdapter.getItem(pos));
											
											//_curAdapter.changeCursor(helper
											//		.getCursor());
											new DeleteTask(pos).execute(curTask);

										} else if (which == 1) {
											// call
											Intent dial = new Intent(
													Intent.ACTION_DIAL,
													Uri.parse("tel:"
															+ telnumber));
											startActivity(dial);
										}

									}
								}).create().show();

				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			Date date = (Date) data.getSerializableExtra("dueDate");
			String title = data.getStringExtra("title");

			// Skip empty titles
			if (!"".equals(title)) {
//				long id = helper.insert(title, date);
//				_curAdapter.add(new TaskDesc(id,title,date));
				//_curAdapter.notifyDataSetChanged();
				new TaskCreater().execute(new TaskDesc(title, date));
			}

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent, 0);

			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

}
