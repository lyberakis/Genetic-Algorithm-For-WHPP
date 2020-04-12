package main;

public class Employee {
	private int days=14;
	
	private int hours;
	private int[] shift = new int[days];
	
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int[] getShift() {
		return shift;
	}
	public void setShift(int[] shift) {
		for (int i=0; i<shift.length; i++)
			this.shift[i] = shift[i];
	}
	
	public Employee() {
		this.hours = 0;
		for (int i=0; i<days; i++)
			shift[i]=0;
	}
	
	
}
