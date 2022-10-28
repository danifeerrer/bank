package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class dataBaseService {

    private String url = "jdbc:mysql://localhost:3306/bank";
    private String username = "root";
    private String password = "$contraseña$11";


    public void select(String table, Map<String, String> whereArguments ){

        try{

            Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
            Statement statement = connection.createStatement();
            String query = "select* from" + table;
            // Vamos a recorrer el mapa para ver las condiciones del where
            if(whereArguments != null){
                query.concat("where = ");

                for (Map.Entry<String, String> entry : whereArguments.entrySet()) {
                    query.concat()
                    System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
                }
            }


            ResultSet resultSet = statement.executeQuery( where id='"+ personal_id + "' " +
                    "AND password='" + password1 + "'");


        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
