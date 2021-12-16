
package SIIL.purchase;


import SIIL.Server.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Azael
 */
public class ProviderMXSIIL extends ProviderMX
{
    @Override
    public int insert(Database conn)  throws SQLException
    {
        int res = super.insert(conn);        

        String sql = "INSERT INTO PurchaseProviderMXSIIL(ID) VALUES(?)";
        
        PreparedStatement pstmt;        
        pstmt = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setLong(1, getID());
        return pstmt.executeUpdate();
    }
    
    public static ArrayList<ProviderMXSIIL> downloadIDs(Database conn, int limit) throws SQLException 
    {
        String sql = "SELECT ID FROM PurchaseProvider_Resolved ORDER BY ID DESC LIMIT " + limit;
        Statement stmt = (Statement) conn.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<ProviderMXSIIL> provs = new ArrayList<>();
        while(rs.next())
        {
            ProviderMXSIIL prov = new ProviderMXSIIL();
            prov.setID(rs.getInt("ID"));
            provs.add(prov);
        }
        
        return provs;
    }
    
    public static ArrayList<ProviderMXSIIL> downloadIDs(Database conn,String where, int limit) throws SQLException 
    {
        String sql = "SELECT ID FROM PurchaseProvider_Resolved WHERE " + where + " ORDER BY ID DESC LIMIT " + limit;
        Statement stmt = (Statement) conn.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<ProviderMXSIIL> provs = new ArrayList<>();
        while(rs.next())
        {
            ProviderMXSIIL prov = new ProviderMXSIIL();
            prov.setID(rs.getInt("ID"));
            provs.add(prov);
        }
        
        return provs;
    }
}
