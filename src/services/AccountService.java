package services;

import objects.Account;
import Engine.Engine;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AccountService {

    public static dataBaseService dataBaseService = new dataBaseService();
    public static int idIncome = 18;
    public static MovementService movementService = new MovementService();

    public static int idOutcome = 19;

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
    public static void withDraw(Double amount){
        Engine.customer.getSelectedAccount().withdraw(amount);
        SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        Date date = new Date();
        LinkedHashMap<String, String> updateArguments = new LinkedHashMap<>();
        LinkedHashMap<String, String> whereArguments = new LinkedHashMap<>();
        updateArguments.put("balance", String.valueOf(Engine.customer.getSelectedAccount().getBalance()));
        whereArguments.put("id", String.valueOf(Engine.customer.getSelectedAccount().getId()));
        dataBaseService.update("account", updateArguments, whereArguments);
        movementService.withDrawMovement();
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
    public static void transaction(Double moneyToSend, int idOtherAccount){

        Engine.customer.getSelectedAccount().withdraw(moneyToSend);
        Map<String,String> selectArguments = new HashMap<>();
        selectArguments.put("id", String.valueOf(idOtherAccount));
        try {
            ResultSet resultSet = dataBaseService.select("balance","account", selectArguments);
            resultSet.next();
            Double balanceOtherAccount = resultSet.getDouble("balance") + moneyToSend;
            LinkedHashMap<String, String> updateArguments = new LinkedHashMap<>();
            LinkedHashMap<String, String> whereArguments = new LinkedHashMap<>();
            updateArguments.put("balance", String.valueOf(Engine.customer.getSelectedAccount().getBalance()));
            whereArguments.put("id", String.valueOf(Engine.customer.getSelectedAccount().getId()));
            //Solo puedes llamar a dataBaseService update / insert / select con table=movement en este servicio
            dataBaseService.update("account", updateArguments, whereArguments);
            updateArguments.remove("balance");
            updateArguments.remove("id");
            updateArguments.put("balance", String.valueOf(balanceOtherAccount));
            whereArguments.put("id", String.valueOf(idOtherAccount));
            dataBaseService.update("account", updateArguments,whereArguments);
            movementService.transactionMovement(idOtherAccount);
            System.out.println("Your current balance is " + Engine.customer.getSelectedAccount().getBalance());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void addMoney(Double amount){
        // Alguna vez este if puede ser true?

        //Si el la tabla principal que modiicas es Account porque estamos en Movement Service
        Engine.customer.getSelectedAccount().addMoney(amount);

        //La creación del hash map tiene que estar dentro del servicio
        LinkedHashMap<String, String> updateArguments = new LinkedHashMap<>();
        LinkedHashMap<String, String> whereArguments = new LinkedHashMap<>();
        updateArguments.put("balance", String.valueOf(Engine.customer.getSelectedAccount().getBalance()));
        whereArguments.put("id", String.valueOf(Engine.customer.getSelectedAccount().getId()));
        dataBaseService.update("account", updateArguments, whereArguments);
        movementService.addMovement();

        //La creación del hash map tiene que estar dentro del servicio

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
    public static boolean checkAccountNumber(String account_number){
        return account_number.matches("^[A-Z]{1}[-][0-9]{3}");
    }
}
