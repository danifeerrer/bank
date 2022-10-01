import java.sql.*;
import java.util.Scanner;

/*
        String url = "jdbc:mysql://localhost:3306/bank";
        String username = "root";
        String password = "$contraseña$11";

        try{

            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from account");

            //Statement statement1 = connection.createStatement();
            //ResultSet resultSet1 = statement1.executeQuery("")

            while (resultSet.next()){
                System.out.println(resultSet.getString("account_number" ) + " | " +
                        resultSet.getString("branch_name") + " | " + resultSet.getString("balance"));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
 */
public class Engine {

    private String url = "jdbc:mysql://localhost:3306/bank";
    private String username = "root";
    private String password = "$contraseña$11";
    public void work() {
        boolean run = true;
        BankAccount account_menu = null;
        ATM atm_menu = null;
        Scanner input = new Scanner(System.in);
        while(run){

            System.out.println("\n1. Crear una cuenta corriente-> ");
            System.out.println("2. Ingresar dinero en la cuenta->");
            System.out.println("3. Retirar dinero de la cuenta-> ");
            System.out.println("4. Imprimir informacion sobre la cuenta-> ");
            System.out.println("5. Salir-> \n");


            String numero = input.nextLine();
            while(!isNumeric(numero)){
                System.out.println("Escriba un numero del 1 al 5: ");
                numero = input.nextLine();
            }
            int number = Integer.parseInt(numero);
            switch(number){
                case 1:
                    System.out.print("Ingrese su nombre: ");
                    String nombre = (input.nextLine());
                    System.out.print("Ingrese su numero de cuenta: ");
                    String numero_cuent = input.nextLine();
                    double cuenta = (Double.parseDouble(numero_cuent));
                    while(numero_cuent.length() != 3){
                        System.out.print("Numero no valido, ingrese otro numero de cuenta: ");
                        numero_cuent = input.nextLine();
                    }
                    System.out.print("Ingrese su balance: ");
                    int balance = (Integer.parseInt(input.nextLine()));
                    System.out.println("Ingrese su contraseña: ");
                    String password2 = input.nextLine();
                    account_menu = new BankAccount(nombre,cuenta,balance);
                    atm_menu = new ATM(account_menu);
                    System.out.println(account_menu.getOwnerName());
                    try{
                        Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("insert into `bank`.`tabla`(`ownerName`, `accountNumber`, `balance`, `password`) VALUES ('" + nombre + "', "  + cuenta + ", " + balance + ", '" + password2 + "');");
                        /*while(resultSet.next()) {
                            System.out.println((resultSet.getString("ownerName") + " " +
                                    resultSet.getString("accountNumber")) + " " +
                                    resultSet.getDouble("balance"));
                        }
                        */

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    System.out.print("Indique la cantidad que quiera ingresar: ");
                    String cantidad = input.nextLine();
                    if(account_menu == null){
                        System.out.println("First you need to create an account: ");
                        break;
                    }
                    while(isDouble(cantidad) != true){
                        System.out.print("Cantidad equivocada, ingrese de nuevo: ");
                        cantidad = input.nextLine();
                    }
                    double cantidad_para_añadir = Double.parseDouble(cantidad);
                    //En que casos se puede utilizar null??
                    atm_menu.addMoney(cantidad_para_añadir);
                    System.out.println(account_menu.getBalance());
                    break;
                case 3:
                    System.out.print("Indique la cantidad que quiera retirar: ");
                    String cantidad2 = input.nextLine();

                    if(account_menu == null){
                        System.out.println("First you need to create an account");
                        break;
                    }
                    while(!isDouble(cantidad2)){
                        System.out.println("Cantidad equivocado, cuanto quiere retirar?  ");
                        cantidad2 = input.nextLine();
                    }
                    double cantidad_para_retirar = Double.parseDouble(cantidad2);
                    atm_menu.withDraw(cantidad_para_retirar);
                    System.out.println(account_menu.getBalance());
                    break;
                case 4:
                    /*
                    if(account_menu == null){
                        System.out.println("First you need to create an account");
                        break;
                    }
                    System.out.println(account_menu.toString());
                     */
                    System.out.println("Enter the name of your account: ");
                    String name_account = input.nextLine();
                    System.out.println("Enter your password: ");
                    String password1 = input.nextLine();
                    try{
                        Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("select* from tabla where ownerName='"+ name_account+ "' " +
                                "AND password='" + password1 + "'");
                        while(resultSet.next()) {
                            System.out.println((resultSet.getString("ownerName") + " " +
                                    resultSet.getString("accountNumber")) + " " +
                                    resultSet.getDouble("balance"));
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;

                case 5:
                    System.out.println("Saliste de la aplicacion");
                    run = false;
            }
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
        return resultado;
    }
    private static boolean isNumeric(String parametro){
        boolean resultado;
        try {
            Integer.parseInt(parametro);
            resultado = true;
        }
        catch(NumberFormatException excepcion){
            resultado = false;
        }
        return resultado;
    }

}
