
package SIIL.Server;

import SIIL.core.Office;
import SIIL.core.Exceptions.DatabaseException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import process.Return;

/**
 *
 * @author areyes
 */
public class Person implements Serializable,session.Person
{
    public enum Type
    {
        MECANICO,
        MECANICO_COTSERV,
        MANAGER
    }
    
    public enum OrderBy
    {
        APN1,
        N1AP,
        RL
    }
    
    public enum Department
    {
        SERVICES,
        REFACCIONES,
        VENTAS,
        ALMACEN
    }
    
    
    private static final String MYSQL_AVATAR_TABLE = "Persons";
    
    protected int pID;
    protected String N1;
    protected String Ns;
    protected String AP;
    protected String AM;
    @Deprecated 
    protected String BD;
    protected String department;
    protected Office office;
    private String email;
    protected String seudonimo;
    private boolean isOrserTec;
    
    @Deprecated
    public boolean upSeudonimo(Database db,String seudonimo) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(seudonimo == null)
        {
            return false;
        }
        this.seudonimo = null;        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET seudonimo='" + seudonimo + "' WHERE pID=" + pID;
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
    
    public boolean upIsOrserOwner(Database db,boolean active) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET isOrserOwner = ";
        if(active)
        {
            sql += "'Y' WHERE pID=" + pID;
        }
        else
        {
            sql += "'N' WHERE pID=" + pID;
        }
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
    
    public boolean upIsOrserTec(Database db,boolean active) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET isOrserTec = ";
        if(active)
        {
            sql += "'Y' WHERE pID=" + pID;
        }
        else
        {
            sql += "'N' WHERE pID=" + pID;
        }
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
    
    public boolean upDepartment(Database db,Department department) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(department == null)
        {
            return false;
        }
        this.department = null;        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET department=";
        if(department == Department.ALMACEN)
        {
            sql += "'al' WHERE pID=" + pID;
        }
        else if(department == Department.REFACCIONES)
        {
            sql += "'rf' WHERE pID=" + pID;
        }
        else if(department == Department.SERVICES)
        {
            sql += "'se' WHERE pID=" + pID;
        }
        else if(department == Department.VENTAS)
        {
            sql += "'vt' WHERE pID=" + pID;
        }
        
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
    
    /**
     * @return the seudonimo
     */
    public String getSeudonimo() {
        return seudonimo;
    }
    
    public boolean upOffice(Database db,Office office) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(office == null)
        {
            return false;
        }
        this.office = null;        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET office='" + office.getCode() + "' WHERE pID=" + pID;
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
    
    public boolean upEmail(Database db,String email) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(email == null)
        {
            return false;
        }
        this.email = null;        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET email='" + email + "' WHERE pID=" + pID;
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
    
    
    public boolean upAM(Database db,String am) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(am == null)
        {
            return false;
        }
        this.AM = null;        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET nameAM='" + am + "' WHERE pID=" + pID;
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
    
    public boolean upAP(Database db,String ap) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(ap == null)
        {
            return false;
        }
        this.AP = null;        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET nameAP='" + ap + "' WHERE pID=" + pID;
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
    
    public boolean upNS(Database db,String ns) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(ns == null)
        {
            return false;
        }
        this.Ns = null;        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET nameNs='" + ns + "' WHERE pID=" + pID;
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
    
