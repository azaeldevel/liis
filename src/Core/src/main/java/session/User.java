
package session;

import SIIL.Server.Database;
import SIIL.core.MD5;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Azael
 */
public class User extends SIIL.Server.User
{  
    private static final String MYSQL_AVATAR_TABLE = "Users";    
    String password;        
    
    /**
     * @param database conexion a la base de datos
     * @param alias usuario
     * @param pw contraseña
     * @return null si falla la validacion de otra forna retoarn el objeto user correspondiente.
     * @throws SQLException 
     */
    public static User checkSession(Database database,String alias,String pw) throws SQLException
    {
        if(database == null)
        {
            return null;
        }
        String sql = "SELECT uID,alias,passwdMD5 FROM  " + MYSQL_AVATAR_TABLE + " WHERE alias ='" + alias + "' and passwdMD5 = '" + pw + "'";
        System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            User user = new User();
            user.pID = rs.getInt(1);
            return user;
        }
        
        return null;
    }
    public boolean upAlias(Database db,String alias) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(alias == null)
        {
            return false;
        }
                
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET alias='" + alias + "' WHERE uID=" + pID;
        Statement stmt = db.getConnection().createStatement();
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
    
    public boolean upPasswdMD5(Database db,String pw) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(pw == null)
        {
            return false;
        }
        this.password = null; 
        MD5 md5 = new MD5();
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET passwdMD5='" + md5.generate(pw) + "' WHERE uID=" + pID;
        Statement stmt = db.getConnection().createStatement();
        System.out.println(sql);
        if(stmt.executeUpdate(sql) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean insert(Database db,String n1,String ap,String alias) throws SQLException
    {
        clean();
        if(db == null)
        {
            return true;
        }
        
        if(n1  == null || ap == null )
        {
            return false;
        }
        
        boolean ret = super.insert(db, n1, ap);
        if(ret == false) return false;
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(BD,passwdMD5,uID,alias) VALUES('bc.tj','X'," + pID + ",'" + alias + "')";
        Statement stmt = db.getConnection().createStatement();
        System.out.println();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public User()
    {}
    
    public User(String alias) 
    {
        super(alias);
    }


    public void setPassword(char[] pass) 
    {
        password = generate(pass);
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public String generate(char[] strpass) 
    {        
        try 
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(new String(strpass).getBytes());
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
            //strpass = sb.toString();
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    /*public Throwable fill(Database db, String alias, String bd) 
    {
        return super.fill(db, alias, bd);
    }*/

    /*public Throwable download(Database db, Integer pid, String alias) 
    {
        password = null;
        return super.download(db,pid,alias);
    }*/

    public void downloadAlias(int aInt) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void clean() 
    {
        password = null;
    }
}
