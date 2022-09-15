import javax.print.DocFlavor;
import java.io.*;
import java.util.Scanner;

public class main {
    public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException {
        /*
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accounts.txt"));
        out.writeObject(account);
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("accounts.txt"));
        BankAccount account2 = (BankAccount) in.readObject();

        System.out.println(account2.getAccountNumber());

        atm.addMoney(valor2);
        atm.transaction(receptor, 30);
        System.out.println(receptor.getBalance());
        System.out.println(account.getBalance());

        */
        boolean run = true;
        Scanner input = new Scanner(System.in);

        BankAccount account_menu = null;
        ATM atm_menu = null;
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
                    account_menu = new BankAccount(nombre,cuenta,balance);
                    atm_menu = new ATM(account_menu);
                    System.out.println(account_menu.getOwnerName());
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
                    if(account_menu == null){
                        System.out.println("First you need to create an account");
                        break;
                    }
                    System.out.println(account_menu.toString());
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
