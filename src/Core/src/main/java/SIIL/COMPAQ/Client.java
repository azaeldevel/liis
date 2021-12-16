
package SIIL.COMPAQ;

import SIIL.Server.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author areyes
 */
public class Client {
    
    public static final String SQLS_COMERCIAL_AVATAR_TABLE = "dbo.vwAccCustomer";
    
    private int CustomerID_Comercial;
    private int BusinessEntityID_Comercial;
    private String number;
    private String name;



    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the CustomerID_Comercial
     */
    public int getCustomerID_Comercial() {
        return CustomerID_Comercial;
    }

    /**
     * @return the BusinessEntityID_Comercial
     */
    public int getBusinessEntityID_Comercial() {
        return BusinessEntityID_Comercial;
    }
    
    public static ArrayList<Client> search(Database sqlsConn, String text, int limit) throws SQLException
    {
        ArrayList<Client> list = new ArrayList<Client>();
        String sqlstr = "SELECT TOP " + limit + " CustomerID, BusinessEntityID, customerName FROM " + SQLS_COMERCIAL_AVATAR_TABLE + " WHERE customerName LIKE '%" + text + "%'";
        java.sql.Statement stmt = sqlsConn.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sqlstr);
        while(rs.next())
        {
            Client cliente = new Client();
            cliente.CustomerID_Comercial = rs.getInt(1);
            cliente.BusinessEntityID_Comercial = rs.getInt(2);
            cliente.number = null;
            cliente.name = rs.getString(3); 
            list.add(cliente);
        }
        
        return list;
    }
}
