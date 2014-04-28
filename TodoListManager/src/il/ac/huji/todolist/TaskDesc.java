package il.ac.huji.todolist;

import java.util.Date;


public class TaskDesc
{
	public TaskDesc(long id,String name, Date due) {
		_name=name;
		_due=due;
		_id=id;
	}
	public String getName(){
		return _name;
	}
	
	public Date due(){
		return _due;
	}
	
	public long id(){
		return _id;
	}
	
	private String _name;
	private Date _due;
	private long _id;
}
