
package SIIL.Server;


import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import java.sql.Statement;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @version 3.0
 * @since Octubre 31, 2014
 * @author Azael Reyes
 */
public class MySQL 
{
   // JDBC driver name and database URL
   private String JDBC_DRIVER;  
   private String DB_URL;
   //  Database credentials
   private String USER;
   private String PASS;
   //
   protected  Connection connection = null;
   private java.sql.Statement stmt;
   private String strSQL;

    public Vector<Factura> getListForDay() 
    {
        String sql = "SELECT emprID FROM CobranzaPeriodos_Resolved WHERE type='EF' ORDER BY cnName ASC";
        //System.out.println(sql);
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) connection.createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            Factura fact;
            Vector<Factura> vFact = new Vector<Factura>();
            while(rs.next())
            {
                fact = new Factura();
                vFact.add(fact);
                fact.setEmprID(rs.getString("emprID"));
                //System.out.println("Adding " + rs.getString("emprID"));                
            }
            rs.close();
            stmt.close();
            return vFact;
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
      
    /*
    * Retorna una lista de los clientes que cuyo PC es del dia que indicado
    */
    public Vector<Factura> getListForDay(Day d, PC pc) 
    {
        String sql = "SELECT emprID FROM CobranzaPeriodos_Resolved WHERE dia = '" + d + "' and type='" + pc + "' ORDER BY cnName ASC";
        //System.out.println(sql);
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) connection.createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            Factura fact;
            Vector<Factura> vFact = new Vector<Factura>();
            while(rs.next())
            {
                fact = new Factura();
                vFact.add(fact);
                fact.setEmprID(rs.getString("emprID"));
                //System.out.println("Adding " + rs.getString("emprID"));                
            }
            rs.close();
            stmt.close();
            return vFact;
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
    
