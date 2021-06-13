package com.cognixia.jump.project.JavaFinal;

import java.io.Serializable;

public class Department implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private int budget;
	private String phone;
	
	public Department(String name, int budget, String phone) {
		this.name = name;
		this.budget = budget;
		this.phone = phone;
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getBudget() {
		return budget;
	}
	
	public void setBudget(int budget) {
		this.budget = budget;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String number) {
			phone = number;
	}

}
