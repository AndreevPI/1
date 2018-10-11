package com.company;
import org.sqlite.JDBC;

import java.io.*;
import java.sql.*;
import java.util.*;

import static com.company.Const.*;

public class DBHandler {
    Scanner in = new Scanner(System.in);
    Connection dbConection = null;
    Statement stmt = null;
    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> countryList = new ArrayList<>();
    String filenameCitys = "citys.dat";
    String filenameCountrys = "countrys.dat";

//Подключение к БД
    public boolean open() {
        try {
            Class.forName("org.sqlite.JDBC");
            dbConection = DriverManager.getConnection("jdbc:sqlite:kurs.db");
            System.out.println("База данных подключена");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

// Метод добавления новой страны
    public void insertCountry() {
        System.out.println("Введите название страны: ");
        String name = in.nextLine();
        if(checkNameCountry(name, countryList)) {
            try {
                System.out.println("Введите площадь страны: ");
                Float area = Float.parseFloat(in.nextLine());
                String query = "INSERT INTO " + COUNTRY_TABLE + " ("+ NAME_COUNTRY +", "+ AREA +") " +
                        "VALUES ('" + name + "', '" + area + "');";
                stmt = dbConection.createStatement();
                stmt.executeUpdate(query);
                System.out.println("Запись добавлена");
            }
            catch (SQLException e) {
                System.out.println("Ошибка вводимых данных. Проверте вводимые данные и попробуйте еще раз.");
            }
            catch (NullPointerException e) {
                System.out.println("База данных не подключена");
            }
            catch (InputMismatchException e) {
                System.out.println("Какое-то из полей было не заполненно или введен не тот тип данных.");
            }
            catch (NumberFormatException e) {
                System.out.println("Некоректная площадь");
            }
       }
        else
            System.out.println("Эта страна уже есть в таблице");
    }

// Метод добавления нового города
    public void insertCity() {
        System.out.println("Введите название города: ");
        String cityName = in.nextLine();
        if (checkNameCity(cityName, cityList)) {
            try {
                System.out.println("Введите население города: ");
                int population = Integer.parseInt(in.nextLine());
                System.out.println("Введите среднюю зарплату в городе: ");
                int salary = Integer.parseInt(in.nextLine());
                System.out.println("Введите страну, в которой находится город.");
                String idCountryString = in.nextLine();
                int idCountryKey = getIdCountry(idCountryString);
                String query = "INSERT INTO "+ CITY_TABLE +" ("+ NAME_CITY +", "+ POPULATION +", "+ SALARY +", "+ FOREIGN_KEY +") " +
                        "VALUES ('"+ cityName +"', '"+ population +"', '"+ salary +"', '"+ idCountryKey +"');";
                stmt = dbConection.createStatement();
                stmt.executeUpdate(query);
                System.out.println("Запись добавлена.");
            }
            catch (SQLException e) {
                System.out.println("Такой страны нет в БД, добавте сначала страну.");
            }
            catch (NullPointerException e) {
                System.out.println("База данных не подключена.");
            }
            catch (InputMismatchException e) {
                System.out.println("Какое-то из полей было не заполненно или введен не тот тип данных.");
            }
            catch (NumberFormatException e) {
                System.out.println("Некоректные значения населения или зарплаты (они должны быть в виде целых чисел)");
            }
        }
        else
            System.out.println("Этот город уже есть в БД");
    }

//Метод вывода полной таблицы
    public void showAll() {
        try {
            String query = "SELECT " + CITY_TABLE + "." + NAME_CITY + ", " + CITY_TABLE + "." + POPULATION + ", " + CITY_TABLE + "." + SALARY + ", " + COUNTRY_TABLE + "." + NAME_COUNTRY + ", " + COUNTRY_TABLE + "." + AREA +
                    " FROM " + CITY_TABLE + " LEFT JOIN " + COUNTRY_TABLE +
                    " ON (" + CITY_TABLE + "." + FOREIGN_KEY + " = " + COUNTRY_TABLE + "." + ID_COUNTRY + ");";
            stmt = dbConection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.printf("%-20s%-15s%-20s%-14s%-20s%n", "Город", "Население", "Средняя зарплата", "Страна", "Площадь страны");
            System.out.println("---------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-20s%-15s%-20s%-14s%-20s%n", rs.getString(NAME_CITY), rs.getString(POPULATION), rs.getString(SALARY), rs.getString(NAME_COUNTRY), rs.getString(AREA));
            }
        }
        catch (NullPointerException e) {
            System.out.println("База данных не подключена.");
        }
        catch (SQLException e) {
            System.out.println("Упс, что-то пошло не так.");
        }

    }

//Метод вывода таблицы город
    public void showCity() {
        try {
            String query = "SELECT * FROM "+ CITY_TABLE +"";
            stmt = dbConection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.printf("%-20s%-15s%-20s%n", "Город", "Население", "Средняя зарплата (руб)");
            System.out.println("---------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-20s%-15s%-20s%n", rs.getString(NAME_CITY), rs.getString(POPULATION), rs.getString(SALARY));
            }
        }
        catch (NullPointerException e) {
            System.out.println("База данных не подключена.");
        }
        catch (SQLException e) {
            System.out.println("Упс, что-то пошло не так.");
        }

    }

//Метод вывода таблицы страна
    public void showCountry() {
        try {
            String query = "SELECT * FROM "+ COUNTRY_TABLE +"";
            stmt = dbConection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.printf("%-20s%-30s%n", "Страна", "Площадь (квадратные километры)");
            System.out.println("---------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-20s%-30s%n", rs.getString(NAME_COUNTRY), rs.getString(AREA));
            }
        }
        catch (NullPointerException e) {
            System.out.println("База данных не подключена.");
        }
        catch (SQLException e) {
            System.out.println("Упс, что-то пошло не так.");
        }
    }

