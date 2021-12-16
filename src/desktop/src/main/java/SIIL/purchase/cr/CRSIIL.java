
package SIIL.purchase.cr;

import SIIL.Server.Database;
import session.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * 
 * @author Azael
 */
public class CRSIIL extends CR
{
    /**
     * SELECT en la BD.
     * @param conn Conexion a la BD
     * @param limit valor de la clausula limit de SQL.
     * @return Retorna un ArrayList<CRSIIL> solamente con el atributo ID asignado.
     * @throws SQLException 
     */
    static ArrayList<CRSIIL> downloadIDs(Database conn,String where, int limit) throws SQLException 
    {
        String sql = "SELECT DISTINCT(ID) FROM PurchaseCR_Resolved WHERE " + where + " ORDER BY fhPg DESC, ID DESC LIMIT " + limit;
        Statement stmt = (Statement) conn.getConnection().createStatement();
        System.out.println("SQL : " + sql);
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<CRSIIL> crs = new ArrayList<>();
        while(rs.next())
        {
            CRSIIL cr = new CRSIIL();
            cr.setID(rs.getInt("ID"));
            crs.add(cr);
        }        
        return crs;
    }
    
    
    /**
     * SELECT en la BD.
     * @param conn Conexion a la BD
     * @param limit valor de la clausula limit de SQL.
     * @return Retorna un ArrayList<CRSIIL> solamente con el atributo ID asignado.
     * @throws SQLException 
     */
    static ArrayList<CRSIIL> downloadIDs(Database conn, int limit) throws SQLException 
    {
        String sql = "SELECT DISTINCT(ID) FROM PurchaseCR_Resolved ORDER BY fhPg DESC, ID DESC LIMIT " + limit;
        Statement stmt = (Statement) conn.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<CRSIIL> crs = new ArrayList<>();
        while(rs.next())
        {
            CRSIIL cr = new CRSIIL();
            cr.setID(rs.getInt("ID"));
            crs.add(cr);
        }
        
        return crs;
    }
    
    private String prov;
    private String provName;
    private Date fhPg;
    private String account;
    private ArrayList<Factura> facturas;
    private String POs;
    private String SAs;
    private String Fs;
    
    @Override
    public boolean insert(Database conn) throws SQLException
    {
        super.insert(conn);
        String sql = "INSERT INTO PurchaseCRSIIL(prov,ID,daycredit,fhPg,provName,account) VALUES(?,?,?,?,?,?)";
        
        PreparedStatement pstmt;

        pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setString(1, getProv());
        pstmt.setLong(2, getID()); 
        //buscando datos del proveedor
        SIIL.purchase.ProviderMX prov = new SIIL.purchase.ProviderMX();
        prov.setRFC(this.prov);
        prov.download(conn);    
        pstmt.setInt(3, prov.getDayCredit());
        //generando fecha de pago 
        pstmt.setDate(4, new java.sql.Date(fhPg.getTime()));
        //
        pstmt.setString(5, prov.getName());
        //
        pstmt.setString(6, prov.getAccount());
        
        pstmt.executeUpdate();
        
        for(int i = 0; i < facturas.size(); i++)
        {
            facturas.get(i).setfID(getID());
            facturas.get(i).insert(conn);
        }
        return true;
    }


    /**
     * @return the prov
     */
    public String getProv() {
        return prov;
    }

    /**
     * @param prov the prov to set
     */
    public void setProv(String prov) {
        this.prov = prov;
    }

    HashMap getParamsJasper(Database conn,User user) throws SQLException, ParseException
    {
        HashMap map = new HashMap();
        String sql = "SELECT * FROM PurchaseCR_Resolved WHERE ID = " + getID();
        ResultSet rs;
        
        Statement stmt = (Statement) conn.getConnection().createStatement();
        rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            map.put("ID", String.valueOf(getID()));
            SimpleDateFormat userDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat dbDate = new SimpleDateFormat("yyyy-MM-dd");
            map.put("fhRev", userDate.format(dbDate.parse(rs.getString("fhRev"))));  
            map.put("dcred", rs.getString("daycredit"));
            map.put("fhPg", userDate.format(dbDate.parse(rs.getString("fhPg"))));
            map.put("total", rs.getString("amountStr"));
            map.put("provName", rs.getString("provName"));
            String mon = "";
            if(rs.getString("currency").equals("MXN"))
            {
                mon = "Pesos";
            }
            else if(rs.getString("currency").equals("USD"))
            {
                mon = "Dólares";
            }
            map.put("currency", mon);
            map.put("recibe", rs.getString("recibeStr"));
            String dir = "";
            if(user.getOffice().getCode().equals("bc.tj"))
            {
                dir = "Calle Uno Poniente #19268 Ciudad Insdustrial C.P. 22444, Tijuana B.C. México";
            }
            else if(user.getOffice().getCode().equals("bc.mx"))
            {
                dir = "Ramón Corona 1700-A Col. Las Hadas C.P. 21216, Mexicali B.C. México";                
            }
            else if(user.getOffice().getCode().equals("bc.ens"))
            {
                dir = "Av. Pedro Loyola No. 366-2 Fracc. Playa Hermosa C.P. 22890, Ensenada B.C. México";                
            }
            map.put("dir", dir);
            map.put("account",rs.getString("account"));
        }
        
        int lenght = 10;
        sql = "SELECT * FROM PurchaseCRF WHERE fID = " + getID() + " LIMIT " + lenght;
        
