
package process;

import SIIL.Server.Database;
import SIIL.core.TypeMail;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class Mail 
{
    private static final String MYSQL_AVATAR_TABLE = "ProcessMailer";
    public enum Flag
    {
        PENDING,
        SENDED,
        ERROR
    }
    private int id;
    private InternetAddress[] to;
    private InternetAddress[] cc;
    private InternetAddress[] bcc;
    private String text;
    private String type;
    private String subject;
    private List<MimeBodyPart> attach;
    private Flag flag;
    
    public String optimizeList(String to,String cc) throws AddressException
    {
        InternetAddress[] arTo = convertToArray(to);
        List<String> lTo = new ArrayList<>();
        for(InternetAddress inet : arTo)
        {
            if(!lTo.contains(inet.getAddress()))lTo.add(inet.getAddress());
        }
        
        InternetAddress[] arCc = convertToArray(cc);
        List<String> lCc = new ArrayList<>();
        for(InternetAddress inet : arCc)
        {
            if(!lTo.contains(inet.getAddress()) && !lCc.contains(inet.getAddress()))lCc.add(inet.getAddress());
        }
        
        return convertToString(lCc);
    }
    
    public String optimizeList(String to) throws AddressException
    {
        InternetAddress[] arTo = convertToArray(to);
        List<String> lTo = new ArrayList<>();
        for(InternetAddress inet : arTo)
        {
            if(!lTo.contains(inet.getAddress()))lTo.add(inet.getAddress());
        }
        return convertToString(lTo);
    }
    
    public static String convertToString(InternetAddress[] ls)
    {
        String str = "";
        if(ls.length > 0) str += ls[0];
        for(int i = 1; i < ls.length; i++)
        {
            str += "," + ls[i];
        }        
        return str;
    }
        
    public static String convertToString(List<String> ls)
    {
        String str = "";
        if(ls.size() > 0) str += ls.get(0);
        for(int i = 1; i < ls.size(); i++)
        {
            str += "," + ls.get(i);
        }        
        return str;
    }
    
    public Return upFlag(Database connection, Flag flag) throws SQLException 
    {
        if(getID() < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null");
        }
        if(flag == null)
        {
            return new Return<>(false,"flag is null.");
        }
        this.flag = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET flag=";
        if(flag == Flag.PENDING)
        {
            sql += "'P' " + getID();
        }
        else if(flag == Flag.SENDED)
        {
            sql += "'S' ";
        }
        else if(flag == Flag.ERROR)
        {
            sql += "'E' ";
        }
        sql += ", fhSended = '" + new java.sql.Date(connection.getDateToday().getTime()) + "'";
        sql += " WHERE id=" + getID();
        java.sql.Statement stmt = connection.getConnection().createStatement();
        return new Return<>(true, stmt.executeUpdate(sql));
    }
    
    public Boolean downFlag(Database db) throws SQLException
    {
        String sql = "SELECT flag FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getID();
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            if(rs.getString(1).equals("P"))
            {
                flag = Flag.PENDING;
            }
            else if(rs.getString(1).equals("S"))
            {
                flag = Flag.SENDED;
            }
            else
            {
                return false;
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static int getCount(Database database) throws SQLException
    {
        String sql = "SELECT COUNT(*) FROM " + MYSQL_AVATAR_TABLE + " WHERE `type` = 'orserv' AND flag ='P'";
        Statement stmt = database.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        //System.out.println(sql);
        if(rs.next())
        {
            return rs.getInt(1);
        }
        else
        {
            return 0;
        }
    }
    
    public boolean insert(Database connection, InternetAddress[] to, InternetAddress[] cc, InternetAddress[] bcc, String text, String type,String subject) throws SQLException, AddressException
    {
        String strto = "";
        //optimizeLists(to,cc,bcc);
        if(to.length > 0)
        {
            strto = to[0].getAddress();
        }
        for(int i = 1; i < to.length; i++)
        {
            strto += "," + to[i].getAddress();
        }
        
        String strcc = "";
        if(cc != null)
        {
            if(cc.length > 0)
            {
                strcc = cc[0].getAddress();
            }
            for(int i = 1; i < cc.length; i++)
            {
                strcc += "," + cc[i].getAddress();
            }
        }
        else
        {
            strcc = null;
        }
        
        String strbcc = "";
        if(bcc != null)
        {
            if(bcc.length > 0)
            {
                strbcc = bcc[0].getAddress();
            }
            for(int i = 1; i < bcc.length; i++)
            {
                strcc += "," + bcc[i].getAddress();
            }
        }
        else
        {
            strbcc = null;
        }
        return insert(connection, strto, strcc, strbcc, text, type, subject);
    }
    
    @Deprecated 
    public boolean insert(Database connection, List<String> to, List<String> cc, List<String> bcc, String text, String type,String subject) throws SQLException, AddressException
    {
        String strto = "";
        //optimizeLists(to,cc,bcc);
        if(to.size() > 0)
        {
            strto = to.get(0);
        }
        for(int i = 1; i < to.size(); i++)
        {
            strto += "," + to.get(i);
        }
        
        String strcc = "";
        if(cc != null)
        {
            if(cc.size() > 0)
            {
                strcc = cc.get(0);
            }
            for(int i = 1; i < cc.size(); i++)
            {
                strcc += "," + cc.get(i);
            }
        }
        else
        {
            strcc = null;
        }
        
        String strbcc = "";
        if(bcc != null)
        {
            if(bcc.size() > 0)
            {
                strbcc = bcc.get(0);
            }
            for(int i = 1; i < bcc.size(); i++)
            {
                strcc += "," + bcc.get(i);
            }
        }
        else
        {
            strbcc = null;
        }
        return insert(connection, strto, strcc, strbcc, text, type, subject);
    }
    
    public Boolean delete(SIIL.Server.Database database) throws SQLException
    {
        if(database == null)
        {
            return false;
        }
        String sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = database.getConnection().createStatement();
        //System.out.println(sql);
        int afected = stmt.executeUpdate(sql);
        if(afected == 1)
        {
            return true;
        }
        return false; 
    }
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downSubject(Connection connection) throws SQLException
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT subject FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            subject = rs.getString(1);
            return true;
        }
        else
        {
            subject = null;
            return false;
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downType(Connection connection) throws SQLException
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT `type` FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            type = rs.getString(1);
            return true;
        }
        else
        {
            type = null;
            return false;
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean download(Connection connection) throws SQLException, AddressException
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT `to`,cc,bcc,`text`,`type`,subject FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            to = convertToArray(rs.getString(1));
            if(rs.getString(2)!= null) cc = convertToArray(rs.getString(2));
            if(rs.getString(3) != null) bcc = convertToArray(rs.getString(3));
            text = rs.getString(4);
            type = rs.getString(5);
            subject = rs.getString(6);
            return true;
        }
        else
        {
            clean();
            return false;
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downText(Connection connection) throws SQLException
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT `text` FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            text = rs.getString(1);
            return true;
        }
        else
        {
            text = null;
            return false;
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downBCC(Connection connection) throws SQLException, AddressException
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT bcc FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            bcc = convertToArray(rs.getString(1));
            return true;
        }
        else
        {
            bcc = null;
            return false;
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downCC(Connection connection) throws SQLException, AddressException
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT cc FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            cc = convertToArray(rs.getString(1));
            return true;
        }
        else
        {
            cc = null;
            return false;
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downTO(Connection connection) throws SQLException, AddressException
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT `to` FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            to = convertToArray(rs.getString(1));
            return true;
        }
        else
        {
            to = null;
            return false;
        }
    }

    public boolean selectLast(Connection connection, TypeMail type) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE flag = 'P' AND `type` = " ;
        if(type == TypeMail.ORSERV)
        {
            sql += "'orserv'";
        }
        else if(type == TypeMail.FACTURA)
        {
            sql += "'factura'";
        }
        sql += " ORDER BY id DESC LIMIT 1";
        Statement stmt = connection.createStatement();
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return true;
        }
        else
        {
            this.id = -1;
            return false;
        }
    }
        
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean selectLast(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE `type` = 'orserv' AND flag = 'P' ORDER BY id DESC LIMIT 1";
        System.out.println(sql);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return true;
        }
        else
        {
            this.id = -1;
            return false;
        }
    }
    
    public Mail(int id)
    {
        this.id = id;
    }
    
    public boolean insert(Database connection, String to, String cc, String bcc, String text, String type,String subject) throws SQLException, AddressException
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(to == null ||  text == null)
        {
            throw new InvalidParameterException("Es necesaria la lita de corrros To y el comentario.");
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (`to`,cc,bcc,`text`,type,subject,fhCreated) VALUES('" + optimizeList(to) + "'";
        if(cc != null)
        {
            sql += ",'" + optimizeList(to,cc) + "'";
        }
        else
        {
            sql += ",NULL";
        }
        if(bcc != null)
        {
            sql += ",'" + bcc + "'";
        }
        else
        {
            sql += ",NULL";
        }
        sql += ",'" + text + "','" + type + "','" + subject + "','" + new java.sql.Date(connection.getDateToday().getTime()) + "')";
        Statement stmt = connection.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return false;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            id = rs.getInt(1);
            return true;
        }
        else
        {
            id = -1;
            return false;
        }
    }

    private void clean() 
    {
        id = -1;
        to = null;
        cc = null;
        bcc = null;
        text = null;
    }

    /**
     * @return the id
     */
    public int getID() 
    {
        return id;
    }

    /**
     * @return the to
     */
    public InternetAddress[] getTO() 
    {
        return to;
    }

    /**
     * @return the cc
     */
    public InternetAddress[] getCC() 
    {
        return cc;
    }

    /**
     * @return the bcc
     */
    public InternetAddress[] getBCC() 
    {
        return bcc;
    }

    /**
     * @return the text
     */
    public String getText() 
    {
        return text;
    }

    /**
     * @return the type
     */
    public String getType() 
    {
        return type;
    }

    /**
     * @return the subject
     */
    public String getSubject() 
    {
        return subject;
    }

    public static void validateList(String string) throws AddressException 
    {
        InternetAddress[] arr = InternetAddress.parse(string);
        for(InternetAddress ar : arr)
        {
            ar.validate();
        }
    }
        
    public static InternetAddress[] convertToArray(String string) throws AddressException 
    {
        InternetAddress[] arr = InternetAddress.parse(string);
        for(InternetAddress ar : arr)
        {
            ar.validate();
        }        
        return arr;
    }

    /**
     * Elimina duplicidad de correos.
     * @param to
     * @param cc
     * @param bcc 
     */
    private void optimizeLists(List<String> to, List<String> cc, List<String> bcc) 
    {
        Map<String,String> toMain = new HashMap<>();
        for(int i = 0; i < to.size(); i++) 
        {
            toMain.put(to.get(i), to.get(i));
        }
        to.clear();
        to.addAll(toMain.values());
        
        Map<String,String> ccMain = new HashMap<>();
        if(cc != null)
        {
            for(int i = 0; i < cc.size(); i++) 
            {
                if(!toMain.containsKey(cc.get(i))) ccMain.put(cc.get(i), cc.get(i));
            }
            cc.clear();
            cc.addAll(ccMain.values());
        }
        
        if(bcc != null && cc != null)
        {
            Map<String,String> bccMain = new HashMap<>();
            for(int i = 0; i < bcc.size(); i++) 
            {
                if(!toMain.containsKey(bcc.get(i)) && !ccMain.containsKey(bcc.get(i))) bccMain.put(bcc.get(i), bcc.get(i));
            }
            bcc.clear();
            bcc.addAll(bccMain.values());
        }
    }
}
