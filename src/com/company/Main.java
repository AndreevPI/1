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
                    break;
                case "add city":
                    db.insertCity();
                    break;
                case "edit city":
                    break;
                case "show":
                    db.showAll();
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
                "add city - добавить новый город \n" +
                "edit city - редактировать город \n" +
                "show - вывод всей таблицы \n" +
                "help - вывод всех команд \n" +
                "exit - выход из программы \n");
    }
}
