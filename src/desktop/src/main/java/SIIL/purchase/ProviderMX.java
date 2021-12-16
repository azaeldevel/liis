
package SIIL.purchase;

import SIIL.Server.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Azael
 */
public class ProviderMX extends Provider
{
    private String rfc;
 
    
    /**
     * @return the rfc
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * @param rfc the rfc to set
     */
    public void setRFC(String rfc) {
        this.rfc = rfc;
    }
    
    public void down(Database conn) throws SQLException
    {
        super.down(conn);
        
        String sql3 = "SELECT rfc FROM PurchaseProvider_Resolved WHERE ID = " + getID();
        Statement stmt3 = (Statement) conn.getConnection().createStatement();
        ResultSet rs = stmt3.executeQuery(sql3);
        if(rs.next())
        {
            rfc = rs.getString("rfc");
        }
        
    }
    
    /**
     * Copia los datas desde la BD hacia esta instancia.
     * @param conn 
     * @throws java.sql.SQLException 
     */
    @Override
    public boolean download(Database conn) throws SQLException 
    {
        PreparedStatement pstmt;
        String sql = "SELECT * FROM PurchaseProvider_Resolved WHERE rfc = ?";
            
        pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setString(1, rfc);               
        ResultSet rs = pstmt.executeQuery();
        if(rs.first())
        {
            downloadSets(rs); 
            return true;
        }
        return false;
    }
    
    @Override
    public int insert(Database conn)  throws SQLException
    {
        int res = super.insert(conn);        

        String sql = "INSERT INTO PurchaseProviderMX(ID,rfc) VALUES(?,?)";
        
        PreparedStatement pstmt;
        pstmt = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setLong(1, getID());
        pstmt.setString(2, rfc);
        return pstmt.executeUpdate();
    }
}
