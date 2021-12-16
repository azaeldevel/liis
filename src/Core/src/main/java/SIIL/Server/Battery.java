
package SIIL.Server;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author areyes
 */
public class Battery extends Titem
{
    private Charger charger;
    
    public boolean checkExistBt(Database conn)
    {
        String sql;
        if(getNumber() != null && getBD() != null)
        {
            try 
            {
                sql = "SELECT number FROM Battery WHERE number = '" + getNumber() + "' and BD='" + getBD() + "'";
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
        }
        else
        {
            return false;
        }
        
        return false;
    }

    public boolean downCharger(Database conn)
    {
        String sql = "SELECT chargerBD,chargerNumber FROM Battery WHERE BD = ? AND number = ? AND chargerBD IS NOT NULL AND chargerNumber IS NOT NULL";
        
        PreparedStatement stmt;
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, getBD());
            stmt.setString(2, getNumber());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                charger = new Charger();
                charger.setBD(rs.getString("chargerBD"));
                charger.setNumber(rs.getString("chargerNumber"));
                return true;
            }
            else
            {
                charger = null;
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
     * 
     * @param conn
     * @param fork
     * @return 
     */
    public boolean downCharger(Database conn, Forklift fork)
    {
        String sql = "SELECT chargerBD,chargerNumber FROM Forklift WHERE BD = ? and number = ?";
        
        PreparedStatement stmt;
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, fork.getBD());
            stmt.setString(2, fork.getNumber());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                charger = new Charger();
                charger.setBD(rs.getString("chargerBD"));
                charger.setNumber(rs.getString("chargerNumber"));
                return true;
            }
            else
            {
                charger = null;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Forklift.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    public Battery()
    {
        //super.setItemClass("battery");
    }
    
    
    public int insertBt(Database conn) throws Exception
    {
        //if(checkExist(conn)) return 0;
        int count  = super.insertTit(conn); 
        if(!checkExistBt(conn))
        {
            String sql = " Battery(BD,number) VALUES('" + getBD() + "','" + getNumber() + "')";
            return conn.insert(sql);
        }
        return count;
    }  

    public String checkLink(Database conn) 
    {
        try
        {//viasualizar si esta asignado a otro montacargas
            String sql = "SELECT batteryNumber FROM Resumov WHERE " + " batteryNumber = '" + getNumber() + "' and batteryBD = '" + getBD() + "'";
            
            Statement stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {                
                return rs.getString("batteryNumber");
            }
            else
            {
                return "";
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Forklift.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public int linkCharger(Database conn) 
    {
        try 
        {
            String sql = "UPDATE Battery SET chargerBD = ?, chargerNumber = ? WHERE BD = ? and number = ?";
            PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setString(1, charger.getBD());
            pstmt.setString(2, charger.getNumber());            
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

    public int unlinkCharger(Database conn) 
    {
        try 
        {
            String sql = "UPDATE Battery SET chargerBD = NULL, chargerNumber = NULL WHERE BD = ? and number = ?";
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
}
