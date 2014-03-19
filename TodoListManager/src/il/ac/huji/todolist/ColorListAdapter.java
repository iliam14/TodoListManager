package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


// Coloring Adapter
@SuppressLint("SimpleDateFormat")
class ColorListAdapter extends ArrayAdapter<Pair<String,Date>> {

	List<Pair<String,Date>> list;
	public ColorListAdapter(Context context, int resource, List<Pair<String,Date>> objects) {
		super(context, resource, objects);
		list=objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		if(v == null){
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.single_list_item, null);
		}
		
		
		Calendar cal = Calendar.getInstance(); 
		Date		taskDue = list.get(position).second;
		
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();


		TextView txtTitle = (TextView) v.findViewById(R.id.txtTodoTitle);
		TextView txtDueDate = (TextView) v.findViewById(R.id.txtTodoDueDate);
		
		
		
		if (today.after(taskDue) )
		{
			txtTitle.setTextColor(Color.RED);
			txtDueDate.setTextColor(Color.RED);
		}
		else
		{
			txtTitle.setTextColor(Color.BLACK);
			txtDueDate.setTextColor(Color.BLACK);
		}
			
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		txtTitle.setText(list.get(position).first);
		txtDueDate.setText(sdf.format(taskDue));
		return v;

		
	}

}
