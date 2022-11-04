package services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerAccountService {
    public dataBaseService dataBaseService = new dataBaseService();
    public void createCustomerAccount(int customer_id, int account_id){
        Map<String, String> insertArguments = new HashMap<>()
        {
            {
                put("customer_id", String.valueOf(customer_id));
                put("account_id", String.valueOf(account_id));
            }
        };
        dataBaseService.insert("customer_account", insertArguments);
    }
    public ArrayList<Integer> getIds(int customer_id) throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        Map<String, String> selectArguments = new HashMap<>()
        {
            {
                put("customer_id", String.valueOf(customer_id));
            }
        };
        ResultSet resultSet = dataBaseService.select("account_id", "customer_account", selectArguments);
        while(resultSet.next()){
            ids.add(resultSet.getInt("account_id"));
        }
        return ids;
    }
}
