
package SIIL.purchase.cr;

import SIIL.Server.Database;
import session.Credential;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azael
 */
public class CR 
{ 
    private int ID;
    //private Date fhRev;
    private Document[] documents;
    private double total;
    private String currency;
    private double tcambio;
    private String observation;
    private Credential recibe;
    private String sucursal;
    private Date fhFolio;
    private String payUser;
    private String payType;
    private String estatus;
    private String payEnding;
    private Date payDate;
    
    
    public boolean insert(Database conn) throws SQLException
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        
        String sql = "INSERT INTO PurchaseCR(ID,amount,amountStr,currency,obser,recibe,recibeStr,suc,fhFolio,status,fhRev) VALUES(?,?,?,?,?,?,?,?,NOW(),'pnPay',NOW())";
        
        PreparedStatement pstmt;
        pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setLong(1, getID());
        //pstmt.setDate(2, new java.sql.Date(fhRev.getTime()));
        //Total del CR
        pstmt.setDouble(2, getTotal());
        pstmt.setString(3, nf.format(getTotal()));
        pstmt.setString(4, currency);
        if(observation != null)
        {
            pstmt.setString(5, observation.replace("\"", "\\\""));            
        }
        else
        {
            pstmt.setNull(5, 0);
        }
        pstmt.setString(6, recibe.getUser().getAlias());
        pstmt.setString(7, recibe.toString());
        pstmt.setString(8, sucursal);
        
        pstmt.executeUpdate();
        
        
        return true;
    }

    /**
     * @return the documents
     */
    public Document[] getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(Document[] documents) {
        this.documents = documents;
    }



    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the tcambio
     */
    public double getTcambio() {
        return tcambio;
    }

    /**
     * @param tcambio the tcambio to set
     */
    public void setTcambio(double tcambio) {
        this.tcambio = tcambio;
    }

    /**
     * @return the observation
     */
    public String getObservation() {
        return observation;
    }

    /**
     * @param observation the observation to set
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the recibe
     */
    public Credential getRecibe() {
        return recibe;
    }

    /**
     * @param recibe the recibe to set
     */
    public void setRecibe(Credential recibe) {
        this.recibe = recibe;
    }

    /**
     * @return the sucursal
     */
    public String getSucursal() {
        return sucursal;
    }

    /**
     * @param sucursal the sucursal to set
     */
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }    
    
    /**
     * @return the fhFolio
     */
    public Date getFhFolio() {
        return fhFolio;
    }

    /**
     * @param fhFolio the fhFolio to set
     */
    public void setFhFolio(Date fhFolio) {
        this.fhFolio = fhFolio;
    }

    void createID(Database conn) 
    {
        String sql = "SELECT MAX(ID) FROM PurchaseCR_Resolved";
        ResultSet rs;
          
        Statement stmt;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                ID = rs.getInt("MAX(ID)") + 1;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(CR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean pay(Database conn) throws SQLException 
    {        
        String sql = "UPDATE PurchaseCR SET status='paid',payDate=NOW(),payType=?,payEnding=?,payUser=? WHERE ID = ?";
        
        PreparedStatement pstmt;
        pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setString(1, payType);
        if(payEnding == null)
        {
            pstmt.setNull(2, 0);
        }
        else
        {
            pstmt.setString(2, payEnding);
        }
        pstmt.setString(3, payUser);
        pstmt.setLong(4, ID);
        
        pstmt.executeUpdate();
        return true;
    }

    /**
     * @return the payUser
     */
    public String getPayUser() {
        return payUser;
    }

    /**
     * @param payUser the payUser to set
     */
    public void setPayUser(String payUser) {
        this.payUser = payUser;
    }

    /**
     * @return the payType
     */
    public String getPayType() {
        return payType;
    }

    /**
     * @param payType the payType to set
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * @return the estatus
     */
    public String getEstatus() {
        return estatus;
    }

    /**
     * @param estatus the estatus to set
     */
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    /**
     * @return the payEnding
     */
    public String getPayEnding() {
        return payEnding;
    }

    /**
     * @param payEnding the payEnding to set
     */
    public void setPayEnding(String payEnding) {
        this.payEnding = payEnding;
    }

    /**
     * @return the payDate
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * @param payDate the payDate to set
     */
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }

    public int delete(Database conn) throws SQLException 
    {
        //
        String sql1 = "DELETE FROM PurchaseCRFD WHERE fID = " + getID();
        Statement stmt1 = (Statement) conn.getConnection().createStatement();
        int crfd = stmt1.executeUpdate(sql1);
        //
        String sql2 = "DELETE FROM PurchaseCRF WHERE fID = " + getID();
        Statement stmt2 = (Statement) conn.getConnection().createStatement();
        int crf = stmt2.executeUpdate(sql2);
        //
        String sql3 = "DELETE FROM PurchaseCR WHERE ID = " + getID();
        Statement stmt3 = (Statement) conn.getConnection().createStatement();
        int cr = stmt3.executeUpdate(sql3);
        
        if(cr > 0 & crf > 0)
        {
            return cr;
        }
        else
        {
            return 0;
        }
    }
}
