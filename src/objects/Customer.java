package objects;

import java.util.ArrayList;

public class Customer {
    private int id;
    private final String name;
    private final String street;
    private final String city;

    private Account selectedAccount;
    private final ArrayList<Account> accounts;
    public Customer(int id, String name, String street, String city){
        this.id = id;
        this.name = name;
        this.street = street;
        this.city = city;
        this.accounts = new ArrayList<>();
    }
    public void addAccount(Account account){

        accounts.add(account);

    }

    public int getId() {
        return id;
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
