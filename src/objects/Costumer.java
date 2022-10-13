package objects;

import java.util.ArrayList;

public class Costumer {
    private String name;
    private String street;
    private String city;

    private Account selectedAccount;
    private ArrayList<Account> accounts;
    public Costumer(String name, String street, String city){
        this.name = name;
        this.street = street;
        this.city = city;
        this.accounts = new ArrayList<>();
    }
    public void addAccount(Account account){

        accounts.add(account);

    }

    public Account getAccount() {
        return selectedAccount;
    }

    public void selectedAccount(Account account) {
        this.selectedAccount = account;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
}
