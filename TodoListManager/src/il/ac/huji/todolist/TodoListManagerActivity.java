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

	// private SimpleCursorAdapter _curAdapter;
	private TodoListCursorAdapter _curAdapter;
	private DBHelper helper;

	private class TaskDesc
	{
		public TaskDesc(String name, Date due) {
			_name=name;
			_due=due;
		}
		public String getName(){
			return _name;
		}
		
		public Date due(){
			return _due;
		}
		
		private String _name;
		private Date _due;
	}
		
	private class TaskCreater extends AsyncTask<TaskDesc, Void, Void>
	{
		@Override
		protected Void doInBackground(TaskDesc... tasks) {
			for (TaskDesc task : tasks)
			{
				helper.insert(task.getName(), task.due());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			_curAdapter.changeCursor(helper.getCursor());
			_curAdapter.notifyDataSetChanged();
		}
		
	}
	
	private class DeleteTask extends AsyncTask<Integer, Void, Void>{

		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			
			for (Integer index : params){
				helper.remove(index.intValue());
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			_curAdapter.changeCursor(helper.getCursor());
			super.onPostExecute(result);
			
		}
		
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		helper = DBHelper.getInstance(this);

		setContentView(R.layout.activity_todo_list_manager);

		final ListView lstToDoItems = (ListView) findViewById(R.id.lstTodoItems);

		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				_curAdapter = new TodoListCursorAdapter(
						TodoListManagerActivity.this, helper.getCursor());
				lstToDoItems.setAdapter(_curAdapter);
			}
		});
		
		
		lstToDoItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				Builder alertDialogBuilder = new AlertDialog.Builder(
						TodoListManagerActivity.this);

				ArrayList<String> items = new ArrayList<String>();
				items.add("Delete item");
				String telnumber1 = null;
				final Cursor cur = (Cursor) _curAdapter.getItem(position);
				String taskName = cur.getString(1);
				if (taskName.startsWith("Call ")) {
					items.add(taskName);
					telnumber1 = taskName.substring("Call ".length());
				}
				final String telnumber = telnumber1;
				alertDialogBuilder
						.setTitle(taskName)
						.setItems(items.toArray(new String[items.size()]),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											//helper.remove(cur.getInt(0));
											//_curAdapter.changeCursor(helper
											//		.getCursor());
											new DeleteTask().execute(cur.getInt(0));

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
				//helper.insert(title, date);
				//_curAdapter.changeCursor(helper.getCursor());
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
