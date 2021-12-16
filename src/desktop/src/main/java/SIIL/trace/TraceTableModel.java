
package SIIL.trace;

import SIIL.Server.Database;
import SIIL.Server.MySQL;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael
 */
public class TraceTableModel extends AbstractTableModel
{
    private static final int COLUMN_ID      = 0;
    private static final int COLUMN_USER    = 1;
    private static final int COLUMN_DATE    = 2;
    private static final int COLUMN_BRIEF   = 3;
    
    private String[] columnNames = {"ID", "Usuario", "Fecha", "Descripción"};
    private List<SIIL.trace.Trace> listTrace;

    public TraceTableModel() 
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
        
        if (dbserver.getConnection() == null) 
        {
            System.err.println("Error en la Conexion de BD.");
        }
        try 
        {            
            listTrace = SIIL.trace.Trace.fill(dbserver);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(TraceTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public int getRowCount() 
    {
        if(listTrace == null)
        {
            return 0;
        }
        return listTrace.size();
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.length;
    }
        
    /**
     * 
     * @param rowIndex 
     * @param columnIndex Si es -1 retorna el objeto en la lista corespondiente a @code rowIndex, mayor que cero indica la columna que se desea.
     * @return 
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        SIIL.trace.Trace trace = listTrace.get(rowIndex);
        Object returnValue = null;
        
        if(columnIndex == -1)
        {
            return trace;
        }
        
        switch (columnIndex) 
        {
        case COLUMN_ID:
            returnValue = trace.getID();
            break;
        case COLUMN_USER:
            returnValue = trace.getUser().toString();
            break;
        case COLUMN_DATE:
            //Fecha de folio
            if(trace.getDate() != null)
            {                                    
                returnValue = trace.getDate();
            }            
            else
            {
                returnValue = null;
            }
            break;
        case COLUMN_BRIEF:
            returnValue = trace.getBrief();
            break;
        default:
            throw new IllegalArgumentException("Invalid column index");
        }
         
        return returnValue;
    }
    
    public void setValueAt(SIIL.trace.Trace value, int rowIndex) 
    {
        listTrace.set(rowIndex, value);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        if (listTrace == null || listTrace.isEmpty()) 
        {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public String getColumnName(int columnIndex)
    { 
        return columnNames[columnIndex];
    }
    
    public void search(String search, int count, Database conn) throws SQLException
    {
        listTrace = SIIL.trace.Trace.fillSearch(conn,search,count);
        fireTableDataChanged();
    }
}
