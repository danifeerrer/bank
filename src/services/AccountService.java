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
        public static void create_account(){

            try{
                while(run2){
                        System.out.println("Your account_id will be " + account_id + "\nSave this number\n");

                        Map<String,String> insertArguments2 = new HashMap<>();
                        insertArguments2.put("customer_name", "'"+ name +"'");
                        insertArguments2.put("customer_street", "'" + street + "'");
                        insertArguments2.put("customer_city", "'"+city+"'");
                        insertArguments2.put("password", "'"+password+"'");
                        dataBaseService.insert("customer", insertArguments2);
                        selectArguments.remove("account_number");
                        selectArguments.put("customer_name", name);
                        selectArguments.put("password", password);
                        resultSet = dataBaseService.select("id", "customer", selectArguments);
                        resultSet.next();
                        int customer_id = resultSet.getInt("id");
                        System.out.println("Your customer_id will be " + customer_id + "\nUse this number and the password\n" +
                                "you entered before to login\n");
                        Map<String,String> insertArguments3 = new HashMap<>();
                        insertArguments3.put("account_id", String.valueOf(account_id));
                        insertArguments3.put("customer_id", String.valueOf(customer_id));
                        dataBaseService.insert("customer_account", insertArguments3);
                    }
                    run2 = false;

                }
                while(run3){
                    if(Engine.customer == null){
                        System.out.println("You need to login first");
                        break;
                    }
                    System.out.println("Enter a five word letter with a capital letter at the beginning \n" +
                            "followed by an underscore and a 3 digit number");
                    String new_account_number = input.nextLine();
                    if(new_account_number.length() != 5){
                        System.out.println("The length of the account number you provided is not correct");
                    }
                    if(checkAccountExist(new_account_number)){
                        System.out.println("This account already exists, provide a new number");
                    }
                    System.out.println("Enter your closest bank branch: ");
                    String branch_name = input.nextLine();
                    System.out.println("Enter your balance: ");
                    String balance_str = input.nextLine();
                    while(isDouble(balance_str)){
                        System.out.println("Please provide a number: ");
                        balance_str = input.nextLine();
                    }
                    Double balance = Double.parseDouble(balance_str);
                    Map<String,String> insertArguments = new HashMap<>();
                    insertArguments.put("account_number", "'"+new_account_number+"'");
                    insertArguments.put("branch_name", "'"+branch_name+"'");
                    insertArguments.put("balance", String.valueOf(balance));
                    dataBaseService.insert("account", insertArguments);
                    Map<String, String> selectArguments = new HashMap<>();
                    selectArguments.put("account_number", new_account_number);
                    ResultSet resultSet = dataBaseService.select("*", "account", selectArguments);
                    Account account = null;
                    int account_id = 0;
                    while(resultSet.next()){
                        account_id = resultSet.getInt("id");
                        account = new Account(resultSet.getInt("id"),
                                resultSet.getString("account_number"),
                                resultSet.getString("branch_name"),
                                resultSet.getDouble("balance"));
                    }
                    System.out.println("Your new account id will be "+ account_id + "\n save this number");
                    Map<String,String> insertArguments2 = new HashMap<>();
                    insertArguments2.put("customer_id", String.valueOf(Engine.customer.getId()));
                    insertArguments2.put("account_id", String.valueOf(account_id));
                    dataBaseService.insert("customer_account", insertArguments2);
                    Engine.customer.addAccount(account);
                    run3 = false;
                }

            }catch(Exception e){
                e.printStackTrace();
            }

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
                put("account_number", account_number );
                put("branch_name", branch_name);
                put("balance", String.valueOf(balance) );
            }
        };
        dataBaseService.insert("account", insertArguments);
    }


}
