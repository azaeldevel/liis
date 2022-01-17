
package SIIL.services.grua;

import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.trace.Trace;
import SIIL.trace.Value;
import core.FailResultOperationException;
import core.bobeda.Business;
import core.bobeda.Vaultable;
import database.mysql.stock.Titem;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import process.Return;
import stock.Flow;

/**
 *
 * @author Azael
 */
public class Resumov implements Vaultable
{
    private static final String MYSQL_AVATAR_TABLE = "Resumov";
    private static final String MYSQL_AVATAR_TABLE_BACKWARD_BD = "bc.tj";
    
    int id;
    private String BD;
    private Office office;
    private Uso uso;
    private String note;
    private Enterprise enterprise;
    private Date fhmov;
    private Battery battery;
    private Charger charger;
    private Flow titem;
    private Mina mina;
    private core.bobeda.Business poFile;
    
    public Boolean downPOFile(Database db) throws SQLException 
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT poFile FROM " + MYSQL_AVATAR_TABLE + " WHERE poFile IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            poFile = new core.bobeda.Business(new core.bobeda.Vault(rs.getInt(1)));
            return true;
        }
        else
        {
            return false;
        }        
    }
    
    /**
     * @return the poFIle
     */
    public core.bobeda.Business getPOFile() 
    {
        return poFile;
    }

    @Override
    public String getFolioInTable() 
    {
        return getTitem().getItem().getNumber();
    }

    @Override
    public Business getBusinesDocument() 
    {
        return getPOFile();
    }

    @Override
    public boolean downloadDataVault(Database dbserver) throws SQLException 
    {
        if(downTitem(dbserver))
        {
            if(getTitem().downItem(dbserver.getConnection()))
            {
                if(getTitem().getItem().downNumber(dbserver.getConnection()).isFlag())
                {
                    
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        downPOFile(dbserver);
        return getPOFile().download(dbserver).isFlag();
    }
        
    public enum Aditamentos
    {
        BATTERY,
        CHARGER
    }
        
    public Return upPOFile(Database connection, core.bobeda.Business poFile) throws SQLException 
    {
        if(id < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(false,"connection is null");
        }
        if(poFile == null)
        {
            return new Return(false,"poFile is null.");
        }
        this.poFile = null;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET poFile=" + poFile.getBobeda().getID() + " WHERE ID=" + id;
        java.sql.Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        return new Return(true, stmt.executeUpdate(sql));
    }
    
    public Boolean extractAditamento(Database db,Aditamentos aditamentos,Trace traceContext) throws SQLException
    {
        Titem titem = null;
        if(aditamentos == Aditamentos.BATTERY)
        {
            titem = downLinkedBattery(db);
            if(upBattery(db.getConnection(), null, traceContext).isFail()) return false;
        }
        else if(aditamentos == Aditamentos.CHARGER)
        {
            titem = downLinkedCharger(db);
            if(upCharger(db.getConnection(), null, traceContext).isFail()) return false;
        } 
        else
        {
            return false;
        }
        Flow flow = new Flow(-1);
        if(titem.downNumber(db.getConnection()).isFail()) return false;
        if(flow.selectTitemNumber(db, titem.getNumber()))
        {
            flow.downItem(db);
            flow.getItem().downNumber(db.getConnection());
        }
        else
        {
            return false;
        }
        
        return insert(db, getOffice(db), getUso(db), getCompany(db), flow, getFh(db), traceContext);
    }
    
    
    public Boolean downCompany(Database db) throws SQLException 
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT compBD,compNumber FROM " + MYSQL_AVATAR_TABLE + " WHERE compNumber IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            enterprise = Enterprise.find(db, rs.getString(2));
            return true;
        }
        else
        {
            return false;
        }        
    }
    
    public int remove(Database db) throws SQLException
    {
        if(id < 1)
        {
            return -1; 
        }
        if(db == null)
        {
            return -1;
        }
        
        int affected = 0;
        Statement stmt = db.getConnection().createStatement();
        
        String sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE  id = " + id;
        //System.out.println(sql);
        affected = stmt.executeUpdate(sql);        
        
        return affected;  
    }
    public boolean join(Database db,Flow flow,Trace traceContext) throws SQLException
    {
        Flow forklift = null;
        if(!Forklift.isForklift(db, (Titem) titem.getItem()))
        {
            throw new FailResultOperationException("Falló Resumov.join: No es un montacargas '" + titem.getItem() + "'");
        }
                
        if(Battery.isBattery(db,(Titem)flow.getItem()))
        {
            if(upBattery(db.getConnection(), flow, traceContext).isFlag())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if(Charger.isCharger(db,(Titem)((Flow)flow).getItem()))
        {
            if(upCharger(db.getConnection(), flow, traceContext).isFlag())
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
            throw new FailResultOperationException("Falló Resumov.join: No  se reconoce '" + titem.getItem() + "' com un equipo enlazable");
        }
    }
    
    public Date getFh(Database db) throws SQLException 
    {
        if(db == null)
        {
            return null;
        }
        String sql = "SELECT fhmov FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        Date fh = null;
        if(rs.next())
        {
            if(rs.getString(1) != null)
            {
                fh = rs.getDate(1);
                return fh;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    public Office getOffice(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return null;
        }
        String sql = "SELECT office,suc FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = connection.query(sql);
        Office office = null;
        if(rs.next())
        {
            if(rs.getInt(1) > 0)
            {
                office = new Office(rs.getInt(1));
                office.download(connection.getConnection());
                return office;
            }
            else if(rs.getString(2) != null)
            {
                office = new Office(-1);
                office.selectCode(connection,rs.getString(2));
                office.download(connection.getConnection());
                return office;
            }
            else
            {
                return null;
            }
        }
        else
        {
            office = null;
            return null;
        }
    }
    
    public Resumov extract(Database db,Aditamentos aditamentos,Trace traceContext) throws SQLException
    {
        if(Forklift.isForklift(db, (Titem) titem.getItem()))
        {
            Forklift forklift = (Forklift)titem.getItem();
            
            if(aditamentos == Aditamentos.BATTERY)
            {
                Battery olB = downLinkedBattery(db);
                if(olB != null)
                {
                    olB.downNumber(db.getConnection());
                    Flow flowBat = new Flow(-1);
                    flowBat.selectTitemNumber(db, olB.getNumber());
                    flowBat.downItem(db);
                    flowBat.getItem().downNumber(db.getConnection());
                    Resumov resumov = new Resumov(-1);
                    if(resumov.insert(db, getOffice(db), getUso(db), getCompany(), flowBat, getFh(db), traceContext))
                    {
                        return resumov;
                    }
                    else
                    {
                        throw new FailResultOperationException("Falló Resumov.extract.insert");
                    }
                }
                else
                {
                    return null;
                }
            }
            
            if(aditamentos == Aditamentos.CHARGER)
            {
                Charger olC = downLinkedCharger(db);
                if(olC != null)
                {
                    olC.downNumber(db.getConnection());
                    Flow flowCha = new Flow(-1);
                    flowCha.selectTitemNumber(db, olC.getNumber());
                    flowCha.downItem(db);
                    flowCha.getItem().downNumber(db.getConnection());
                    Resumov resumov = new Resumov(-1);
                    if(resumov.insert(db, getOffice(db), getUso(db), getCompany(), flowCha, getFh(db), traceContext))
                    {
                        return resumov;
                    }
                    else
                    {
                        throw new FailResultOperationException("Falló Resumov.extract.insert");
                    }
                }
                else
                {
                    return null;
                }
            }
        }
        
        return null;
    }
    public static Resumov findLinked(Database db, Flow hequi) throws SQLException 
    {
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        
        String sql;
        ResultSet rs;
        sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE batteryNumber = '" + hequi.getItem().getNumber() + "' OR chargerNumber = '" + hequi.getItem().getNumber()+ "'";
        rs = db.query(sql);
        if(rs.next())
        {
            return new Resumov(rs.getInt(1));
        }
        return null;
    }
    
    
    /**
     * Se el aditamento va a ser remplasado se genera un nuevo registro
     * @param connection
     * @param office
     * @param uso
     * @param company
     * @param flow
     * @param date
     * @param traceContext
     * @return
     * @throws SQLException 
     */
    public Boolean extractAditamento(Database connection,Office office,Uso uso,Company company, Flow flow,Date date,Trace traceContext) throws SQLException
    {
        //Flow deve ser un montacargas
        if(Forklift.isForklift(connection, (Titem) flow.getItem()))
        {
            Forklift forklift = (Forklift)flow.getItem();
            if(downLinkedBattery(connection) != null)
            {
                Battery olB = downLinkedBattery(connection);
                olB.downNumber(connection.getConnection());
                if(forklift.battery == null)
                {
                    Flow flowBat = new Flow(-1);
                    flowBat.selectTitemNumber(connection, olB.getNumber());
                    flowBat.downItem(connection);
                    flowBat.getItem().downNumber(connection.getConnection());
                    Resumov resumov = new Resumov(-1);
                    if(!resumov.insert(connection, office, uso, company, flowBat, date, traceContext)) return false;
                }
            }
            
            if(downLinkedCharger(connection) != null)
            {
                Charger olC = downLinkedCharger(connection);
                olC.downNumber(connection.getConnection());
                if(forklift.charger == null)
                {
                    Flow flowCha = new Flow(-1);
                    flowCha.selectTitemNumber(connection, olC.getNumber());
                    flowCha.downItem(connection);
                    flowCha.getItem().downNumber(connection.getConnection());
                    Resumov resumov = new Resumov(-1);
                    if(!resumov.insert(connection, office, uso, company, flowCha, date, traceContext)) return false;
                }
            }
            
            return true;
        }
        return true;
    }
    
    public Battery downLinkedBattery(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return null;
        }
        String sql = "SELECT batteryNumber FROM " + MYSQL_AVATAR_TABLE + " WHERE batteryNumber IS NOT NULL AND id = " + id;
        //System.out.println(sql);
        ResultSet rs = connection.query(sql);
        if(rs.next())
        {
            Battery battery = new Battery(-1);
            battery.selectNumber(connection.getConnection(), rs.getString(1));
            return battery;
        }
        else
        {
            return null;
        }
    }
    
    public Charger downLinkedCharger(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return null;
        }
        String sql = "SELECT chargerNumber FROM " + MYSQL_AVATAR_TABLE + " WHERE chargerNumber IS NOT NULL AND id = " + id;
        //System.out.println(sql);
        ResultSet rs = connection.query(sql);
        if(rs.next())
        {
            Charger charger = new Charger(-1);
            charger.selectNumber(connection.getConnection(), rs.getString(1));
            return charger;
        }
        else
        {
            return null;
        }
    }
        
    public Boolean selectRandom(Database connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";        
        ResultSet rs = connection.query(sql);
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
    
    public int removeNotThis(Database connection, Flow flow) throws SQLException 
    {
        if(id < 1)
        {
            return -1; 
        }
        if(connection == null)
        {
            return -1;
        }
        
        int affected = 0;
        Statement stmt = connection.getConnection().createStatement();
        
        String sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE titem = " + flow.getID() + " AND id != " + id;
        //System.out.println(sql);
        affected += stmt.executeUpdate(sql);        
        
        return affected;
    }
    
    public int removeDuplicated(Database connection, Flow flow) throws SQLException 
    {
        if(id < 1)
        {
            return -1; 
        }
        if(connection == null)
        {
            return -1;
        }
        
        int affected = 0;
        Statement stmt = connection.getConnection().createStatement();        
        //if(Forklift.isForklift(connection, (Titem) flow.getItem()))
        {
            //Forklift forklift = (Forklift)flow.getItem();
            //if(forklift.battery != null)
            {
                String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET batteryBD = NULL, batteryNumber = NULL WHERE batteryNumber = '" + flow.getItem().getNumber() + "' AND id != " + id;
                affected += stmt.executeUpdate(sql);
                //sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE titemNumber = '" + flow.getItem().getNumber() + "' AND id != " + id;
                //affected += stmt.executeUpdate(sql);
            }
            //if(forklift.charger != null)
            {
                String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET chargerBD = NULL, chargerNumber = NULL WHERE chargerNumber = '" + flow.getItem().getNumber() + "' AND id != " + id;
                affected += stmt.executeUpdate(sql);
                //sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE titemNumber = '" + flow.getItem().getNumber() + "' AND id != " + id ;
                //affected += stmt.executeUpdate(sql);
            }
        }
        
        //String sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE titem = " + flow.getID() + " AND id != " + id;
        //affected += stmt.executeUpdate(sql);        
        
        return affected;
    }
    
    @Deprecated
    public int removeAllReferences(Database connection, Flow flow) throws SQLException 
    {
        if(id < 1)
        {
            return -1; 
        }
        if(connection == null)
        {
            return -1;
        }
        
        int affected = 0;
        Statement stmt = connection.getConnection().createStatement();        
        if(Forklift.isForklift(connection, (Titem) flow.getItem()))
        {
            Forklift forklift = (Forklift)flow.getItem();
            if(forklift.battery != null)
            {
                String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET batteryBD = NULL, batteryNumber = NULL WHERE batteryNumber = '" + forklift.battery.getNumber() + "'" ;
                affected += stmt.executeUpdate(sql);
                sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE titemNumber = '" + forklift.battery.getNumber() + "'" ;
                affected += stmt.executeUpdate(sql);
            }
            if(forklift.charger != null)
            {
                String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET chargerBD = NULL, chargerNumber=NULL WHERE chargerNumber = '" + forklift.charger.getNumber() + "'" ;
                affected += stmt.executeUpdate(sql);
                sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE titemNumber = '" + forklift.charger.getNumber() + "'" ;
                affected += stmt.executeUpdate(sql);
            }
        }
        
        String sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE titem = " + flow.getID();
        affected += stmt.executeUpdate(sql);        
        
        return affected;
    }
       
    public static int counterRefences(Database connection, Flow flow) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE titem = " + flow.getID() + " OR titemNumber = '" + flow.getItem().getNumber() + "' OR chargerNumber = '" + flow.getItem().getNumber() + "' OR batteryNumber = '" + flow.getItem().getNumber() + "'";
        ResultSet rs = connection.query(sql);
        int count = 0;
        while(rs.next())
        {
            count++;
        }
        return count;
    }
    
    @Deprecated
    public int removeAditamento(Database connection, Flow flow) throws SQLException 
    {
        if(id < 1)
        {
            return -1; 
        }
        if(connection == null)
        {
            return -1;
        }
        
        //String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET forkliftBD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "'";
        int affected = 0;
        if(Forklift.isForklift(connection, (Titem) flow.getItem()))
        {
            Forklift forklift = (Forklift)flow.getItem();
            Statement stmt = connection.getConnection().createStatement();
            if(forklift.battery != null)
            {
                String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET batteryBD = NULL, batteryNumber = NULL WHERE batteryNumber = '" + forklift.battery.getNumber() + "'" ;
                affected += stmt.executeUpdate(sql);
                sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE titemNumber = '" + forklift.battery.getNumber() + "'" ;
                affected += stmt.executeUpdate(sql);
            }
            if(forklift.charger != null)
            {
                String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET chargerBD = NULL, chargerNumber = NULL WHERE chargerNumber = '" + forklift.charger.getNumber() + "'" ;
                affected += stmt.executeUpdate(sql);
                sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE titemNumber = '" + forklift.charger.getNumber() + "'" ;
                affected += stmt.executeUpdate(sql);
            }
        }
        
        return affected;
    }
    
    /**
     * Asigna el Item a el montacarga.
     * @param connection
     * @param flow
     * @param revomeRefenrences
     * @return
     * @throws SQLException 
     */
    public Boolean upAditamento(Database connection, Flow flow, boolean revomeRefenrences) throws SQLException 
    {
        if(id < 1)
        {
            return false; 
        }
        if(connection == null)
        {
            return false;
        }
        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET forkliftBD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "'";
        if(Forklift.isForklift(connection, (Titem) flow.getItem()))
        {
            Forklift forklift = (Forklift)flow.getItem();
            if(forklift.battery != null)
            {
                sql = sql + ",batteryBD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "',batteryNumber = '" + forklift.battery.getNumber() + "'" ;
            }
            else
            {
                sql = sql + ",batteryBD=NULL,batteryNumber=NULL" ;
            }
            if(forklift.charger != null)
            {
                sql = sql + ",chargerBD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "',chargerNumber = '" + forklift.charger.getNumber() + "'";
            }
            else
            {
                sql = sql + ",chargerBD=NULL,chargerNumber=NULL" ;
            }
            if(forklift.mina != null)
            {
                sql = sql + ",minaBD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "',minaNumber = '" + forklift.mina.getNumber() + "'";
            }
            else
            {
                sql = sql + ",minaBD=NULL,minaNumber=NULL";
            }
        }
        sql += " WHERE id = " + id;
        if(revomeRefenrences) removeNotThis(connection, flow);
        //System.out.println(sql);
        Statement stmt = connection.getConnection().createStatement();
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
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Enterprise getCompany(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return null;
        }
        String sql = "SELECT compNumber FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = connection.query(sql);
        Enterprise company = null;
        if(rs.next())
        {
            /*if(rs.getInt(1) > 0)
            {
                company = new Company(rs.getInt(1));
                company.download(connection);
                return company;
            }
            else*/if(rs.getString(1) != null)
            {
                company = new Enterprise(-1);
                company.setNumber(rs.getString(1));
                company.complete(connection);
                return company;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    public Flow getTitem()
    {
        return titem;
    }
    
    public Boolean upTitem(Database connection, Flow flow) throws SQLException 
    {
        if(id < 1)
        {
            return false; 
        }
        if(connection == null)
        {
            return false;
        }
        
        this.mina = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET titem=" + flow.getID() + ",titemBD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "', titemNumber = '" + flow.getItem().getNumber() + "'" ;
        if(Forklift.isForklift(connection, (Titem) flow.getItem()))
        {
            sql += ",forkliftBD='" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "',forkliftNumber='" + flow.getItem().getNumber() + "'" ;
        }
        sql += " WHERE id = " + id;
        //System.out.println(sql);
        Statement stmt = connection.getConnection().createStatement();
        if(stmt.executeUpdate(sql) == 1)       
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static Boolean emperejarV3(Database db) throws SQLException
    {
        String sql = "SELECT id,titemNumber FROM "  + MYSQL_AVATAR_TABLE + " WHERE titem IS NULL AND titemNumber IS NOT NULL";
        ResultSet rs = db.query(sql);
        Flow flow = null;
        Resumov resumov = null;
        boolean fl = false;
        while(rs.next())
        {
            resumov = new Resumov(rs.getInt(1));            
            flow = new Flow(-1);
            if(!flow.selectTitemNumber(db, rs.getString(2))) continue;
            flow.downItem(db);
            flow.getItem().downNumber(db.getConnection());
            if(!resumov.upTitem(db,flow)) return false;
        }
        
        return true;
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Uso getUso(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return null;
        }
        String sql = "SELECT `use`,uso FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = connection.query(sql);
        Uso uso = null;
        if(rs.next())
        {
            if(rs.getInt(1) > 0)
            {
                uso = new Uso(rs.getInt(1));
                uso.download(connection.getConnection());
                return uso;
            }
            else if(rs.getString(2) != null)
            {
                uso = new Uso(-1);
                uso.selectCode(connection,rs.getString(2));
                uso.download(connection.getConnection());
                return uso;
            }
            else
            {
                return null;
            }
        }
        else
        {
            uso = null;
            return null;
        }
    }
        
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Boolean downTitem(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT titem FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = connection.query(sql);
        if(rs.next())
        {
            if(rs.getInt(1) > 0)
            {
                this.titem = new Flow(rs.getInt(1));
                this.titem.downItem(connection.getConnection());
                this.titem.getItem().downNumber(connection.getConnection());
                return true;
            }
            return false;
        }
        else
        {
            return false;
        }        
    }
    
    public Return<Integer> upNote(Connection connection, String note,Trace traceContext) throws SQLException 
    {
        if(id < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null");
        }
        if(note == null)
        {
            return new Return<>(false,"note is null.");
        }
        this.note = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET note='" + note + "' WHERE id=" + id;
        Statement stmt = connection.createStatement();
        if(traceContext != null)
        {
            Value val = new Value();
            val.setTraceID(traceContext.getID());
            val.setTable(MYSQL_AVATAR_TABLE);
            int minLeng = Math.min(note.length(), 10);
            val.setAfter(note.substring(0, minLeng) + "...");
            val.setField("note");
            val.setBrief("Se Modifico el campo de comentario");    
            val.setLlave("folio= sin orden"); 
        }
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return<>(false,affected,"Cantidad de registos affectos es incorrecto : " + affected);
        }
        else
        {
            return new Return<>(true,affected);
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
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 0, 1";
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
    
    /**
     * 
     * @param id 
     */
    public Resumov(int id)
    {
        this.id = id;
    }
        
    /**
     * 
     * @param connection
     * @param office
     * @param uso
     * @param company
     * @param flow
     * @param date
     * @param traceContext
     * @param order
     * @return
     * @throws SQLException 
     */
    public Boolean insert(Database connection,Office office,Uso uso,Company company, Flow flow,Date date,Trace traceContext) throws SQLException
    {
        if(connection == null)
        {
            return false;
        }
        if(office == null)
        {
            return false;
        }
        if(uso == null)
        {
            return false;
        }
        if(flow == null)
        {
            return false;
        }
        if(date == null)
        {
            return false;
        }
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(BD,suc,office,uso,`use`,compBD,compNumber,fhmov";
        if(Forklift.isForklift(connection, (Titem) flow.getItem()))
        {
            Forklift fork = (Forklift)flow.getItem();
            sql = sql + ",titemBD,titemNumber,forkliftBD,forkliftNumber,titem";
            if(fork.battery != null)
            {
                sql = sql + ",batteryBD,batteryNumber";
            }
            if(fork.charger != null)
            {
                sql = sql + ",chargerBD,chargerNumber";
            }
        }
        else if(Battery.isBattery(connection, (Titem) flow.getItem()))
        {
            sql = sql + ",titem,titemBD,titemNumber";
        }
        else if(Charger.isCharger(connection, (Titem) flow.getItem()))
        {
            sql = sql + ",titem,titemBD,titemNumber";
        }
        else if(Mina.isMina(connection, (Titem) flow.getItem()))
        {
            sql = sql + ",titem,titemBD,titemNumber";
        }
        else
        {
            sql = sql + ",titemBD,titemNumber,titem";
        }
            
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sql = sql + ") VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + office.getCode() + "','" + office.getID() + "','" + uso.getCode() + "'," + uso.getID() + ",'" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + company.getNumber() + "','" + new java.sql.Date(date.getTime()) + "'" ;

        
        Value val = null;
        if(traceContext != null)
        {
            val = new Value();
            val.setTraceID(traceContext.getID());
            val.setTable(MYSQL_AVATAR_TABLE);
        }
        if(Forklift.isForklift(connection, (Titem) flow.getItem()))
        {
            Forklift fork = (Forklift)flow.getItem();
            sql = sql + ",'" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + fork.getNumber()  + "','" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + fork.getNumber() + "'," + flow.getID();
            if(fork.battery != null)
            {
                sql = sql + ",'" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + fork.battery.getNumber() + "'";
            }
            if(fork.charger != null)
            {
                sql = sql + ",'" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + fork.charger.getNumber() + "'";
            }
            if(traceContext != null)
            {
                val.setAfter(fork.getNumber());
                val.setField("titem");
                val.setBrief("Se agrego montacargas a hoja de Renta");    
                val.setLlave("folio= sin orden"); 
                val.insert(connection);
            }
        }
        else if(Battery.isBattery(connection, (Titem) flow.getItem()))
        {
            sql = sql + "," + flow.getID() + ",'" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + ((Battery)flow.getItem()).getNumber()  + "'";
            if(traceContext != null)
            {
                val.setAfter(((Battery)flow.getItem()).getNumber());
                val.setField("titem");
                val.setBrief("Se agrego Bateria a hoja de Renta");    
                val.setLlave("folio= sin orden"); 
            }
        }
        else if(Charger.isCharger(connection, (Titem) flow.getItem()))
        {
            sql = sql + "," + flow.getID() + ",'" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + ((Charger)(flow.getItem())).getNumber()  + "'" ;
            if(traceContext != null)
            {
                val.setAfter(((Charger)(flow.getItem())).getNumber());
                val.setField("titem");
                val.setBrief("Se agrego Cargador a hoja de Renta");    
                val.setLlave("folio= sin orden"); 
            }
        }
        else if(Mina.isMina(connection, (Titem) flow.getItem()))
        {
            sql = sql + "," + flow.getID() + ",'" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + ((Mina)(flow.getItem())).getNumber() + "'";
            if(traceContext != null)
            {
                val.setAfter(((Mina)(flow.getItem())).getNumber());
                val.setField("titem");
                val.setBrief("Se agrego Mina a hoja de Renta");    
                val.setLlave("folio= sin orden"); 
            }
        }
        else
        {
            sql = sql + "," + flow.getID() + ",'" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + flow.getItem().getNumber() + "'";
            if(traceContext != null)
            {
                val.setAfter(flow.getItem().getNumber());
                val.setField("titem");
                val.setBrief("Se agrego Equipo a hoja de Renta");    
                val.setLlave("folio= sin orden"); 
            }
        }
        sql = sql + ")";
        
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
            return true;
        }
        else
        {
            id = -1;
            return false;
        }
    }

    /**
     * @return the BD
     */
    public String getBD() {
        return BD;
    }

    /**
     * @return the office
     */
    public Office getOffice() {
        return office;
    }

    /**
     * @return the uso
     */
    public Uso getUso() {
        return uso;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the company
     */
    public Enterprise getCompany() {
        return enterprise;
    }

    /**
     * @return the fhmov
     */
    public Date getFhmov() {
        return fhmov;
    }

    /**
     * @return the battery
     */
    public Battery getBattery() {
        return battery;
    }

    /**
     * @return the charger
     */
    public Charger getCharger() {
        return charger;
    }

    public Return upMina(Connection connection, Mina mina,Trace traceContext) throws SQLException 
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"connection is null");
        }
        if(mina == null)
        {
            return new Return(Return.Status.FAIL,"mina is null.");
        }
        this.mina = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET minaNumber='" + mina.getNumber() + "', minaBD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' WHERE id=" + id;
        Statement stmt = connection.createStatement();
        Value val = new Value();
        val.setTraceID(traceContext.getID());
        val.setTable(MYSQL_AVATAR_TABLE);
        val.setAfter(mina.getNumber());
        val.setField("mina");
        val.setBrief("Se agrego mina a hoja de Renta");    
        val.setLlave("folio= sin orden"); 
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }

    public Return upBattery(Connection connection, Flow battery,Trace traceContext) throws SQLException 
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"connection is null");
        }
        this.battery = null;
        String sql = "";
        if(battery != null)
        {
            sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET batteryNumber='" + battery.getItem().getNumber() + "', batteryBD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' WHERE id=" + id;
        }
        else
        {
            sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET batteryNumber=NULL,batteryBD=NULL WHERE id=" + id;
        }
        //System.out.println(sql);
        Statement stmt = connection.createStatement();  
        if(traceContext != null)
        {
            Value val = new Value();
            val.setTraceID(traceContext.getID());
            val.setTable(MYSQL_AVATAR_TABLE);
            val.setLlave("folio= sin orden"); 
            val.setField("battery");
            val.setBrief("Se agrego Bateria a hoja de Renta");    
            val.setLlave("folio= sin orden");
        }
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }

    public Return upCharger(Connection connection, Flow charger,Trace traceContext) throws SQLException 
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"connection is null");
        }
        this.charger = null;
        String sql = "";
        if(charger != null)
        {
            sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET chargerNumber='" + charger.getItem().getNumber() + "',chargerBD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "'" + " WHERE id=" + id;
        }
        else
        {
            sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET chargerNumber=NULL, chargerBD=NULL WHERE id=" + id;
        }
        Statement stmt = connection.createStatement();
        if(traceContext != null)
        {
            Value val = new Value();
            val.setTraceID(traceContext.getID());
            val.setTable(MYSQL_AVATAR_TABLE);
            val.setLlave("folio= sin orden"); 
            val.setField("charger");
            val.setBrief("Se agrego cargador a hoja de Renta");    
            val.setLlave("folio= sin orden"); 
        }
        //System.out.println(sql);
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }

    /**
     * @return the mina
     */
    public Mina getMina() {
        return mina;
    }

    private void clean() 
    {
        id = -1;
        BD = null;
        battery = null;
        charger = null;
        enterprise = null;
        fhmov = null;
        titem = null;
        mina = null;
        note = null;
        office = null;
        uso = null;
    }

    public boolean find(Database db, Flow hequi) throws SQLException 
    {
        clean();
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        
        String sql;
        ResultSet rs;
        sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE titem = " + hequi.getID() + " OR titemNumber = '" + hequi.getItem().getNumber()+ "'";
        rs = db.query(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return true;
        }
        this.id = -1;
        return false;
    }
    public boolean find(Database db, Titem titem) throws SQLException 
    {
        clean();
        if(titem.getID() <= 0) throw new InvalidParameterException("Dato invalido");
        if(db == null) throw new InvalidParameterException("Connection is null.");
        
        String sql;
        ResultSet rs;
        sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE titemNumber = '" + titem.getNumber() + "'";
        //System.out.println(sql);
        rs = db.query(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return true;
        }
        
        this.id = -1;
        return false;
    }
    
    boolean upOffice(Database db, Office office, Trace traceContext) throws SQLException 
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID incorrecto."); 
        }
        if(db == null)
        {
            throw new InvalidParameterException("Conecion a BD incorrecta."); 
        }
        this.office = null;
        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET suc = '" + office.getCode() + "', office = " + office.getID() + " WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return true;
        }
        else if(retUp > 1)
        {
            throw new FailResultOperationException("Se afectaron '" + retUp + "' registros ");
        }
        else if(retUp == 0)
        {
            return false;
        }
        else            
        {
            throw new FailResultOperationException("Se afectaron '" + retUp + "' registros ");
        }
    }

    boolean upCompany(Database db, Company company, Trace traceContext) throws SQLException 
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID incorrecto."); 
        }
        if(db == null)
        {
            throw new InvalidParameterException("Conecion a BD incorrecta."); 
        }
        this.enterprise = null;
        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET compBD = 'bc.tj', compNumber = '" + company.getNumber() + "' WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return true;
        }
        else if(retUp > 1)
        {
            throw new FailResultOperationException("Se afectaron '" + retUp + "' registros ");
        }
        else if(retUp == 0)
        {
            return false;
        }
        else            
        {
            throw new FailResultOperationException("Se afectaron '" + retUp + "' registros ");
        }
    }

    boolean upFecha(Database db, Date fecha, Trace traceContext) throws SQLException 
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID incorrecto."); 
        }
        if(db == null)
        {
            throw new InvalidParameterException("Conecion a BD incorrecta."); 
        }
        this.fhmov = null;
        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET fhMov = '" + new java.sql.Date(fecha.getTime()) + "' WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return true;
        }
        else if(retUp == 0)
        {
            return false;
        }
        else if(retUp > 1)
        {
            throw new FailResultOperationException("Se afectaron '" + retUp + "' registros ");
        }
        else            
        {
            throw new FailResultOperationException("Se afectaron '" + retUp + "' registros ");
        }
    }

    boolean upUso(Database db, Uso uso, Trace traceContext) throws SQLException 
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID incorrecto."); 
        }
        if(db == null)
        {
            throw new InvalidParameterException("Conecion a BD incorrecta."); 
        }
        this.uso = null;        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET uso = '" + uso.getCode() + "', `use`= " + uso.getID() + " WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return true;
        }
        else if(retUp == 0)
        {
            return false;
        }
        else
        {
            throw new FailResultOperationException("Se afectaron '" + retUp + "' registros ");
        }
    }    
}
