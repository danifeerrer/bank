import java.net.SocketOption;

public class ATM {
    private BankAccount account;
    public ATM(BankAccount account){
        this.account = account;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void withDraw(double moneyToWithdraw){
        if(moneyToWithdraw > account.getBalance()){
            System.out.println("No puedes mandar mas de lo que tienes");
            return;
        }
        this.account.setBalance(account.getBalance() - moneyToWithdraw);
    }
    public void addMoney(double moneyToAdd){
        if(moneyToAdd < 0){
            System.out.println("La cantidad es negativa");
            return;
        }
        this.account.setBalance(account.getBalance() + moneyToAdd);
    }
    public void transaction(BankAccount receptor, double moneyToSend){
        if(moneyToSend > account.getBalance()){
            System.out.println("No puedes mandar mas de lo que tienes");
            return;
        }
        receptor.setBalance(receptor.getBalance() + moneyToSend);
        this.account.setBalance(account.getBalance() - moneyToSend);
    }
}
