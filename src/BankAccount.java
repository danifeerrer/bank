import java.io.Serializable;
public class BankAccount implements Serializable{
    private double accountNumber;
    private String ownerName;
    private double balance;

    public BankAccount(String ownerName, double accountNumber, double balance){
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
    public BankAccount(){

    }
    public double getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setAccountNumber(double accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber=" + accountNumber +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                '}';
    }
}
