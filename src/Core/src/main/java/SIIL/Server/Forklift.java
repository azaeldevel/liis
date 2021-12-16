
package SIIL.Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author areyes
 */
public class Forklift extends Titem
{ 
    private SIIL.Server.Charger charger;
    private SIIL.Server.Battery battery;
    private float horomentro;
       
    public Forklift()
    {
        //super.setItemClass("forklift");
    }
    
    public int insert(Database conn)  throws Exception
    {
        if(super.insertTit(conn) == 1)
        {
            String sql = " Forklift( BD,number ) VALUES('" + getBD() + "','" + getNumber() + "')";
            return conn.insert(sql);
        }
        return 0;
    }

    @Override
    public int update(Database conn) 
    {
        return super.update(conn);
    }

    /**
     * @return the charger
     */
    public SIIL.Server.Charger getCharger() {
        return charger;
    }

    /**
     * @param charger the charger to set
     */
    public void setCharger(SIIL.Server.Charger charger) {
        this.charger = charger;
    }

    /**
     * @return the battery
     */
    public SIIL.Server.Battery getBattery() {
        return battery;
    }

    /**
     * @param battery the battery to set
     */
    public void setBattery(SIIL.Server.Battery battery) {
        this.battery = battery;
    }
    
    public boolean linkCharger(Database conn) 
    {
        int cres = 0;
        String flLiked = checkLinkCharger(conn);
        
        String sqlL;
        cres = setLinkCharger(flLiked, cres, conn);
        
        switch(cres)
        {
            case 0:
                System.err.println("No se enlazo el cargador [" + conn.getSQL() + "]");                
                return false;
            case 1:
            case 2:
                //System.out.println("Se enlazo el cargador]");
                return true;
        }
        return false;
    }
    
    private int setLinkCharger(String flLiked, int cres, Database conn) {
        String sqlL;
        //System.out.println("Asignado cargador " + charger.getNumber() + " a " + getNumber());
        if(!flLiked.equals(""))
        {//Si esta asignado
            //1.- desasignar al anterior montacargas
            sqlL = " Resumov SET chargerBD =NULL,chargerNumber=NULL WHERE " + " titemNumber = '" + charger.getNumber() + "' and titemBD = '" + charger.getBD() + "'";
            cres += conn.update(sqlL);
            //2.- asignar al nuevo montacargas
            sqlL = " Resumov SET chargerBD ='" + charger.getBD() + "',chargerNumber='" + charger.getNumber() + "' WHERE " + " titemNumber = '" + getNumber() + "' and titemBD = '" + getBD() + "'" ;
            cres += conn.update(sqlL);
        }
        else
        {
            //simplemente asignarlo
            sqlL = " Resumov SET chargerBD ='" + charger.getBD() + "',chargerNumber='" + charger.getNumber() + "' WHERE " + " titemNumber = '" + getNumber() + "' and titemBD = '" + getBD() + "'" ;
            cres += conn.update(sqlL);
        }
        return cres;
    }

    private String checkLinkCharger(Database conn) 
    {
        return charger.checkLink(conn);
    }
    
    public boolean linkBattery(Database conn) 
    {
        int cres = 0;
        String flLiked = checkLinkBattery(conn);
        
        String sqlL;
        cres = setLinkBattery(flLiked, cres, conn);
        
        switch(cres)
        {
            case 0:  
                System.err.println("No se enlazo la bateria [" + conn.getSQL() + "]");
                return false;
            case 1:
            case 2:
                //System.out.println("No se enlazo la bateria [" + conn.getSQL() + "]");
                return true;
        }
        return false;
    }
    
    private int setLinkBattery(String flLiked, int cres, Database conn) 
    {
        String sqlL;
        //System.out.println("Asignado bateria " + battery.getNumber() + " a " + getNumber());
        if(!flLiked.equals(""))
        {//Si esta asignado
            //1.- desasignar al anterior montacargas
            sqlL = " Resumov SET batteryBD = NULL, batteryNumber = NULL WHERE " + " titemNumber = '" + battery.getNumber() + "' and titemBD = '" + battery.getBD() + "'";
            cres += conn.update(sqlL);
            //2.- asignar al nuevo montacargas
            sqlL = " Resumov SET batteryBD ='" + battery.getBD() + "',batteryNumber='" + battery.getNumber() + "' WHERE " + " titemNumber = '" + getNumber() + "' and titemBD = '" + getBD() + "'" ;
            cres += conn.update(sqlL);
        }
        else
        {
            //simplemente asignarlo
            sqlL = " Resumov SET batteryBD ='" + battery.getBD() + "',batteryNumber='" + battery.getNumber() + "' WHERE " + " titemNumber = '" + getNumber() + "' and titemBD = '" + getBD() + "'" ;
            cres += conn.update(sqlL);
        }
        return cres;
    }

    private String checkLinkBattery(Database conn) 
    {
        return battery.checkLink(conn);
    }

    /**
     * @return the horomentro
     */
    public float getHoromentro() {
        return horomentro;
    }

    /**
     * @param horomentro the horomentro to set
     */
    public void setHoromentro(float horomentro) {
        this.horomentro = horomentro;
    }

    public boolean downBattery(Database conn) 
    {
        String sql = "SELECT batteryBD,batteryNumber FROM Forklift WHERE BD = ? AND number = ? AND batteryBD IS NOT NULL AND batteryNumber IS NOT NULL";
        
        PreparedStatement stmt;
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, getBD());
            stmt.setString(2, getNumber());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                battery = new Battery();
                battery.setBD(rs.getString("batteryBD"));
                battery.setNumber(rs.getString("batteryNumber"));
                return true;
            }
            else
            {
                battery = null;
                return false;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Forklift.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    public int reflex(Database conn) 
    {
        try 
        {
            String sql = "UPDATE Battery SET chargerBD = ?, chargerNumber = ? WHERE BD = ? and number = ?";
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, battery.getCharger().getBD());
            pstmt.setString(2, battery.getCharger().getNumber());
            pstmt.setString(3, battery.getBD());
            pstmt.setString(4, battery.getNumber());
            int count = pstmt.executeUpdate();
            sql = "UPDATE Forklift SET chargerBD = NULL, chargerNumber = NULL WHERE BD = ? and number = ?";
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, getBD());
            pstmt.setString(2, getNumber());
            count = count + pstmt.executeUpdate();
            return count;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Resumov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    
    public int unlinkBattery(Database conn) 
    {
        try 
        {
            String sql = "UPDATE Forklift SET batteryBD = NULL, batteryNumber = NULL WHERE BD = ? and number = ?";
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, getBD());
            pstmt.setString(2, getNumber());
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Battery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    /*
    public int linkBattery(MySQL conn) 
    {
        try 
        {
            String sql = "UPDATE Forklift SET batteryBD = ?, batteryNumber = ? WHERE BD = ? and number = ?";
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, battery.getBD());
            pstmt.setString(2, battery.getNumber());            
            pstmt.setString(3, getBD());
            pstmt.setString(4, getNumber());
            return pstmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Battery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    */

    public void downSucursal(Database conn) 
    {
        String sql = "SELECT suc FROM Resumov WHERE titemNumber = '" + getNumber() + "'";
        
        PreparedStatement stmt;
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                setSucursal(rs.getString("suc"));
            }            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Forklift.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
