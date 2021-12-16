
package core.calendar;

import SIIL.Server.Database;
import SIIL.services.Trabajo;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azael Reyes
 */
public class Marking implements IMarking
{
    private Database db;
        

    /**
     * @return the db
     */
    public Database getDB() {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDB(Database db) {
        this.db = db;
    }

    
    @Override
    public boolean isMarkWhite(Object object) 
    {
        if(object instanceof Date)
        {
            try 
            {
                return Trabajo.have(db,(Date)object,1);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Marking.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public boolean isMarkRed(Object object) 
    {
        if(object instanceof Date)
        {
            try 
            {
                return Trabajo.have(db,(Date)object,2);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Marking.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean isMarkBlue(Object object) 
    {
        if(object instanceof Date)
        {
            try 
            {
                return Trabajo.have(db,(Date)object,3);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Marking.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    

    @Override
    public boolean isMarkPurple(Object object) 
    {
        if(object instanceof Date)
        {
            try 
            {
                return Trabajo.have(db,(Date)object,4);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Marking.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public boolean isMarkYellow(Object object) 
    {
        if(object instanceof Date)
        {
            try 
            {
                return Trabajo.have(db,(Date)object,5);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Marking.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        else
        {
            return false;
        }
    }


    @Override
    public boolean isMarkGreen(Object object) 
    {
        if(object instanceof Date)
        {
            try 
            {
                return Trabajo.have(db,(Date)object,6);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Marking.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public boolean isMarkGray(Object object) 
    {
        if(object instanceof Date)
        {
            try 
            {
                return Trabajo.have(db,(Date)object);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Marking.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        else
        {
            return false;
        }
    }

}
