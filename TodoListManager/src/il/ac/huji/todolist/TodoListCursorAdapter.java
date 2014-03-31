package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class TodoListCursorAdapter extends CursorAdapter {

	// set date format
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public TodoListCursorAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	}

	@Override
	public void bindView(View v, Context cntx, Cursor cursor) {
		// Set the today date (without time)
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();

		// The due date of the current task
		Date taskDue = new Date(cursor.getLong(2));

		// Get handle to text views
		TextView txtTitle = (TextView) v.findViewById(R.id.txtTodoTitle);
		TextView txtDueDate = (TextView) v.findViewById(R.id.txtTodoDueDate);

		if (taskDue != null) {
			// set the color of the task
			if (today.after(taskDue)) {
				txtTitle.setTextColor(Color.RED);
				txtDueDate.setTextColor(Color.RED);
			} else {
				txtTitle.setTextColor(Color.BLACK);
				txtDueDate.setTextColor(Color.BLACK);
			}
			txtDueDate.setText(sdf.format(taskDue));
		}
		// else
		// {
		// txtDueDate.setText("No due date");
		// }
		txtTitle.setText(cursor.getString(1));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// when the view will be created for first time, we inflate the row
		// layout
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.single_list_item, parent, false);

		return view;
	}

}
