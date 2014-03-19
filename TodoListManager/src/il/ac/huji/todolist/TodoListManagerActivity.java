package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	private ArrayList<Pair<String, Date>> list;
	private ArrayAdapter<Pair<String, Date>> taskAdapter;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ArrayList<Pair<String, Date>>();

		setContentView(R.layout.activity_todo_list_manager);
		taskAdapter = new ColorListAdapter(this, R.layout.single_list_item,
				list);
		taskAdapter.setNotifyOnChange(true);

		ListView lstToDoItems = (ListView) findViewById(R.id.lstTodoItems);
		lstToDoItems.setAdapter(taskAdapter); // apply our adapter
		lstToDoItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				final int posToRemove = position;

				Builder alertDialogBuilder = new AlertDialog.Builder(
						TodoListManagerActivity.this);

				ArrayList<String> items = new ArrayList<String>();
				items.add("Delete item");
				String telnumber1 = null;
				if (list.get(position).first.startsWith("Call ")) {
					items.add(list.get(position).first);
					telnumber1 = list.get(position).first.substring("Call "
							.length());
				}
				final String telnumber = telnumber1;
				alertDialogBuilder
						.setTitle(list.get(position).first)
						.setItems(items.toArray(new String[items.size()]),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											list.remove(posToRemove);
											taskAdapter.notifyDataSetChanged();
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

			Pair<String, Date> pair = new Pair<String, Date>(title, date);
			if (!"".equals(title))
			{
				list.add(pair);
				taskAdapter.notifyDataSetChanged();
			}
			
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			// EditText edtNewItem = (EditText) findViewById(R.id.edtNewItem);
			// String input = edtNewItem.getText().toString();
			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent, 0);

			// don't add empty strings
			// if (!"".equals(input)) {
			// list.add(input);
			// taskAdapter.notifyDataSetChanged();
			// }
			//
			// edtNewItem.setText(null);
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

}
