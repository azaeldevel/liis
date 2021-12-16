
package SIIL.Server;

import SIIL.client.sales.Enterprise;
import session.Credential;
import java.sql.Statement;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import process.Return;

/**
 * @version 0.3
 * @author areyes
 */
public class Company extends Objeto
{
    protected static final String MYSQL_AVATAR_TABLE = "Companies";
    private static final String MYSQL_AVATAR_TABLE_BACKWARD_BD = "bc.tj";
    
    protected String name;
    private int id;
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return select(Database connection,String number) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return(false,"Connection is null.");
        }
        String sql = "SELECT number,id FROM " + MYSQL_AVATAR_TABLE + " WHERE number = '" + number + "'";
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            BD = MYSQL_AVATAR_TABLE_BACKWARD_BD;
            this.number = rs.getString(1);
            id = rs.getInt(2);
            return new Return(true);
        }
        else
        {
            return new Return(false);
        }
    }
    
    public void copy(Company company)
    {
        this.id = company.id;
        this.BD = company.BD;
        this.name = company.name;
        this.number = company.number;                
    }
    
    public static ArrayList<Enterprise> search(String text, int limit, Database conn,String BD) 
    {
        String sql = "SELECT number,name,id,rfc FROM Companies WHERE  (number LIKE '%" + text + "%' or name LIKE '%" + text + "%') and BD = '" + BD + "' ORDER BY name ASC LIMIT " + limit;
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();  
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Enterprise> arr = new ArrayList<>();
            Enterprise empresa;
            while(rs.next())
            {
                empresa = new Enterprise(rs.getInt("id"));
                empresa.download(conn);
                arr.add(empresa);
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
    
    public String toString()
    {
        if(id == -1000)
        {
            return "Seleccione..";
        }
        else
        {
            return  number + " - " + name;
        }
    }
    
    public Boolean download(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }        
        
        String sql = "SELECT number,name,rfc FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.number = rs.getString(1);
            this.name = rs.getString(2);
            return true;
        }
        else
        {
            this.id = 0;
            this.number = null;
            this.name = null;
            return false;
        }
    }
    
    public Company(int id)
    {
        this.id = id;
    }

    public static boolean checkExistCo(Database conn, String number)
    {
        String sql;
        if(number != null)
        {
            try 
            {
                sql = "SELECT number FROM Companies WHERE number = '" + number + "' and BD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "'";
                Statement stmt = (Statement) conn.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                //System.out.println(sql);
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
    
    public boolean checkExistCo(Database conn)
    {
        String sql;
        if(getNumber() != null && getBD() != null)
        {
            try 
            {
                sql = "SELECT number FROM Companies WHERE number = '" + getNumber() + "' and BD='" + getBD() + "'";
                Statement stmt = (Statement) conn.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                //System.out.println(sql);
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
     * @param number
     * @param name
     * @return Returna wrapper for the reault;
     * @throws SQLException 
     */
    public Return<Integer> insert(Connection connection,String number,String name) throws SQLException //throws SQLException
    {
        if(getID() > 0)
        {
            return new Return<>(false,"Allacated tine un ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(BD,number,name) VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + number + "','" + name + "')";
        Statement stmt = (Statement) connection.createStatement();
        int affected = 0;

        affected = stmt.executeUpdate(sql);
        
        if(affected != 1)
        {
            clean();
            return new Return<>(false,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        return new Return<>(true, affected);  
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    @Override
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
    @Override
    public Return selectRandom(Database connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT number,id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            BD = MYSQL_AVATAR_TABLE_BACKWARD_BD;
            number = rs.getString(1);
            id = rs.getInt(2);
            return new Return<>(true);
        }
        else
        {
            return new Return<>(false);
        }
    }
    
    public static void fillCB(JComboBox clients,int limit,String input, MySQL conn) 
    {
        String sql = "SELECT numb, name FROM Companies ORDER BY name WHERE numb LIKE %'" + input + "'% or name LIKE %'" + input + "'%";
        Statement stmt = null;
        try
        {
            System.out.println(sql);
            stmt = (Statement) conn.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            Company itemCliente = new Company();
            itemCliente.setNumber(null);
            itemCliente.setName("Seleccione");
            clients.addItem(itemCliente);
            while(rs.next())
            {
                itemCliente = new Company();
                itemCliente.setNumber(rs.getString("numb"));
                itemCliente.setName(rs.getString("name"));
                clients.addItem(itemCliente);
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

    public static void fillCB(JComboBox clients, MySQL conn) 
    {
        String sql = "SELECT number, name FROM Companies ORDER BY name";
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            Company itemCliente = new Company();
            itemCliente.setNumber("");
            itemCliente.setName("Seleccione");
            clients.addItem(itemCliente);
            while(rs.next())
            {
                itemCliente = new Company();
                itemCliente.setNumber(rs.getString("bumber"));
                itemCliente.setName(rs.getString("name"));
                clients.addItem(itemCliente);
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

    public int insertCo(Database conn) throws Exception 
    {
        int count = super.insertObj(conn);
        if(!checkExistCo(conn))
        {
            String sql = " Companies(number,name,BD)VALUES ('" + getNumber() + "','" + getName() + "','" + getBD() + "')"; ;
            return conn.insert(sql);
        }
        return count;
    }

    public static ArrayList<Company> search(String text, int limit, MySQL conn,String BD) 
    {
        String sql = "SELECT number,name,id,rfc FROM Companies WHERE  (number LIKE '%" + text + "%' or name LIKE '%" + text + "%') and BD = '" + BD + "' ORDER BY name ASC LIMIT " + limit;
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();  
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Company> arr = new ArrayList<Company>();
            Company empresa;
            while(rs.next())
            {
                empresa = new Company(rs.getInt("id"));
                empresa.setName(rs.getString("name"));
                empresa.setNumber(rs.getString("number"));
                arr.add(empresa);
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

    public boolean check(Connection conn) 
    {
        String sql = "SELECT number,name FROM Companies WHERE number='" + getNumber() + "' and BD = '" + getBD() + "'";
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.createStatement();            
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next())
            {
                return true;
            }
            else
            {
                return false;
            }
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
        return false;
    }
    
    public Company() 
    {
        super();
    }
    
    public Company(String number, String name,String BD) 
    {
        super(number,BD);
        this.name = name;
    }

    /**
     * Asigna las varibles name,id y number
     * @param db
     * @param number
     * @return 
     */
    public boolean complete(Database db,String number) 
    { 
        String sql = "SELECT name,id,number FROM Companies WHERE number='" + number + "'";
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) db.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next())
            {
                setName(rs.getString("name"));
                this.number = rs.getString("number");
                id = rs.getInt(2);
                return true;
            }
            else
            {
                return false;
            }
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
        return false;
    }
    
    public boolean complete(Database db) 
    { 
        String sql = "SELECT name,id FROM Companies WHERE number='" + number + "'";
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) db.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next())
            {
                setName(rs.getString("name"));
                id = rs.getInt(2);
                return true;
            }
            else
            {
                return false;
            }
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
        return false;
    }
    
    /*@Deprecated 
    public boolean complete(Database conn) 
    { 
        String sql = "SELECT name FROM Companies WHERE number='" + getNumber() + "'";
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next())
            {
                setName(rs.getString("name"));
                return true;
            }
            else
            {
                return false;
            }
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
        return false;
    }*/

    public boolean valid(Database bd) 
    {
        return check(bd.getConnection());
    }

    public Throwable fill(Database db, Credential cred, String compNumber) 
    {        
        Throwable ret = super.fill(cred.getBD(),compNumber);
        if(ret != null) return ret;
        
        String query = "SELECT name,id FROM Companies WHERE BD = '" + cred.getBD() + "' and number='" + compNumber + "'";
        Statement stmt;  
        try 
        {
            stmt = (Statement) db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next())
            {
                name = rs.getString("name");
                id = rs.getInt(2);
                return null;
            }
            else
            {
                return null;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
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

    private void clean() 
    {
        name = null;
    }

    /**
     * @return the id
     */
    public int getID() {
        return id;
    }
    
}