    public void loadcbClient(JComboBox cbClient) 
    {
        String sql = "SELECT cnNumber, cnName FROM Empresas ORDER BY cnName";
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) connection.createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            Company itemCliente = new Company();
            itemCliente.setNumber("");
            itemCliente.setName("Seleccione");
            cbClient.addItem(itemCliente);
            while(rs.next())
            {
                itemCliente = new Company();
                itemCliente.setNumber(rs.getString("cnNumber"));
                itemCliente.setName(rs.getString("cnName"));
                cbClient.addItem(itemCliente);
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

    
    public void loadtbFacturas(JTable tb, String sql) 
    {        
        DefaultTableModel modelo=(DefaultTableModel) tb.getModel();                
        //Vector<Factura> emprs = getListForDay();
        Statement stmt = null;
        try
        {
            //for(int i=0;i < emprs.size();i++)
            {
                String sqltr = "SELECT folio,cnNumber,cnName,dateFact,total,moneda,dateEF,dateDP,dateCancel,datePago,status,name,prioridad FROM Facturas_Resolved WHERE " + sql + " ORDER BY folio" ;
                
                stmt = (Statement) connection.createStatement();            
                ResultSet rs = stmt.executeQuery(sqltr);
                //System.out.println(sqltr);
                Object[] fact;
                while(rs.next())
                {
                    fact = new Object[13];
                    fact[0] = rs.getString("folio");
                    fact[1] = rs.getString("dateFact");
                    fact[2] = rs.getString("cnNumber");
                    fact[3] = rs.getString("cnName");
                    fact[4] = rs.getString("total");
                    fact[5] = rs.getString("moneda");
                    fact[6] = rs.getString("dateEF");
                    fact[7] = rs.getString("dateDP");
                    fact[8] = rs.getString("dateCancel");
                    fact[9] = rs.getString("datePago");
                    //status
                    {
                        if(rs.getString("status").equals("I"))
                        {
                            fact[10] = "Ignorar";                    
                        }
                        else if(rs.getString("status").equals("C"))
                        {
                            fact[10] = "Cancelada";                    
                        }
                        else if(rs.getString("status").equals("PR"))
                        {
                            fact[10] = "In-Process";                    
                        }
                        else if(rs.getString("status").equals("PC"))
                        {
                            fact[10] = "Parcialidades";                    
                        }
                        else if(rs.getString("status").equals("PG"))
                        {
                            fact[10] = "Pagada";                    
                        }
                    }
                    fact[11] = rs.getString("name");
                    //prioridad
                    {
                        if(rs.getString("prioridad").equals("N"))
                        {
                            fact[12] = "";
                        }
                        else if(rs.getString("prioridad").equals("A"))
                        {
                            fact[12] = "Atrasado";
                        }
                        else if(rs.getString("prioridad").equals("M"))
                        {
                            fact[12] = "Moroso";
                        }
                    }
                    modelo.addRow(fact);
                }
                rs.close();
                stmt.close();
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
    }

    public void loadtbFacturas(JTable tb) 
    {        
        DefaultTableModel modelo=(DefaultTableModel) tb.getModel();                
        Vector<Factura> emprs = getListForDay();
        Statement stmt = null;
        try
        {
            for(int i=0;i < emprs.size();i++)
            {
                String sql = "SELECT folio,cnName,dateFact,total,moneda FROM Facturas_Resolved WHERE cnNumber='" + emprs.get(i).getEmprID() + "' and dateEF is not NULL and dateDP is not NULL and (Pagada is NULL or Pagada='N') ORDER BY folio" ;
                //System.out.println(sql);
                stmt = (Statement) connection.createStatement();            
                ResultSet rs = stmt.executeQuery(sql);

                Object[] fact;
                while(rs.next())
                {
                    fact = new Object[5];
                    fact[0] = rs.getString("folio");
                    fact[1] = rs.getString("cnName");
                    fact[2] = rs.getString("dateFact");
                    fact[3] = rs.getDouble("total");
                    fact[4] = rs.getString("moneda");
                    /*if(rs.getString("moneda").equals("$D"))
                    {
                        fact[4] = "Dolar";
                    }
                    else if(rs.getString("moneda").equals("$P"))
                    {
                        fact[4] = "Pesos";
                    }*/
                    modelo.addRow(fact);
                }
                rs.close();
                stmt.close();
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
    }
        
    public void loadtbFacturas(JTable tb,int day, SIIL.Server.User u) 
    {        
        DefaultTableModel modelo=(DefaultTableModel) tb.getModel();                
        //Vector<Factura> emprs = getListForDay(day,PC.DP);
        Statement stmt = null;
        try
        {
            //for(int i=0;i < emprs.size();i++)
            {
                String sql = "SELECT folio,cnNumber,cnName,DATE_FORMAT(dateFact,'%d-%m-%Y') as dateFact,total,moneda,dateDP FROM Facturas_Resolved WHERE dateDPofweek = " + day + " and (status = 'PR' or status = 'PC') and  uID=" + u.getuID() + " ORDER BY dateDP ASC" ;
                //System.out.println(sql);
                stmt = (Statement) connection.createStatement();            
                ResultSet rs = stmt.executeQuery(sql);

                Object[] fact;
                while(rs.next())
                {
                    fact = new Object[7];
                    fact[0] = rs.getString("folio");
                    fact[1] = rs.getString("dateFact");
                    fact[2] = rs.getString("cnNumber");
                    fact[3] = rs.getString("cnName");
                    fact[4] = rs.getString("total");
                    fact[5] = rs.getString("moneda");
                    fact[6] = rs.getString("dateDP");
                    modelo.addRow(fact);
                }
                rs.close();
                stmt.close();
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
    }

    ResultSet select(String sql) 
    {
        Statement stmt = null;
        try
        {
            //for(int i=0;i < emprs.size();i++)
            {                
                //System.out.println(sql);
                stmt = (Statement) connection.createStatement();            
                ResultSet rs = stmt.executeQuery(sql);
                return rs;                
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
        return null;
    }
    
    
   public Timestamp getTimestamp() throws SQLException
   {
        String sql = "SELECT NOW()";
        Statement pstmt = (Statement) connection.createStatement();
        ResultSet rs = pstmt.executeQuery(sql);     
        if(rs.next())
        {
            return rs.getTimestamp("NOW()");
        }
        
        return null;
   }
   
   public boolean check()
   {
       if(connection == null)
       {
           return false;
       }
       else
       {
           return true;
       }
   }


    @Deprecated
    public int update(String sqlStr) 
    {
        try
        {
            String sql = "UPDATE " + sqlStr;
            System.out.println(sql);
            strSQL = sql;
            int r = connection.createStatement().executeUpdate(sql);
            //conn.commit();
            return r;            
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        
        return 0;
    }

    @Deprecated
    public int insert(String sqlStr, int autoGeneratedKeys) 
    {
        stmt = null;
        try
        {
            String sql = "INSERT INTO " + sqlStr;
            stmt = connection.createStatement();
            System.out.println(sql);
            strSQL = sql;
            int r = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            if(r > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) 
                {
                    return rs.getInt(1);
                }
            }
            else
            {
                connection.rollback();
                return -1;            
            }                
            return -1;
        }
        catch(SQLException se)
        {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, se);
        }
        return -1;
   }
    
    @Deprecated
    public int INSERT(String sqlStr) throws SQLException 
    {
        stmt = connection.createStatement();
        String sql = "INSERT INTO " + sqlStr;
        //System.out.println(sql);
        strSQL = sql;
        return stmt.executeUpdate(sql);
    }
    
    @Deprecated
    public int insert(String sqlStr) 
    {
        stmt = null;
        try
        {
            stmt = connection.createStatement();
            String sql = "INSERT INTO " + sqlStr;
            System.out.println(sql);
            strSQL = sql;
            int r = stmt.executeUpdate(sql);
            if(r > 0)
            {
                //conn.commit();            
            }
            else
            {
                connection.rollback();            
            }                
            return r;
        }
        catch(SQLException se)
        {
            return 0;
        }
   }
   public Connection getConnection()
   {
       return connection;
   }
   public void Close()
   {
        try
        {
            if(connection != null ) connection.close();
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
   }
   
   public Connection Create(boolean  autocommit)
   {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            connection.setAutoCommit(autocommit);
        }
        catch(CommunicationsException se)
        {
            JOptionPane.showMessageDialog(new JFrame(),
            "No se encuentra el serividor de base de datos",
            "Error Interno",
            JOptionPane.ERROR_MESSAGE
            );
            return connection;
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }    
        return connection;
   }
   
   public Connection Create()
   {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            connection.setAutoCommit(false);
        }
        catch(CommunicationsException se)
        {
            JOptionPane.showMessageDialog(new JFrame(),
            "No se encuentra el serividor de base de datos",
            "Error Interno",
            JOptionPane.ERROR_MESSAGE
            );
            return connection;
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }    
        return connection;
   }
   
   /**
    * Para compatiblidad en retroceso, es lladamo por Database para evitar la 
    * llamada a el constructor vacio.
    * @param flag 
    */
   public MySQL(Object flag)
   {
       ;
   }
   
   public MySQL()
   {
        SIIL.artifact.Artifact artifact = null;
        try 
        {
            artifact = new SIIL.artifact.Artifact();            
            if(artifact.getPhase() == SIIL.artifact.Artifact.Word.alpha)
            {
                JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
                DB_URL = "jdbc:mysql://192.168.1.200/DBSSIILa";
                USER = "application";
                PASS = "65094%?¡wrs";  
            }
            else if(artifact.getPhase() == SIIL.artifact.Artifact.Word.beta)
            {
                JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
                DB_URL = "jdbc:mysql://192.168.1.200/DBSSIILbr";
                USER = "application";
                PASS = "65094%?¡wrs";  
            }
            else if(artifact.getPhase() == SIIL.artifact.Artifact.Word.release)
            {
                JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
                DB_URL = "jdbc:mysql://192.168.1.200/DBSSIIL";
                USER = "application";
                PASS = "65094%?¡wrs";
            } 
            else
            {
                
            }
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int delete(String sqlstr) 
    {
        try
        {
            String sql = " DELETE FROM " + sqlstr;
            //System.out.println(sql);
            int r = connection.createStatement().executeUpdate(sql);
            return r;            
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        
        return 0;
    }

    public void commit() throws SQLException 
    {
        connection.commit();
    }

    public void rollback() throws SQLException 
    {
        connection.rollback();
    }

    public int getGeneratedKeys() 
    {
        ResultSet rs;
        try 
        {
            rs = stmt.getGeneratedKeys();
            if(rs.next())
            {
                return rs.getInt(1);
            }
            rs.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public String getSQL() 
    {
        return strSQL;
    }
    
}
