package services;
// ¿Por qué engine me lo has metido en el páquete service?

import objects.Customer;

import java.sql.SQLException;
import java.util.*;

import static services.CustomerService.input;


public class Engine {

    Scanner input = new Scanner(System.in);
    public static Customer customer;
    public MovementService movementService = new MovementService();
    public CustomerService customerService = new CustomerService();

    public AccountService accountService = new AccountService();

    public CustomerAccountService customerAccount = new CustomerAccountService();

    public void work() throws SQLException {
        while(true){
            while(customer == null){
                // ¿Por qué menu es user service? Sería mejor que fuera un método que pertezca a Engine
                menu1();
                String numero = input.nextLine();
                while(isNumeric(numero)){
                    System.out.println("Write a name between 1 and 3: ");
                    numero = input.nextLine();
                }
                int number = Integer.parseInt(numero);
                switch(number){
                    case 1:
                        createCustomerAccount();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        System.out.println("Saliste de la aplicacion");
                        System.exit(0);
                        break;
                }
            }
            while(customer != null){
                menu2();
                String numero = input.nextLine();
                while(isNumeric(numero)){
                    System.out.println("Write a name between 1 and 7: ");
                    numero = input.nextLine();
                }
                int number = Integer.parseInt(numero);
                switch(number){
                    case 1:
                        addFunction();
                        break;
                    case 2:
                        withDrawFunction();
                        break;
                    case 3:
                        AccountService.info();
                        break;
                    case 4:
                        transaction();
                        break;
                    case 5:
                        movementService.movements();
                        break;
                    case 6:
                        customer = null;
                        break;
                    case 7:
                        createAccount();
                        break;
                    case 8:
                        System.exit(0);
                }

            }
        }
    }
    private void transaction(){
        System.out.print("Enter the amount you'd like to send: ");
        double moneyToSend = Double.parseDouble(input.nextLine());
        System.out.print("Enter the id of the account you'd like to send money to: ");
        int idOtherAccount = Integer.parseInt(input.nextLine());
        while(moneyToSend < 0){
            System.out.println("Please provide a positive amount");
            moneyToSend = Double.parseDouble(input.nextLine());
        }
        accountService.transaction(moneyToSend, idOtherAccount);
    }
    private void addFunction(){
        System.out.println("Enter the amount you'd like to add: ");
        double amount = Double.parseDouble(input.nextLine());
        while(amount < 0){
            System.out.println("Please provide a positive amount");
            amount = Double.parseDouble(input.nextLine());
        }
        accountService.addMoney(amount);
    }
    private void withDrawFunction(){
        System.out.print("Enter the amount you'd like to withdraw: ");
        double amount = Double.parseDouble(input.nextLine());
        while(amount < 0){
            System.out.println("Please provide a positive amount");
            amount = Double.parseDouble(input.nextLine());
        }
        accountService.withDraw(amount);
    }
    private void createCustomerAccount() throws SQLException {
        System.out.print("Enter your name: ");
        String name = (input.nextLine());
        System.out.print("Enter your street: ");
        String street = input.nextLine();
        System.out.print("Enter your city: ");
        String city = input.nextLine();
        System.out.print("Enter your password:");
        String password = input.nextLine();
        System.out.println("Enter your closest bank branch: ");
        String branch_name = input.nextLine();
        System.out.println("Enter your balance: ");
        String balance_str = input.nextLine();
        double balance = 0.0;
        boolean isNotDouble = true;
        while(isNotDouble){
            try {
                balance = Double.parseDouble(balance_str);
                isNotDouble = false;
            }catch (Exception ignored){
            }
        }
        System.out.println("\nEnter a five word letter with a capital letter at the \nbeginning followed by " +
                "an underscore and a 3 digit number");
        String account_number = input.nextLine();

        while(account_number.length() != 5){
            System.out.println("Enter a five_word letter");
            account_number = input.nextLine();
        }

        while(!accountService.checkAccountNumber(account_number)){
            System.out.println("Not valid sintax");
            System.out.println("Enter a new five word letter:");
            account_number = input.nextLine();
        }


        while(accountService.checkAccountExist(account_number)){
            System.out.println("Already exists");
            System.out.println("Enter a new five word letter:");
            account_number = input.nextLine();
        }

        customerService.createCustomerAccount(name, street, city, password, branch_name, balance, account_number);
        int idAccount = accountService.getIdAccount(account_number);
        CustomerAccountService.addAccountoCostumer(idAccount);
    }



    private void createAccount() throws SQLException {
        System.out.print("Enter a new account number: ");
        String account_number = input.nextLine();
        while(account_number.length() != 5){
            System.out.println("It must be a five word letter");
            account_number = input.nextLine();
        }
        while(!accountService.checkAccountNumber(account_number)){
            System.out.println("Enter a new five word letter, wrong syntax: ");
            account_number = input.nextLine();
        }
        while(accountService.checkAccountExist(account_number)){
            System.out.print("Already exists, enter a new one:");
            account_number = input.nextLine();
        }
        System.out.print("Enter your closest branch: ");
        String branch_name = input.nextLine();

        System.out.print("Enter your balance: ");
        double balance = Double.parseDouble(input.nextLine());

        accountService.createAccount(account_number, branch_name, balance);
        int idAccount = accountService.getIdAccount(account_number);

        System.out.println("Your new account id will be " + idAccount);

        customerAccount.addAccountoCostumer(idAccount);
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
        return !resultado;
    }

    public static void menu1() {
        System.out.println("\n1. Create an account-> ");
        System.out.println("2. Login->");
        System.out.println("3. Close application->\n");
    }
    public static void menu2(){
        System.out.println("1. Add money in your account->");
        System.out.println("2. Withdraw money from your account -> ");
        System.out.println("3. Print information about your account-> ");
        System.out.println("4. Wire transfer->");
        System.out.println("5. Show movements->");
        System.out.println("6. Logout->");
        System.out.println("7. Create new associate account->");
        System.out.println("8. Close application-> \n");
    }

    public void login() throws SQLException {
        System.out.println("Enter your Customer Id");
        String personal_id = input.nextLine();
        System.out.println("Enter your password");
        String password = input.nextLine();
        customer = customerService.login( personal_id, password);
    }


}
