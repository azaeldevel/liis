
package SIIL.Administracion.Cheques;

import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.Servicios.Grua.Resumov;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azael
 */
public class Cheque 
{
    private int ID;
    @Deprecated
    private String user;
    private Date fh;
    @Deprecated
    private Company comp;
    private double monto;
    private String moneda;
    private String factSerie;
    private int factFolio;
    @Deprecated
    private int number;
    @Deprecated
    private String sucursal;
    private float tcambio;
    private String note;
    private double pesos;
    @Deprecated
    private Date fhDepo;
    private ChequeRow row;
    
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
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = Integer.parseInt(ID);
    }
    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the fh
     */
    public Date getFh() {
        return fh;
    }

    /**
     * @param fh the fh to set
     */
    public void setFh(Date fh) {
        this.fh = fh;
    }

    /**
     * @return the comp
     */
    public Company getComp() {
        return comp;
    }

    /**
     * @param comp the comp to set
     */
    public void setComp(Company comp) {
        this.comp = comp;
    }

    /**
     * @return the monto
     */
    public double getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(float monto) {
        this.monto = monto;
    }

    /**
     * @return the moneda
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the factSerie
     */
    public String getFactSerie() {
        return factSerie;
    }

    /**
     * @param factSerie the factSerie to set
     */
    public void setFactSerie(String factSerie) {
        this.factSerie = factSerie;
    }

    /**
     * @return the factFolio
     */
    public int getFactFolio() {
        return factFolio;
    }

    /**
     * @param factFolio the factFolio to set
     */
    public void setFactFolio(int factFolio) {
        this.factFolio = factFolio;
    }

    int insert(Database conn,ChequeRow chr) 
    {
        String sql = " INSERT INTO Cheques(fh,compBD,compNumber,monto,moneda,factSerie,factFolio,numcheque,suc,dllCambio,note,pesos,fhDeposito,chequeID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        PreparedStatement pstmt;
        try 
        {
            pstmt = conn.getConnection().prepareStatement(sql);            
            pstmt.setDate(1, new java.sql.Date(fh.getTime()));
            pstmt.setString(2,comp.getBD());
            pstmt.setString(3,comp.getNumber());
            pstmt.setDouble(4, monto);
            pstmt.setString(5, moneda);
            pstmt.setString(6, factSerie);
            pstmt.setInt(7, factFolio);
            pstmt.setInt(8, number);
            pstmt.setString(9, sucursal);
            DecimalFormat df = new DecimalFormat("#.##"); 
            if(moneda.equals("dll"))
            {
                pstmt.setFloat(10, tcambio);
                pstmt.setString(12, df.format(tcambio*monto));
            }
            else if(moneda.equals("mnx"))
            {
                pstmt.setNull(10, 0);
                pstmt.setDouble(12, monto);
            }
            
            pstmt.setString(11, note);                      
            pstmt.setDate(13, new java.sql.Date(fhDepo.getTime()));
            pstmt.setInt(13, chr.getID());
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;    
    }
    
    int insert(Database conn) 
    {
        String sql = " INSERT INTO Cheques(fh,compBD,compNumber,monto,moneda,factSerie,factFolio,numcheque,suc,dllCambio,note,pesos,fhDeposito) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        PreparedStatement pstmt;
        try 
        {
            pstmt = conn.getConnection().prepareStatement(sql);            
            pstmt.setDate(1, new java.sql.Date(fh.getTime()));
            pstmt.setString(2,comp.getBD());
            pstmt.setString(3,comp.getNumber());
            pstmt.setDouble(4, monto);
            pstmt.setString(5, moneda);
            pstmt.setString(6, factSerie);
            pstmt.setInt(7, factFolio);
            pstmt.setInt(8, number);
            pstmt.setString(9, sucursal);
            DecimalFormat df = new DecimalFormat("#.##"); 
            if(moneda.equals("dll"))
            {
                pstmt.setFloat(10, tcambio);
                pstmt.setString(12, df.format(tcambio*monto));
            }
            else if(moneda.equals("mnx"))
            {
                pstmt.setNull(10, 0);
                pstmt.setDouble(12, monto);
            }
            
            pstmt.setString(11, note);                      
            pstmt.setDate(13, new java.sql.Date(fhDepo.getTime()));
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;    
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
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

    boolean download(Database conn) 
    {
        PreparedStatement pstmt;
        String sql = "SELECT * FROM Cheques_Resolved WHERE ID = ?";
        
        try 
        {
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, ID);               
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                comp = new Company();
                comp.setBD(rs.getString("compBD"));
                comp.setNumber(rs.getString("compNumber"));
                comp.setName(rs.getString("compName"));
                factSerie = rs.getString("factSerie");
                factFolio = rs.getInt("factFolio");
                fh = rs.getDate("fh");
                moneda = rs.getString("moneda");
                monto = rs.getFloat("monto");
                number = rs.getInt("numcheque");
                sucursal = rs.getString("suc");
                fhDepo = rs.getDate("fhDeposito");
                note = rs.getString("note");
                return true;
            }            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Cheque.class.getName()).log(Level.SEVERE, null, ex);
        }       
        return false;
    }

    int update(Database conn) 
    {
        String sql = "UPDATE Cheques SET fh=?, compBD=?,compNumber=?,monto=?,moneda=?,factSerie=?,factFolio=?,numcheque=?,note=?,fhDeposito=?,dllCambio=?,pesos=?  WHERE ID = ?";
        
        PreparedStatement pstmt;
        try 
        {
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(fh.getTime()));
            pstmt.setString(2, comp.getBD());
            pstmt.setString(3, comp.getNumber());
            pstmt.setDouble(4, monto);
            pstmt.setString(5, moneda);
            pstmt.setString(6, factSerie);
            pstmt.setInt(7, factFolio);
            pstmt.setInt(8, number);
            pstmt.setString(9, note);
            pstmt.setDate(10, new java.sql.Date(fhDepo.getTime()));
            DecimalFormat df = new DecimalFormat("#.##"); 
            if(moneda.equals("dll"))
            {
                pstmt.setFloat(11, tcambio);
                pstmt.setString(12, df.format(tcambio*monto));
            }
            else if(moneda.equals("mnx"))
            {
                pstmt.setNull(11, 0);
                pstmt.setDouble(12, monto);
            }
            pstmt.setInt(13,ID);
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;  
    }

    /**
     * @return the tcambio
     */
    public float getTcambio() {
        return tcambio;
    }

    /**
     * @param tcambio the tcambio to set
     */
    public void setTcambio(float tcambio) {
        this.tcambio = tcambio;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the pesos
     */
    public double getPesos() {
        return pesos;
    }

    /**
     * @param pesos the pesos to set
     */
    public void setPesos(double pesos) {
        this.pesos = pesos;
    }

    /**
     * @return the fhDepo
     */
    public Date getFhDeposito() {
        return fhDepo;
    }

    /**
     * @param fhDepo the fhDepo to set
     */
    public void setFhDeposito(Date fhDepo) {
        this.fhDepo = fhDepo;
    }

    /**
     * @return the row
     */
    public ChequeRow getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(ChequeRow row) {
        this.row = row;
    }
}