//Метод изменения строк таблицы Страна
    public void editCountry() {
        try {
            System.out.println("Введите название страны, которую хотите редактировать");
            String name = in.nextLine();
            System.out.print("Новое название страны: ");
            String newName = in.nextLine();
            System.out.print("Новая площадь страны: ");
            Float newArea = Float.parseFloat(in.nextLine());
            String query = "UPDATE "+ COUNTRY_TABLE +" SET "+ NAME_COUNTRY +" = '"+ newName +"', "+ AREA +" = "+ newArea +
                    " WHERE "+ ID_COUNTRY +" = "+ getIdCountry(name) +";";
            stmt = dbConection.createStatement();
            stmt.executeQuery(query);
            System.out.println("Запись изменена");
        }
        catch (SQLException e) {
            System.out.println("Упс, что-то пошло не так.");
        }
        catch (NullPointerException e) {
            System.out.println("База данных не подключена.");
        }
        catch (InputMismatchException e) {
            System.out.println("Какое-то из полей было не заполненно или введен не тот тип данных.");
        }
        catch (NumberFormatException e) {
            System.out.println("Некоректная площадь");
        }
    }

//Метод изменения строк таблицы Город
    public void editCity() {
        try {
            System.out.println("Введите название города, который хотите редактировать");
            String name = in.nextLine();
            System.out.print("Название города: ");
            String newName = in.nextLine();
            System.out.print("Численность города: ");
            int newPopulation = Integer.parseInt(in.nextLine());
            System.out.print("Средняя зарплата: ");
            int newSalary = Integer.parseInt(in.nextLine());
            String query = "UPDATE "+ CITY_TABLE +
                    " SET "+ NAME_CITY +" = '"+ newName +"', "+ POPULATION +" = "+ newPopulation +", "+ SALARY +" = "+ newSalary +
                    " WHERE "+ ID_CITY +" = "+ getIdCity(name) +";";
            stmt = dbConection.createStatement();
            stmt.executeQuery(query);
            System.out.println("Запись изменена");
        }
        catch (SQLException e) {
            System.out.println("Упс, что-то пошло не так.");
        }
        catch (NullPointerException e) {
            System.out.println("База данных не подключена.");
        }
        catch (InputMismatchException e) {
            System.out.println("Какое-то из полей было не заполненно или введен не тот тип данных.");
        }
        catch (NumberFormatException e) {
            System.out.println("Некоректные значения населения или зарплаты (они должны быть в виде целых чисел)");
        }
    }

