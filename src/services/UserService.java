package services;

import objects.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

public class UserService   {

    public static Scanner input = new Scanner(System.in);
    static dataBaseService dataBaseService = new dataBaseService();



    public static void login(){
        System.out.print("Enter your id: ");
        String personal_id = " ";
        personal_id = input.nextLine();
        System.out.print("Enter your password: ");
        String password1 = input.nextLine();

        Map<String, String> loginArguments = new HashMap<String, String>();

        loginArguments.put("id", personal_id );
        loginArguments.put("password", password1);

        try {
            ResultSet resultSet = dataBaseService.select("*","customer", loginArguments);
            if(!resultSet.isBeforeFirst()){
                System.out.println("\nThere's no profile with this id or password");
                return;
            }
            while(resultSet.next()) {

                Engine.customer = new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("customer_street"),
                        resultSet.getString("customer_city"));
                System.out.println("\nYou've logged in");
            }
            if(Engine.customer == null){
                System.out.println("You couldn't log in");
            }
            loginArguments.remove("id");
            loginArguments.remove("password");
            loginArguments.put("customer_id", personal_id);
            //resultSet = statement.executeQuery("select account_id from customer_account where customer_id=" + this.costumer.getId() +";");
            resultSet = dataBaseService.select("account_id", "customer_account", loginArguments);
            ArrayList<Integer> ids = new ArrayList<>();
            while(resultSet.next()){
                ids.add(resultSet.getInt("account_id"));
            }
            int account_id = 0;

            if(ids.size() > 1){
                System.out.print("Choose the id of the account you'd like to use: ");
                for(int i = 0; i < ids.size(); i++){
                    System.out.print(ids.get(i) + " ");
                }
                System.out.println("\n");
                account_id = Integer.parseInt(input.nextLine());
            }
            else if(ids.size() == 1){
                account_id = ids.get(0);
            }
            loginArguments.remove("customer_id");
            loginArguments.put("id", String.valueOf(account_id));
            //resultSet = statement.executeQuery("select id, account_number, branch_name, balance from account where id="+account_id+ ";");
            resultSet = dataBaseService.select("id, account_number, branch_name, balance","account", loginArguments);
            while(resultSet.next()){
                Engine.customer.selectedAccount(new Account(resultSet.getInt("id"),
                        resultSet.getString("account_number"),
                        resultSet.getString("branch_name"),
                        resultSet.getDouble("balance")));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void movements(){
        if(Engine.customer == null){
            System.out.println("First you need to create a profile");
        }
        try{
            Map<String,String> movementArguments = new HashMap<>();
            movementArguments.put("from_account", String.valueOf(Engine.customer.getSelectedAccount().getId()));
            ResultSet resultSet = dataBaseService.select("time_and_date, transaction", "movements",movementArguments);
            while(resultSet.next()){
                System.out.println(resultSet.getString("transaction") + " was made the "
                        + resultSet.getString("time_and_date") );
            }
            System.out.println("\n");

        }catch(Exception e){
            e.printStackTrace();
        }
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
        System.out.println("7. Close application-> \n");
    }
}