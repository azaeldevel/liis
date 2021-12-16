
package SIIL.purchase;

import SIIL.Server.Database;
import SIIL.Servicios.Grua.Resumov;
import SIIL.purchase.cr.CR;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/**
 *
 * @author Azael
 */
public class Provider 
{

    public static int Load(Database conn, JComboBox cbProv) 
    {
        String sql = " SELECT rfc FROM PurchaseProvider_Resolved";
        PreparedStatement pstmt;
        try 
        {
            pstmt = conn.getConnection().prepareStatement(sql);
            cbProv.addItem("Seleccione ...");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            {
                cbProv.addItem(rs.getString("rfc"));
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private long ID;
    private int dayCredit;
    private String account;
    private String name;
    
    /**
     * @return the ID
     */
    public long getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * @return the dayCredit
     */
    public int getDayCredit() {
        return dayCredit;
    }

    /**
     * @param dayCredit the dayCredit to set
     */
    public void setDayCredit(int dayCredit) {
        this.dayCredit = dayCredit;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(int account) {
        this.account = String.valueOf(account) ;
    }
    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }
    
    public int insert(Database conn)  throws SQLException
    {
        String sql = "INSERT INTO PurchaseProvider(dayCredit,account,ID,name) VALUES(?,?,?,?)";
        
        PreparedStatement pstmt;
        pstmt = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, dayCredit);
        pstmt.setString(2, account);
        pstmt.setLong(3, ID);
        pstmt.setString(4, name);
        return pstmt.executeUpdate();
    }
    
    /**
     * Copia los datas desde la BD hacia esta instancia.
     * @param conn 
     */
    public boolean download(Database conn) throws SQLException 
    {
        PreparedStatement pstmt;
        String sql = "SELECT * FROM Provider_Resolved WHERE id = ?";            
        pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setLong(1, ID);               
        ResultSet rs = pstmt.executeQuery();
        return downloadSets(rs);
    }
    
    public void down(Database conn) throws SQLException 
    {
        String sql3 = "SELECT account,daycredit,name FROM PurchaseProvider_Resolved WHERE ID = " + getID();
        Statement stmt3 = (Statement) conn.getConnection().createStatement();
        ResultSet rs = stmt3.executeQuery(sql3);
        if(rs.next())
        {
            account = rs.getString("account");
            dayCredit = rs.getInt("daycredit");
            name = rs.getString("name");
        }
    }

    protected boolean downloadSets(ResultSet rs) throws SQLException 
    {
        account = rs.getString("account");
        dayCredit = rs.getInt("daycredit");
        name = rs.getString("name");
        return true;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void createID(Database conn) 
    {
        String sql = "SELECT MAX(ID) FROM PurchaseProvider_Resolved";
        ResultSet rs;
        
        Statement stmt;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                ID = rs.getLong("MAX(ID)") + 1;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
