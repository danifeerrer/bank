package services;

import objects.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static services.CustomerService.input;
import static services.CustomerService.dataBaseService;
public class AccountService {

    public dataBaseService dataBaseService = new dataBaseService();
    private static boolean isDouble(String parametro){
        boolean resultado;
        try {
            Double.parseDouble(parametro);
            resultado = true;
        }
        catch(NumberFormatException excepcion){
            resultado = false;
        }
        return !resultado;
    }
    public boolean checkAccountExist(String account_number2) throws SQLException {
        Map<String,String> inputArguments = new HashMap<>();
        inputArguments.put("account_number", account_number2);
        ResultSet resultSet = dataBaseService.select("*", "account", inputArguments);
        //ResultSet resultSet = statement.executeQuery("select* from `bank`.`account` where account_number='" + account_number2 + "';");
        while(resultSet.next()){
            if(Objects.equals(resultSet.getString("account_number"), account_number2)){
                return true;
            }
        }
        return false;
    }
    public static void info(){

        System.out.println("The id of your account is " + Engine.customer.getSelectedAccount().getId());
        System.out.println("Your account number is " + Engine.customer.getSelectedAccount().getAccount_number());
        System.out.println("Your nearest branch is " + Engine.customer.getSelectedAccount().getBranch_name());
        System.out.println("Your balance is " + Engine.customer.getSelectedAccount().getBalance());

    }
     public int getIdAccount(String account_number2) throws SQLException {
         Map<String, String> selectArguments = new HashMap<>();
         selectArguments.put("account_number", account_number2);
         ResultSet resultSet = dataBaseService.select("id","account",selectArguments);
         resultSet.next();
         return resultSet.getInt("id");
    }

    public void createAccount(String account_number, String branch_name, double balance) {
        Map<String, String> insertArguments = new HashMap<String, String>()
        {
            {
                put("account_number", "'"+account_number+"'");
                put("branch_name", "'"+branch_name+"'");
                put("balance", "'"+balance+"'");
            }
        };
        dataBaseService.insert("account", insertArguments);
    }

    public Account getAccount(int accountId) throws SQLException {
        Account account = null;
        Map<String, String> selectArguments = new HashMap<>()
        {
            {
                put("id", String.valueOf(accountId));
            }
        };
        ResultSet resultSet = dataBaseService.select("account_number, branch_name, balance", "account", selectArguments);
        while(resultSet.next()){
            account = new Account(accountId,
                    resultSet.getString("account_number"),
                    resultSet.getString("branch_name"),
                    resultSet.getDouble("balance"));
        }
        return account;
    }
}