        stmt = (Statement) conn.getConnection().createStatement();        
        rs = stmt.executeQuery(sql);
        for(int i = 1; i < lenght ;i++)
        {
            if(rs.next())
            {
                map.put("folio" + i, rs.getString("folio"));
                map.put("monto" + i, rs.getString("montoStr"));                
                downPOs(rs.getString("folio"), conn);
                map.put("POs" + i, POs);                
                downSAs(rs.getString("folio"), conn);
                map.put("SAs" + i, SAs);
            }
            else
            {
                map.put("folio"+i, "");
                map.put("monto"+i, "");
                map.put("POs" + i, "");
                map.put("SAs" + i, "");
            }
        }
        
 
        return map;
    }

    public void downSAs(String folio, Database conn) throws SQLException {
        String sql3 = "SELECT SA FROM PurchaseCRFD WHERE SA is not NULL and fID = " + getID();
        if(folio == null)
        {
            ;
        }
        else
        {
            sql3 = sql3 + " and folio = '" + folio + "'";
        }
        Statement stmt3 = (Statement) conn.getConnection().createStatement();
        ResultSet rs3 = stmt3.executeQuery(sql3);
        SAs = "";
        while(rs3.next())
        {
            SAs = SAs + rs3.getString("SA")  + ",";
        }
    }
    
    public void downFs(Database conn) throws SQLException {
        String sql3 = "SELECT DISTINCT(folio) FROM PurchaseCRFD WHERE fID = " + getID();
        Statement stmt3 = (Statement) conn.getConnection().createStatement();
        ResultSet rs3 = stmt3.executeQuery(sql3);
        Fs = "";
        while(rs3.next())
        {
            Fs = Fs + rs3.getString("folio")  + ",";
        }
    }

    public void downPOs(String folio, Database conn) throws SQLException {
        String sql2 = "SELECT PO FROM PurchaseCRFD WHERE PO is not NULL and fID = " + getID() ;
        if(folio == null)
        {
            ;
        }
        else
        {
            sql2 = sql2 + " and folio = '" + folio + "'";
        }
        Statement stmt2 = (Statement) conn.getConnection().createStatement();
        ResultSet rs2 = stmt2.executeQuery(sql2);
        POs = "";
        while(rs2.next())
        {
            POs = POs + rs2.getString("PO") + ",";
        }
    }


    void dowloadResum(Database conn) throws SQLException 
    {
        String sql = "SELECT prov,provName,amount,currency,fhPg,obser,fhFolio,account,status FROM PurchaseCR_Resolved WHERE ID = " + getID() + " limit 1";
        ResultSet rs;
          
        Statement stmt = (Statement) conn.getConnection().createStatement();
        rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            setProv(rs.getString("prov"));
            setProvName(rs.getString("provName"));
            setTotal(rs.getDouble("amount"));
            setCurrency(rs.getString("currency"));
            setObservation(rs.getString("obser"));
            setFhFolio(rs.getDate("fhFolio"));
            setEstatus(rs.getString("status"));
            account = rs.getString("account");
        }
    }

    /**
     * @return the provName
     */
    public String getProvName() {
        return provName;
    }

    /**
     * @param provName the provName to set
     */
    public void setProvName(String provName) {
        this.provName = provName;
    }

    /**
     * @return the fhPg
     */
    public Date getFhPg() {
        return fhPg;
    }

    /**
     * @param fhPg the fhPg to set
     */
    public void setFhPg(Date fhPg) {
        this.fhPg = fhPg;
    }
    

    static public void display(CRSIIL cr, Database conn,User user) 
    {
        JasperReport jr;
        try
        {

            /*String decodedPath = new File(".").getAbsolutePath();
            File fPath = new File(decodedPath + "\\reports");
            boolean exists = fPath.exists();
            InputStream is = null;
            if(!exists)
            {
                is = new FileInputStream("C:\\Users\\Azael\\Documents\\Proyectos\\SIIL\\trunk\\System\\desktop\\src\\main\\java\\SIIL\\purchase\\cr\\stand.jasper");
            }
            else
            {
                is = new FileInputStream(decodedPath + "\\reports\\stand.jasper");
            }*/
            Map params = cr.getParamsJasper(conn,user);            
            jr = (JasperReport) JRLoader.loadObject(cr.getClass().getResourceAsStream("/SIIL/purchase/cr/Resumen.jasper"));
            JasperPrint jp = JasperFillManager.fillReport(jr, params, conn.getConnection());
            JasperViewer jv = new JasperViewer(jp,false);
            jv.setVisible(true);
            jv.setTitle("Reporte");
        }
        catch (JRException | SQLException | ParseException ex)
        {
            Logger.getLogger(CRSIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the facturas
     */
    public ArrayList<Factura> getFacturas() {
        return facturas;
    }

    /**
     * @param facturas the facturas to set
     */
    public void setFacturas(ArrayList<Factura> facturas) {
        this.facturas = facturas;
    }

    /**
     * @return the POs
     */
    public String getPOs() {
        return POs;
    }

    /**
     * @param POs the POs to set
     */
    public void setPOs(String POs) {
        this.POs = POs;
    }

    /**
     * @return the SAa
     */
    public String getSAs() {
        return SAs;
    }

    /**
     * @param SAa the SAa to set
     */
    public void setSAs(String SAa) {
        this.SAs = SAa;
    }

    void downloadPOs(Database conn) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void downloadSAa(Database conn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the Fs
     */
    public String getFs() {
        return Fs;
    }

    /**
     * @param Fs the Fs to set
     */
    public void setFs(String Fs) {
        this.Fs = Fs;
    }

    public int delete(Database conn) throws SQLException 
    {
        
        super.delete(conn);
        
        String sql = "DELETE FROM PurchaseCRSIIL WHERE ID = " + getID();
        Statement stmt = (Statement) conn.getConnection().createStatement();
        return stmt.executeUpdate(sql);
    }

}
