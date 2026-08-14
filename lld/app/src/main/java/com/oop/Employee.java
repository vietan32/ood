package com.oop;

public class Employee extends Person {
    private String position;

    public Employee(String name, int age, String position) {
        super(name, age);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void info() {
        System.out.println(String.format("%s, %d, %s", getName(), getAge(), position));
    }

    public static void main(String[] args) {
        Employee employee = new Employee("Alice", 22, "Receptionist");
        employee.info();
    }
}