    public boolean upN1(Database db,String n1) throws SQLException
    {
        if(pID < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        if(n1 == null)
        {
            return false;
        }
        this.N1 = null;        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET nameN1='" + n1 + "' WHERE pID=" + pID;
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
    
    /**
     * Inserta una persona en la BD de Datos e inicializa su llave.
     * @param db
     * @param n1
     * @param ap
     * @return
     * @throws SQLException 
     */
    public boolean insert(Database db,String n1,String ap) throws SQLException
    {
        clean();
        if(pID > 0)
        {
            return false;
        }
        if(db == null)
        {
            return true;
        }
        
        if(n1  == null || ap == null)
        {
            return false;
        }
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(nameN1,nameAP,BD) VALUES('" + n1 + "','" + ap + "','bc.tj')";
        System.out.println(sql);
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return false;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        int generated = -1;        
        if (rs.next())
        {
            pID = rs.getInt(1);
            return true;
        }
        else
        {
            pID = -1;
            return false;
        }
    }
    
    public static List<Person> search(Database db,Type type,Office office,OrderBy orderby,int limit,String[] tokens) throws SQLException
    {
        String sql = "SELECT pID FROM " + MYSQL_AVATAR_TABLE;        
        switch(type)
        {
            case MECANICO:
            case MECANICO_COTSERV:
                sql += " WHERE active = 'Y' AND isOrserTec = 'Y' AND ";
                break;
            default:
                return null;                
        }
        
        for(int i = 0; i < tokens.length; i++)
        {
            sql = sql + " ( nameN1 LIKE '%" + tokens[i] + "%' OR nameNs LIKE '%" + tokens[i] + "%' OR nameAP LIKE '%" + tokens[i] + "%' OR nameAM LIKE '%" + tokens[i] + "%' OR seudonimo LIKE '%" + tokens[i] + "%') " ;
            if((i == 0 && tokens.length > 1 )|| (i < tokens.length-1)) sql = sql + " AND ";
        }
        
        office.download(db.getConnection());
        if(office.getType().equals("m"))
        {
            ;
        }
        else if(office.getType().equals("s"))
        {
            sql += " office = '" + office.getCode() + "' ";
        }
        else
        {
            ;
        }
        if(orderby == OrderBy.N1AP)
        {
            sql += " ORDER BY nameN1 ASC,nameAP ASC";
        }
        else if(orderby == OrderBy.APN1)
        {
            sql += " ORDER BY nameAP ASC,nameN1 ASC";
        }
        else if(orderby == OrderBy.RL)
        {
            sql += " ORDER BY nameN1 ASC,nameNs ASC,nameAP ASC,nameAM ASC";
        }
        if(limit > 0) sql = sql + " LIMIT " + limit;
        //System.out.println(sql);
        ArrayList<Person> persons = new ArrayList<Person>();
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            Person p = new Person();
            p.fill(db, rs.getInt(1), "bc.tj");
            persons.add(p);
        }
        
        return persons;
    }
    
    public static List<Person> search(Database db,Type type,Office office,OrderBy orderby,int limit,String text) throws SQLException
    {
        String sql = "SELECT pID FROM " + MYSQL_AVATAR_TABLE;        
        switch(type)
        {
            case MECANICO:
            case MECANICO_COTSERV:
                sql += " WHERE active = 'Y' AND isOrserTec = 'Y' AND ";
                break;
            default:
                return null;                
        }
        sql = sql + " ( nameN1 LIKE '%" + text + "%' OR nameNs LIKE '%" + text + "%' OR nameAP LIKE '%" + text + "%' OR nameAM LIKE '%" + text + "%' OR seudonimo LIKE '%" + text + "%') " ;
        
        office.download(db.getConnection());
        if(office.getType().equals("m"))
        {
            ;
        }
        else if(office.getType().equals("s"))
        {
            sql += " office = '" + office.getCode() + "' ";
        }
        else
        {
            ;
        }
        if(orderby == OrderBy.N1AP)
        {
            sql += " ORDER BY nameN1 ASC,nameAP ASC";
        }
        else if(orderby == OrderBy.APN1)
        {
            sql += " ORDER BY nameAP ASC,nameN1 ASC";
        }
        else if(orderby == OrderBy.RL)
        {
            sql += " ORDER BY nameN1 ASC,nameNs ASC,nameAP ASC,nameAM ASC";
        }
        if(limit > 0) sql = sql + " LIMIT " + limit;
        ArrayList<Person> persons = new ArrayList<Person>();
        ResultSet rs = db.query(sql);
        //System.out.println(sql);
        while(rs.next())
        {
            Person p = new Person();
            p.fill(db, rs.getInt(1), "bc.tj");
            persons.add(p);
        }
        
        return persons;
    }
    
    public Return  download(Database db) throws SQLException
    {
        String sql = "SELECT pID,nameN1,nameNs,nameAP,nameAM,department,office,seudonimo FROM " + MYSQL_AVATAR_TABLE + " WHERE pID = " + pID;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            N1 = rs.getString(2);
            Ns = rs.getString(3);
            AP = rs.getString(4);
            AM = rs.getString(5);
            department = rs.getString(6);           
            if(rs.getString(7) != null)
            {
                office = new Office(-1);
                office.selectCode(db,rs.getString(7));
                Throwable retO = office.download(db.getConnection());
                if(retO != null)
                {
                    return new Return(false,retO.getMessage());
                }
                else
                {
                    return new Return(true);
                }
            }
            seudonimo = rs.getString(8);
        }
        return new Return(false,"No se encontro elemento");
    }
    
    public Person()
    {
        pID = -1;
    }
    public Person(int id)
    {
        pID = id;
    }

    public static List<Person> listing(Database db,Type type,Office office,OrderBy orderby) throws SQLException
    {
        String sql = "SELECT pID FROM " + MYSQL_AVATAR_TABLE;        
        switch(type)
        {
            case MECANICO:
            case MECANICO_COTSERV:
                sql += " WHERE active = 'Y' AND isOrserTec = 'Y' ";
                break;
            case MANAGER:
                sql += " WHERE active = 'Y' AND isOrserOwner = 'Y' ";
                break;
            default:
                return null;                
        }
        if(office != null) office.download(db.getConnection());
        
        if(office == null)
        {
            ;
        }
        else if(office.getType().equals("m"))
        {
            ;
        }
        else if(office.getType().equals("s"))
        {
            sql += " AND office = '" + office.getCode() + "' ";
        }
        else
        {
            ;
        }
        
        if(orderby == OrderBy.N1AP)
        {
            sql += " ORDER BY nameN1 ASC,nameAP ASC";
        }
        else if(orderby == OrderBy.APN1)
        {
            sql += " ORDER BY nameAP ASC,nameN1 ASC";
        }
        
        ArrayList<Person> persons = new ArrayList<Person>();
        System.out.println(sql);
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            Person p = new Person();
            p.fill(db, rs.getInt(1), "bc.tj");
            persons.add(p);
        }
        
