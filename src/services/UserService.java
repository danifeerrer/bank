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

                Engine.costumer = new Costumer(
                        resultSet.getInt("id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("customer_street"),
                        resultSet.getString("customer_city"));
                System.out.println("\nYou've logged in");
            }
            if(Engine.costumer == null){
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
                System.out.println("Choose the id of the account you'd like to use: ");
                for(int i = 0; i < ids.size(); i++){
                    System.out.print(ids.get(i) + " ");
                }
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
                Engine.costumer.selectedAccount(new Account(resultSet.getInt("id"),
                        resultSet.getString("account_number"),
                        resultSet.getString("branch_name"),
                        resultSet.getDouble("balance")));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}