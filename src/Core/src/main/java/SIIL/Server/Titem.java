
package SIIL.Server;

import java.sql.Connection;
import session.Credential;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import process.Return;

/**
 * @version 0.2
 * @author areyes
 */
@Deprecated
public class Titem extends Item
{
    @Deprecated
    public boolean checkExistTit(Database conn)
    {
        String sql;
        if(getNumber() != null && getBD() != null)
        {
            try 
            {
                sql = "SELECT number FROM Titem WHERE number = '" + getNumber() + "' and BD='" + getBD() + "'";

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
        else if(getMarca() != null && getModelo() != null && getSerie() != null)
        {
            try 
            {
                sql = "SELECT number FROM Titem_Resolved WHERE marca like '" + getMarca() + "' and modelo like '" + getModelo() + "' and serie like '" + getSerie() + "'";

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
    public boolean complete(Database conn, boolean full) 
    {
        try 
        {
            String sql;
        
            if(full)
            {
                sql = "SELECT marca,modelo,serie,number,name from Titem_Resolved WHERE number = '" + getNumber() + "' and BD = '" + getBD() + "' and marca is not NULL and modelo is not NULL and serie is not NULL";
            }
            else
            {
                sql = "SELECT marca,modelo,serie,number,name from Titem_Resolved WHERE number = '" + getNumber() + "' and BD = '" + getBD() + "'";
            }
            Statement stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                if(rs.getString("marca") != null)
                {
                    setMarca(rs.getString("marca"));
                }
                else
                {
                    setMarca(null);
                }
                if(rs.getString("modelo") != null)
                {
                    setModelo(rs.getString("modelo"));
                }
                else
                {
                    setModelo(null);
                }
                if(rs.getString("serie") != null)
                {
                    setSerie(rs.getString("serie"));
                }
                else
                {                    
                    setSerie(null);
                }
                if(rs.getString("name") != null)
                {
                    setName(rs.getString("name"));
                }
                else
                {                    
                    setName(null);
                }
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
        return false;
    }
    
    @Deprecated
    public boolean complete(Database conn) 
    {
        try 
        {
            String sql = "SELECT marca,modelo,serie,number,name from Titem_Resolved WHERE number = '" + getNumber() + "' and BD = '" + getBD() + "' and marca is not NULL and modelo is not NULL and serie is not NULL";
            
            Statement stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                if(rs.getString("marca").length() >0)
                {
                    setMarca(rs.getString("marca"));
                }
                else
                {
                    setMarca(null);
                }
                if(rs.getString("modelo").length() > 0 )
                {
                    setModelo(rs.getString("modelo"));
                }
                else
                {
                    setModelo(null);
                }
                if(rs.getString("serie").length() > 0)
                {
                    setSerie(rs.getString("serie"));
                }
                else
                {                    
                    setSerie(null);
                }
                if(rs.getString("name").length() > 0)
                {
                    setName(rs.getString("name"));
                }
                else
                {                    
                    setName(null);
                }
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
        return false;
    }
    /**
     * 
     * @param conn
     * @return
     * @throws Exception 
     */
    @Deprecated
    public int insertTit(Database conn)throws Exception
    {
        //if(checkExist(conn)) return 0;
        int count = super.insertIt(conn);
        if(!checkExistTit(conn))
        {
            String fields = "";
            String values = "";
            
            fields = fields + "BD";
            values = values + "'" + getBD() + "'";
            
            fields = fields + ",number";
            values = values + ",'" + getNumber() + "'";
            
            if(getItemClass() != null)
            {
                fields = fields + ",class";
                values = values + ",'" + getItemClass() + "'";            
            }
            else
            {
                throw new Exception("Se agrego el elemento " + getNumber()+ " como ancestro y la clase es " + this.toString());
            }
            if(marca != null)
            {
                fields += ",marca";
                values = values + ",'" + marca + "'";
            }
            if(modelo != null)
            {
                fields += ",modelo";
                values = values + ",'" + modelo + "'";
            }
            if(serie != null)
            {
                fields += ",serie";
                values = values + ",'" + serie + "'";
            }

            String sql = " Titem(" + fields + " ) VALUES(" + values + ")";
            
            return conn.insert(sql);
        }        
        return count;        
    } 
    
    private String marca;
    private String modelo;
    private String serie;
    //private String itemClass;

    /**
     * @return the marca
     */
    @Deprecated
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    @Deprecated
    public void setMarca(String marca) {
        if(marca == null)
        {
            this.marca = null;
        }
        else
        {
            this.marca = marca.trim();
        }
    }

    /**
     * @return the modelo
     */
    @Deprecated
    public String getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    @Deprecated
    public void setModelo(String modelo) {
        if(modelo == null)
        {
            this.modelo = null;
        }
        else
        {
            this.modelo = modelo.trim();
        }
    }

    /**
     * @return the serie
     */    
    @Deprecated
    public String getSerie() {
        return serie;
    }

    /**
     * @param serie the serie to set
     */    
    @Deprecated
    public void setSerie(String serie) {
        if(serie == null)
        {
            this.serie = null;
        }
        else
        {
            this.serie = serie.trim();
        }
    }


    /*
    *
    */
    @Deprecated
    public int update(Database conn) 
    {
        String values = "";
        if(values.length() > 0)
        {
            if(marca != null)
            {
                values = values + "marca='" + marca + "'";
            }
        }
        else
        {
            if(marca != null)
            {
                values = values + "marca='" + marca + "'";
            }
        }
        if(values.length() > 0)
        {
            if(modelo != null)
            {
                values = values + ",modelo='" + modelo + "'";
            }
        }
        else
        {
            if(modelo != null)
            {
                values = values + "modelo='" + modelo + "'";
            }
        }
        
        if(values.length() > 0)
        { 
            if(serie != null)
            {
                values = values + ",serie='" + serie + "'";
            }
        }
        else
        { 
            if(serie != null)
            {
                values = values + "serie='" + serie + "'";
            }
        }
        String sql = " Titem SET " + values + " WHERE BD = '" + getBD() + "' and number ='" + getNumber() + "'" ;
        return conn.update(sql);
    }
    
    @Deprecated
    public boolean checkRequest(Database conn,String suc) 
    {
        String sql = "SELECT suc FROM Titem_Resolved WHERE (suc is null or suc = '" + suc + "') and (BD = '" + getBD() + "' and number = '" + getNumber() + "')";
        ResultSet rs = null;
        try 
        {
            Statement stmt = (Statement) conn.getConnection().createStatement();
            rs = stmt.executeQuery(sql);
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
            Logger.getLogger(Titem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    
    @Deprecated
    public Throwable download(Database db, String BD, String number)
    {
        String sql = "SELECT marca,modelo,serie FROM Titem WHERE BD = ? AND number = ?";
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
                marca = rs.getString(1);
                modelo = rs.getString(2);
                serie = rs.getString(3);
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
    public Throwable download(Database db, Credential credential, String number)
    {
        String sql = "SELECT marca,modelo,serie FROM Titem WHERE BD = ? AND number = ?";
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
                marca = rs.getString(1);
                modelo = rs.getString(2);
                serie = rs.getString(3);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Titem.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
        
        return null;
    }
    

    /**
     * 
     * @param connection
     * @return 
     * @throws java.sql.SQLException 
     */
    @Deprecated
    public Return download(Database connection) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT marca,modelo,serie FROM Titem WHERE BD = '" + getBD() + "' AND number = " + getNumber();
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            marca = rs.getString(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            clean();
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + getNumber());
        }        
    }

    @Deprecated
    private void clean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
