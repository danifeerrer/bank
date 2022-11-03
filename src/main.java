import services.Engine;

import java.io.*;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) throws SQLException {
        Engine engine = new Engine();
        engine.work();
    }
}