package model;

import main.Logable;
import javax.swing.JTextField;
import dao.Dao;
import dao.DaoImplJDBC;

public class Employee extends Person implements Logable {
    
	private Dao employeeDao = new DaoImplJDBC();
    /* private int employeeId;
    private String password;
    private static final int USER = 123;
    private static final String PASSWORD = "test"; */

    public Employee(int employeeId, String password) {
        super("Default Name");
    }

    @Override
    public boolean login (int employeeId, String password) {
    	
    	boolean isAuthenticated = false;
    	
    	try {
    		employeeDao.connect();
    		Employee employee = employeeDao.getEmployee(employeeId, password);
    		if (employee != null) {
    			isAuthenticated = true;
    		}
    		employeeDao.disconnect();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return isAuthenticated;
    	
    }

   
}