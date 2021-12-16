
package SIIL.services.trabajo;

import SIIL.Server.Database;
import SIIL.services.Trabajo;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import process.State;

/**
 *
 * @author Azael Reyes
 */
public class Assistant extends core.Assistant
{
    public void checkQuotedPending() throws SQLException
    {
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            JOptionPane.showMessageDialog(null,
                "Fallo importacion.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        //buscar las cotizaciones en la relacion que ya tiene cotizacion.
        State asigned = new State(-1);
        asigned.select(dbserver, State.Steps.RT_ASIGNED);
        State attending = new State(-1);
        attending.select(dbserver, State.Steps.RT_ATTENDING);
        State end = new State(-1);
        end.select(dbserver, State.Steps.RT_END);
        
        String sqlRLQ = "SELECT id FROM " + Trabajo.MYSQL_AVATAR_TABLE + " WHERE quotedService > 0 AND state != " + end.getID() + " ORDER BY fhToDo ASC";//listar todas la que esta en cotizado
        ResultSet rs = dbserver.query(sqlRLQ);
        ArrayList<Trabajo> lsT = new ArrayList<>();
        if(rs != null)
        {
            while(rs.next())
            {
                Trabajo t = new Trabajo(rs.getInt(1));
                if(t.download(dbserver))
                {
                    if(t.getQuotedService().downState(dbserver))
                    {
                        if(t.getQuotedService().getState().download(dbserver))
                        {
                            if(t.getQuotedService().getState().getOrdinal() == 5)
                            {//ya esta en almacen
                                lsT.add(t);                        
                            }
                        }
                    }
                }
            }
        }
        /*JInternalFrame inter = new JInternalFrame("Relacion de Trabajo - Cotizadas Pendientes",true,true);
        SIIL.services.trabajo.Relacion rel = new SIIL.services.trabajo.Relacion(inter,lsT);
        inter.setContentPane(rel);
        inter.setSize(rel.getPreferredSize());
        inter.setMaximizable(true);
        SIIL.servApp.getInstance().getDesktopPane().add(inter);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - rel.getSize().width/2;
        int y = 50;
        inter.setLocation(x, y);
        inter.setVisible(true);  */     
    }

    @Override
    public void run() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
