import objects.Account;
import objects.BankAccount;
import objects.Costumer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

/*
        String url = "jdbc:mysql://localhost:3306/bank";
        String username = "root";
        String password = "$contraseña$11";

        try{

            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from account");

            //Statement statement1 = connection.createStatement();
            //ResultSet resultSet1 = statement1.executeQuery("")

            while (resultSet.next()){
                System.out.println(resultSet.getString("account_number" ) + " | " +
                        resultSet.getString("branch_name") + " | " + resultSet.getString("balance"));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
 */
public class Engine {
    private static JLabel userLabel;
    private static JTextField userText;
    private static JLabel passwordLabel;
    private static JPasswordField passwordText;
    private static JButton button;
    private static JLabel success;
    Scanner input = new Scanner(System.in);
    private Costumer costumer;
    private String url = "jdbc:mysql://localhost:3306/bank";
    private String username = "root";
    private String password = "$contraseña$11";


    public void work() {
        boolean run = true;
        BankAccount account_menu = null;
        ATM atm_menu = null;
        String ownerName;
        double account_number, balance1;
        while(run){

            System.out.println("\n1. Crear una cuenta corriente-> ");
            System.out.println("2. Login");
            System.out.println("3. Ingresar dinero en la cuenta->");
            System.out.println("4. Retirar dinero de la cuenta-> ");
            System.out.println("5. Imprimir informacion sobre la cuenta-> ");
            System.out.println("6. Logout");
            System.out.println("7. Salir-> \n");


            String numero = input.nextLine();
            while(!isNumeric(numero)){
                System.out.println("Escriba un numero del 1 al 5: ");
                numero = input.nextLine();
            }
            int number = Integer.parseInt(numero);
            switch(number){
                case 1:
                    System.out.print("Ingrese su nombre: ");
                    String nombre = (input.nextLine());
                    System.out.print("Ingrese su numero de cuenta: ");
                    String numero_cuent = input.nextLine();
                    double cuenta = (Double.parseDouble(numero_cuent));
                    while(numero_cuent.length() != 3){
                        System.out.print("Numero no valido, ingrese otro numero de cuenta: ");
                        numero_cuent = input.nextLine();
                    }
                    System.out.print("Ingrese su balance: ");
                    int balance = (Integer.parseInt(input.nextLine()));
                    System.out.println("Ingrese su contraseña: ");
                    String password2 = input.nextLine();
                    try{
                        Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("insert into `bank`.`tabla`(`ownerName`, `accountNumber`, `balance`, `password`) VALUES ('" + nombre + "', "  + cuenta + ", " + balance + ", '" + password2 + "');");
                        /*while(resultSet.next()) {
                            System.out.println((resultSet.getString("ownerName") + " " +
                                    resultSet.getString("accountNumber")) + " " +
                                    resultSet.getDouble("balance"));
                        }
                        */

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 2:

                    login();
                    break;

                case 3:
                    System.out.print("Indique la cantidad que quiera ingresar: ");
                    String cantidad = input.nextLine();
                    if(account_menu == null){
                        System.out.println("First you need to login: ");
                        break;
                    }
                    while(isDouble(cantidad) != true){
                        System.out.print("Cantidad equivocada, ingrese de nuevo: ");
                        cantidad = input.nextLine();
                    }
                    double cantidad_para_añadir = Double.parseDouble(cantidad);
                    //En que casos se puede utilizar null??
                    atm_menu.addMoney(cantidad_para_añadir);
                    System.out.println(account_menu.getBalance());
                    try{
                        Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("update tabla set balance=" + account_menu.getBalance() + "where ownerName='" + account_menu.getOwnerName() + "';");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    System.out.print("Indique la cantidad que quiera retirar: ");
                    String cantidad2 = input.nextLine();

                    if(account_menu == null){
                        System.out.println("First you need to login");
                        break;
                    }
                    while(!isDouble(cantidad2)){
                        System.out.println("Cantidad equivocado, cuanto quiere retirar?  ");
                        cantidad2 = input.nextLine();
                    }
                    double cantidad_para_retirar = Double.parseDouble(cantidad2);
                    atm_menu.withDraw(cantidad_para_retirar);
                    System.out.println(account_menu.getBalance());
                    try{
                        Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("update tabla set balance=" + account_menu.getBalance() + "where ownerName='" + account_menu.getOwnerName() + "';");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    /*
                    if(account_menu == null){
                        System.out.println("First you need to Login, if you don't\nhave an account, create one, pressing 1");
                        break;
                    }
                    System.out.println(account_menu.toString());
                     */
                    info();
                    break;
                case 6:
                    account_menu = null;
                    break;
                case 7:
                    System.out.println("Saliste de la aplicacion");
                    run = false;
            }
        }
    }

    private void info() {
        if(costumer == null){
            System.out.println("No existe");
            return;
        }
        System.out.println(this.costumer.getAccounts().toString());

    }

    private static boolean isDouble(String parametro){
        boolean resultado;
        try {
            Double.parseDouble(parametro);
            resultado = true;
        }
        catch(NumberFormatException excepcion){
            resultado = false;
        }
        return resultado;
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

    /*@Override
    public void actionPerformed(ActionEvent e) {
        userLogin = userText.getText();
        passwordLogin = passwordText.getText();
    }
     */
    public boolean login(){
        System.out.print("Enter the name of your account: ");
        String name_account = input.nextLine();
        System.out.print("Enter your password: ");
        String password1 = input.nextLine();
        try{

            Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select* from customer where customer_name='"+ name_account + "' " +
                    "AND password='" + password1 + "'");
            while(resultSet.next()){
                this.costumer = new Costumer(
                        resultSet.getString("customer_name"),
                        resultSet.getString("customer_street"),
                        resultSet.getString("customer_city"));
                System.out.println("Has entrado");
            }

            resultSet = statement.executeQuery("select branch_name, balance, account_number from account where account_number in (select account_number from depositor where customer_name ='" + this.costumer.getName() + "');");
            while(resultSet.next()){

                this.costumer.addAccount(new Account(resultSet.getString("account_number"),
                        resultSet.getDouble("balance"),
                        resultSet.getString("branch_name")));

            }
            if (this.costumer.getAccounts().size() > 1){
                System.out.println("Choose one of your accounts: ");
                this.costumer.getAccounts().forEach((account -> System.out.println(" - " + account.getAccount_number())
                        ));
                String account_choosen = input.nextLine();
                for(int i = 0; i < this.costumer.getAccounts().size(); i++){
                    if (this.costumer.getAccounts().get(i).getAccount_number() == account_choosen){
                        this.costumer.selectedAccount(this.costumer.getAccounts().get(i));
                    }
                }

            }
            else if(this.costumer.getAccounts().size() == 1){
                this.costumer.selectedAccount(this.costumer.getAccounts().get(0));
            }
            else{
                System.out.println("You don't have an account");
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(this.costumer.getSelectedAccount().getAccount_number());
        //System.out.println(this.costumer.getAccount().getBranch_name());
        //System.out.println(this.costumer.getAccount().getBalance());
        return true;
    }
}
