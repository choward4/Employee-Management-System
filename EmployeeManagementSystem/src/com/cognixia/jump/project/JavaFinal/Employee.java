package com.cognixia.jump.project.JavaFinal;

import java.io.File;
import java.io.Serializable;

public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static int nextId;
	
	private String name;
	private String department;
	private int salary;
	private int id;
	
	public Employee(String name, String department, int salary) {
		this.name = name;
		this.department = department;
		this.salary = salary;
		this.id = nextId++;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getSalary() {
		return this.salary;
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	public String getDepartment() {
		return this.department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	// used by Management System to manually set the static ID upon reading Employees from .data file
	protected static void setNextId(int value) {
		Employee.nextId = value;
	}

}
