package model;

import dao.DaoImplMongoDB;
import main.Logable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import dao.Dao;
import dao.DaoImplHibernate;

import java.sql.SQLException;

@Entity
@Table(name = "users")
public class Employee extends Person implements Logable {
    
	@Id
    @Column(name = "employeeId")
    private int id;

    @Column(name = "employeePassword")
    private String password;

    public Employee() {
        super("Default Name");
    }

    public Employee(int id, String name, String password) {
        super(name);
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean login(int employeeId, String password) {
        DaoImplMongoDB dao = new DaoImplMongoDB();
        boolean isAuthenticated = false;

        try {
            dao.connect();
            Employee employee = dao.getEmployee(employeeId, password);
            isAuthenticated = (employee != null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.disconnect();
        }

        return isAuthenticated;
    }

   
}