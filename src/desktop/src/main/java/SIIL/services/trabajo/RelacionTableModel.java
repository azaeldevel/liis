
package SIIL.services.trabajo;

import SIIL.Server.Database;
import SIIL.services.Trabajo;
import SIIL.services.Trabajo.Sheet;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class RelacionTableModel extends AbstractTableModel  
{
    public static final String MYSQL_AVATAR_TABLE = "ServicesTrabajo";    
    
    private static final int COLUMN_SA      = 0;
    private static final int COLUMN_SERVQUO = 1;
    private static final int COLUMN_CLIENTNUM  = 2;
    private static final int COLUMN_CLIENTNAM  = 3;
    private static final int COLUMN_MECHANIC    = 4;
    private static final int COLUMN_BRIEF       = 5;
    private static final int COLUMN_STATUS      = 6;
    private static final int COLUMN_DATE        = 7;
    
    private List<String> columnNames;
    private List<Trabajo> list;
    private String whereSQL;
    private String tableSQL;
    private String lastSearch;
    private boolean activeDate;
    private Date now;
    
    public RelacionTableModel()
    {
        list = new ArrayList<>();
        columnNames = new ArrayList<>();
        columnNames.add("S.A.");
        columnNames.add("Ser. Cot.");
        columnNames.add("Cliente");
        columnNames.add("Nombre");
        columnNames.add("Mecánico");
        columnNames.add("Descripción");
        columnNames.add("Estado");  
    }
    
    /**
     * @return the whereSQL
     */
    public String getWhereSQL() {
        return whereSQL;
    }

    /**
     * @return the tableSQL
     */
    public String getTableSQL() {
        return tableSQL;
    }

    /**
     * @return the lastSearch
     */
    public String getLastSearch() {
        return lastSearch;
    }

    /**
     * @return the now
     */
    public Date getNow() {
        return now;
    }

    /**
     * @param now the now to set
     */
    public void setNow(Date now) {
        this.now = now;
    }
        
    public enum ReadMode
    {
        LOAD,
        REFRESH,
        RELOAD,
        NOREAD,
        SEARCH
    }
    
    
       
    public RelacionTableModel(List<Trabajo> list)
    {
        addDate();
        this.list = list;
    }
    
    
    public void search(Database database,String search) throws SQLException
    {
        addDate();
        String table = MYSQL_AVATAR_TABLE;
        String sql = "SELECT id FROM " + table;  
        whereSQL =  " WHERE " + search;
        whereSQL = whereSQL + " ORDER BY id ASC ";
        tableSQL = table;
        sql = sql + whereSQL; 
        lastSearch = sql;
        System.out.println(sql);
        download(database, sql);
    }

    private void addDate() 
    {
        activeDate = true;
        columnNames.add("Fecha");
        fireTableStructureChanged();
    }
    
    public void load(Database database,int limit,Sheet sheet,Date fecha) throws SQLException
    {
        String table = MYSQL_AVATAR_TABLE;
        String sql = "SELECT id FROM " + table ;  
        whereSQL = " WHERE (flag IS NULL OR flag != 'D') AND sheet = ";
        if(sheet == Sheet.CAMPO)
        {
            whereSQL += "'CM'";
        }
        else if(sheet == Sheet.TALLER)
        {
            whereSQL += "'TLL'";
        }
        else if(sheet == Sheet.GRUA)
        {
            whereSQL += "'GR'";
        }
        else if(sheet == Sheet.MTTO)
        {
            whereSQL += "'MAN'";
        }        
        if(sheet == Sheet.CAMPO)
        {
            whereSQL += " AND fhToDo = '" + fecha.toString() + "'";
        }
        else
        {
            whereSQL += " AND (state !=  14 AND state !=  15  AND state !=  16) ";
        }
        whereSQL = whereSQL + " ORDER BY id ASC";
        if(limit > 0)
        {
            whereSQL = whereSQL + " LIMIT " + limit;
        }
        tableSQL = table;
        whereSQL = whereSQL;
        sql = sql + whereSQL; 
        //System.out.println(sql);         
        download(database, sql);  
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_SA:
                if(list.get(rowIndex).getSA() != null)
                {
                    if(list.get(rowIndex).getSA().getID() > 1) 
                    {
                        return list.get(rowIndex).getSA().getFolio();
                    }
                }
                return "";
            case COLUMN_SERVQUO:
                if(list.get(rowIndex).getQuotedService() != null) return list.get(rowIndex).getQuotedService().getFolio();
                return "";
            case COLUMN_CLIENTNUM:
                return list.get(rowIndex).getCompany().getNumber();
            case COLUMN_CLIENTNAM:
                return list.get(rowIndex).getCompany().getName();
            case COLUMN_MECHANIC:
                if(list.get(rowIndex).getMechanic() != null) return list.get(rowIndex).getMechanic().toString();
            case COLUMN_BRIEF:
                return list.get(rowIndex).getBrief();
            case COLUMN_STATUS:
                return list.get(rowIndex).getState().getName();
        }
        if(activeDate)
        {
            if(columnIndex == COLUMN_DATE)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
                return sdf.format(list.get(rowIndex).getFhToDo());
            }
        }
        return null;
    }    
    
    public List<Trabajo> getData()
    {
        return list;
    }
    
    public void download(Database database, String sql) throws SQLException 
    {
        ResultSet rs = database.query(sql);    
        list = new ArrayList<>();
        while(rs.next())
        {
            list.add(new Trabajo(rs.getInt(1)));            
        }
        refresh(database);
    }
    
    public void refresh(Database database) throws SQLException
    {
        for(Trabajo tr : list)
        {
            if(tr.download(database))
            {
                tr.getState().download(database);
                tr.getCompany().download(database);
                if(tr.getSA() != null) tr.getSA().download(database);
                tr.getMechanic().fill(database, tr.getMechanic().getpID(), SIIL.servApp.cred.getBD());
                if(tr.downQuotedService(database))
                {
                    tr.getQuotedService().downFolio(database.getConnection());
                    tr.getQuotedService().downSerie(database.getConnection());
                }             
                if(activeDate)
                {
                    tr.downDate(database);
                }
            }
        }
        fireTableDataChanged();
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
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        return String.class;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames.get(columnIndex);
    }

    public Trabajo getValueAt(int row)
    {
        return list.get(row);
    }
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
}

