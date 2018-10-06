package com.company;

public class City {
    private int id;
    private String name;
    private int population;
    private int salary;
    private String country;

    public City(int id, String name, int population, int salary, String country) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.salary = salary;
        this.country = country;
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

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
