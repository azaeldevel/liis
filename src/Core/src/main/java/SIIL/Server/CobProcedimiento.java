
package SIIL.Server;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author areyes
 */
public class CobProcedimiento 
{
    public static int insert(CobProcedimiento proc, MySQL conn)
    {                
        String sql = "CobProcedimientos(name,proc,emprID) VALUES ('" + proc.getName() + "',\"" + proc.getProc() + "\",'" + proc.getEmpresa().getNumber() + "')";
        return conn.insert(sql);
    }
    public static void fillCB(JComboBox cb, MySQL conn, Company empre) 
    {
        String sql = "SELECT id,name,proc,emprID FROM CobProcedimientos WHERE emprID ='" + empre.getNumber() + "'";
        Statement stmt = null;
        
        try 
        {            
            stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            cb.removeAllItems();
            CobProcedimiento item = new CobProcedimiento();
            item.setId(0);
            cb.addItem(item);
            while(rs.next())
            {
                item = new CobProcedimiento();
                item.setEmpresa(empre);
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setProc(rs.getString("proc"));
                cb.addItem(item);
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(CobProcedimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void fillTXA(JTextArea txa, CobProcedimiento item) 
    {
        txa.setText(item.getProc());
    }

    public static int delete(CobProcedimiento item, MySQL conn) 
    {
        String sql = " CobProcedimientos WHERE id=" + item.getId();
        return conn.delete(sql);
    }
    
    private int id;
    private String name;
    private String proc;
    private Company empresa;
    
    @Override
    public String toString()
    {
        if(id==0) return "Seleccione..";      
        return name;
    }
    /**
     * @return the id
     */
    public int getId() 
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) 
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the proc
     */
    public String getProc() {
        return proc;
    }

    /**
     * @param proc the proc to set
     */
    public void setProc(String proc) 
    {
        this.proc = proc;
        
    }

    public int updateProc(MySQL conn)
    {
        String sql = "CobProcedimientos SET proc =\"" + proc + "\" WHERE id=" + id;        
        int res = conn.update(sql);
        try 
        {
            if(res == 1)
            {
                conn.getConnection().commit();
            }
            else
            {
                conn.getConnection().rollback();
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(CobProcedimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
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

}
