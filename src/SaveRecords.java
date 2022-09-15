/*
import java.io.*;

public class SaveRecords {
    private FileOutputStream file_to_write;
    private FileInputStream file_to_read;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void setFile_to_write(FileOutputStream file_to_write) {
        this.file_to_write = file_to_write;
    }

    public void setFile_to_read(FileInputStream file_to_read) {
        this.file_to_read = file_to_read;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }
    public SaveRecords(FileOutputStream file_write, FileInputStream file_read, ObjectOutputStream out, ObjectInputStream in){
        this.file_to_write = file_write;
        this.file_to_read = file_read;
        this.out = out;
        this.in = in;
    }
    public void inputData(BankAccount some_account){
        out.writeObject(some_account);
    }
    public BankAccount readData(BankAccount some_account){
        BankAccount toReturn = (BankAccount) in.readObject();
        return toReturn;
    }
}
*/