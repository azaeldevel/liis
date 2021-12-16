
package SIIL.services.trabajo;

import SIIL.Server.Database;
import SIIL.services.Trabajo;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
class Reload implements Runnable  
{
    private Relacion relacion;
    private Database dbserver; 
    private Timestamp last;
    private boolean enable;
    private boolean run;
        
    public Reload(Relacion relacion, boolean  autoLoad)
    {
        openDatabase(true);
        this.relacion = relacion;
        last = null;
        enable = autoLoad;
        this.run = true;
    }
    
    @Override
    public void run() 
    {
        boolean ret = false;
        while(run)
        {
            if(enable) openDatabase(true);            
            Timestamp last = null;
            try 
            {
                last = Trabajo.lastUpdated(dbserver, this.last);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Reload.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(last != null)
            {
                if(enable)
                {
                    relacion.reload(RelacionTableModel.ReadMode.REFRESH, false);
                    this.last = last;
                    closeDatabase();
                }
            }
            else
            {
                try
                {
                    synchronized(this)
                    {
                        if(enable)closeDatabase();
                        wait(5000);
                    }
                }
                catch (InterruptedException ex) 
                {
                    //Logger.getLogger(Reload.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void openDatabase(boolean  reclicleConextion)
    {
        try 
        {            
            SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            
            if(reclicleConextion)
            {
                if(dbserver != null)
                {
                    if(dbserver.getConnection().isValid(50))
                    {
                        return;
                    }
                    else
                    {
                        ;
                    }
                }
                else
                {
                    ;
                }
            }
            else
            {
                if(dbserver != null)
                {
                    if(!dbserver.getConnection().isClosed())dbserver.close();
                    dbserver = null;                    
                }
            }
            dbserver = null;
            dbserver = new Database(serverConfig);
        }
        catch (IOException | ClassNotFoundException | SQLException | ParserConfigurationException | SAXException ex) 
        {
            JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void closeDatabase() 
    {
        if(dbserver != null)
        {
            dbserver.close();
            dbserver = null;
        }
    }

    /**
     * @return the enable
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable the enable to set
     */
    public void setEnable(boolean enable) 
    {
        this.enable = enable;
    }

    /**
     * @return the run
     */
    public boolean isRun() {
        return run;
    }

    /**
     * @param run the run to set
     */
    public void setRun(boolean run) 
    {
        this.run = run;
        if(run == false)
        {
            closeDatabase();
        }
    }
    
    
}
