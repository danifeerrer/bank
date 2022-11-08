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
                query = query.replaceAll(" and$",";");
            }
            resultSet = statement.executeQuery(query);
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
                String values = ")VALUES(";

                for(Map.Entry<String, String> entry : insertArguments.entrySet()){
                    query += (entry.getKey())+ (" , ");
                    values += (entry.getValue()) + (", ");
                }
                query = (query.substring(0,query.length() -2) + values).replaceAll(", $", ");") ;
            }
            statement.executeUpdate(query.toString());

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void update(String table, LinkedHashMap<String, String> updateArguments){
        //String query = new StringBuilder("update " + table + " set ";
        String query = "update " + table + " set ";
        try{
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            Statement statement = connection.createStatement();
            // Lo mismo que en los otros métodos
            if(updateArguments != null){

                int i = 0;
                for(Map.Entry<String, String> entry : updateArguments.entrySet()){
                    if(i == updateArguments.entrySet().size() -1){
                        query += "where " + entry.getKey() + "= " + "'"+entry.getValue()+"'";
                        query = query.replace(", where", " where");
                        break;
                    }
                    query += entry.getKey() + "='" + entry.getValue() + "', ";

                    i++;
                }
            }

            statement.executeUpdate(query + ";");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
