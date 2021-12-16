package SIIL.Server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author areyes
 */
public class Login 
{
    private int uID;
    private String alias;
    MySQL conn;
    String BD;
    private String suc;
    
    public boolean valid()
    {
        if(uID > 0 && alias != null)
        {
            return true;
        }
        
        return false;
    }
    private String convertMD5(String strpass) 
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
    
    /**
     * @version 1.0.0
     * @since Octubre 17, 2014
     * @author Azael Reyes
     */
    public Login(String user, char[] password,MySQL c,String bd)
    {
        alias = user;
        conn = c;
        BD = bd;
        check(password);
    }
    
    public boolean check(char[] password)
    {
        String strpass = new String(password);
        strpass = convertMD5(strpass);
        try
        {
            ResultSet res = null;
            PreparedStatement statement = null;
            String strSql = "SELECT uID,alias,suc FROM Users_Resolved WHERE alias = '" + alias + "' and passwdMD5='" + strpass + "' and BD ='" + BD + "'";
            //System.out.println(strSql);
            statement = conn.getConnection().prepareStatement(strSql);            
            res = statement.executeQuery(strSql);
            if(res.next())
            {
                uID = res.getInt("uID");
                setSuc(res.getString("suc"));
                return true;
            }
            else
            {
                uID = -1;
                alias = null;
                return false;
            }
        }
        catch(NullPointerException nex)
        {
            return false;
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * @return the uID
     */
    public int getuID() {
        return uID;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    private boolean checkPolice(int uID, int police) 
    {
        try
        {
            ResultSet res = null;
            PreparedStatement statement = null;
            String strSql = "SELECT acces FROM UserPolicy where usID = " + uID + " and polID = " + police + " and acces ='Y'";
            //System.out.println(strSql);
            statement = conn.getConnection().prepareStatement(strSql);            
            res = statement.executeQuery(strSql);
            if(res.next())
            {
                return true;
            }
            else
            {
                uID = -1;
                alias = null;
                return false;
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return false;
    }

    /**
     * @return the suc
     */
    public String getSuc() {
        return suc;
    }

    /**
     * @param suc the suc to set
     */
    public void setSuc(String suc) {
        this.suc = suc;
    }

}
