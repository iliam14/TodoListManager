package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class TodoListArrayAdapter extends ArrayAdapter<TaskDesc> {

	
	private final Context _context;
	private final int _resource;
	
	// set date format
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	
	public TodoListArrayAdapter(Context context, int resource) {
		super(context, resource);
		_context = context;
		_resource = resource;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(_resource,parent,false);

		TaskDesc curTask = getItem(position);
		
		// Set the today date (without time)
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();

		// the raw format of the the date
		Long rawDate = curTask.due().getTime();
		
		// Get handle to text views
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTodoTitle);
		TextView txtDueDate = (TextView) rowView.findViewById(R.id.txtTodoDueDate);

		if (rawDate != null) {
			// The due date of the current task
			Date taskDue = new Date(rawDate);
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
		else
		{
			txtDueDate.setText("No due date");
		}
		txtTitle.setText(curTask.getName());

		
		return rowView;
	}
	
	

}
