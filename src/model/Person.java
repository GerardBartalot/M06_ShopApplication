package model;

public abstract class Person {
    
	protected String name;

	
    public Person(String name) {
        this.name = name;
    }

    
    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
