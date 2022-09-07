public class main {
    public static void main(String args[]) {
        BankAccount account = new BankAccount("Daniel", 23, 4);
        BankAccount receptor = new BankAccount("Luis", 23, 30);
        ATM atm = new ATM(account);
        atm.withDraw(2);
        atm.addMoney(2);
        atm.transaction(receptor, 30);
        System.out.println(receptor.getBalance());
        System.out.print(account.getBalance());

    }
}
