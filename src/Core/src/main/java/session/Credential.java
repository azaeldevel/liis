
package session;

import SIIL.Server.Database;
import SIIL.artifact.AmbiguosException;
import SIIL.artifact.DeployException;
import SIIL.core.Office;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael
 */
public class Credential 
{
    private boolean valid;
    protected session.User u;
    //private String suc;
    private Office office;
    private String BD;
    
    
    public void setUser(session.User usr)
    {
        u =usr;
    }
    
    public session.User getUser()
    {        
        return u;
    }
           
    
    /**
     * 
     * @return 
     */
    public boolean isValid()
    {
        return valid;
    }
    
    @Override
    public String toString()
    {
        return u.toString();
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
    
    @Deprecated
    public boolean check(String BD, String alias, char[] password) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, SQLException
    {
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
        Database dbserver = null;
        
        serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        dbserver = new Database(serverConfig);
        
        String strpass = new String(password);
        strpass = convertMD5(strpass);
        try
        {
            ResultSet res = null;
            PreparedStatement statement = null;
            String strSql = "SELECT uID,alias,suc,nameN1,nameAP FROM Users_Resolved WHERE alias = '" + alias + "' and passwdMD5='" + strpass + "' and BD ='" + BD + "' and active='Y'";
            System.out.println(strSql);
            statement = dbserver.getConnection().prepareStatement(strSql);            
            res = statement.executeQuery(strSql);
            if(res.next())
            {
                this.BD = BD;
                u = null;
                //u.setAlias(alias);
                //u.setBD(BD);
                //u.setN1(res.getString("nameN1"));
                //u.setAP(res.getString("nameAP"));
                office = u.getOffice();
                valid = true;
                return true;
            }
            else
            {
                alias = null;
                valid = false;
                return false;
            }
        }
        catch(NullPointerException nex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, nex);
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        dbserver.close();
        return false;
    }

    public boolean acces(Database conn, String policy) 
    {
        try
        {
            ResultSet res = null;
            PreparedStatement statement = null;
            String strSql = "SELECT policy FROM AccesTable_Resolved WHERE acces = 'Y' and alias = '" + u.getAlias() + "' and polName='" + policy + "'";
            //System.out.println(strSql);
            statement = conn.getConnection().prepareStatement(strSql);            
            res = statement.executeQuery(strSql);
            if(res.next())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(NullPointerException | SQLException nex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, nex);
        }
        
        return false;
    }

    /**
     * @return the BD
     */
    public String getBD() {
        return BD;
    }

    /**
     * @return the suc
     */
    public String getSuc() {
        return office.getCode();
    }
    
    public Office getOffice()
    {
        return office;
    }
    
    public String filterSucursal(Database conn) 
    {
        //Si es de la matriz
        if(getSuc().equals("bc.tj"))
        {
            //Retorna acceso para todas las sucursales
            return "(suc='bc.tj' OR suc='bc.ens' OR suc='bc.mx')";
        }
        else
        {
            return "(suc = '" + getSuc() + "')";
        }            
    }

    public boolean check(User user,String phase,MysqlDataSource ds)
    {
        try
        {        
            Database conn = new Database(phase,ds);
            if(conn.getConnection() == null)
            {
                JOptionPane.showMessageDialog(null,
                    "Conexion a Servidor Invalida",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                    );
                return false;
            }
        
            ResultSet res = null;
            PreparedStatement statement = null;
            String strSql = "SELECT pID FROM Users_Resolved WHERE alias = '" + user.getAlias() + "' and passwdMD5='" + user.getPassword() + "' and BD ='" + user.getBD() + "' and active='Y'";
            System.out.println(strSql);
            statement = conn.getConnection().prepareStatement(strSql);            
            res = statement.executeQuery(strSql);
            if(res.next())
            {
                this.BD = user.getBD();                
                u = user;
                Throwable th = u.download(conn,res.getInt(1),user.getAlias());
                //u.setpID(res.getInt("uID"));
                //u.setN1(res.getString("nameN1"));
                //u.setAP(res.getString("nameAP"));
                //u.setSucursal(res.getString("suc"));
                //u.setOffice(res.getString("office"));
                //u.setDepartment(res.getString("department"));
                office = u.getOffice();                
                valid = true;
                conn.close();
                return true;
            }
            else
            {
                valid = false;
                return false;
            }
        } 
        catch(DeployException | AmbiguosException | ClassNotFoundException | NullPointerException | SQLException nex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, nex);
        }
        
        return false;
    }
}
