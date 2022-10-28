import objects.Account;
import objects.BankAccount;
import objects.Costumer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;


public class Engine {

    Scanner input = new Scanner(System.in);
    private Costumer costumer;
    private String url = "jdbc:mysql://localhost:3306/bank";
    private String username = "root";
    private String password = "$contraseña$11";

    private static int idOutcome = 19;

    private static int idIncome = 18;
    public void work() {
        boolean run = true;
        BankAccount account_menu = null;
        ATM atm_menu = null;
        while(run){
            menu();
            String numero = input.nextLine();
            while(!isNumeric(numero)){
                System.out.println("Write a name between 1 and 9: ");
                numero = input.nextLine();
            }
            int number = Integer.parseInt(numero);
            switch(number){
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    addmoney();
                    break;
                case 4:
                    withdraw();
                    break;
                case 5:
                    info();
                    break;
                case 6:
                    transaction();
                    break;
                case 7:
                    movements();
                case 118:
                    admin();
                    break;
                case 8:
                    this.costumer = null;
                    break;
                case 9:
                    System.out.println("Saliste de la aplicacion");
                    run = false;
                    break;
            }
        }
    }

    private void admin() {
        try{
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            Statement statement = connection.createStatement();
            System.out.println("Feel free to change whatever you'd like to change");
            String update;
            System.out.println("Write the SQL statement for updating data(insert, delete)");
            update = input.nextLine();
            statement.executeUpdate(update);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void withdraw() {
        if(this.costumer == null){
            System.out.println("First you need to create a profile");
            return;
        }
        if(this.costumer.getSelectedAccount() == null){
            System.out.println("First you need to create an account");
            return;
        }
        System.out.println("Enter the amount you'd like to withdraw");
        Double moneyToSubstract = Double.parseDouble(input.nextLine());
        if(moneyToSubstract < 0){
            System.out.println("Please provide a positive amount");
        }
        else{
            this.costumer.getSelectedAccount().withdraw(moneyToSubstract);
        }
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("update account set balance=" + this.costumer.getSelectedAccount().getBalance()
                    + "where account_number='" +
                    this.costumer.getSelectedAccount().getAccount_number() + "';");
            statement.executeUpdate("insert into movements (time_and_date, from_account, to_account, transaction," +
                    " customer_in_charge)" + " VALUES ('"+formatter.format(date)+"', "+
                    this.costumer.getSelectedAccount().getId() +", " + idOutcome +
                    ", 'Withdraw'" + ", " + this.costumer.getId() +");");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(this.costumer.getSelectedAccount().getBalance());
    }

    private void movements(){
        if(this.costumer == null){
            System.out.println("First you need to create a profile");
        }
        else if(this.costumer.getSelectedAccount() == null){
            System.out.println("You'll need to create an account");

        }
        else{
            try{
                Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select time_and_date, transaction from movements where" +
                        " from_account=" + this.costumer.getSelectedAccount().getId() + ";");
                while(resultSet.next()){
                    System.out.println(resultSet.getString("transaction") + " was made the " +
                            resultSet.getString("time_and_date"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void addmoney() {
        if(this.costumer == null){
            System.out.println("First you need to create a profile");
            return;
        }
        if(this.costumer.getSelectedAccount() == null){
            System.out.println("You need to login first");
            return;
        }
        System.out.println("Enter the amount you'd like to add");
        Double moneyToAdd = Double.parseDouble(input.nextLine());
        if(moneyToAdd < 0){
            System.out.println("Please provide a positive amount");
        }
        else{
            this.costumer.getSelectedAccount().addMoney(moneyToAdd);
        }
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("update account set balance=" + this.costumer.getSelectedAccount().getBalance()
                    + "where account_number='" +
                    this.costumer.getSelectedAccount().getAccount_number() + "';");
            statement.executeUpdate("insert into movements (time_and_date, from_account, to_account, transaction, customer_in_charge)" +
                    " VALUES ('"+formatter.format(date)+"', "+ idIncome +", "+ this.costumer.getSelectedAccount().getId() + ", 'Adding'"
                    + ", " + this.costumer.getId() +");");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(this.costumer.getSelectedAccount().getBalance());
    }
    private void transaction(){
        if(this.costumer == null){
            System.out.println("First you need to create a profile");
            return;
        }
        if(this.costumer.getSelectedAccount() == null){
            System.out.println("You need to create an account first");
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println("Enter the amount you'd like to send");
        Double moneyToSend = Double.parseDouble(input.nextLine());
        System.out.println("Enter the id of the account you'd like to send money to");
        int idOtherAccount = Integer.parseInt(input.nextLine());
        if(moneyToSend < 0){
            System.out.println("Please provide a positive amount");
        }
        else{
            this.costumer.getSelectedAccount().withdraw(moneyToSend);
            try{
                Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                Statement statement = connection.createStatement();
                double balanceOtherAccount;
                ResultSet resultSet = statement.executeQuery("select balance from account where id='"+idOtherAccount+"';");
                resultSet.next();
                balanceOtherAccount = resultSet.getDouble("balance") + moneyToSend;
                statement.executeUpdate("update account set balance=" + this.costumer.getSelectedAccount().getBalance()
                        + "where account_number='" + this.costumer.getSelectedAccount().getAccount_number() + "';");
                statement.executeUpdate("update account set balance=" + balanceOtherAccount +" where id='"+ idOtherAccount +"';");
                statement.executeUpdate("insert into movements (time_and_date, from_account, to_account, transaction, customer_in_charge)" +
                        " VALUES ('"+formatter.format(date)+"', "+ this.costumer.getSelectedAccount().getId() +", "+ idOtherAccount + ", 'Transfering'"
                        + ", " + this.costumer.getId() +");");

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }



    private void info() {
        if(costumer == null){
            System.out.println("First you need to login");
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

    public void login(){
        System.out.print("Enter your id: ");
        int personal_id = Integer.parseInt(input.nextLine());
        System.out.print("Enter your password: ");
        String password1 = input.nextLine();
        try{

            Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select* from customer where id='"+ personal_id + "' " +
                    "AND password='" + password1 + "'");

            if(!resultSet.isBeforeFirst()){
                System.out.println("\nThere's no profile with this id or password");
                return;
            }
            while(resultSet.next()) {

                this.costumer = new Costumer(
                        resultSet.getInt("id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("customer_street"),
                        resultSet.getString("customer_city"));
                System.out.println("\nYou've logged in");
            }
            int account_id = 0;
            resultSet = statement.executeQuery("select account_id from customer_account where customer_id=" + this.costumer.getId() +";");
            ArrayList<Integer> ids = new ArrayList<>();
            while(resultSet.next()){
                ids.add(resultSet.getInt("account_id"));
            }
            if(ids.size() > 1){
                System.out.println("Choose the id of the account you'd like to use: ");
                for(int i = 0; i < ids.size(); i++){
                    System.out.print(ids.get(i) + " ");
                }
                account_id = Integer.parseInt(input.nextLine());
            }
            else if(ids.size() == 1){
                account_id = ids.get(0);
            }
            resultSet = statement.executeQuery("select id, account_number, branch_name, balance from account where id="+account_id+ ";");
            while(resultSet.next()){
                this.costumer.selectedAccount(new Account(resultSet.getInt("id"),
                        resultSet.getString("account_number"),
                        resultSet.getString("branch_name"),
                        resultSet.getDouble("balance")));

            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void createAccount(){
        System.out.println("If you want to create an account and a customer profile associate with this profile, press 1: \n");
        System.out.println("If you already have a profile, press 2: \n");
        String create_account_str = input.nextLine();
        while(!isNumeric(create_account_str)){
            System.out.println("Please provide a numeric value: ");
            create_account_str = input.nextLine();
        }
        int create_account = Integer.parseInt(create_account_str);
        boolean run2 = false;
        boolean run3 = false;
        while(create_account != 1 || create_account != 2){;
            create_account = Integer.parseInt(input.nextLine());
            if(create_account == 1){
                run2 = true;
                break;
            }
            if(create_account == 2){
                run3 = true;
                break;
            }
            System.out.println("Please provide just one of the available numbers: 1 or 2");
        }

        try{

            Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
            Statement statement = connection.createStatement();

            while(run2){
                System.out.print("Enter your name: ");
                String name = (input.nextLine());
                System.out.print("Enter your street: ");
                String street = input.nextLine();
                System.out.print("Enter your city: ");
                String city = input.nextLine();
                System.out.print("Enter your password:");
                String password = input.nextLine();
                System.out.println("Enter a five word letter with a capital letter at the \nbeginning followed by " +
                        "an underscore and a 3 digit number");
                String account_number2 = input.nextLine();
                while(checkAccountExist(statement, account_number2)){
                    System.out.println("Already exists");
                    System.out.println("Enter a new five word letter:");
                    account_number2 = input.nextLine();
                }
                System.out.println("Enter your closest bank branch: ");
                String branch_name = input.nextLine();
                System.out.println("Enter your balance: ");
                String balance_str = input.nextLine();
                while(!isDouble(balance_str)){
                    System.out.println("Please provide a number");
                    balance_str = input.nextLine();
                }
                Double balance = Double.parseDouble(balance_str);
                if(account_number2.length() == 5){
                    statement.executeUpdate("insert into `bank`.`account`(`account_number`, `branch_name`, `balance`)" +
                            " VALUES ('" + account_number2 + "', '"+ branch_name + "', " + balance + ");");
                    ResultSet resultSet = statement.executeQuery("select id from `bank`.`account` " +
                            "where account_number='" +account_number2+ "';" );
                    resultSet.next();
                    int account_id = resultSet.getInt("id");
                    statement.executeUpdate("insert into `bank`.`customer`(`customer_name`, `customer_street`," +
                            " `customer_city`, `password`) VALUES ('" + name + "', '"  + street + "', '" +
                            city + "', '" + password + "');");
                    resultSet = statement.executeQuery("select id from `bank`.`customer` where customer_name='"
                            +name+ "' AND password='" +password+ "';" );
                    resultSet.next();
                    int customer_id = resultSet.getInt("id");
                    statement.executeUpdate("insert into `bank`.`customer_account`(`account_id`, `customer_id`) " +
                            "VALUES ("+account_id+", "+customer_id+");");
                    run2 = false;
                }

            }
            while(run3){
                System.out.println("Enter a five word letter with a capital letter at the beginning \n" +
                        "followed by an underscore and a 3 digit number");
                String new_account_number = input.nextLine();
                if(new_account_number.length() != 5){
                    System.out.println("The length of the account number you provided is not correct");
                }
                if(checkAccountExist(statement, new_account_number)){
                    System.out.println("This account already exists, provide a new number");
                }
                System.out.println("Enter your closest bank branch: ");
                String branch_name = input.nextLine();
                System.out.println("Enter your balance: ");
                String balance_str = input.nextLine();
                while(!isDouble(balance_str)){
                    System.out.println("Please provide a number: ");
                    balance_str = input.nextLine();
                }
                Double balance = Double.parseDouble(balance_str);
                statement.executeUpdate("insert into `bank`.`account`(`account_number`, `branch_name`, `balance`) VALUES ('" + new_account_number + "', '"+ branch_name + "', " + balance + ");");
                ResultSet resultSet = statement.executeQuery("select id from `bank`.`account` where account_number='" + new_account_number + "';" );
                resultSet.next();
                int account_id = resultSet.getInt("id");
                statement.executeUpdate("insert into `bank`.`customer_account`(`customer_id`, `account_id`) VALUES ("+this.costumer.getId()+ ", " +
                        account_id + ");");
                run3 = false;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkAccountExist(Statement statement, String account_number2) throws SQLException {
        ResultSet resultSet = statement.executeQuery("select* from `bank`.`account` where account_number='" + account_number2 + "';");
        while(resultSet.next()){
            if(Objects.equals(resultSet.getString("account_number"), account_number2)){
                return true;
            }
        }
        return false;
    }
    private boolean checkCostumerExists(ResultSet resultSet, int customer_id, String password) throws SQLException{
        while(resultSet.next()){
            if(Objects.equals(resultSet.getInt("id"), customer_id) && Objects.equals(resultSet.getString("password"), password)){
                System.out.println(resultSet.getString("id"));
                return true;
            }
        }
        return false;
    }
    public void menu(){
        if(this.costumer.getSelectedAccount() == null){
            System.out.println("\n1. Create an account-> ");
            System.out.println("2. Login->");
            System.out.println("3. Close application-> \n");
        }else{
            System.out.println("1. Add money in your account->");
            System.out.println("2. Withdraw money from your account -> ");
            System.out.println("3. Print information about your account-> ");
            System.out.println("4. Wire transfer->");
            System.out.println("5. Show movements->");
            System.out.println("6. Logout->");
            System.out.println("7. Close application-> \n");
        }
    }
}
