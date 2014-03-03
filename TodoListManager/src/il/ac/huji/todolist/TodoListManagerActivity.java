package il.ac.huji.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	private ArrayList<String> list;
	private ArrayAdapter<String> taskAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ArrayList<String>();

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
				// Configure the dialog
				alertDialogBuilder
						.setTitle(list.get(position))
						.setMessage("Do you want to delete this task?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// remove this item
										list.remove(posToRemove);
										taskAdapter.notifyDataSetChanged();

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// just close the dialog
										dialog.cancel();

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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			EditText edtNewItem = (EditText) findViewById(R.id.edtNewItem);
			String input = edtNewItem.getText().toString();

			// don't add empty strings
			if (!"".equals(input)) {
				list.add(input);
				taskAdapter.notifyDataSetChanged();
			}

			edtNewItem.setText(null);
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}
}
