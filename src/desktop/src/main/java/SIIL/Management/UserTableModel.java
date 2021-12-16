
package SIIL.Management;

import SIIL.Server.Database;
import SIIL.purchase.cr.Factura;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import process.Return;

/**
 *
 * @author areyes
 */
public class UserTableModel extends AbstractTableModel
{

    private static final int COLUMN_PNAME  = 0;
    private static final int COLUMN_UName  = 1;
    private static final int COLUMN_PWD  = 2;
    private static final int COLUMN_ACTIV = 3;
     private static final int COLUMN_OFICE = 4;
    private String[] columnNames = {"Nombre", "Username", "Contraseña","Activo","Oficina"};
    private java.util.ArrayList<SIIL.Server.User> list;
    
    public boolean download(Database dbserver)
    {
        for(SIIL.Server.User u : list)
        {
            try
            {
                Return ret = u.down(dbserver);
                if(ret.isFlag())
                {
                }
                else
                {
                    //JOptionPane.showMessageDialog(null, ret.getMessage() + " \n alias = " + u.getAlias(), "Fallo 2 la descarga de la informacion", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(SQLException ex)
            {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Fallo 3 la descarga de la informacion", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        fireTableDataChanged();
        return true;
    }
    
    public UserTableModel()
    {
        list =  new ArrayList<>();
    }
    
    
    void clear() 
    {
        list.clear();
    }

    void set(java.util.ArrayList<SIIL.Server.User> list)
    {
        this.list = list;
        fireTableStructureChanged();
    }
    void add(SIIL.Server.User user) 
    {
        list.add(user);
    }

    @Override
    public int getRowCount() 
    {
        return list.size();
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if(list.get(rowIndex).getuID() < 1) throw new RuntimeException("La instancia no esta Inicializada.");
        
        switch(columnIndex)
        {
            case COLUMN_PNAME:
                return list.get(rowIndex);
            case COLUMN_UName:
                return list.get(rowIndex).getAlias();
            case COLUMN_PWD:
                    if(list.get(rowIndex).getPasswdMD5() == null )
                    {
                        return "No Asignado";
                    }
                    else if(list.get(rowIndex).getPasswdMD5().length() == 0)
                    {
                      return "No Asignada";  
                    }
                    else if(list.get(rowIndex).getPasswdMD5().length()> 0)
                    {
                        return "Asignada";
                    }
            case COLUMN_ACTIV:
                if(list.get(rowIndex).getActive()){
                    return "Activo";
                }else{
                    return "Desactivado";  
                }
            case COLUMN_OFICE:
                if(list.get(rowIndex).getuID() > 0) {
                    if(list.get(rowIndex).getOffice() != null){
                        return list.get(rowIndex).getOffice().getName();
                    }                        
                }
        }
        
        return "";
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        if (list == null || list.isEmpty()) 
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
}
