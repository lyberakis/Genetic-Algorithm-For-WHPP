package main;

import java.util.ArrayList;
import java.util.Random;

public class Chromosome {
	final int numOfEmployees=30, numOfDays=14, numOfShifts=3;
	ArrayList<Employee> employees = new ArrayList<Employee>();
	private double evaluation;

	public double getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(double evaluation) {
		this.evaluation = evaluation;
	}
	
	public ArrayList<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(ArrayList<Employee> employees) {
		this.employees = employees;
	}

	public Chromosome() {
		this.evaluation=0;
		
		for (int i=0; i<numOfEmployees; i++) {//init employees
			Employee emp = new Employee();
			employees.add(emp);
		}
		
		for (int i=0; i<numOfDays; i++) {//for each day
			int[] dayShifts = findShiftsOfCurrentDay(i);//find what day is it
			int possibility = new Random().nextInt(10);
			
			for (int j=0; j<numOfShifts; j++) {//for each shift
				int randEmp = new Random().nextInt(numOfEmployees);//random employee
				boolean assign = true;
				
				if (possibility!=8 && possibility!=9)
					assign = checkEmployeeAvailability(employees.get(randEmp), i);//check if we can assign shift
					
				if (assign==false) {
					j--;
					continue;
				}else {
					int[] shifts = employees.get(randEmp).getShift();//assign shift
					int addedHours=0;
					
					shifts[i]=j+1;
					employees.get(randEmp).setShift(shifts);
										
					if (j==0 || j==1) 
						addedHours=8;
					else
						addedHours=10;
					employees.get(randEmp).setHours(employees.get(randEmp).getHours()+addedHours);
					
					if (dayShifts[j]!=1) {
						dayShifts[j]--;
						j--;
					}
				}
			}
		}
	}
	
	public int[] findShiftsOfCurrentDay(int day) {
		int[] shifts = new int[3];
		if (day==0 || day==1 || day==7 || day==8) {
			shifts[0]=10;
			shifts[1]=10;
			shifts[2]=5;
			return shifts; 
		}else if (day==2 || day==4 || day==9 || day==11) {
			shifts[0]=5;
			shifts[1]=10;
			shifts[2]=5;
			return shifts;
		}else {
			shifts[0]=5;
			shifts[1]=5;
			shifts[2]=5;
			return shifts;
		}
	}
	
	public boolean checkEmployeeAvailability(Employee emp, int day) {
		int[] shift = emp.getShift();
				
		if (shift[day]!=0)
			return false;
		else		
			return true;
	}
	
	public void printChromo () {
		System.out.println("Ä Ô Ô Ð Ð Ó Ê Ä Ô Ô Ð Ð Ó Ê");
		for (int i=0; i<numOfEmployees; i++) {
			int[] shifts = employees.get(i).getShift();
			for (int j=0; j<shifts.length; j++)
				System.out.print(shifts[j]+" ");
			System.out.println("Employee "+i);
		}
	}
	
	public boolean evaluateChromo () {
		int[] shifts = new int[numOfDays];
		boolean hardConstraint = true;
		
		for (int i=0; i<numOfDays; i++) {//checking hard constraint
			int counter=0;
			
			for (int j=0; j<numOfEmployees; j++) {
				shifts = employees.get(j).getShift();
				if (shifts[i]==0)
					counter++;
			}
			
			if (i==0 || i==1 || i==7 || i==8) {
				if (counter!=5)
					hardConstraint = false; 
			}else if (i==2 || i==4 || i==9 || i==11) {
				if (counter!=10)
					hardConstraint = false; 
			}else {
				if (counter!=15)
					hardConstraint = false; 
			}
		}//result (200-300)/5000 on 80% possibility of having a totally random day
		
		if (hardConstraint==true) {//checking soft constraints
			chromoScore();
		}
		return hardConstraint;
	}
	
	public void chromoScore () {
		int hours=0;
		int[] shifts = new int[numOfDays];
		this.evaluation=0;
		correctingHours();		
		for (int i=0; i<numOfEmployees; i++) {
			hours = employees.get(i).getHours();
			shifts = employees.get(i).getShift();
			
			if (hours>70)//max 70 hours
				this.evaluation = this.evaluation+1000;
			
			int countConsecutiveDays=0, countConsecutiveNights=0;
			for (int j=0; j<numOfDays; j++) {
				if (shifts[j]!=0)//max 7 consecutive days
					countConsecutiveDays++;
				else
					countConsecutiveDays=0;
				if (countConsecutiveDays>7)
					this.evaluation = this.evaluation+1000;
										
				if (shifts[j]==3)//max 4 consecutive nights
					countConsecutiveNights++;
				else
					countConsecutiveNights=0;
				if (countConsecutiveNights>4)
					this.evaluation = this.evaluation+1000;
				
				if (j!=numOfDays-1 && shifts[j]==3 && shifts[j+1]==1)//avoid night shift and the next day morning shift
					this.evaluation = this.evaluation+1000;
					
				if (j!=numOfDays-1 && shifts[j]==2 && shifts[j+1]==1)//avoid evening shift and the next day morning shift
					this.evaluation = this.evaluation+800;
				
				if (j!=numOfDays-1 && shifts[j]==3 && shifts[j+1]==2)//avoid night shift and the next day evening shift
					this.evaluation = this.evaluation+800;
				
				if (j<numOfDays-5 && shifts[j]==3 && shifts[j+1]==3 && shifts[j+2]==3 && shifts[j+3]==3 && (shifts[j+4]!=0 || shifts[j+5]!=0))
					this.evaluation = this.evaluation+100;//at least 2 free days after 4 night shifts
				
				if (j<numOfDays-8 && shifts[j]!=0 && shifts[j+1]!=0 && shifts[j+2]!=0 && shifts[j+3]!=0 && shifts[j+4]!=0 && shifts[j+5]!=0 && shifts[j+6]!=0 &&
				   (shifts[j+7]!=0 || shifts[j+8]!=0)) {//at least 2 free days after 7 working days
					this.evaluation = this.evaluation+100;
				}
				
				if (j<numOfDays-2 && shifts[j]!=0 && shifts[j+1]==0 && shifts[j+2]!=0)//avoid work-free-work days
					this.evaluation = this.evaluation+1;
				
				if (j<numOfDays-2 && shifts[j]==0 && shifts[j+1]!=0 && shifts[j+2]==0)//avoid free-work-free days
					this.evaluation = this.evaluation+1;
				
				if (j==numOfDays-1 && (shifts[5]!=0 || shifts[6]!=0) && (shifts[12]!=0 || shifts[13]!=0))//not consecutive weekends
					this.evaluation = this.evaluation+1;
			}
		}
		this.evaluation = 10000000/this.evaluation;//reverse result from the worst being the highest to being the lowest
	}
	
	public void correctingHours() {
		int[] shifts = new int[numOfDays];
		int addedHours;
		
		for (int i=0; i<numOfEmployees; i++) {
			shifts = employees.get(i).getShift();
						
			for (int j=0; j<numOfDays; j++){
				addedHours=0;
				if (shifts[j]==1 || shifts[j]==2) 
					addedHours=8;
				else if (shifts[j]==3)
					addedHours=10;
				employees.get(i).setHours(employees.get(i).getHours()+addedHours);
			}
		}
	}
}
