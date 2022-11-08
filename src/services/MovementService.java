package services;
import Engine.Engine;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//Por que input tengo que importarlo y Engine.Engine no?
public class MovementService {
    public static dataBaseService dataBaseService = new dataBaseService();
    public static int idIncome = 18;
    public static int idOutcome = 19;

    public static void addMovement(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Map<String, String> insertArguments = new HashMap<>();
        insertArguments.put("time_and_date", "'"+formatter.format(date) + "'");
        insertArguments.put("from_account", String.valueOf(idIncome));
        insertArguments.put("to_account", String.valueOf(Engine.customer.getSelectedAccount().getId()));
        insertArguments.put("transaction", "'Adding'");
        insertArguments.put("customer_in_charge" ,String.valueOf(Engine.customer.getId()));
        dataBaseService.insert("movements", insertArguments);
        System.out.println("Your current balance is " + Engine.customer.getSelectedAccount().getBalance());
    }
    public static void withDrawMovement(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Map<String, String> insertArguments = new HashMap<>();
        insertArguments.put("time_and_date", "'"+formatter.format(date) + "'");
        insertArguments.put("from_account", String.valueOf(Engine.customer.getSelectedAccount().getId()));
        insertArguments.put("to_account", String.valueOf(idOutcome));
        insertArguments.put("transaction", "'Withdraw'");
        insertArguments.put("customer_in_charge" ,String.valueOf(Engine.customer.getId()));
        dataBaseService.insert("movements", insertArguments);
        System.out.println("Your current balance is " + Engine.customer.getSelectedAccount().getBalance());
    }
    public static void transactionMovement(int idOtherAccount){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Map<String, String> insertArguments = new HashMap<>();
        insertArguments.put("time_and_date", "'" + formatter.format(date)+ "'");
        insertArguments.put("from_account", String.valueOf(Engine.customer.getSelectedAccount().getId()));
        insertArguments.put("to_account", String.valueOf(idOtherAccount));
        insertArguments.put("transaction", "'Transfering'");
        insertArguments.put("customer_in_charge", String.valueOf(Engine.customer.getId()));
        dataBaseService.insert("movements", insertArguments);
    }
    public static void movementsTransactionAndWithdraw(){
        // El movimiento lo registra el sistema, ¿Alguna vez este if es true?
        try{
            Map<String,String> movementArguments = new HashMap<>();
            movementArguments.put("from_account", String.valueOf(Engine.customer.getSelectedAccount().getId()));
            ResultSet resultSet = dataBaseService.select("time_and_date, transaction", "movements",movementArguments);
            while(resultSet.next()){
                System.out.println(resultSet.getString("transaction") + " was made the "
                        + resultSet.getString("time_and_date") );
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void movementsAdd() throws SQLException {
        try{
            Map<String, String > movementArguments = new HashMap<>();
            movementArguments.put("to_account", String.valueOf(Engine.customer.getSelectedAccount().getId()));
            ResultSet resultSet = dataBaseService.select("time_and_date, transaction", "movements", movementArguments);
            while(resultSet.next()){
                System.out.println(resultSet.getString("transaction")
                + " was made the " + resultSet.getString("time_and_date"));
            }
            System.out.println("\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}