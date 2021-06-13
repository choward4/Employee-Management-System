package com.cognixia.jump.project.JavaFinal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class ManagementSystemConsole {
	
	// variables for Object/data files for employees and departments
	private static final File employeeFile = new File("./resources/employee.data");
	private static final File departmentFile = new File("./resources/department.data");
	
	// ArrayLists for employees and departments
	private static ArrayList<Employee> employees = new ArrayList<Employee>();
	private static ArrayList<Department> departments = new ArrayList<Department>();
	
	// formatting strings
	public final static String breaker1 = "\n***************";
	public final static String breaker2 = "***************\n";
	public final static String error = "!!!!";
	public final static String right = "(>^-^)>";
	public final static String left = "<(^-^<)";
	
	public static void main(String[] args) {
		
		// load in employees and departments
		setup();
		
		Scanner input = new Scanner(System.in);
		
		String optionsString = breaker2 + "Press \"1\" to add an Employee\n"
				+ "Press \"2\" to update an Employee\n"
				+ "Press \"3\" to remove an Employee\n"
				+ "Press \"4\" to list Employee Information\n"
				+ "Press \"5\" to add a Department\n"
				+ "Press \"6\" to update a Department\n"
				+ "Press \"7\" to remove a Department\n"
				+ "Press \"8\" to list the Departments\n"
				+ "Press \"9\" to exit the Terminal\n"
				+ breaker2;
		
		System.out.println("Welcome to the Employee Management Console " + right + "\n");
		System.out.println(optionsString);
		
		// give options to user until exiting with option "9"
		while(true) {
			
			System.out.println("(Press \"0\" to list the options again)");
			System.out.print("Please enter a number for your option: ");
			
			int option = input.nextInt();
			input.nextLine();
			
			switch(option) {
				case 1:
					addEmployee(input);
					break;
				case 2:
					updateEmployee(input);
					break;
				case 3: 
					removeEmployee(input);
					break;
				case 4:
					listEmployeeInfo(input);
					break;
				case 5:
					addDepartment(input);
					break;
				case 6:
					updateDepartment(input);
					break;
				case 7:
					try {
						removeDepartment(input);
					} catch (DepartmentNotEmptyException e) {
						System.out.println("\n"+ error + "Cannot remove a department that has employees" + error + "\n");
					}
					break;
				case 8:
					listDepartments();
					break;
				case 9:
					input.close();
					saveData();
					System.out.println("\nThank you for using the Employee Management System. Come again " + right + " " + left);
					return;
				case 0:
					System.out.println("\n" + optionsString);
					break;
				default:
					System.out.println(error + "Invalid number" + error + "\n");
					break;
				
			}

		}

	}
	
	// Adds employee to employees
	public static void addEmployee(Scanner input) {
		
		// variables for employee constructor
		String name;
		String department;
		int salary;
		
		System.out.print("Enter name: ");
		name = input.nextLine();
		
		System.out.print("Enter department: ");
		department = input.nextLine();
		
		System.out.print("Enter salary: ");
		salary = input.nextInt();
		input.nextLine();
		
		employees.add(new Employee(name, department, salary));
	}
	
	// updates employee based on ID
	public static void updateEmployee(Scanner input) {
		
		System.out.print("Enter Id of the employee you would like to update: ");
		int id = input.nextInt();
		input.nextLine();
		
		for(int i = 0; i < employees.size(); i++) {
			Employee curr = employees.get(i);
			if(curr.getId() == id) {
				
				char response;
				
				// name update
				System.out.print("Would you like to update the name? (y/n) [n]: ");
				response = (char) input.next().charAt(0);
				input.nextLine();
				
				if(response == 'y' || response == 'Y') {
					System.out.println("Enter new name: ");
					String name = input.nextLine();
					curr.setName(name);
				}
				
				// department update
				System.out.print("Would you like to update the department? (y/n) [n]: ");
				response = (char) input.next().charAt(0);
				input.nextLine();
				
				if(response == 'y' || response == 'Y') {
					System.out.print("Enter new department: ");
					String department = input.nextLine();
					curr.setDepartment(department);
				}
				
				// salary update
				System.out.print("Would you like to update the salary? (y/n) [n]: ");
				response = (char) input.next().charAt(0);
				input.nextLine();
				
				if(response == 'y' || response == 'Y') {
					System.out.print("Enter new salary: ");
					int salary = input.nextInt();
					input.nextLine();
					curr.setSalary(salary);
				}
				
				return;
			}
		}
		
		System.out.println(error + "No employee exists with id "+ id + " " + error);
	}
	
	// removes Employee based on ID
	public static void removeEmployee(Scanner input) {
		
		System.out.print("Enter the ID of the employee you would like to remove: ");
		int id = input.nextInt();
		input.nextLine();
		
		for(int i = 0; i < employees.size(); i++) {
			if(employees.get(i).getId() == id) {
				employees.remove(i);
				
				System.out.println(breaker1);
				System.out.println("Employee removed");
				System.out.println(breaker2);
				return;
			}
		}
		
		System.out.println(error + "Employee does not exist" + error);
		return;
	}
	
	// lists Employees and all their data
	public static void listEmployeeInfo(Scanner input) {
		
		if (!employees.isEmpty()) {
			System.out.println(breaker1);
			Stream<Employee> stream = employees.stream();
			stream.forEach(e -> System.out.println("ID: " + e.getId()
													+ "; Name: " + e.getName()
													+ "; Department: " + e.getDepartment()
													+ "; Salary: $" + e.getSalary()) );
			System.out.println(breaker2);
		} else {
			System.out.println(breaker1 + "\nNo employees");
			System.out.println(breaker2);
		}
		
	}
	
	// adds Department based on user input to departments
	public static void addDepartment(Scanner input) {
		
		// variables for department constructor
		String name;
		int budget;
		String phone;
		Stream<Department> stream = departments.stream();
		
		System.out.print("Enter name for department: ");
		name = input.nextLine();
		final String nameCopy = name;
		
		// Department names must be unique. Return if not unique
		if(stream.anyMatch(d -> d.getName().equals(nameCopy))) {
			System.out.println(error + "Department already exists");
			return;
		} 
		
		System.out.print("Enter a budget for company ");
		budget = input.nextInt();
		input.nextLine();
		
		System.out.print("Enter phone number for department: ");
		phone = input.nextLine();
		

		departments.add(new Department(name, budget, phone));
		
	}
	
	// updates values of department
	public static void updateDepartment(Scanner input) {
		
		System.out.print("Enter the name of the department: ");
		String depName = input.nextLine();
		
		for(int i = 0; i < departments.size(); i++) {	
			Department curr = departments.get(i);
			if(curr.getName().equals(depName)) {
				char response;
				
				// name update
				System.out.print("Would you like to update the name? (y/n) [n]: ");
				response = (char) input.next().charAt(0);
				input.nextLine();
				
				if(response == 'y' || response == 'Y') {
					System.out.print("Enter a new name: ");
					String name = input.nextLine();
					curr.setName(name);
				}
				
				// budget update
				System.out.println("Would you like to update the budget? (y/n) [n]");
				response = (char) input.next().charAt(0);
				input.nextLine();
				
				if(response == 'y' || response == 'Y') {
					System.out.print("Enter a new budget: ");
					int budget = input.nextInt();
					input.nextLine();
					curr.setBudget(budget);
				}
				
				// phone update
				System.out.println("Would you like to update the phone? (y/n) [n]");
				response = (char) input.next().charAt(0);
				input.nextLine();
				
				if(response == 'y' || response == 'Y') {
					System.out.print("Enter a new phone number: ");
					String phone = input.nextLine();
					curr.setPhone(phone);
				}
				
				return;
			}
		}
		
		System.out.println(error + "No department exists under the name \"" + depName + "\"" + error);
		return;
	}
	
	// returns true if there are no employees currently in the department
	public static boolean departmentEmpty(String departmentName) {
		
		Stream<Employee> stream = employees.stream();
		boolean empty = !stream.anyMatch(e -> e.getDepartment().equals(departmentName));
		
		return empty;
	}
	
	// removes department, cannot remove a department with active employees
	public static void removeDepartment(Scanner input) throws DepartmentNotEmptyException {
		
		String toRemove;
		
		System.out.print("Enter the name of the department to remove: ");
		toRemove = input.nextLine();
		
		for(int i = 0; i < departments.size(); i++) {
			
			String name = departments.get(i).getName();
			if(name.equals(toRemove)) {
				
				// Check if department is empty before removing it
				if(departmentEmpty(name)) {
					
					departments.remove(i);
					System.out.println(breaker1);
					System.out.println("Successfully Removed " + name);
					System.out.println(breaker2);
					
				} else {
					throw new DepartmentNotEmptyException(name);
				}
				
				return;
			}
		}
		
		System.out.println("No department exists under the name \"" + toRemove + "\"");
		return;
		
	}
	
	// lists all of the departments and their attributes
	public static void listDepartments() {
		System.out.println(breaker1);
		if (!departments.isEmpty()) {
			Stream<Department> stream = departments.stream();
			stream.forEach(d -> System.out.println("Name: " + d.getName()
													+ "; Budget: $" + d.getBudget()
													+ "; Phone: " + d.getPhone()));
			System.out.println(breaker2);
		} else {
			System.out.println("No Departments\n" + breaker2);
			
		}
	}
	
	// saves data to the .data files
	public static void saveData() {
		
		if(!employees.isEmpty()) {
			// variables for saving employees
			FileOutputStream employeeOut = null;
			ObjectOutputStream employeeWriter = null;
			
			try {

				// write employees
				//employeeFile.createNewFile();
				employeeOut = new FileOutputStream(employeeFile);
				employeeWriter = new ObjectOutputStream(employeeOut);
				
				employeeWriter.writeObject(employees);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					employeeWriter.close();
					employeeOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} else {
			employeeFile.delete();
		}
		
		if(!departments.isEmpty()) {
			//variables for saving departments
			FileOutputStream departmentOut = null;
			ObjectOutputStream departmentWriter = null;
			
			try {
				//departmentFile.createNewFile(););
				departmentOut = new FileOutputStream(departmentFile);
				departmentWriter = new ObjectOutputStream(departmentOut);
				
				departmentWriter.writeObject(departments);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					departmentWriter.close();
					departmentOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} else {
			departmentFile.delete();
		}
		
		
	}
	
	// reads in data from the .data files for employees and departments
	@SuppressWarnings("unchecked")
	public static void setup() {
		// variables for reading employees
		FileInputStream employeeIn = null;
		ObjectInputStream employeeReader = null;
		
		try {
			// read in employees
			employeeIn = new FileInputStream(employeeFile);
			employeeReader = new ObjectInputStream(employeeIn);
			
			employees = (ArrayList<Employee>) employeeReader.readObject();
			
			// update nextId
			if(!employees.isEmpty()) {
				Stream<Employee> eStream = employees.stream();
				int i = 1;
				
				int nextId = eStream.map(e -> e.getId()).reduce(0, (max, curr) -> curr > max ? curr : max);
				Employee.setNextId(++nextId);
				
			}
		} catch (FileNotFoundException e) {
			// this is fine
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// close employee readers
				if(employeeReader != null) employeeReader.close();
				if(employeeIn != null) employeeIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// variables for reading departments
		FileInputStream departmentIn = null;
		ObjectInputStream departmentReader = null;
		
		try {
			// read departments
			departmentIn = new FileInputStream(departmentFile);
			departmentReader = new ObjectInputStream(departmentIn);
			
			departments = (ArrayList<Department>) departmentReader.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// this is fine
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// close department readers
				if(departmentReader != null) departmentReader.close();
				if(departmentIn != null) departmentIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	

}
