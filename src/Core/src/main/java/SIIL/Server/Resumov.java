
package SIIL.Server;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/**
 *
 * @author areyes
 */
public class Resumov 
{
    private static String BD = "bc.tj";
    private SIIL.Server.Forklift forklift;
    protected SIIL.Server.Company company;
    protected String suc;
    protected String uso;
    protected String note;
    private ArrayList<SIIL.Server.Titem> titems;
    protected Date fhMov;
    protected SIIL.Server.Titem titem;
    public boolean flag;
    protected int id;
    
    /**
     * Fordware Compatible para adaptarla a el nuevo modelo de datos.
     * @param database
     * @param number
     * @return
     * @throws SQLException 
     */
    @Deprecated
    public Boolean downTitem(Database database) throws SQLException
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT titemNumber FROM  Resumov WHERE id = " + id;
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            this.titem = new Titem();
            this.titem.download(database,BD,rs.getString(1));
            return true;
        }
        else
        {
            this.titem = null;
            return false;
        }     
    }
    
    public boolean checkExistFk(Database conn)
    {
        String sql;
        try 
        {
            sql = "SELECT id FROM Resumov WHERE titemNumber = '"  + forklift.getNumber() + "' AND titemBD='" + forklift.getBD() + "'";
            Statement stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(sql);
            if(rs.next())
            {
                return true;
            }
            else
            {
                return false;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Forklift.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * @return the fhMov
     */
    public Date getFhMov() {
        return fhMov;
    }

    /**
     * @param fhMov the fhMov to set
     */
    public void setFhMov(Date fhMov) {
        this.fhMov = fhMov;
    }
    public void setFhMov(String fh) throws ParseException 
    {
        fhMov = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH).parse(fh);
    }

    public boolean downloadCompany(Database conn) 
    {
        String sql = "SELECT compBD, compNumber FROM Resumov WHERE forkliftBD='" + forklift.getBD() +"' and forkliftNumber='" + forklift.getNumber() + "'";
        try 
        {
            Statement stmt = conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {           
                company = new Company();
                company.setBD(rs.getString("compBD"));
                company.setNumber(rs.getString("compNumber"));
                return true;
            }
            else
            {
                return false;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * @return the ttm
     */
    public SIIL.Server.Titem getTtm() {
        return titem;
    }

    /**
     * @param ttm the ttm to set
     */
    public void setTtm(SIIL.Server.Titem ttm) {
        this.titem = ttm;
    }

    public int reflexKey(String BD, String number, MySQL conn) 
    {
        try 
        {
            String sql = "UPDATE Resumov SET titemBD = ?,titemNumber = ? WHERE forkliftBD = ? and forkliftNumber = ?";
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1,BD);
            pstmt.setString(2,number);
            pstmt.setString(3, BD);
            pstmt.setString(4, number);
            
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int updateFecha(MySQL conn) 
    {
        try 
        {
            String sql = "UPDATE Resumov SET fhmov=? WHERE titemBD = ? and titemNumber = ?";
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setDate(1,new java.sql.Date(fhMov.getTime()));
            pstmt.setString(2, getTtm().getBD());
            pstmt.setString(3, getTtm().getNumber());
            
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int addTitem(Database conn) 
    {
        try 
        {
            String sql = "INSERT Resumov(titemBD,titemNumber,suc,uso,compBD,compNumber) VALUES(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, getTtm().getBD());
            pstmt.setString(2, getTtm().getNumber());   
            pstmt.setString(3, suc);
            pstmt.setString(4, uso);
            pstmt.setString(5, company.getBD());
            pstmt.setString(6, company.getNumber());
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int delete(Database conn) throws SQLException 
    {          
        String sql = "DELETE FROM Resumov WHERE id = ?";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setInt(1, id); 
        return pstmt.executeUpdate();   
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    public enum Commands
    {
        UpdateNote,
        UpdateClient,
        UpdateSucursal
    }
    /**
     * @return the forklift
     */
    public SIIL.Server.Forklift getForklift() {
        return forklift;
    }

    /**
     * @param forklift the forklift to set
     */
    public void setForklift(SIIL.Server.Forklift forklift) {
        this.forklift = forklift;
    }

    /**
     * @return the company
     */
    public SIIL.Server.Company getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(SIIL.Server.Company company) {
        this.company = company;
    }

    /**
     * Retorna la sucuarsal en texto de base de datos
     * @return the suc
     */
    public String getSucursal() 
    {
        return suc;
    }
    /**
     * Retorna la sucursa en texto complatible con combobox
     * @return the suc
     */
    public String getSucursal(boolean code) 
    {
        if(code)
        {
            switch(suc)
            {
                case "bc.tj":
                    return "Tijuana";
                case "bc.mx":
                    return "Mexicali";
                case "Ensenada":
                    return "bc.ens";
            }
        }
        
        return suc;
    }
    public void setSucursal(JComboBox cb)
    {
        switch(cb.getSelectedIndex())
        {
            case 0://Seleccione..
                
                break;
            case 1://Tijuana
                this.suc = "bc.tj";
                break;
            case 2://Ensenada
                this.suc = "bc.ens";
                break;
            case 3://Mexicali
                this.suc = "bc.mx";
                break;
        }
    }
    /**
     * @param suc the suc to set
     */
    public void setSucursal(String suc) {
        switch(suc.trim())
        {
            case "Tijuana":
                this.suc = "bc.tj";
                break;
            case "Ensenada":
                this.suc = "bc.ens";
                break;
            case "Mexicali":
                this.suc = "bc.mx";
                break;
            case "bc.tj":
            case "bc.ens":
            case "bc.mx":
                this.suc = suc;
        }
    }

    /**
     * @return the tipo
     */
    public String getUso() 
    {
        return uso;
    }

    /**
     * @return the tipo
     */
    public String getUso(boolean mode) 
    {
        if(mode)
        {
            switch(uso)
            {
                case "disp":
                    return "Disponible";
                case "rtacp":
                    return "Renta Corto Plazo";
                case "pres":
                    return "Prestamo";
                case "rtaoc":
                    return "Renta Opcion de Compra";
                case "rep":
                    return "Reparacion";
                case "vta":
                    return "Venta";
                case "mov":
                    return "Movimiento";
                case "tpint":
                    return "T. de Pintura";
            }
        }        
        return uso;
    }    
    public void setUso(JComboBox cbusomov) 
    {
        switch(cbusomov.getSelectedIndex())
        {
            case 0:
                break;
            case 1:
                this.uso =  "disp";
                break;
            case 2:
                this.uso =  "rtacp";
                break;
            case 3:
                this.uso =  "pres";
                break;
            case 4:   
                this.uso =  "rtaoc";
                break; 
            case 5:    
                this.uso =  "rep";  
                break;
            case 6:    
                this.uso =  "vta"; 
                break;
            case 7:    
                this.uso =  "mov";  
                break;
            case 8:
                this.uso =  "tpint";
                break;
            case 9:
                this.uso =  "baja";
            case 10:
                this.uso = "corr";
            case 11:
                this.uso = "otras";
                break;
        }
    }
    
    public void setUso(int cbusomov) 
    {
        switch(cbusomov)
        {
            case 0:
                break;
            case 1:
                this.uso =  "disp";
                break;
            case 2:
                this.uso =  "rtacp";
                break;
            case 3:
                this.uso =  "pres";
                break;
            case 4:   
                this.uso =  "rtaoc";
                break; 
            case 5:    
                this.uso =  "rep";  
                break;
            case 6:    
                this.uso =  "vta"; 
                break;
            case 7:    
                this.uso =  "mov";  
                break;
            case 8:
                this.uso =  "tpint";
                break;
        }
    }
    
    /**
     * @param tipo the tipo to set
     */
    public void setUso(String uso) 
    {       
        switch(uso)
        {
            case "Disponible":
                this.uso = "disp";
                break;
            case "Renta Corto Plazo":
                this.uso = "rtacp";
                break;
            case "Prestamo":
                this.uso = "pres";
                break;
            case "Renta Opcion de Compra":
                this.uso = "rtaoc";
                break;
            case "Reparacion":
                this.uso = "rep";
                break;
            case "Venta":
                this.uso = "vta";
                break;
            case "Movimiento":
                this.uso = "mov";
                break;
            case "T. de Pintura":
                this.uso = "tpint";
                break;
            case "Baja":
                this.uso = "baja";
                break;
            case "Cubriendo":
                this.uso = "corr";
                break;
            case "Otras...":
                this.uso = "otra";
                break;
            case "disp":
            case "rtacp":
            case "pres":
            case "rtaoc":
            case "rep":
            case "vta":
            case "mov":
            case "tpint":
            case "corr":
            case "otra":
            case "baja":
                this.uso = uso;
                break;
            default:
                this.uso = null;                
        }
    }

    public int insertTitem(Database conn) throws SQLException 
    {
        String sql = " INSERT Resumov(titemBD,titemNumber,compBD,compNumber,suc,uso,note,fhmov) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setString(1, titem.getBD());
        pstmt.setString(2, titem.getNumber());
        pstmt.setString(3, company.getBD());
        pstmt.setString(4, company.getNumber());
        pstmt.setString(5, "bc.tj");
        pstmt.setString(6, uso);
        pstmt.setString(7, note);
        pstmt.setDate(8, new java.sql.Date(fhMov.getTime()));
        System.out.println(sql);
        return pstmt.executeUpdate();
    }
        
    /**
     * @deprecated 
     * @param conn
     * @return 
     */
    public int insert(Database conn) 
    {
        if(!checkExistFk(conn))
        {
            String values = "'" + forklift.getBD() + "','" + forklift.getNumber() + "','" + forklift.getBD() + "','" + forklift.getNumber() + "','" + company.getBD()+ "','" + company.getNumber() + "','" + suc + "','" + uso + "','" + note + "'";

            String sql = " Resumov(titemBD,titemNumber,forkliftBD,forkliftNumber,compBD,compNumber,suc,uso,note) VALUES(" + values + ")";
            return conn.insert(sql);
        }
        return 0;
    }
    
    public int update(Database conn,Commands cmd) 
    {
        String values = "";
        if(Commands.UpdateNote == cmd)
        {
            if(note != null)
            {
                if(values.length() > 0)
                {
                    values = values + ",note=\"" + note + "\"";
                }
                else
                {
                    values = "note=\"" + note + "\"";
                }
            }
        }
        else if(Commands.UpdateClient == cmd)
        {
            if(company != null && company.getBD() != null && company.getNumber() != null)
            {
                if(values.length() > 0)
                {
                    values = values + "compBD='" + company.getBD() + "',compNumber='" + company.getNumber() + "'";
                }
                else
                {
                    values = "compBD='" + company.getBD() + "',compNumber='" + company.getNumber() + "'";
                }
            }
        }
        else if(Commands.UpdateSucursal == cmd)
        {            
            if(suc != null)
            {
                if(values.length() > 0)
                {
                    values = values + ",suc='" + suc + "'";
                }
                else
                {
                    values = "suc='" + suc + "'";
                }
            }
        }
        
        String sql = " Resumov SET " + values + " WHERE titemBD='" + forklift.getBD() + "' and titemNumber='" + forklift.getNumber() + "'";
        if(values.length() > 0 ) 
        {
            return conn.update(sql);
        }
        else
        {
            return 0;
        }
    }
    
        public int update(Database conn)
        {
        if(exist(conn))
        {
            String values = "";
            if(company != null && company.getBD() != null && company.getNumber() != null)
            {
                if(values.length() > 0)
                {
                    values = values + "compBD='" + company.getBD() + "',compNumber='" + company.getNumber() + "'";
                }
                else
                {
                    values = "compBD='" + company.getBD() + "',compNumber='" + company.getNumber() + "'";
                }
            }
            if(suc != null)
            {
                if(values.length() > 0)
                {
                    values = values + ",suc='" + suc + "'";
                }
                else
                {
                    values = "suc='" + suc + "'";
                }
            }
            if(uso != null)
            {
                if(values.length() > 0)
                {
                    values = values + ",uso ='" + uso + "'";
                }
                else
                {
                    values = "uso ='" + uso + "'";
                }
            }
            if(note != null)
            {
                if(values.length() > 0)
                {
                    values = values + ",note=\"" + note + "\"";
                }
                else
                {
                    values = "note=\"" + note + "\"";
                }
            }
            if(fhMov != null)
            {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                if(values.length() > 0)
                {
                    values = values + ",fhMov = STR_TO_DATE('" + df.format(fhMov) + "','%d/%m/%Y')"; 
                }
                else
                {
                    values = "fhMov = STR_TO_DATE('" + df.format(fhMov) + "','%d/%m/%Y')"; 
                }
            }
            
            if(forklift!=null)
            {
                
            }
            
            String sql = " Resumov SET " + values + " WHERE titemBD='" + forklift.getBD() + "' and titemNumber='" + forklift.getNumber() + "'";
            if(values.length() > 0 ) return conn.update(sql);
            return 0;
        }
        return 0;
    }

    public boolean exist(Database conn) 
    {
        String sql = " SELECT id FROM Resumov WHERE titemBD = '" + forklift.getBD() + "' and titemNumber='" + forklift.getNumber() + "'";
        PreparedStatement statement = null;
        ResultSet res = null;
        try
        {
            statement = conn.getConnection().prepareStatement(sql);
            res = statement.executeQuery();
            if(res.next())
            {
                return true;
            }
            else
            {
                return false;
            }   
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return false;
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
     * @return the titems
     */
    public ArrayList<SIIL.Server.Titem> getTitems() {
        return titems;
    }

    /**
     * @param titems the titems to set
     */
    public void setTitems(ArrayList<SIIL.Server.Titem> titems) {
        this.titems = titems;        
    }
    
    public int addMina(Database conn) 
    {
        try 
        {
            String sql = "INSERT Resumov(titemBD,titemNumber,suc,uso,compBD,compNumber) VALUES(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, getTtm().getBD());
            pstmt.setString(2, getTtm().getNumber());   
            pstmt.setString(3, suc);
            pstmt.setString(4, uso);
            pstmt.setString(5, company.getBD());
            pstmt.setString(6, company.getNumber());
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Battery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void addBattery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