        return persons;
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return selectRandom(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT pID FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.pID = rs.getInt(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            this.pID = -1;
            return new Return(Return.Status.DONE);
        }
    }
    
    public String toString(boolean detail)
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
            if(detail)
            {
                if(Ns != null)
                {
                    name += " " + Ns;
                }                
            }
            if(AP != null)
            {
                name += " " + AP;
            }
            if(detail)
            {
                if(AM != null)
                {
                    name += " " + AM;
                }
                if(seudonimo != null)
                {
                    name = name + " (" + seudonimo + ")";
                }
            }
        }
        return name;
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
            /*
            if(seudonimo != null)
            {
                name = name + " (" + seudonimo + ")";
            }
            */
        }
        return name;
    }
    
    /**
     * @return the AM
     */
    public String getAM() {
        return AM;
    }

    /**
     * @param AM the AM to set
     */
    public void setAM(String AM) {
        this.AM = AM;
    }

    /**
     * @return the pID
     */
    public int getpID() {
        return pID;
    }

    /**
     * @param pID the pID to set
     */
    public void setpID(int pID) {
        this.pID = pID;
    }

    /**
     * @return the N1
     */
    public String getN1() {
        return N1;
    }

    /**
     * @param N1 the N1 to set
     */
    public void setN1(String N1) {
        this.N1 = N1;
    }

    /**
     * @return the Ns
     */
    public String getNs() {
        return Ns;
    }

    /**
     * @param Ns the Ns to set
     */
    public void setNs(String Ns) {
        this.Ns = Ns;
    }

    /**
     * @return the AP
     */
    public String getAP() {
        return AP;
    }

    /**
     * @param AP the AP to set
     */
    public void setAP(String AP) {
        this.AP = AP;
    }

    /**
     * @return the BD
     */
    public String getBD() {
        return BD;
    }

    @Deprecated
    public Exception fill(Database db,Integer ID, String BD) throws SQLException 
    {
        pID = ID;
        this.BD = BD;
        
        String sql = "SELECT pID,nameN1,nameNs,nameAP,nameAM,department,office,seudonimo FROM " + MYSQL_AVATAR_TABLE + " WHERE pID = " + getpID();
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            N1 = rs.getString(2);
            Ns = rs.getString(3);
            AP = rs.getString(4);
            AM = rs.getString(5);
            department = rs.getString(6);           
            office = new Office(-1);
            office.selectCode(db,rs.getString(7));
            Exception retO = office.download(db.getConnection());
            seudonimo = rs.getString(8);
            return retO;
        }
        return null;
    }

    /**
     * @param BD the BD to set
     */
    public void setBD(String BD) {
        this.BD = BD;
    }

    public Exception fill(Database db, String BD, String ownerName, int ownerPerson) 
    {
        String query = "SELECT BD,pID,nameN1,nameNs,nameAP,nameAM,email,department,office FROM Persons WHERE ";
        if(ownerPerson > 0)
        {
            query += " BD = '" + BD + "' and pID = " + ownerPerson ;
        }
        else if(ownerName != null)
        {
            String[] name = ownerName.split("[\\s\\xA0]+");
            if(name.length == 1 ) 
            {
                System.out.println("Fúe implosible determinar como '" + ownerName + "','" + ownerPerson + " [" + query + "]");
            }
            query += " BD = '" + BD + "' and nameN1 = '" + name[0] + "' and nameAP = '" + name[1] + "'";            
        }
        else
        {
            return new Exception("Fúe implosible determinar como '" + ownerName + "','" + ownerPerson + " [" + query + "]");
        }
        
        try 
        {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next())
            {
                this.BD = BD;
                this.pID = rs.getInt("pID");
                this.N1 = rs.getString("nameN1");
                this.Ns = rs.getString("nameNs");
                this.AP = rs.getString("nameAP");
                this.AM = rs.getString("nameAM");
                this.department = rs.getString("department");
                this.office = new Office(db, rs.getString("office")) ;
            }
            else
            {
                return new Exception("Fúe implosible determinar como '" + ownerName + "','" + ownerPerson + "[" + query + "]");
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
        
        return null;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the office
     */
    public Office getOffice() {
        return office;
    }

    public void downloadEmail(Database db) throws DatabaseException 
    {
        String sql = "SELECT email FROM Persons WHERE pID = " + pID;
        Statement stmt = null;
        try 
        {
            stmt = db.getConnection().createStatement();
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Fallo la creacion de Statement", ex);
        }
        ResultSet rs = null;        
        try 
        {
            rs = stmt.executeQuery(sql);
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Fallo la ejecucion de la consulta para: " + sql, ex);
        }
        try 
        {
            if(rs.next())
            {
                this.email = rs.getString(1);
            }
            else
            {
                this.email = null;
                //throw new DatabaseException("No se encontraron resultados para: " + sql);
            }
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Fallo la ejecucion de la consulta para: " + sql, ex);
        }
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    private void clean() 
    {
        pID = -1;
        AM = null;
        AP = null;
        N1 = null;
        Ns = null;
        department = null;
        email = null;
        office = null;
    }
}
