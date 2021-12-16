package SIIL.Server;

import session.Credential;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version 1.0
 * @since Noviembre 4, 2014
 * @author Azael
 */
public class Item extends Objeto
{
    public boolean checkExistIt(Database conn)
    {
        String sql;
        if(getNumber() != null && getBD() != null)
        {
            try 
            {
                sql = "SELECT number FROM Item WHERE number = '" + getNumber() + "' AND BD='" + getBD() + "'";

                Statement stmt = (Statement) conn.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                System.out.println(sql);
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
                Logger.getLogger(Forklift.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            return false;
        }        
        return false;
    }

    @Deprecated
    public int updateSucursal(Database conn) 
    {
        String sql = " Item SET suc ='" + sucursal + "' WHERE BD='" + getBD() + "' and number='" + getNumber() + "'";
        return conn.update(sql);
    }
    
    @Deprecated    
    public int insertIt(Database conn) throws Exception
    {
        int count = super.insertObj(conn);
        if(!checkExistIt(conn))
        {
            String fields;
            String values;
            if( super.getBD() == null)
            {
                throw new Exception("Se deve de especificar la Base de Datos");
            }
            else
            {
                fields = "BD";
                values = "'" + super.getBD() + "'";
            }

            if(super.getNumber() != null)
            {
                fields = fields + ",number";
                values = values + ",'" + super.getNumber() + "'";
            }
            if(name != null)
            {
                fields = fields + ",name";
                values = values + ",'" + name + "'";
            }
            String sql = " Item(" + fields + " ) VALUES(" + values + ")";
            return conn.insert(sql);
        }
        return count;
    }
    
    private String name;
    @Deprecated
    private String sucursal;

    /**
     * @return the sucursal
     */
    @Deprecated
    public String getSucursal() {
        return sucursal;
    }

    /**
     * @param sucursal the sucursal to set
     */
    @Deprecated
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
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
    @Deprecated
    public void setName(String name) 
    {
        this.name = name;
    }

    @Deprecated
    Throwable download(Database db, String BD, String number) 
    {
        String sql = "SELECT name FROM Item WHERE BD = ? AND number = ?";
        try 
        {
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, BD);
            stmt.setString(2, number);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                Throwable ret = super.download(db,BD,number);
                if(ret != null) return ret;
                name = rs.getString(1);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Titem.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
        
        return null;
    }
    
    @Deprecated
    Throwable download(Database db, Credential credential, String number) 
    {
        String sql = "SELECT name FROM Item WHERE BD = ? AND number = ?";
        try 
        {
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, credential.getBD());
            stmt.setString(2, number);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                Throwable ret = super.download(db,credential,number);
                if(ret != null) return ret;
                name = rs.getString(1);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Titem.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
        
        return null;
    }

}
