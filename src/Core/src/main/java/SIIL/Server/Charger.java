
package SIIL.Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;

/**
 *
 * @author areyes
 */
public class Charger extends Titem
{
    public Charger()
    {
        //super.setItemClass("charger");
    }
    
    public boolean checkExistCh(Database conn)
    {
        String sql;
        if(getNumber() != null && getBD() != null)
        {
            try 
            {
                sql = "SELECT number FROM Charger WHERE number = '" + getNumber() + "' and BD='" + getBD() + "'";
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
    
    public int insertCh(Database conn) throws Exception
    {
        int count = super.insertTit(conn);
        if(!checkExistCh(conn))
        {
        String sql = " Charger( BD,number ) VALUES('" + getBD() + "','" + getNumber() + "')";
        return conn.insert(sql);
        }
        return count;
    }

    public String checkLink(Database conn) 
    {
        try
        {//viasualizar si esta asignado a otro montacargas
            String sql = "SELECT chargerNumber FROM Resumov WHERE " + " chargerNumber = '" + getNumber() + "' and chargerBD = '" + getBD() + "'";
            
            Statement stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                
                return rs.getString("chargerNumber");
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

}
