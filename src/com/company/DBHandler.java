package com.company;
import org.sqlite.JDBC;
import java.sql.*;
import java.util.Scanner;
import java.util.Formatter;

import static com.company.Const.*;

public class DBHandler {
    Scanner in = new Scanner(System.in);
    Connection dbConection = null;
    Statement stmt = null;

//Подключение к БД
    public boolean open() {
        try {
            Class.forName("org.sqlite.JDBC");
            dbConection = DriverManager.getConnection("jdbc:sqlite:/Users/a1/Documents/Kursovaya/kurs.db");
            System.out.println("База данных подключена");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

// Метод добавления новой страны
    public void insertCountry() throws SQLException {
        System.out.println("Введите название страны: ");
        String name = in.nextLine();
        System.out.println("Введите площадь страны: ");
        Float area = in.nextFloat();
        String query = "INSERT INTO " + COUNTRY_TABLE + " ("+ NAME_COUNTRY +", "+ AREA +") " +
                "VALUES ('" + name + "', '" + area + "');";
        stmt = dbConection.createStatement();
        stmt.executeUpdate(query);
        System.out.println("Запись добавлена");
    }


// Метод добавления нового города
    public void insertCity() throws SQLException, NullPointerException{
        System.out.println("Введите название города: ");
        String cityName = in.nextLine();
        System.out.println("Введите население города: ");
        int population = in.nextInt();
        System.out.println("Введите среднюю зарплату в городе: ");
        int salary = in.nextInt();
        System.out.println("Введите страну, в которой находится город");
        in.nextLine();
        String idCountryString = in.nextLine();
        int idCountryKey = getIdCountry(idCountryString);
        String query = "INSERT INTO "+ CITY_TABLE +" ("+ NAME_CITY +", "+ POPULATION +", "+ SALARY +", "+ FOREIGN_KEY +") " +
                "VALUES ('"+ cityName +"', '"+ population +"', '"+ salary +"', '"+ idCountryKey +"');";
        stmt = dbConection.createStatement();
        stmt.executeUpdate(query);
        System.out.println("Запись добавлена");
    }

//Метод вывода полной таблицы (тревиальный способ)
    public void showAll() throws SQLException {
        String query = "SELECT "+ CITY_TABLE +"."+ NAME_CITY +", "+ CITY_TABLE +"."+ POPULATION +", "+ CITY_TABLE +"."+ SALARY +", "+ COUNTRY_TABLE +"."+ NAME_COUNTRY +", "+ COUNTRY_TABLE +"."+ AREA +
                " FROM "+ CITY_TABLE +" LEFT JOIN "+ COUNTRY_TABLE +
                " ON ("+ CITY_TABLE +"."+ FOREIGN_KEY +" = "+ COUNTRY_TABLE +"."+ ID_COUNTRY +");";
        stmt = dbConection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.printf("%-20s%-15s%-20s%-14s%-20s%n","Город", "Население", "Средняя зарплата", "Страна", "Площадь страны");
        System.out.println("---------------------------------------------------------------------------------------");
        while (rs.next()) {
            System.out.printf("%-20s%-15s%-20s%-14s%-20s%n", rs.getString(NAME_CITY), rs.getString(POPULATION), rs.getString(SALARY), rs.getString(NAME_COUNTRY), rs.getString(AREA));
        }

    }

//Метод получения значения id страны
    public int getIdCountry(String idCountryString) throws SQLException, NullPointerException {
        String query = "SELECT "+ ID_COUNTRY +" FROM "+ COUNTRY_TABLE +" WHERE "+ COUNTRY_TABLE +"."+ NAME_COUNTRY +" = '"+ idCountryString +"';";
        stmt = dbConection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int idCountryKey = rs.getInt("id");
        return idCountryKey;
    }

//Метод получения значения id города
    public int getIdCity(String name) throws SQLException, NullPointerException {
        String query = "SELECT " + ID_CITY + " FROM " + CITY_TABLE + " WHERE " + CITY_TABLE + "." + NAME_CITY + " = '" + name + "';";
        stmt = dbConection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int id = rs.getInt("id");
        return id;
    }

//Закрытие соеденения с БД
    public void close() {
        try {
            dbConection.close();
            System.out.println("База данных закрыта");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
