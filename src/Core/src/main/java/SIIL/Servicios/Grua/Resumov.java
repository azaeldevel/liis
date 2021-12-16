
package SIIL.Servicios.Grua;

import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.Server.MySQL;
import SIIL.Server.Titem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/**
 *
 * @author Azael
 */
public class Resumov extends SIIL.Server.Resumov
{
    /**
     * 
     * @param conn
     * @return 
     */
    public int update(Database conn) 
    {
        String sql = "UPDATE Resumov SET titemBD = ? , titemNumber = ? , compBD = ?, compNumber = ?, suc = ?,uso = ? ,note = ?, fhmov = ? WHERE id = ?";
        
        PreparedStatement pstmt;
        try 
        {
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, titem.getBD());
            pstmt.setString(2, titem.getNumber());
            pstmt.setString(3, company.getBD());
            pstmt.setString(4, company.getNumber());
            pstmt.setString(5, suc);
            pstmt.setString(6, uso);
            pstmt.setString(7, note);
            pstmt.setDate(8, new java.sql.Date(fhMov.getTime()));

            pstmt.setInt(9,id);
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    /**
     * 
     * @param conn
     * @param tm
     * @param forklift
     * @return 
     */
    public int link(Database conn, Titem tm, Forklift forklift)
    {
        
        if(tm.getItemClass().equals("battery"))
        {                
            try 
            {
                String sql = "UPDATE Resumov SET batteryBD = ?, batteryNumber = ? WHERE titemBD = ? and titemNumber = ?";
                PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
                pstmt.setString(1, tm.getBD());
                pstmt.setString(2, tm.getNumber());            
                pstmt.setString(3, forklift.getBD());
                pstmt.setString(4, forklift.getNumber());
                return pstmt.executeUpdate();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(tm.getItemClass().equals("charger"))
        {
            try 
            {
                String sql = "UPDATE Resumov SET chargerBD = ?, chargerNumber = ? WHERE titemBD = ? and titemNumber = ?";
                PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
                pstmt.setString(1, tm.getBD());
                pstmt.setString(2, tm.getNumber());             
                pstmt.setString(3, forklift.getBD());
                pstmt.setString(4, forklift.getNumber());
                return pstmt.executeUpdate();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return 0;
    }
    
    /**
     * Retorna un arreglo de Titem, donde cada titem puede ser una bateriao un
     * o un montacargar. Si es una bateria singnifca que no esta asociada y solo
     * fue asignada al cliente, si es un montacarga signific que esta asociado a
     * montacargas
     * @param battery
     * @return 
     */
    public ArrayList<Titem> check(Database conn, Battery battery)
    {

        ArrayList<Titem> arrT = new ArrayList<>();
        try 
        {
            PreparedStatement pstmt;
            String sql = "SELECT titemBD,titemNumber FROM Resumov WHERE batteryBD = ? and batteryNumber = ? ";
            
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, battery.getBD());            
            pstmt.setString(2, battery.getNumber());            
            ResultSet rs = pstmt.executeQuery(sql);
            while(rs.next())
            {
                Forklift fork = new Forklift();
                fork.setBD(rs.getString("titemBD"));
                fork.setNumber(rs.getString("titemNumber"));
                arrT.add(fork);
            }
            
            sql = "SELECT titemBD,titemNumber FROM Resumov WHERE titemBD = ? and titemNumber = ? ";            
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, battery.getBD());            
            pstmt.setString(2, battery.getNumber());            
            rs = pstmt.executeQuery(sql);
            while(rs.next())
            {
                Battery batt = new Battery();
                batt.setBD(rs.getString("titemBD"));
                batt.setNumber(rs.getString("titemNumber"));
                arrT.add(batt);
            }
            
            return arrT;
        } 
        catch (SQLException ex) {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Asigna la forklift.getBattery() en la base de datos Resumov.Battery en el
     * registro de forklift.getNumber().
     * @param conn
     * @param forklift
     * @return 
     */
    boolean setBattery(Database conn, Forklift forklift)
    {
        
        return false;
    }

    /**
     * Copia los datas desde la BD hacia esta instancia.
     * @param conn 
     */
    public void download(Database conn) 
    {
        try
        {
            PreparedStatement pstmt;
            String sql = "SELECT * FROM Resumov WHERE id = ?";
            
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);               
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                titem = new Titem();
                titem.setBD(rs.getString("titemBD"));
                titem.setNumber(rs.getString("titemNumber"));
                company = new Company();
                company.setBD(rs.getString("compBD"));
                company.setNumber(rs.getString("compNumber"));
                
                suc = rs.getString("suc");
                
                uso = rs.getString("uso");
                
                note = rs.getString("note");
                
                fhMov = rs.getDate("fhmov");
                
                id = rs.getInt("id");
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Object getTMov(boolean code) 
    {
        if(code)
        {
            switch(tmov)
            {
                case "ent":
                    return "Entrada";
                case "sal":
                    return "Salida";
                case "mov":
                    return "Movimiento";
                case "ret":
                    return "Retorno";
                case "canc":
                    return "Cancelar";
            }
        }
        return tmov;
    }
    
    String tmov;
    private Forklift forklift;
    private Battery battery;
    private Charger charger;

    public void setTMov(int cbtmov) 
    {
        switch(cbtmov)
        {
            case 0:                
                break;
            case 1:
                tmov = "ent";
                break;
            case 2:
                tmov = "sal";                
                break;
            case 3:
                tmov = "mov";                
                break;
            case 4:
                tmov = "ret";                
                break;
            case 5:
                tmov = "canc";                
                break;  
        }
    }
    
    public void setTMov(JComboBox cbtmov) 
    {
        switch(cbtmov.getSelectedIndex())
        {
            case 0:                
                break;
            case 1:
                tmov = "ent";
                break;
            case 2:
                tmov = "sal";                
                break;
            case 3:
                tmov = "mov";                
                break;
            case 4:
                tmov = "ret";                
                break;
            case 5:
                tmov = "canc";                
                break;  
        }
    }
    
    /**
     * Asigna el cargador al montacargar. Si el cargador es null se limpiara el 
     * campo del cargador.
     * @param conn
     * @return 
     */
    public int updateCharger(Database conn) 
    {
        String sql = "UPDATE Resumov SET chargerBD = ?, chargerNumber = ? WHERE titemBD = ? and titemNumber = ?";
        try
        {
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            if(charger == null)
            {
                pstmt.setNull(1, 0);
                pstmt.setNull(2, 0);
            }
            else
            {
                pstmt.setString(1, charger.getBD());
                pstmt.setString(2, charger.getNumber());
            }
            pstmt.setString(3, forklift.getBD());
            pstmt.setString(4, forklift.getNumber());
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Asigna la bateria al montacargas. Si la bateria es null, se limpiera
     * el valor del campo bateria.
     * @param conn
     * @return 
     */
    public int updateBattery(Database conn) 
    {
        String sql = "UPDATE Resumov SET batteryBD = ?, batteryNumber = ? WHERE titemBD = ? and titemNumber = ?";
        try
        {
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            if(battery == null)
            {
                pstmt.setNull(1, 0);
                pstmt.setNull(2, 0);
            }
            else
            {
                pstmt.setString(1, battery.getBD());
                pstmt.setString(2, battery.getNumber());
            }
            pstmt.setString(3, forklift.getBD());
            pstmt.setString(4, forklift.getNumber());
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * @return the forklift
     */
    public Forklift getForklift() {
        return forklift;
    }

    /**
     * @param forklift the forklift to set
     */
    public void setForklift(Forklift forklift) {
        this.forklift = forklift;
        super.setForklift(forklift);
    }

    /**
     * @return the battery
     */
    public Battery getBattery() {
        return battery;
    }

    /**
     * @param battery the battery to set
     */
    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    /**
     * @return the charger
     */
    public Charger getCharger() {
        return charger;
    }

    /**
     * @param charger the charger to set
     */
    public void setCharger(Charger charger) {
        this.charger = charger;
    }
    
}
