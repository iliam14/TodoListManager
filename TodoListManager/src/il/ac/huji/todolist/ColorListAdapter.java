package il.ac.huji.todolist;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


class ColorListAdapter extends ArrayAdapter<String> {

	public ColorListAdapter(Context context, int resource,
			List<String> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) super.getView(position, convertView, parent);
		if (position %2 == 0 )
			tv.setTextColor(Color.RED);
		else
			tv.setTextColor(Color.BLUE);
		return tv;
		//return super.getView(position, convertView, parent);
		
	}
 
 
  }

 