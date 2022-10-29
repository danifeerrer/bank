package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class dataBaseService {

    private String url = "jdbc:mysql://localhost:3306/bank";
    private String username = "root";
    private String password = "$contraseña$11";


    public ResultSet select(String column ,String table, Map<String, String> whereArguments ){
        ResultSet resultSet = null;
        String query = "select " + column + " from " + table ;


        try{
            Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
            Statement statement = connection.createStatement();
            // Vamos a recorrer el mapa para ver las condiciones del where
            if(whereArguments != null){
                if(whereArguments.size() == 1){
                    query += " where";
                    for (Map.Entry<String, String> entry : whereArguments.entrySet()) {
                        query += " " + entry.getKey() + "= '" + entry.getValue() + "'";
                    }
                }
                else if(whereArguments.size() != 1){
                    query += " where";
                    int i = 0;
                    for (Map.Entry<String, String> entry : whereArguments.entrySet()) {
                        i++; //select from customer where password = mdz12 and id = '30'
                        if (i != whereArguments.size()){
                            query += " " + entry.getKey() + "= '" + entry.getValue() + "' and";
                            //i = 1
                        }
                        else if(i == whereArguments.size()){
                            query += " " + entry.getKey() + "= '" + entry.getValue() + "'";
                        }
                        else{
                            break;
                        }
                    }
                }

            }
            System.out.println(query);
            resultSet = statement.executeQuery(query + ";");



        } catch(Exception e){
            e.printStackTrace();
        }

        return resultSet;
    }
    public void insert(String table, Map<String, String> insertArguments){
        String query = "insert into " + table + "(";

        try{
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            Statement statement = connection.createStatement();
            if(insertArguments != null){
                int i = 0, j = -1;
                for(Map.Entry<String, String> entry : insertArguments.entrySet()){
                    i++;
                    if(i == insertArguments.size()){
                        query += entry.getKey();
                    }
                    else if(i != insertArguments.size()){
                        query += entry.getKey() + " , ";
                    }
                    else{
                        break;
                    }
                }
                query += ")VALUES(";
                for(Map.Entry<String, String> entry : insertArguments.entrySet()){
                    j++;
                    if(j == insertArguments.size() - 1){
                        query += entry.getValue() + ");";
                    }
                    else if(j != insertArguments.size()){
                        query += entry.getValue() + ", ";
                    }

                }
            }
            System.out.println(query);
            statement.executeUpdate(query);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void update(String table, LinkedHashMap<String, String> updateArguments){
        String query = "update " + table + " set ";
        try{
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            Statement statement = connection.createStatement();
            if(updateArguments != null){
                int i = -1;
                for(Map.Entry<String, String> entry : updateArguments.entrySet()){
                    i++;
                    if(i == 0){
                        query += entry.getKey() + " = " + entry.getValue();
                    }
                    if(i==1){
                        query += " where " + entry.getKey() + " ='" + entry.getValue() +"' ;";
                    }
                }
            }
            System.out.println(query);
            statement.executeUpdate(query + ";");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