//Метод удаления строк таблицы Страна
    public void deleteCountry() {
        try {
            System.out.println("Введите название страны, которую нужно удалить (При этом удалятся все города в этой стране)");
            String name = in.nextLine();
            try {
                String query1 = "DELETE FROM "+ CITY_TABLE +" WHERE "+ FOREIGN_KEY +" = "+ getIdCountry(name) +";";
                stmt = dbConection.createStatement();
                stmt.executeQuery(query1);
            }
            catch (SQLException e) {
            }
            String query2 = "DELETE FROM "+ COUNTRY_TABLE +" WHERE "+ ID_COUNTRY +" = '"+ getIdCountry(name) +"';";
            stmt = dbConection.createStatement();
            stmt.executeUpdate(query2);
            System.out.println("Запись удалена");
        }
        catch (SQLException e) {
            System.out.println("Такой страны нет в БД");
        }
        catch (NullPointerException e) {
            System.out.println("База данных не подключена.");
        }
        catch (InputMismatchException e) {
            System.out.println("Какое-то из полей было не заполненно или введен не тот тип данных.");
        }
    }

//Метод удаления строк таблицы Город
    public void deleteCity() {
        try {
            System.out.println("Введите название города, который хотите удалить");
            String name = in.nextLine();
            String query = "DELETE FROM "+ CITY_TABLE +" WHERE "+ ID_CITY +" = '"+ getIdCity(name) +"';";
            stmt = dbConection.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Запись удалена");
        }
        catch (SQLException e) {
            System.out.println("Такого города нет в БД");
        }
        catch (NullPointerException e) {
            System.out.println("База данных не подключена.");
        }
        catch (InputMismatchException e) {
            System.out.println("Какое-то из полей было не заполненно или введен не тот тип данных.");
        }

    }

//Метод сохранения последних изменений в БД
    public void commitDB() {
        try {
            dbConection.commit();
            System.out.println("Изменения сохранены");
        }
        catch (SQLException e) {
            System.out.println("Изменений не обнаружено");
        }
        catch (NullPointerException e) {
            System.out.println("База данных не подключена.");
        }
    }

//Метод отката к придыдущему сохранению
    public void rollbackDB() {
        try {
            dbConection.rollback();
            System.out.println("База данных успешно откатилась до последнего сохранения");
        }
        catch (SQLException e) {
            System.out.println("Последнего сохранения не обнаружено");
        }
        catch (NullPointerException e) {
            System.out.println("База данных не подключена.");
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

//Метод проверки уникальности вводимых названий стран
    public boolean checkNameCountry(String name, ArrayList<String> type) {
        try {
            String query = "SELECT "+ NAME_COUNTRY +" FROM "+ COUNTRY_TABLE +";";
            stmt = dbConection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                type.add(rs.getString(NAME_COUNTRY).toLowerCase().replaceAll("\\s+", ""));
            }
        }
        catch (SQLException e) {
            System.out.println("Error in SQL string");
        }
        if (type.size() == 0)
            type.add(" ");
        boolean result = false;
        String tmpName = name.toLowerCase().replaceAll("\\s+", "");

        if (type.indexOf(tmpName) == -1) {
                result = true;
            }
            else {
                result = false;
            }

        return result;
    }

//Метод проверки уникальности вводимых названий стран
    public boolean checkNameCity(String name, ArrayList<String> type) {
        try {
            String query = "SELECT "+ NAME_CITY +" FROM "+ CITY_TABLE +";";
            stmt = dbConection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                type.add(rs.getString(NAME_CITY).toLowerCase().replaceAll("\\s+", ""));
            }
        }
        catch (SQLException e) {
            System.out.println("Error in SQL string");
        }
        if (type.size() == 0)
            type.add(" ");
        boolean result = false;
        String tmpName = name.toLowerCase().replaceAll("\\s+", "");

        if (type.indexOf(tmpName) == -1) {
            result = true;
        }
        else {
            result = false;
        }

        return result;
}

