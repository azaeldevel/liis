
package SIIL.Server;

import java.security.InvalidParameterException;
import java.sql.Connection;
import session.Credential;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import process.Return;

/*
 * @version 1.0
 * @since Noviembre 6, 2014
 * @author Azael Reyes
 */
public class Objeto 
{
    private final String MYSQL_AVATAR_TABLE = "Object";
    private static final String MYSQL_AVATAR_TABLE_BACKWARD_BD = "bc.tj";

    
    public boolean checkExistObj(Database conn)
    {
        String sql;
        if(getNumber() != null && getBD() != null)
        {
            try 
            {
                sql = "SELECT number FROM Object WHERE number = '" + getNumber() + "' and BD='" + getBD() + "'";
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
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean selectLast(Database connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT number FROM  " + MYSQL_AVATAR_TABLE + " WHERE BD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' ORDER BY id DESC LIMIT 0, 1";
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            BD = MYSQL_AVATAR_TABLE_BACKWARD_BD;
            number = rs.getString(1);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return selectRandom(Database connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT number FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            BD = MYSQL_AVATAR_TABLE_BACKWARD_BD;
            number = rs.getString(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            return new Return(Return.Status.FAIL);
        }
    }
    
    public Objeto()
    {
        ;
    }
    public Objeto(String number,String BD)
    {
        this.number = number;
        this.BD = BD;
    }
    /**
     * @param itemClass the itemClass to set
     */
    public String getItemClass() 
    {
        String classObj = this.toString().split("@")[0];
        switch(classObj)
        {
            case "SIIL.Server.Forklift":
                return "forklift";
            case "SIIL.Server.Charger":
                return "charger";
            case "SIIL.Server.Battery":
                return "battery";
            case "SIIL.Server.Titem":
                return "titem";
            case "SIIL.Server.Item":
                return "item";
            case "SIIL.Server.Objeto":
                return "objeto";
        }
        return null;
    } 
    
    public static Objeto parse(String numb)
    {
        String cnNum = numb.trim();
        if(cnNum.matches("^09[0-9]{3,3}|9[0-9]{3,3}|M-[0-9]+"))
        {
            SIIL.Server.Forklift f = new SIIL.Server.Forklift();
            f.setNumber(cnNum);
            f.setTipo("Fk");
            return f;
        }
        else if(cnNum.matches("^C\\-[0-9]+$"))
        {
            SIIL.Server.Charger c = new SIIL.Server.Charger();
            c.setNumber(cnNum);
            c.setTipo("Ch");
            return c;
        }
        else if(cnNum.matches("^B\\-[0-9]+$"))
        {
            SIIL.Server.Battery b = new SIIL.Server.Battery();
            b.setNumber(cnNum);
            b.setTipo("Bt");
            return b;
        }
        else if(cnNum.equals("MINA"))
        {
            SIIL.Server.Titem t = new SIIL.Server.Titem();
            t.setNumber(cnNum);
            t.setTipo("Mn");
            return t;
        }
        else if(cnNum.matches("^[0-9]+$"))
        {
            int val = Integer.parseInt(cnNum);
            if(val > 998 && val < 8000)
            {
                SIIL.Server.Company comp = new SIIL.Server.Company();
                comp.setNumber(cnNum);
                comp.setTipo("Co");
                return comp;
            }
            else if(val > 299 && val < 400)
            {
                SIIL.Server.Charger c = new SIIL.Server.Charger();
                c.setNumber(cnNum);
                c.setTipo("Ch");
                return c;
            }
            else if(val > 405 && val < 418)
            {
                SIIL.Server.Battery b = new SIIL.Server.Battery();
                b.setNumber(cnNum);
                b.setTipo("Bt");
                return b;
            }
            else if(val > 699 && val < 997)
            {
                SIIL.Server.Forklift f = new SIIL.Server.Forklift();
                f.setNumber(cnNum);
                f.setTipo("Fk");
                return f;
            }
            else if(val > 9999 && val < 18000)
            {
                SIIL.Server.Forklift f = new SIIL.Server.Forklift();
                f.setNumber(cnNum);
                f.setTipo("Fk");
                return f;
            }
            else
            {
               return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    public int insertObj(Database conn) throws Exception
    {
        String fields;
        String values;
        if(!this.checkExistObj(conn))
        {
        if( BD == null)
        {
            throw new Exception("Se deve de especificar la Base de Datos");
        }
        else
        {
            fields = "BD";
            values = "'" + BD + "'";
        }
        
        if(number != null)
        {
            fields = fields + ",number";
            values = values + ",'" + number + "'";
        }
        
        if(tipo != null)
        {
            if(!tipo.isEmpty())
            {
                fields = fields + ",tipo";
                values = values + ",'" + tipo + "'";

            }
        }
        
        String sql = " Object(" + fields + ") VALUES(" + values + ")";
        return conn.insert(sql);
        }
        return 0;
    }    
    
    protected String number;
    protected String BD;
    private String tipo;
    
    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        if(number != null) 
        {
            this.number = number.trim();
        }
        else
        {
            this.number = null;
        }
    }

    /**
     * @return the BD
     */
    public String getBD() {
        return BD;
    }

    /**
     * @param BD the BD to set
     */
    public void setBD(String BD) {
        this.BD = BD;
    }

    /**
     * @return the strclass
     */
    public String getTipo(Database conn) 
    {
        String sql = "SELECT tipo FROM Object WHERE BD = ? AND number = ?";
        
        PreparedStatement stmt;
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, getBD());
            stmt.setString(2, getNumber());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                tipo = rs.getString("tipo");
                return tipo;
            }
            else
            {
                return null;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Forklift.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * @param strclass the strclass to set
     */
    public void setTipo(String strclass) {
        this.tipo = strclass;
    }

    Throwable fill(String bd, String num) 
    {
        BD = bd;
        number = num;
        return null;
    }

    @Deprecated
    Throwable download(Database db, Credential credential, String number)
    {
        String sql = "SELECT number FROM Item WHERE BD = ? AND number = ?";
        try 
        {
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, credential.getBD());
            stmt.setString(2, number);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                this.BD = credential.getBD();
                this.number = number;
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
    Throwable download(Database db, String BD, String number)
    {
        String sql = "SELECT number FROM Item WHERE BD = ? AND number = ?";
        try 
        {
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, BD);
            stmt.setString(2, number);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                this.BD = BD;
                this.number = number;
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Titem.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
        
        return null;
    }

    private void clean() 
    {
        BD = null;
        number = null;
        tipo = null;
    }
    
}
