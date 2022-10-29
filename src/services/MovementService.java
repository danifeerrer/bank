package services;

import static services.UserService.input;
//Por que input tengo que importarlo y Engine no?
public class MovementService {

    public static void addmoney(){
        if(Engine.costumer == null){
            System.out.println("First you need to login or create an account\n if don't have one");
            return;
        }
        if(Engine.costumer.getSelectedAccount() == null){
            System.out.println("You need to login first");
        }
        System.out.print("Enter the amount you'd like to add: ");
        Double moneyToAdd = Double.parseDouble(input.nextLine());
        if(moneyToAdd < 0){
            System.out.println("Please provide a positive amount");
        }
        else{
            Engine.costumer.getSelectedAccount().addMoney(moneyToAdd);
        }

    }

}