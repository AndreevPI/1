package com.company;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner in = new Scanner(System.in);
        printHelp();
        String x = " ";
        DBHandler db = new DBHandler();
        while (x != "exit") {
            x = in.nextLine();
            switch (x) {
                case "add country":
                    db.insertCountry();
                    break;
                case "edit country":
                    db.editCountry();
                    break;
                case "add city":
                    db.insertCity();
                    break;
                case "edit city":
                    db.editCity();
                    break;
                case "show":
                    db.showAll();
                    break;
                case "show city":
                    db.showCity();
                    break;
                case "show country":
                    db.showCountry();
                    break;
                case "commit":
                    db.commitDB();
                    break;
                case "rollback":
                    db.rollbackDB();
                    break;
                case "delete country":
                    db.deleteCountry();
                    break;
                case "delete city":
                    db.deleteCity();
                    break;
                case "open":
                    db.open();
                    break;
                case "close":
                    db.close();
                    break;
                case "help":
                    printHelp();
                    break;
                case "exit":
                    x = "exit";
                    break;
                default:
                        System.out.println("Неизвестная команда");
                        break;
            }
        }
    }

    public static void printHelp() {
        System.out.println("open - открыть базу данных \n" +
                "close - закрыть базу данных \n" +
                "add country - добавить новую страну \n" +
                "edit country - редактировать страну \n" +
                "delete country - удалить страну \n" +
                "show country - вывод всех стран \n" +
                "add city - добавить новый город \n" +
                "edit city - редактировать город \n" +
                "delete city - удалить город \n" +
                "show city - вывод всех городов \n" +
                "commit- выполнить подтверждение БД \n" +
                "rollback- откат до последнего подтверждения \n" +
                "show - вывод всей таблицы \n" +
                "help - вывод всех команд \n" +
                "exit - выход из программы \n");
    }
}