//Закрытие соеденения с БД
    public void close() {
        try {
            dbConection.close();
            System.out.println("База данных закрыта");
        }
        catch (SQLException e) {
            System.out.println("Упс, что-то пошло не так.");
        }
        catch (NullPointerException e) {
            System.out.println("Она и так закрыта)");
        }
    }

    public String getNameCountry( int idCountryInt) throws SQLException, NullPointerException {
        String query = "SELECT "+ NAME_COUNTRY +" FROM "+ COUNTRY_TABLE +" WHERE "+ ID_COUNTRY +" = '"+ idCountryInt +"';";
        stmt = dbConection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String idCountryKey = rs.getString("nameCountry");
        return idCountryKey;//получить имя по айди и записать в сериализацию
    }

    public void Serializable() throws SQLException, NullPointerException, IOException {
        //страны
        ArrayList<Country> listCountrys = new ArrayList<Country>();
        //String filename = "country.dat";
        String query = "SELECT * FROM "+ COUNTRY_TABLE +";";
        stmt = dbConection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next())
        {
            listCountrys.add(new Country(rs.getInt("id"),rs.getString("nameCountry"), rs.getFloat("area")));
        }
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filenameCountrys)))
        {
            oos.writeObject(listCountrys);
            System.out.println("Файл был успешн записан");
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
        //город
        ArrayList<City> listCity = new ArrayList<City>();
        query = "SELECT * FROM "+ CITY_TABLE +";";
        stmt = dbConection.createStatement();
        rs = stmt.executeQuery(query);
        while(rs.next())
        {
            listCity.add(new City(rs.getInt("id"), rs.getString("nameCity"), rs.getInt("population"), rs.getInt("salary"), getNameCountry(rs.getInt("idCountry"))));
        }
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filenameCitys)))
        {
            oos.writeObject(listCity);
            System.out.println("Файл был успешн записан");
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
    }
    public void Derializable() throws SQLException, NullPointerException, IOException {
        //country
        ArrayList<Country> newCountrys= new ArrayList<Country>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filenameCountrys)))
        {

            newCountrys=((ArrayList<Country>)ois.readObject());
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
        for(Country p : newCountrys) {
            // System.out.printf("Name: %s Area: %f \n", p.getName(), p.getArea());
            String query = "INSERT INTO " + COUNTRY_TABLE + " ("+ NAME_COUNTRY +", "+ AREA +") " +
                    "VALUES ('" + p.getName() + "', '" + p.getArea() + "');";
            stmt = dbConection.createStatement();
            stmt.executeUpdate(query);
        }
        //city
        ArrayList<City> listCitys= new ArrayList<City>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filenameCitys)))
        {

            listCitys=((ArrayList<City>)ois.readObject());
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
        for(City t : listCitys) {
            //System.out.printf("Name: %s Area: %f \n", p.getName(), p.getArea());
            String query = "INSERT INTO "+ CITY_TABLE +" ("+ NAME_CITY +", "+ POPULATION +", "+ SALARY +", "+ FOREIGN_KEY +") " +
                    "VALUES ('"+ t.getName() +"', '"+ t.getPopulation() +"', '"+ t.getSalary() +"', '"+ getIdCountry(t.getCountry()) +"');";
            stmt = dbConection.createStatement();
            stmt.executeUpdate(query);
        }

    }
}
