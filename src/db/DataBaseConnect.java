package db;

import java.sql.*;

public class DataBaseConnect {
    public static void main(String[] args) {
        try {
            Connection lib = DriverManager.getConnection("jdbc:derby:lib;create=true"), data = DriverManager.getConnection("jdbc:ucanaccess://MJUFoodMap.accdb");
            Statement table = lib.createStatement(), row = data.createStatement();
            table.execute("CREATE TABLE FoodMap(RID int primary key, R_Name varchar(255), Category varchar(255), LinkNum int)");
            ResultSet resultSet = row.executeQuery("SELECT * FROM FoodMap");
            while (resultSet.next())
                table.execute("INSERT INTO FoodMap VALUES("+resultSet.getInt("RID")+", '"+
                        resultSet.getString("R_Name")+"', '"+resultSet.getString("Category")+
                        "', "+resultSet.getInt("LinkNum")+")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
