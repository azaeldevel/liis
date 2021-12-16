
package core.bobeda;

import SIIL.Server.Database;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.services.grua.Resumov;
import core.FailResultOperationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class CRUDTableModel extends AbstractTableModel 
{
    //public static final String MYSQL_AVATAR_TABLE = "BobedaBusiness_Resolved";    
    
    private static final int COL_FOLIO      = 0;
    private static final int COL_FOLIOTABLE = 1;
    private static final int COL_FH             = 2;
    private static final int COL_CLIENT_NUMBER  = 3;
    private static final int COL_CLIENT_NAME    = 4;
    private static final int COL_BRIEF      = 5;
    private static final int COL_COUNT      = 6;

    private ArrayList<String> columnNames;
    private List<InVault> list;
    private String sql;
    private Table table;
    private core.Mode mode;
    
    /*public void listing(Database dbserver,Mode mode,Table table,int limit) throws SQLException
    {        
        this.table = table;
        String tbSQL = "";
        String whereSQL = "";
        String limitSQL  = " LIMIT ";
        String orderSQL = " Order BY fhFolio DESC";
        if(table == Table.SERVICEQUOTATION)
        {
            tbSQL = "BobedaOrcom_Resolved";
        }
        else if(table == Table.RESUMOV)
        {            
            tbSQL = "BobedaResumov_Resolved";
        }
        String sql = "SELECT inBobedaID,inTableID FROM " + tbSQL ;
        sql +=  orderSQL + limitSQL + "  " + limit ;
        System.out.println(sql);
        this.sql = sql;
        download(dbserver, sql);
    }*/
    
    public void searchTriple(Database dbserver,core.Mode mode,Table table,String inTable,String inBobeda,String enterprise) throws SQLException
    {        
        this.table = table;
        this.mode = mode;
        String tbSQL = "";
        String whereSQL = "";
        String orderSQL = " Order BY fhFolio DESC";
        if(core.Mode.SELECTION == mode)//
        {
            tbSQL = "BobedaBusiness_Resolved";
            if(enterprise != null)
            {
                whereSQL = whereSQL + " compNumber = '" + enterprise +"'"; 
            }
            if(inBobeda != null)
            {
                if(whereSQL.length() > 0) whereSQL += " AND ";
                whereSQL = whereSQL + " inBobedaFolio LIKE '%" + inBobeda +"%'"; 
            }
        }
        else if(core.Mode.VIEW == mode && table == Table.SERVICEQUOTATION)
        {
            tbSQL = "BobedaOrcom_Resolved";
            if(inTable != null)
            {
                if(whereSQL.length() > 0) whereSQL += " OR ";
                whereSQL = whereSQL + " inTableFolio LIKE '%" + inTable +"%'"; 
            }
            if(inBobeda != null)
            {
                if(whereSQL.length() > 0) whereSQL += " OR ";
                whereSQL = whereSQL + " inBobedaFolio LIKE '%" + inBobeda +"%'"; 
            }
        }
        else if(core.Mode.VIEW == mode && table == Table.RESUMOV)
        {
            tbSQL = "BobedaResumov_Resolved";
            if(inTable != null)
            {
                if(whereSQL.length() > 0) whereSQL += " OR ";
                whereSQL = whereSQL + " inTableFolio LIKE '%" + inTable +"%'"; 
            }
            if(inBobeda != null)
            {
                if(whereSQL.length() > 0) whereSQL += " OR ";
                whereSQL = whereSQL + " inBobedaFolio LIKE '%" + inBobeda +"%'"; 
            }
        }
        else
        {
            tbSQL = "BobedaBusiness_Resolved";
            if(enterprise != null)
            {
                whereSQL = whereSQL + " compNumber LIKE '%" + enterprise +"%'"; 
            }
            if(inBobeda != null)
            {
                if(whereSQL.length() > 0) whereSQL += " AND ";
                whereSQL = whereSQL + " inBobedaFolio LIKE '%" + inBobeda +"%'"; 
            }             
        }
        String sql = "SELECT inBobedaID,inTableID FROM " + tbSQL ;
        if(inTable != null | inBobeda != null | enterprise != null)
        {
            sql += " WHERE " + whereSQL;
        }
        sql += " " + orderSQL;
        System.out.println(sql);
        this.sql = sql;
        download(dbserver, sql);
    }
    
    
    public CRUDTableModel()
    {
        columnNames = new ArrayList<>();
        columnNames.add("Folio en Bobeda");
        columnNames.add("Folio en Tabla");
        columnNames.add("Fecha");
        columnNames.add("Número");
        columnNames.add("Nombre");
        columnNames.add("Descripcion");        
    }
    
    @Override
    public int getRowCount() 
    {
        if(list != null)
        {
            return list.size();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.size();
    }
    
    public InVault getValueAt(int rowIndex)
    {
        return list.get(rowIndex);
    }

    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COL_FOLIO:
                return list.get(rowIndex).businessDocument.getFolio();
            case COL_FOLIOTABLE:
                if(list.get(rowIndex).vaultable == null)
                {
                    return "";
                }
                else
                {
                    return list.get(rowIndex).vaultable.getFolioInTable();
                }
            case COL_FH:
            {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String fh = df.format(list.get(rowIndex).businessDocument.getBobeda().getFhFolio());
                return fh;
            }
            case COL_CLIENT_NUMBER:
                return list.get(rowIndex).businessDocument.getEnterprise().getNumber();
            case COL_CLIENT_NAME:
                return list.get(rowIndex).businessDocument.getEnterprise().getName();
            case COL_BRIEF:
                return list.get(rowIndex).businessDocument.getBobeda().getBrief();
            default:
                return null;                
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        return String.class;
    }

    public void reload(Database database) throws SQLException
    {
        ResultSet rs = database.query(this.sql);    
        list = new ArrayList<>();
        while(rs.next())
        {
            InVault inVault = new InVault();
            inVault.businessDocument = new Business(new Vault(rs.getInt(1)));
            if(core.Mode.SELECTION == mode)
            {
                inVault.vaultable = null;
            }
            else if(Table.SERVICEQUOTATION == table)
            {
                inVault.vaultable = new ServiceQuotation(rs.getInt(2));
            }
            else if(Table.RESUMOV == table)
            {
                inVault.vaultable = new Resumov(rs.getInt(2));
            }
            else
            {
                inVault.vaultable = null;            
            }
            list.add(inVault);            
        }
        refresh(database);
    }
    
    private void download(Database database, String sql) throws SQLException 
    {
        ResultSet rs = database.query(sql);    
        list = new ArrayList<>();
        while(rs.next())
        {
            InVault inVault = new InVault();
            inVault.businessDocument = new Business(new Vault(rs.getInt(1)));
            if(core.Mode.SELECTION == mode)
            {
                inVault.vaultable = null;
            }
            else if(Table.SERVICEQUOTATION == table)
            {
                inVault.vaultable = new ServiceQuotation(rs.getInt(2));
            }
            else if(Table.RESUMOV == table)
            {
                inVault.vaultable = new Resumov(rs.getInt(2));
            }
            else
            {
                inVault.vaultable = null;            
            }
            list.add(inVault);            
        }
        refresh(database);
    }

    public void refresh(Database database) throws SQLException 
    {
        for(InVault inVault : list)
        {
            inVault.businessDocument.download(database);
            if(inVault.vaultable != null) inVault.vaultable.downloadDataVault(database);
        }
        fireTableDataChanged();
    }    

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames.get(columnIndex);
    }
    
}
