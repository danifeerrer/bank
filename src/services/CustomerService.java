package services;

import objects.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomerService {

    public static Scanner input = new Scanner(System.in);
    static dataBaseService dataBaseService = new dataBaseService();

    public AccountService accountService = new AccountService();
    public CustomerAccountService customerAccount = new CustomerAccountService();

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
            assert customer != null;
            int account_id = 0;
            ArrayList<Integer> ids = customerAccount.getIds(customer.getId());
            if(ids.size() > 1){
                System.out.println("Choose the id of the account you'd like to use: ");
                for (Integer id : ids) {
                    System.out.print(id + " ");
                }
                account_id = Integer.parseInt(input.nextLine());
            }
            else if(ids.size() == 1){
                account_id = ids.get(0);
            }
            customer.selectedAccount(accountService.getAccount(account_id));
        }
        else{
            System.out.println("You couldn't log in");
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

        Map<String,String> insertArguments = new HashMap<>()
        {
            {
                put("customer_name", "'"+ name +"'");
                put("customer_street", "'" + street + "'");
                put("customer_city", "'"+city+"'");
                put("password", "'"+password+"'");
            }
        };

        dataBaseService.insert("customer", insertArguments);
        int customer_id = getCostumerId(name, password);

        accountService.createAccount(account_number, branch_name, balance);
        int idAccount = accountService.getIdAccount(account_number);

        System.out.println("Your customer id will be "+ customer_id + "\nYour account id will be " + idAccount);

        customerAccount.createCustomerAccount(customer_id, idAccount);
    }
    public int getCostumerId(String username, String password) throws SQLException {
        Map<String, String> selectArguments = new HashMap<>()
        {
            {
                put("customer_name", username);
                put("password", password);
            }
        };
        ResultSet resultSet = dataBaseService.select("id", "customer", selectArguments);
        resultSet.next();
        return resultSet.getInt("id");

    }
}