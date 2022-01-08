
package SIIL.services.grua;

import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.services.grua.Captura.Owner;
import static SIIL.services.grua.Captura.Owner.CLIENT;
import static SIIL.services.grua.Captura.Owner.SIIL;
import core.PlainTitem;
import core.Searchable;
import database.mysql.sales.Remision;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import process.Return;
import stock.Flow;

/**
 *
 * @author Azael
 */
public class Movements implements Searchable
{
    private static final String MYSQL_AVATAR_TABLE = "Movements";
    private static final String MYSQL_AVATAR_TABLE_BACKWARD_BD = "bc.tj";
    
    private int id;
    private String folio;
    private Date fhMov;
    private String tmov;
    private Uso uso;
    private String firma;
    private String note;
    private String sa;
    private String owner;
    private Office office;
    private Date createTime;
    private Enterprise company;
    private Person creator;
    private List<Flow> movhequis;
    private Remision remision;
       
    public Return cancel(Database connection, Office office, Date date, String folio) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(office == null)
        {
            return new Return(Return.Status.FAIL,"office is null.");
        }
        if(date == null)
        {
            return new Return(Return.Status.FAIL,"date is null.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(BD,folio,fhMov,suc,tmov) VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + folio + "','" + sdf.format(date) + "','" + office.getCode() + "','canc')";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);        
        if(affected != 1)
        {
            return new Return(Return.Status.FAIL,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        return new Return(true);
    }
    
    public boolean download(Database db) throws SQLException
    {
        String sql = "SELECT folio,fhMov,compNumber,sa2,note FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        System.out.println(sql);
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            folio = rs.getString(1);
            fhMov = rs.getDate(2);
            company = new Enterprise(rs.getInt(3));
            company.download(db);
            if(rs.getInt(4) > 0)
            {
                remision = new Remision(rs.getInt(4));
                remision.download(db);
            }
            note = rs.getString(5);
        }
        
        return false;
    }
    
    public static List<Searchable> search(Database dbserver,Office office,String text,boolean activeSA) throws SQLException
    {
        List<Searchable> list = new ArrayList<>();
        String sql = "SELECT id FROM ";  
        //String whereSQL = " WHERE  ";
        if(activeSA == true)
        {
            List<Remision> lstR = Remision.search(dbserver, office, text, 0);
            for(Remision remision : lstR)
            {
                sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE;
                sql = sql + " WHERE  sa2 = " + remision.getID();
                //System.out.println(sql);
                ResultSet rs = dbserver.query(sql);
                while(rs.next())
                {
                    Movements mov = new Movements(rs.getInt(1));
                    mov.download(dbserver);
                    list.add(mov);                    
                }
            }            
        }        
        return list;
    }
    
    /**
     *
     * @param connection
     * @param office
     * @param date
     * @param folio
     * @param hequis
     * @param plainTitem no se usa solo para evitar la sobrecarga del identificador
     * @return
     * @throws SQLException
     */
    public Boolean insert(Database connection,Office office,Date date,String folio, List<PlainTitem> hequis,PlainTitem plainTitem) throws SQLException
    {
        if(connection == null)
        {
            return false;
        }
        if(office == null)
        {
            return false;
        }
        if(date == null)
        {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(BD,folio,fhMov,suc) VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + folio + "','" + sdf.format(date) + "','" + office.getCode() + "')";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);        
        if(affected != 1)
        {
            return false;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            id = rs.getInt(1);
            //Insertando childs
            Movitems movitems = new Movitems();
            int inserted = 0;
            for(PlainTitem t : hequis)
            {
                inserted += movitems.insert(connection, this, t);
            }
            if(inserted == hequis.size())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            id = -1;
            return false;
        }
    }

    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return selectLast(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 0, 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            this.id = -1;
            return new Return(Return.Status.FAIL);
        }
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
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            this.id = -1;
            return new Return(Return.Status.DONE);
        }
    }
    
    public Movements(int id)
    {
        this.id = id;
    }
    
