package il.ac.huji.todolist;

import java.util.Date;

// This class describes a task and used for sending the data to async task
public class TaskDesc
{
	public TaskDesc(String name, Date due){
		_name=name;
		_due=due;
		_id=0;
	}
	
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
	
	public void updateId(long id){
		_id = id;
	}
	
	private String _name;
	private Date _due;
	private long _id;
}
