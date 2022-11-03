package services;

import objects.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomerService {

    public static Scanner input = new Scanner(System.in);
    static dataBaseService dataBaseService = new dataBaseService();

    public AccountService accountService = new AccountService();


    public Customer login(String personal_id, String password1) throws SQLException {

        //Creamos el hash map cpn
        Map<String, String> loginArguments = new HashMap<String, String>();
        loginArguments.put("id", personal_id );
        loginArguments.put("password", password1);

        ResultSet resultSet = dataBaseService.select("*","customer", loginArguments);

        Customer customer = null;
        if(resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                customer = new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("customer_street"),
                        resultSet.getString("customer_city"));
                System.out.println("\nYou've logged in");
            }
        }
        return customer;

    }
    public static void movements(){
        // El movimiento lo registra el sistema, ¿Alguna vez este if es true?
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

    public void createCustomerAccount(String name, String street, String city, String password, String branch_name, double balance, String account_number) throws SQLException {

        Map<String,String> insertArguments2 = new HashMap<>();
        insertArguments2.put("customer_name", "'"+ name +"'");
        insertArguments2.put("customer_street", "'" + street + "'");
        insertArguments2.put("customer_city", "'"+city+"'");
        insertArguments2.put("password", "'"+password+"'");
        dataBaseService.insert("customer", insertArguments2);

        accountService.createAccount(account_number, branch_name, balance);

        int idAccount = accountService.getIdAccount(account_number);



    }
}