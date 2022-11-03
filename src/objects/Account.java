package objects;


public class Account {
    private String account_number;
    private double balance;
    private String branch_name;
    private int id;
    public Account(int id, String account_number, String branch_name, double balance){
        this.id = id;
        this.branch_name = branch_name;
        this.account_number = account_number;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }


    public void addMoney(double moneyToAdd){
        this.balance += moneyToAdd;
    }
    public void withdraw(double moneyToSubstract){
        this.balance -= moneyToSubstract;
    }
    public String getAccount_number() {
        return account_number;
    }
    public String getBranch_name() {
        return branch_name;
    }

}
