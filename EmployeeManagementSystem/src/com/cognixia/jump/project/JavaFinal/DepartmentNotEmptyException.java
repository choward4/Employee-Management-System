package com.cognixia.jump.project.JavaFinal;

public class DepartmentNotEmptyException extends Exception {

	private static final long serialVersionUID = 1L;

	// Used when trying to remove a department that has employees. Operation is not allowed
	// Employees must be removed or change departments first
	public DepartmentNotEmptyException(String s) {
		super(s);
	}
}
