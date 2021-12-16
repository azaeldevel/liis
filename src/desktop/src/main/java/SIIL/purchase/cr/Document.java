
package SIIL.purchase.cr;

import SIIL.Server.Database;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;

/**
 *
 * @author Azael
 */
public class Document 
{
    private String folio;
    private String PO;
    private String SA;
    private int fID;
    
    /**
     * @return the folio
     */
    public String getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * @return the PO
     */
    public String getPO() {
        return PO;
    }

    /**
     * @param PO the PO to set
     */
    public void setPO(String PO) {
        this.PO = PO;
    }

    /**
     * @return the SA
     */
    public String getSA() {
        return SA;
    }

    /**
     * @param SA the SA to set
     */
    public void setSA(String SA) {
        this.SA = SA;
    }
    
    public boolean insert(Database conn) throws SQLException
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance();        
        PreparedStatement pstmt;
        
        String sql = "INSERT INTO PurchaseCRFD(fID,folio,PO,SA) VALUES(?,?,?,?)";
        pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setInt(1, fID);
        pstmt.setString(2, folio);  
        if(PO == null)
        {
            pstmt.setNull(3, 0);
        }
        else
        {
            pstmt.setString(3, PO);
        }
        if(SA == null)
        {
            pstmt.setNull(4, 0);
        }
        else
        {
            pstmt.setString(4, SA);
        }
        
        pstmt.executeUpdate();
        
        
        return true;
    }

    /**
     * @return the fID
     */
    public int getfID() {
        return fID;
    }

    /**
     * @param fID the fID to set
     */
    public void setfID(int fID) {
        this.fID = fID;
    }
}
