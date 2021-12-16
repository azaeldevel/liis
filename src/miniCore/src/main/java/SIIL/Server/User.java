package SIIL.Server;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import process.Return;

/**
 *
 * @author areyes
 */
public class User extends Person
{
    private static final String MYSQL_AVATAR_TABLE = "Users";
    int uID;
    String passwdMD5;
    boolean active;
    
    /**
     * Asigna un ID al azar
     * @param database
     * @return
     * @throws SQLException 
     */
    public Boolean select(Database database, String alias) throws SQLException 
    {
        clean();
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT uID,alias FROM  " + MYSQL_AVATAR_TABLE + " WHERE alias ='" + alias + "'";
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            this.pID = rs.getInt(1);
            this.uID = rs.getInt(1);
            this.alias = rs.getString(2);
            return true;
        }
        else
        {
            this.pID = -1;
            this.alias = null;
            return false;
        }
    }
    
    public boolean upPasswdMD5(Database db,String passwd) throws SQLException
    {   
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET passwdMD5=md5('" + passwd + "') WHERE uID=" + uID;
        java.sql.Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        if(stmt.executeUpdate(sql) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }         
    }
    
    /**
     * Crea un  usuario a partir de la Persona pasada como parametro.
     * @param db
     * @param p
     * @param alias
     * @param passwd
     * @param active
     * @return
     * @throws SQLException 
     */
    public boolean  insert(Database db,Person p, String alias,String passwd, boolean active) throws SQLException
    {
        clean();
        if(uID > 0)
        {
            return false;
        }
        if(db == null)
        {
            return false;
        }
        
        if(alias  == null)
        {
            return false;
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(uID,alias,passwdMD5,active) VALUES('" + p.getpID() + "','" + alias + "',md5('" + passwd + "')";
        if(active)
        {
            sql = sql + ",'Y'";
        }
        else
        {
            sql = sql + ",'Y'";
        }
        sql = sql + ")";
        System.out.println(sql);
        java.sql.Statement stmt = db.getConnection().createStatement();
        if(stmt.executeUpdate(sql) == 1)
        {
            uID = p.pID;
            this.alias = alias;
            return true;
        }
        else
        {
            pID = -1;
            return false;
        }
    }
    public boolean getActive()
    {
        return active;
    }
    public String getPasswdMD5()
    {
        return passwdMD5;
    }
    
    public static ArrayList<User> listAllUsers(Database conn) 
    {
        String sql = "SELECT uID,alias FROM " + MYSQL_AVATAR_TABLE;
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            ArrayList<User> arr = new ArrayList<User>();
            while(rs.next())
            {
                User u = new User();
                u.uID = rs.getInt(1);
                u.pID = rs.getInt(1);
                u.alias = rs.getString(2);
                arr.add(u);
            }
            rs.close();
            stmt.close();
            return arr;
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
        return null;
    }
    
    public static String md5(String strpass) 
    {
        try 
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(strpass.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            strpass = sb.toString();
            return strpass;
        }
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static User valid(Database dbserver,String user, String pwd) throws SQLException
    {
        String strmd5 = md5(pwd);
        String sql = "SELECT uID FROM " + MYSQL_AVATAR_TABLE + " WHERE alias = '" + user + "' AND passwdMD5 = '" + strmd5 + "'";
        //System.out.println(sql);
        ResultSet rs = dbserver.select(sql);
        User u = null;
        if(rs.next())
        {
            int id = rs.getInt(1);
            if(rs.next())
            {
                return null;
            }
            else
            {
                u = new User();
                Throwable ret = u.download(dbserver, id, user);
                if(ret != null)
                {
                    return null;
                }
                else
                {
                    return u;
                }
            }
        }        
        return null;
    }
    /**
     * Asigna un ID al azar generalmente para propositos de pruebas
     * @param database
     * @return
     * @throws SQLException 
     */
    public Boolean selectRandom(Database database) throws SQLException 
    {
        clean();
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT uID,alias FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            this.pID = rs.getInt(1);            
            this.alias = rs.getString(2);
            return true;
        }
        else
        {
            this.pID = -1;
            this.alias = null;
            return false;
        }
    }
    
    @Override
    public Return download(Database db) throws SQLException 
    {
        return super.download(db);
    }
    
    public static void Load(Database c, JComboBox cb) 
    {
        String sql = "SELECT pID,nameN1,nameAP,alias FROM Users_Resolved ORDER BY alias";
        Statement stmt = null;
        try
        {
            stmt = (Statement) c.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            User item = new User();
            item.setpID(-1);
            item.setN1("Seleccione");
            item.setAP("..");
            cb.addItem(item);
            while(rs.next())
            {
                item = new User();
                item.setpID(rs.getInt("pID"));
                item.setN1(rs.getString("nameN1"));
                item.setAP(rs.getString("nameAP"));
                item.setAlias(rs.getString("alias"));
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

    public static ArrayList<User> listUsers(Database conn) 
    {
        String sql = "SELECT alias FROM " + MYSQL_AVATAR_TABLE;
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            ArrayList<User> arr = new ArrayList<User>();
            while(rs.next())
            {
                User u = new User();
                u.setAlias(rs.getString("alias"));
                arr.add(u);
            }
            rs.close();
            stmt.close();
            return arr;
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
        return null;
    }

    //private int uID;
    private String alias;

    public User() 
    {
        
    }
    public User(int aInt, String string) 
    {
        pID = aInt;
        alias = string;
    }
    public User(String string) 
    {
        alias = string;
    }

    /**
     * @return the uID
     */
    public int getuID() {
        return pID;
    }

    /**
     * @param uID the uID to set
     */
    /*public void setuID(int uID) {
        this.uID = uID;
    }*/

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    @Override
    public String toString()
    {
        String name = "";
        if(pID == -1000)
        {
            name = "Seleccione...";
        }
        else
        {
            if(N1 != null)
            {
                name += " " + N1;
            }
            if(Ns != null)
            {
                name += " " + Ns;
            }
            if(AP != null)
            {
                name += " " + AP;
            }
            if(AM != null)
            {
                name += " " + AM;
            }
            if(seudonimo != null)
            {
                name = name + " (" + seudonimo + ")";
            }
        }
        return name;
    }

    public Return down(Database conn) throws SQLException 
    {
        String sql = "SELECT uID,passwdMD5,active FROM " + MYSQL_AVATAR_TABLE +  " WHERE alias = '" + alias + "'";
        ResultSet rs;
          
        java.sql.Statement stmt = (Statement) conn.getConnection().prepareStatement(sql);
        rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            pID = rs.getInt(1);
            uID = rs.getInt(1);
            passwdMD5 = rs.getString(2);            
            if(rs.getString(3) != null)
            {
                //String strActive = new String(rs.getString(3).getBytes(ISO_8859_1));
                //String str = "N";
                //System.out.println("Active:" + rs.getString(3));
                if(rs.getString(3).equals("Y"))
                {
                    active = true;
                }
                else
                {
                    active = false;
                } 
            }
            else
            {
                active = false;
            }
        }
        return super.download(conn);
    }

    public Exception fill(Database db, String user, String bd)
    {
        String query = "SELECT pID,alias FROM Users_Resolved WHERE alias = '" + user + "'";
        ResultSet rs;
        Exception ret = null;
        
        java.sql.Statement stmt;
        try 
        {
            stmt = (Statement) db.getConnection().prepareStatement(query);
            rs = stmt.executeQuery(query);
            if(rs.next())
            {
                ret = super.fill(db, rs.getInt("pID"), bd);
                if(ret != null) return ret;
                this.alias = user;
                return null;
            }
            else
            {
                return new Exception("No se logro la identificacion del usuario como '" + user + "' ");
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
    }

    public Exception download(Database db, Integer pid,String alias) 
    {
        String query = "SELECT BD,uID,alias FROM Users WHERE uID = '" + pid + "' AND alias = '" + alias + "'" ;
        ResultSet rs;
        Exception ret = null;
        
        java.sql.Statement stmt;
        try 
        {
            stmt = (Statement) db.getConnection().prepareStatement(query);
            rs = stmt.executeQuery(query);
            if(rs.next())
            {
                this.alias = rs.getString(3);
                return ret = super.fill(db, rs.getInt(2), rs.getString(1));
            }
            else
            {
                return new Exception("No se logro la identificacion del usuario como '" + pid + "' ");
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
    }

    private void clean() 
    {
        alias = null;
    }
}
