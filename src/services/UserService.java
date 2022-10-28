package services;


import Engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static javax.swing.UIManager.put;

public class UserService   {


    dataBaseService dataBaseService = new dataBaseService();

    public void login(){
        System.out.print("Enter your id: ");
        String personal_id = Integer.parseInt(input.nextLine());
        System.out.print("Enter your password: ");
        String password1 = input.nextLine();

        Map<String, String> loginArguments = new HashMap<String, String>()
        {
            {
                put("id", personal_id );
                put("password", password1);
            }
        };





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
            Engine.costumer.selectedAccount(new Account(resultSet.getInt("id"),
                    resultSet.getString("account_number"),
                    resultSet.getString("branch_name"),
                    resultSet.getDouble("balance")));

        }

    }
}