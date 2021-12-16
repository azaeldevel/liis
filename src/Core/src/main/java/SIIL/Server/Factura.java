package SIIL.Server;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author areyes
 */
public class Factura 
{

    public static void loadCBMoneda(MySQL conn, JComboBox cb) 
    {
        String sql = "SELECT DISTINCT moneda FROM Facturas";
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            String item = "Seleccione ..";
            cb.addItem(item);
            while(rs.next())
            {
                cb.addItem(rs.getString("moneda"));
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
    
    public static void fillCCR(JTable tb,int day, SIIL.Server.User u, MySQL conn, Where where) 
    {        
        DefaultTableModel modelo=(DefaultTableModel) tb.getModel();                
        //Vector<Factura> emprs = getListForDay(day,PC.DP);
        Statement stmt = null;
        try
        {
            //for(int i=0;i < emprs.size();i++)
            {
                String sql = "SELECT folio,cnNumber,cnName,DATE_FORMAT(dateFact,'%d-%m-%Y') as dateFact,total,moneda,dateDP,dateEF FROM Facturas_Resolved WHERE (dateDPofweek = " 
                        + day + " or (dateDPofweek is null and dateEF is not null )) and (status = 'PR' or status = 'PC' or status = 'PR-CCR' or status = 'PR-PC') and  uID=" 
                        + u.getuID();
                        if(where.buildWhere().isEmpty())
                        {
                            ;
                        }
                        else
                        {
                            sql = sql + " and (" + where.buildWhere() + ")";
                        }
                        sql = sql + " ORDER BY dateDP ASC" ;
                //System.out.println(sql);
                stmt = (Statement) conn.getConnection().createStatement();            
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
    public static void fillTB(JTable tb, Where sqlRest, MySQL conn) 
    {        
        DefaultTableModel modelo=(DefaultTableModel) tb.getModel();                
        //Vector<Factura> emprs = getListForDay();
        Statement stmt = null;
        Statement stmtComment = null;
        try
        {
            //for(int i=0;i < emprs.size();i++)
            {
                String sqltr = "SELECT folio,cnNumber,cnName,DATE_FORMAT(dateFact,'%d-%m-%Y') as dateFact,total,moneda,dateEF,dateDP,dateCancel,datePago,status,name,prioridad FROM Facturas_Resolved ";
                if(sqlRest.buildWhere().isEmpty())
                {
                   sqltr =  sqltr + " ORDER BY folio" ;                     
                }
                else
                {
                    sqltr =  sqltr + " WHERE " + sqlRest.buildWhere() + " ORDER BY folio" ;
                }
                stmt = (Statement) conn.getConnection().createStatement();            
                ResultSet rs = stmt.executeQuery(sqltr);
                //System.out.println(sqltr);
                Object[] fact;
                while(rs.next())
                {
                    fact = new Object[14];
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
                        else if(rs.getString("status").equals("PR-CCR"))
                        {
                            fact[10] = "Con CR";                    
                        }
                        else if(rs.getString("status").equals("PR-PC"))
                        {
                            fact[10] = "Parcialidades";                    
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
                    //comentarios
                    {
                        sqltr = "SELECT comment FROM FacturasComment WHERE folio = '" + rs.getString("folio") + "' ORDER BY id DESC" ;
                        stmtComment = (Statement) conn.getConnection().createStatement();            
                        ResultSet rsComment = stmtComment.executeQuery(sqltr);
                        fact[13] = "";
                        while(rsComment.next())
                        {
                            fact[13] = fact[13]  + ">>>" + rsComment.getString("comment");
                        }
                        rsComment.close();
                        stmtComment.close();
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
    public static int updateCR(Factura fact, MySQL conn) 
    {
        SimpleDateFormat  sdf = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = " Facturas SET dateEF='" +
                sdf.format(fact.getDateEF()) + "', dateDP='" +
                sdf.format(fact.getDateDP())  + "' WHERE folio='" + fact.getFolio() + "'";
        return conn.update(sql);
    }
    
    public static int insert(Factura fact, MySQL conn) 
    {
        SimpleDateFormat  sdf = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = " Facturas(folio,dateFact,emprID,total,moneda,status,uCreator,local)VALUES ('" +
                fact.getFolio() + "'," +
                "'" + sdf.format(fact.getDateFact()) + "','" +
                fact.getEmpre().getNumber() + "'," +
                fact.getTotal() + ",'" +
                fact.getMoneda() + "'," +
                "'PR'," + 
                fact.getCreator().getpID() +
                ",'" + fact.localidad + "')";
        return conn.insert(sql);
    }

    public static Factura getFactura(String f, MySQL conn) 
    {
        String sql = "SELECT folio,dateFact,emprID,total,moneda,status,uCreator,dateEF,dateDP FROM Facturas WHERE folio = '" + f + "'";
        Statement stmt = null;
        Statement stmtComment = null;
        try
        {
            stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                Factura fact = new Factura();
                fact.folio = rs.getString("folio");
                fact.dateFact = rs.getDate("dateFact");
                fact.empre = new Company(rs.getString("emprID"),"","");
                fact.total = rs.getFloat("total");
                fact.moneda = rs.getString("moneda");
                fact.status = rs.getString("status");
                fact.creator = new User();
                fact.creator.setpID(rs.getInt("uCreator"));
                fact.dateEF = rs.getDate("dateEF");
                fact.dateDP = rs.getDate("dateDP");
                return fact;
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
        return null;
    }

    public static boolean checkCR(String f, MySQL conn) 
    {
        String sql = "SELECT dateEF FROM Facturas WHERE dateEF is not null and folio = '" + f + "'";
        Statement stmt = null;
        Statement stmtComment = null;
        try
        {
            stmt = (Statement) conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) return true;
            return false;
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
    
    private String folio;
    private Company empre;
    private Date dateFact;
    private float total;
    private String moneda;
    private Date dateEF;
    private Date dateDP;    
    private User creator;
    private String localidad;
    String status;
    
    /**
     * @return the folio
     */
    public String getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * @return the emprID
     */
    public String getEmprID() {
        return empre.getNumber();
    }

    /**
     * @param emprID the emprID to set
     */
    public void setEmprID(String emprID) 
    {
        this.empre.setName(emprID);
    }

    /**
     * @return the emprName
     */
    public String getEmprName() 
    {
        return empre.getName();
    }

    /**
     * @param emprName the emprName to set
     */
    public void setEmprName(String emprName) 
    {
        empre.setName(emprName);
    }

    /**
     * @return the dateFact
     */
    public Date getDateFact() {
        return dateFact;
    }

    /**
     * @param dateFact the dateFact to set
     */
    public void setDateFact(Date dateFact) {
        this.dateFact = dateFact;
    }

    /**
     * @return the total
     */
    public float getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(float total) {
        this.total = total;
    }
    public void setTotal(String total) 
    {
        this.total = Float.parseFloat(total);
    }
    /**
     * @return the moneda
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(String moneda) throws Exception 
    {
        
        switch(moneda)
        {
            case "DLL":
                this.moneda = moneda;
                break;
            case "MXN":
                this.moneda = moneda;
                break;
            case "Dlls.":                
                this.moneda = "DLL";
                break;
            case "Pesos":                
                this.moneda = "MXN";
                break;
            case "PESOS":                
                this.moneda = "MXN";
                break;
            default:
                throw new Exception("Formato de moneda incorrecto");
        }
    }

    /**
     * @return the dateEF
     */
    public Date getDateEF() {
        return dateEF;
    }

    /**
     * @param dateEF the dateEF to set
     */
    public void setDateEF(Date dateEF) {
        this.dateEF = dateEF;
    }

    /**
     * @return the dateDP
     */
    public Date getDateDP() {
        return dateDP;
    }

    /**
     * @param dateDP the dateDP to set
     */
    public void setDateDP(Date dateDP) {
        this.dateDP = dateDP;
    }

    /**
     * @return the empre
     */
    public Company getEmpre() {
        return empre;
    }

    /**
     * @param empre the empre to set
     */
    public void setEmpre(Company empre) {
        this.empre = empre;
    }

    public int send(MySQL conn, SIIL.Server.User u) 
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strSQL = "Facturas(folio,dateFact,emprID,total,moneda,status,uCreator,local)VALUES ('" +
                folio + 
                "','" +  df.format(dateFact) + 
                "','" +  empre.getNumber() + 
                "'," +  total +
                ",'" +  moneda + "'" +
                ",'PR'" +  
                "," +  u.getuID() +
                ",'" + localidad + "')";
        //System.out.println(strSQL);
        int ret = conn.insert(strSQL);
        if(ret > 0)
        {
            try 
            {
                conn.getConnection().commit();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

    /**
     * @return the creator
     */
    public User getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * @return the localidad
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * @param localidad the localidad to set
     */
    public void setLocalidad(String localidad) 
    {
        switch(localidad)
        {
            case "Tijuana":
                this.localidad = "bc.tj";
                break;
            case "Mexicali":
                this.localidad = "bc.mx";
                break;
            case "Ensenada":
                this.localidad = "bc.ens";
                break;
            default:
                this.localidad = "";                
        }
    }
}
