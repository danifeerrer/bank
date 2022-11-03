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
        StringBuilder query = new StringBuilder("select " + column + " from " + table);


        try{
            Connection connection = DriverManager.getConnection(this.url, this.username,this.password);
            Statement statement = connection.createStatement();
            // Vamos a recorrer el mapa para ver las condiciones del where
            if(whereArguments != null){
                if(whereArguments.size() == 1){
                    query.append(" where");
                    for (Map.Entry<String, String> entry : whereArguments.entrySet()) {
                        query.append(" ").append(entry.getKey()).append("= '").append(entry.getValue()).append("'");
                    }
                }
                else {
                    query.append(" where");
                    int i = 0;
                    for (Map.Entry<String, String> entry : whereArguments.entrySet()) {
                        i++; //select from customer where password = mdz12 and id = '30'
                        if (i != whereArguments.size()){
                            query.append(" ").append(entry.getKey()).append("= '").append(entry.getValue()).append("' and");
                            //i = 1
                        }
                        else {
                            query.append(" ").append(entry.getKey()).append("= '").append(entry.getValue()).append("'");
                        }
                    }
                }

            }
            resultSet = statement.executeQuery(query + ";");



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
