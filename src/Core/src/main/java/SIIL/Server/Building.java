
package SIIL.Server;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author areyes
 */
public class Building {

    public static void fillTB(JTable tb, MySQL conn, Company empre)
    {
        String sql = "SELECT tipo,name,address FROM Buildings WHERE emprID = '" + empre.getNumber() + "'";
        
        Statement stmt;            
        DefaultTableModel modelo=(DefaultTableModel) tb.getModel(); 
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Object[] row;
            while(rs.next())
            {
                row = new Object[3];
                row[0] = rs.getString("name");
                row[1] = rs.getString("address");
                char t = rs.getString("tipo").charAt(0);
                if(t == 'M')
                {
                    row[2] = "Matriz";
                }
                else if(t == 'S')
                {
                    row[2] = "Sucursal";
                }
                modelo.addRow(row);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Building.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int insert(Building office, MySQL conn)
    {        
        String sql = " Buildings(name,address,tipo,emprID) VALUES('" + office.getName() + "',\"" + office.getAddress() + "\",'" + office.getType() + "','" + office.getEmpresa().getNumber() + "')";
        return conn.insert(sql);
    }

    public static int findID(Company empre, String name, MySQL conn) 
    {
        String sql = "SELECT id FROM Buildings WHERE emprID ='" + empre.getNumber() + "' and name = '" + name + "'";
        Statement stmt = null;
                
        try 
        {            
            stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);            
            if(rs.next())
            {
                int res = rs.getInt("id");
                if(rs.next())
                {
                    return -1;
                }
                else
                {
                    return res;
                }
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Building.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }

    public static int delete(int id, MySQL conn) 
    {
        String sql = " Buildings WHERE id = " + id;
        return conn.delete(sql);
    }

    public static int update(Building office, MySQL conn) 
    {
        String sql = " Buildings SET name='" + office.getName() + "', tipo='" + office.getType() + "', address=\"" + office.getAddress() + "\" WHERE id =" + office.getId();
        return conn.update(sql);
    }
    
    private String name;
    private String address;
    private Company empresa;
    private char type;
    private int id;
    String BD;
    String clave;

    public Building() 
    {}
    public Building(String bd, String numb, String suc) 
    {
        BD = bd;
        empresa = new Company();
        empresa.setBD(BD);
        empresa.setNumber(numb);
        clave = suc;
    }
    
    /**
     * @return the name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the empresa
     */
    public Company getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Company empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the type
     */
    public char getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(char type) {
        this.type = type;
    }

    public Building getOffice(int id, MySQL conn) 
    {
        String sql = "SELECT id,name,tipo,address,emprID FROM Buildings WHERE id =" + id;
        Statement stmt = null;
                
        try 
        {            
            stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);            
            if(rs.next())
            {
                Building office = new Building();
                office.setId(id);
                office.setName(rs.getString("name"));
                office.setAddress(rs.getString("address"));
                if(rs.getString("tipo")!=null) office.setType(rs.getString("tipo").charAt(0));                
                office.setType('M');
                if(rs.next())
                {
                    return null;
                }
                else
                {
                    return office;
                }
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Building.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public boolean isMatriz(MySQL conn) 
    {
        String sql = "SELECT name FROM Buildings WHERE tipo ='M' and compBD='" + empresa.getBD() + "' and compNumber = '" + empresa.getNumber() + "' and clave = '" + clave + "'";
        Statement stmt = null;
        
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
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
            Logger.getLogger(Building.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
