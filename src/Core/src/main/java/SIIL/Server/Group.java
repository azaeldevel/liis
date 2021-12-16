package SIIL.Server;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/**
 *
 * @author areyes
 */
public class Group 
{

    public static void Load(MySQL c,JComboBox cb) 
    {
        String sql = "SELECT gID,name,comment,parent FROM Groups ORDER BY name";
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) c.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            Group item = new Group();
            item.setgID(-1);
            item.setName("Seleccione..");
            cb.addItem(item);
            while(rs.next())
            {
                item = new Group();
                item.setgID(rs.getInt("gID"));
                item.setName(rs.getString("name"));
                item.setCommnet(rs.getString("comment"));
                item.setParent(rs.getInt("parent"));
                cb.addItem(item);
            }
            rs.close();
            stmt.close();
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        catch(Exception e)
        {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        finally
        {
            //finally block used to close resources
            try
            {
                if(stmt != null) stmt.close();
            }
            catch(SQLException se2)
            {
                ;
            }// nothing we can do
        }//end try 
    }

    private int gID;
    private String name;
    private String commnet;
    private int parent;

    @Override
    public String toString()
    {
        return name;
    }
    /**
     * @return the dID
     */
    public int getgID() 
    {
        return gID;
    }

    /**
     * @param dID the dID to set
     */
    public void setgID(int dID) 
    {
        this.gID = dID;
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
    public void setName(String name) 
    {
        this.name = name;
    }

    /**
     * @return the commnet
     */
    public String getCommnet() 
    {
        return commnet;
    }

    /**
     * @param commnet the commnet to set
     */
    public void setCommnet(String commnet) 
    {
        this.commnet = commnet;
    }

    /**
     * @return the parent
     */
    public int getParent() 
    {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(int parent) 
    {
        this.parent = parent;
    }

    public static int Insert(MySQL conn, Group gr) 
    {
            if(gr.gID==0)
            {
                String fields = "";
                String values = "";
                
                fields = fields + "name";
                values = values + "'" + gr.name + "'";
                
                fields = fields + ",comment";
                values = values + ",\"" + gr.commnet + "\"";
                
                if(gr.parent > 0)
                {
                    fields = fields + ",parent";
                    values = values + "," + gr.parent;
                }
                String sql = " Groups(" + fields + ") VALUES(" + values + ")";
                return conn.insert(sql);
            }
            return 0;
    }

    public void clean() 
    {
        gID=-1;
        name="";
        commnet="";
        parent=-1;
    }
}
