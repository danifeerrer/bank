package services;


import objects.Account;
import objects.Customer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import java.util.Date;


public class Engine {

    Scanner input = new Scanner(System.in);
    public static Customer customer;
    private final String url = "jdbc:mysql://localhost:3306/bank";
    private final String username = "root";
    private final String password = "$contraseña$11";
    private static AccountService accountService;
    public static int idOutcome = 19;

    public static int idIncome = 18;
    private static UserService userService;
    private static MovementService movementService;

    static dataBaseService dataBaseService = new dataBaseService();

    public void work() {
        boolean run = true;
        while(run){
            while(customer == null){
                UserService.menu1();
                String numero = input.nextLine();
                while(!isNumeric(numero)){
                    System.out.println("Write a name between 1 and 9: ");
                    numero = input.nextLine();
                }
                int number = Integer.parseInt(numero);
                switch(number){
                    case 1:
                        AccountService.create_account();
                        break;
                    case 2:
                        UserService.login();
                        break;
                    case 3:
                        System.out.println("Saliste de la aplicacion");
                        System.exit(0);
                        break;
                }
            }
            while(customer != null){
                UserService.menu2();
                String numero = input.nextLine();
                while(!isNumeric(numero)){
                    System.out.println("Write a name between 1 and 9: ");
                    numero = input.nextLine();
                }
                int number = Integer.parseInt(numero);
                switch(number){
                    case 1:
                        MovementService.addMoney();
                        break;
                    case 2:
                        MovementService.withDraw();
                        break;
                    case 3:
                        AccountService.info();
                        break;
                    case 4:
                        MovementService.transaction();
                        break;
                    case 5:
                        UserService.movements();
                        break;
                    case 6:
                        customer = null;
                        break;
                    case 7:
                        System.exit(0);
                }

            }
        }
    }
    private static boolean isNumeric(String parametro){
        boolean resultado;
        try {
            Integer.parseInt(parametro);
            resultado = true;
        }
        catch(NumberFormatException excepcion){
            resultado = false;
        }
        return resultado;
    }


}
