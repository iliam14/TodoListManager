package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_new_todo_item);
		final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

		final EditText edtNewItem = (EditText) findViewById(R.id.edtNewItem);
		Button btnOK = (Button) findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(datePicker.getYear(), datePicker.getMonth(),
						datePicker.getDayOfMonth());
				Date date = calendar.getTime();

				// Create and send result intent
				Intent result = new Intent();
				result.putExtra("title", edtNewItem.getText().toString());
				result.putExtra("dueDate", date);
				setResult(RESULT_OK, result);
				finish();
			}
		});
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();

			}
		});
	}

}