    /**
     * 
     * @param connection
     * @param office
     * @param date
     * @param folio
     * @param hequis
     * @return
     * @throws SQLException 
     */
    public Return insert(Database connection, Office office, Date date, String folio, List<Flow> hequis,Flow flow) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(office == null)
        {
            return new Return(Return.Status.FAIL,"office is null.");
        }
        if(date == null)
        {
            return new Return(Return.Status.FAIL,"date is null.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(BD,folio,fhMov,suc) VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + folio + "','" + sdf.format(date) + "','" + office.getCode() + "')";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);        
        if(affected != 1)
        {
            return new Return(Return.Status.FAIL,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            id = rs.getInt(1);
            //Insertando childs
            Movitems movitems = new Movitems();
            Return insert = movitems.insert(connection, hequis, this);
            if(insert.getStatus() == Return.Status.FAIL) return  insert;
            return new Return(Return.Status.DONE, rs.getInt(1));
        }
        else
        {
            id = -1;
            return new Return(Return.Status.FAIL,"No genero ID para el registro.");
        }
    }
/**
     * 
     * @param connection
     * @param office
     * @param date
     * @param folio
     * @param hequis
     * @return
     * @throws SQLException 
     */
    public Return insert(Database connection, Office office, Date date, String folio, List<Flow> hequis) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(office == null)
        {
            return new Return(Return.Status.FAIL,"office is null.");
        }
        if(date == null)
        {
            return new Return(Return.Status.FAIL,"date is null.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(BD,folio,fhMov,suc) VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + folio + "','" + sdf.format(date) + "','" + office.getCode() + "')";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);        
        if(affected != 1)
        {
            return new Return(Return.Status.FAIL,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            id = rs.getInt(1);
            //Insertando childs
            Movitems movitems = new Movitems();
            Return insert = movitems.insert(connection, hequis, this);
            if(insert.getStatus() == Return.Status.FAIL) return  insert;
            return new Return(Return.Status.DONE, rs.getInt(1));
        }
        else
        {
            id = -1;
            return new Return(Return.Status.FAIL,"No genero ID para el registro.");
        }
    }
    
    /**
     * @return the office
     */
    public Office getOffice() 
    {
        return office;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() 
    {
        return createTime;
    }

    /**
     * @return the company
     */
    public Company getCompany() 
    {
        return company;
    }

    /**
     * @return the creator
     */
    public Person getCreator() 
    {
        return creator;
    }

    /**
     * @return the movtitems
     */
    public List<Flow> getMovtitems() 
    {
        return movhequis;
    }

    /**
     * @return the folio
     */
    public String getFolio() 
    {
        return folio;
    }

    /**
     * @return the fhMov
     */
    public Date getFhMov() 
    {
        return fhMov;
    }

    /**
     * @return the tmov
     */
    public String getTmov() 
    {
        return tmov;
    }

    /**
     * @return the uso
     */
    public Uso getUso() 
    {
        return uso;
    }

    /**
     * @return the firma
     */
    public String getFirma() {
        return firma;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the sa
     */
    public String getSa() {
        return sa;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return the id
     */
    public int getID() 
    {
        return id;
    }

    private void clean() 
    {
        company = null;
        createTime = null;
        creator = null;
        fhMov = null;
        firma = null;
        folio = null;
        id = -1;
        movhequis = null;
        note = null;
        office = null;
        owner = null;
        sa = null;
        tmov = null;
        uso = null;
    }

    public Return<Integer> upUso(Connection connection,Uso uso) throws SQLException 
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null.");
        }
        if(uso == null)
        {
            return new Return<>(false,"connection is null.");
        }
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET uso='" + uso.getCode() + "' WHERE id=" + id;
        System.out.println(sql);
        Statement stmt = connection.createStatement();
        return new Return<>(true, (Integer)stmt.executeUpdate(sql));
    }

    public Return<Integer> upTMov(Connection connection,Tipo tipo) throws SQLException 
    {
        if(id < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null.");
        }
        if(tipo == null)
        {
            return new Return<>(false,"tmoz is null.");
        }
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET tmov='" + tipo.getCode() + "' WHERE id=" + id;
        //System.out.println(sql);
        Statement stmt = connection.createStatement();       
        int retV = (Integer)stmt.executeUpdate(sql);
        return new Return<>(true, retV);
    }

    public Return<Integer> upFirma(Connection connection, String firma_Prueba) throws SQLException 
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null.");
        }
        if(firma_Prueba == null)
        {
            return new Return<>(false,"firma is null.");
        }
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET firma='" + firma_Prueba + "' WHERE id=" + id;
        //System.out.println(sql);
        Statement stmt = connection.createStatement();        
        return new Return<>(true, (Integer)stmt.executeUpdate(sql));
    }

    public Return<Integer> upNote(Connection connection, String nota_de_Prueba) throws SQLException 
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null.");
        }
        if(nota_de_Prueba == null)
        {
            return new Return<>(false,"nota is null.");
        }
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET note='" + nota_de_Prueba + "' WHERE id=" + id;
        //System.out.println(sql);
        Statement stmt = connection.createStatement();        
        return new Return<>(true, (Integer)stmt.executeUpdate(sql));
    }

    Return<Integer> upSA(Connection connection,Remision remision) throws SQLException 
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null.");
        }
        if(remision == null)
        {
            return new Return<>(false,"sa is null.");
        }
        else if(remision.getID() < 1)
        {
            return new Return<>(false,"sa is is fail");
        }
            
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET sa='" + remision.getFolio() + "' ,sa2 = " + remision.getID();
        sql += " WHERE id=" + id;
        //System.out.println(sql);
        Statement stmt = connection.createStatement();        
        return new Return<>(true, (Integer)stmt.executeUpdate(sql));
    }
    
    Return<Integer> upOwner(Connection connection, Owner owner) throws SQLException 
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null.");
        }
        if(owner == null)
        {
            return new Return<>(false,"sa is null.");
        }
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET owner='";
        switch (owner) 
        {
            case SIIL:
                sql = sql + "SIIL' WHERE id=" + id;
                break;
            case CLIENT:
                sql = sql + "client' WHERE id=" + id;
                break;
            default:
                break;
        }
        //System.out.println(sql);
        Statement stmt = connection.createStatement();        
        return new Return<>(true, (Integer)stmt.executeUpdate(sql));
    }
    
    public Return<Integer> upCompany(Connection connection, Company company) throws SQLException 
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null.");
        }
        if(company == null)
        {
            return new Return<>(false,"company is null.");
        }
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET compNumber='" + company.getNumber() + "' WHERE id=" + id;
        //System.out.println(sql);
        Statement stmt = connection.createStatement();        
        return new Return(true, (Integer)stmt.executeUpdate(sql));
    }

    @Override
    public String getIdentificator() 
    {
        return (fhMov + " #" + folio);
    }

    @Override
    public String getBrief() 
    {
        return note;
    }
}
