package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class dataBaseService {

    private final String url = "jdbc:mysql://localhost:3306/bank";
    private final String username = "root";
    private final String password = "$contraseña$11";

    public ResultSet select(String column ,String table, Map<String, String> whereArguments ){
        ResultSet resultSet = null;
        String query = "select " + column + " from " + table;


        try{
            Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
            Statement statement = connection.createStatement();
            // Vamos a recorrer el mapa para ver las condiciones del where
            if(whereArguments != null){
                //Si una linea esta en el if y en el else, sácala
                query += " where";
                for (Map.Entry<String, String> entry : whereArguments.entrySet()) {
                    query += (" ") + (entry.getKey()) + ("= '") + (entry.getValue()) + ("' and");
                }
                query = query.replaceAll("' and",";");
            }
            resultSet = statement.executeQuery(query);
        } catch(Exception e){
            e.printStackTrace();
        }

        return resultSet;
    }

    public void insert(String table, Map<String, String> insertArguments){
        StringBuilder query = new StringBuilder("insert into " + table + "(");

        try{
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            Statement statement = connection.createStatement();
            if(insertArguments != null){
                int i = 0, j = -1;
                for(Map.Entry<String, String> entry : insertArguments.entrySet()){
                    i++;
                    if(i == insertArguments.size()){
                        query.append(entry.getKey());
                    }
                    else {
                        query.append(entry.getKey()).append(" , ");
                    }
                }
                query.append(")VALUES(");
                for(Map.Entry<String, String> entry : insertArguments.entrySet()){
                    j++;
                    if(j == insertArguments.size() - 1){
                        query.append(entry.getValue()).append(");");
                    }
                    else if(j != insertArguments.size()){
                        query.append(entry.getValue()).append(", ");
                    }

                }
            }
            statement.executeUpdate(query.toString());

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void update(String table, LinkedHashMap<String, String> updateArguments){
        StringBuilder query = new StringBuilder("update " + table + " set ");
        try{
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            Statement statement = connection.createStatement();
            if(updateArguments != null){
                int i = -1;
                for(Map.Entry<String, String> entry : updateArguments.entrySet()){
                    i++;
                    if(i == 0){
                        query.append(entry.getKey()).append(" = ").append(entry.getValue());
                    }
                    if(i==1){
                        query.append(" where ").append(entry.getKey()).append(" ='").append(entry.getValue()).append("' ;");
                    }
                }
            }
            statement.executeUpdate(query + ";");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
