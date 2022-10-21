import objects.Account;
import objects.BankAccount;
import objects.Costumer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class Engine {

    Scanner input = new Scanner(System.in);
    private Costumer costumer;
    private String url = "jdbc:mysql://localhost:3306/bank";
    private String username = "root";
    private String password = "$contraseña$11";

    private static int idOutcome = 19;
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
                case 8:
                    this.costumer = null;
                    break;
                case 9:
                    System.out.println("Saliste de la aplicacion");
                    run = false;
            }
        }
    }



    private void withdraw() {
        if()
        if(this.costumer.getSelectedAccount() == null){
            System.out.println("First you need to login first");
        }
        System.out.println("Enter the amount you'd like to substract");
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
                    this.costumer.getSelectedAccount().getId() +", " + this.costumer.getSelectedAccount().getId() +
                    ", 'Withdraw'" + ", " + this.costumer.getId() +");");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(this.costumer.getSelectedAccount().getBalance());
    }

    private void movements(){
        if(this.costumer == null){
            System.out.println("First you need to login");
        }
        else if(this.costumer.getSelectedAccount() == null){
            System.out.println("You'll need to create an account");
        }
        else{
            try{
                Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select time_and_date, transaction from movements;");
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
        if(this.costumer.getSelectedAccount() == null){
            System.out.println("You need to login first");
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
                    " VALUES ('"+formatter.format(date)+"', "+ this.costumer.getSelectedAccount().getId() +", "+ this.costumer.getSelectedAccount().getId() + ", 'Adding'"
                    + ", " + this.costumer.getId() +");");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(this.costumer.getSelectedAccount().getBalance());
    }
    private void transaction(){
        if(this.costumer.getSelectedAccount() == null){
            System.out.println("You need to login first");
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
                        resultSet.getInt("id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("customer_street"),
                        resultSet.getString("customer_city"));
                System.out.println("Has entrado");
            }
            System.out.println(this.costumer.getId());
            resultSet = statement.executeQuery("select account_id from customer_account where customer_id=" + this.costumer.getId() +";");
            resultSet.next();
            int account_id = resultSet.getInt("account_id");
            resultSet = statement.executeQuery("select id, account_number, branch_name, balance from account where id="+account_id+ ";");
            while(resultSet.next()){
                this.costumer.addAccount(new Account(resultSet.getInt("id"),
                        resultSet.getString("account_number"),
                        resultSet.getString("branch_name"),
                        resultSet.getDouble("balance")));

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
    public void createAccount(){
        int id;
        System.out.print("Enter your name: ");
        String name = (input.nextLine());
        System.out.print("Enter your street: ");
        String street = input.nextLine();
        System.out.print("Enter your city: ");
        String city = input.nextLine();
        System.out.print("Enter your password:");
        String password = input.nextLine();
        System.out.println("If you want to create an account associate with this profile, press 1: ");
        int create_account = Integer.parseInt(input.nextLine());
        boolean run2 = false;
        if(create_account == 1){
            run2 = true;
        }

        try{

            Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
            Statement statement = connection.createStatement();

            while(run2){
                System.out.println("Enter a five word letter with a capital letter at the beginning \nfollowed by an underscore and a 3 digit number");
                String account_number2 = input.nextLine();
                if(checkAccountExist(statement, account_number2)){
                    System.out.println("Already exists");
                    break;
                }
                System.out.println("Enter your closest bank branch: ");
                String branch_name = input.nextLine();
                System.out.println("Enter your balance: ");
                Double balance = Double.parseDouble(input.nextLine());
                if(account_number2.length() == 5){
                    //statement.executeUpdate("insert into `bank`.`depositor`(`customer_name`, `account_number`) VALUES ('" + name + "', '" + account_number2 + "');");
                    statement.executeUpdate("insert into `bank`.`account`(`account_number`, `branch_name`, `balance`) VALUES ('" + account_number2 + "', '"+ branch_name + "', " + balance + ");");
                    ResultSet resultSet = statement.executeQuery("select id from `bank`.`account` where account_number='" +account_number2+ "';" );
                    resultSet.next();
                    int account_id = resultSet.getInt("id");
                    statement.executeUpdate("insert into `bank`.`customer`(`customer_name`, `customer_street`, `customer_city`, `password`) VALUES ('" + name + "', '"  + street + "', '" + city + "', '" + password + "');");
                    ResultSet resultSet2 = statement.executeQuery("select id from `bank`.`customer` where customer_name='" +name+ "' AND password='" +password+ "';" );
                    resultSet2.next();
                    int customer_id = resultSet2.getInt("id");
                    statement.executeUpdate("insert into `bank`.`customer_account`(`account_id`, `customer_id`) VALUES ("+account_id+", "+customer_id+");");
                }
                break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkAccountExist(Statement statement, String account_number2) throws SQLException {
        ResultSet resultSet = statement.executeQuery("select* from `bank`.`account` where account_number='" + account_number2 + "';");
        while(resultSet.next()){
            if(Objects.equals(resultSet.getString("account_number"), account_number2)){
                System.out.println(resultSet.getString("account_number"));
                return true;
            }
        }
        return false;
    }
    public void menu(){
        System.out.println("\n1. Create an account-> ");
        System.out.println("2. Login->");
        System.out.println("3. Add money in your account->");
        System.out.println("4. Withdraw money from your account -> ");
        System.out.println("5. Print information about your account-> ");
        System.out.println("6. Wire transfer->");
        System.out.println("7. Show movements->");
        System.out.println("8. Logout->");
        System.out.println("9. Close application-> \n");
    }
}
