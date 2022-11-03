package services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static services.CustomerService.input;
import static services.CustomerService.dataBaseService;
//Por que input tengo que importarlo y Engine no?
public class MovementService {

    public static void addMoney(){
        // Alguna vez este if puede ser true?
        if(Engine.customer == null){
            System.out.println("First you need to login or create an account\nif don't have one");
            return;
        }
        System.out.print("Enter the amount you'd like to add: ");
        double moneyToAdd = Double.parseDouble(input.nextLine());
        if(moneyToAdd < 0){
            System.out.println("Please provide a positive amount");
        }
        //Si el la tabla principal que modiicas es Account porque estamos en Movement Service
        Engine.customer.getSelectedAccount().addMoney(moneyToAdd);

        //La creación del hash map tiene que estar dentro del servicio
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        LinkedHashMap<String, String> updateArguments = new LinkedHashMap<>();
        updateArguments.put("balance", String.valueOf(Engine.customer.getSelectedAccount().getBalance()));
        updateArguments.put("id", String.valueOf(Engine.customer.getSelectedAccount().getId()));
        dataBaseService.update("account", updateArguments);


        //La creación del hash map tiene que estar dentro del servicio
        Map<String, String> insertArguments = new HashMap<>();
        insertArguments.put("time_and_date", "'"+formatter.format(date) + "'");
        insertArguments.put("from_account", String.valueOf(Engine.idIncome));
        insertArguments.put("to_account", String.valueOf(Engine.customer.getSelectedAccount().getId()));
        insertArguments.put("transaction", "'Adding'");
        insertArguments.put("customer_in_charge" ,String.valueOf(Engine.customer.getId()));
        dataBaseService.insert("movements", insertArguments);
        System.out.println("Your current balance is " + Engine.customer.getSelectedAccount().getBalance());
    }
    public static void withDraw(){
        //Lo mismo que en los if anteriores
        if(Engine.customer == null){
            System.out.println("First you need to create a profile or login");
        }
        System.out.println("Enter the amount you'd like to withdraw");
        double moneyToWithdraw = Double.parseDouble(input.nextLine());
        if(moneyToWithdraw < 0){
            System.out.println("Please provide a positive amount");
        }
        // Si la cosa gira entorno a cuentas porque estamos en MovementService
        Engine.customer.getSelectedAccount().withdraw(moneyToWithdraw);
        SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        Date date = new Date();
        LinkedHashMap<String, String> updateArguments = new LinkedHashMap<>();
        updateArguments.put("balance", String.valueOf(Engine.customer.getSelectedAccount().getBalance()));
        updateArguments.put("id", String.valueOf(Engine.customer.getSelectedAccount().getId()));
        dataBaseService.update("account", updateArguments);
        Map<String, String> insertArguments = new HashMap<>();
        insertArguments.put("time_and_date", "'"+formatter.format(date) + "'");
        insertArguments.put("from_account", String.valueOf(Engine.idOutcome));
        insertArguments.put("to_account", String.valueOf(Engine.customer.getSelectedAccount().getId()));
        insertArguments.put("transaction", "'Withdraw'");
        insertArguments.put("customer_in_charge" ,String.valueOf(Engine.customer.getId()));
        dataBaseService.insert("movements", insertArguments);
        System.out.println("Your current balance is " + Engine.customer.getSelectedAccount().getBalance());
    }
    public static void transaction(){
        if(Engine.customer == null){
            System.out.println("First you need to create a profile");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println("Enter the amount you'd like to send");
        double moneyToSend = Double.parseDouble(input.nextLine());
        System.out.println("Enter the id of the account you'd like to send money to");
        int idOtherAccount = Integer.parseInt(input.nextLine());
        if(moneyToSend < 0){
            System.out.println("Please provide a positive amount");
        }
        Engine.customer.getSelectedAccount().withdraw(moneyToSend);
        Map<String,String> selectArguments = new HashMap<>();
        selectArguments.put("id", String.valueOf(idOtherAccount));
        try {
            ResultSet resultSet = dataBaseService.select("balance","account", selectArguments);
            resultSet.next();
            Double balanceOtherAccount = resultSet.getDouble("balance") + moneyToSend;
            LinkedHashMap<String, String> updateArguments = new LinkedHashMap<>();
            updateArguments.put("balance", String.valueOf(Engine.customer.getSelectedAccount().getBalance()));
            updateArguments.put("id", String.valueOf(Engine.customer.getSelectedAccount().getId()));
            //Solo puedes llamar a dataBaseService update / insert / select con table=movement en este servicio
            dataBaseService.update("account", updateArguments);
            updateArguments.remove("balance");
            updateArguments.remove("id");
            updateArguments.put("balance", String.valueOf(balanceOtherAccount));
            updateArguments.put("id", String.valueOf(idOtherAccount));
            dataBaseService.update("account", updateArguments);
            Map<String, String> insertArguments = new HashMap<>();
            insertArguments.put("time_and_date", "'" + formatter.format(date)+ "'");
            insertArguments.put("from_account", String.valueOf(Engine.customer.getSelectedAccount().getId()));
            insertArguments.put("to_account", String.valueOf(idOtherAccount));
            insertArguments.put("transaction", "'Transfering'");
            insertArguments.put("customer_in_charge", String.valueOf(Engine.customer.getId()));
            dataBaseService.insert("movements", insertArguments);
            System.out.println("Your current balance is " + Engine.customer.getSelectedAccount().getBalance());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}