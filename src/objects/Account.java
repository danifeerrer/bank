package objects;

public class Account {
    private String account_number;
    private double balance;
    private String branch_name;
    public Account(String account_number, double balance, String branch_name ){
        this.branch_name = branch_name;
        this.account_number = account_number;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }
}
