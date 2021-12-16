
package SIIL.service.quotation;

import SIIL.CN.Tables.COTIZACI;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import session.Credential;
import SIIL.trace.Value;
import java.sql.Statement;
import core.Folio;
import core.bobeda.Vaultable;
import core.bobeda.Business;
import database.mysql.sales.Quotation;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import process.Module;
import process.Return;
import process.State;

/**
 * Manejador BD de Tabla Orcom
 * @author Azael
 */
public class ServiceQuotation extends Orcom implements Vaultable
{
    public static final String MYSQL_AVATAR_TABLE = "Orcom";
    public final String CN_SERIE = "CN";
    
    protected SIIL.Server.Person technical;
    protected session.Credential credential;
    protected com.galaxies.andromeda.util.Progress progress;
    protected Database database;
    private Date fhCretion;
    private Date fhEdit;
    private Date fhETAfl;
    private Date fhEnd;
    protected String terminalComment;
    private Date fhSurtir;
    protected Throwable fail;
    private Quotation quotation;
    private Person pAutho;
    private core.bobeda.Business poFile;
    
    
    /**
     * Busca los registros cuyo campo company esta en null y asigna el ID 
     * correspondiente de la tabla Companies, Este prcedimiento es 
     * necesario para matner compatibilidad con la base de datos 
     * Comercial por que no maneja numero de cliente.
     * 
     * @param connection
     * @return 
     */
    public static boolean fillCompanies(Database connection) throws SQLException
    {
        String queryStr = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE  company is NULL";//todas las que no tiene id en la BD de tools
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(queryStr);
        while(rs.next())
        {
            ServiceQuotation q = new ServiceQuotation(rs.getInt(1));
            //primer hay que importar las cotizaciones
        }
        return true;
    }
    
    public static ArrayList<ServiceQuotation> selectServiceQuotation(Database dbserver,ArrayList<Quotation> qs) throws SQLException
    {
        ArrayList<ServiceQuotation> sqs = new ArrayList<>();
        for(Quotation qt : qs)
        {
            String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE quotation = " + qt.getID();
            ResultSet rs = dbserver.query(sql);
            if(rs.next())
            {
                sqs.add(new ServiceQuotation(rs.getInt(1)));
            }
        }
        if(sqs.size() > 0) return sqs;
        return null;
    }
    
    public void select(Database connection, int id)
    {
        ID = id;
    }
    
    public Return importFromCN(Database dbserver, COTIZACI tbCotiza, Office office) throws SQLException, IOException
    {
        SIIL.CN.Records.COTIZACI recCotiza = tbCotiza.readWhere(getFolio());
        
        Quotation oper = null;
        if(getSerie() == null) downSerie(dbserver.getConnection());
        if(getSerie() == null) return new Return(false,"No hay serie");
        downQuotation(dbserver.getConnection());
        if(getQuotation() != null)
        {
            quotation = getQuotation();
            quotation.upTotal(dbserver, recCotiza.getTotal());
        }
        else
        {
            quotation = new Quotation(-1);
            downCompany(dbserver);
            String serie = "";
            Return retOpe = quotation.insert(dbserver, office, new State(1),getCredential().getUser(),dbserver.getTimestamp(), (Enterprise) getCompany(),getFolio(),getSucursal().getSerieOffice(Office.Platform.CN60),"SQ",this);
            if(retOpe.isFail()) return retOpe;
            quotation.upFlag(dbserver.getConnection(), 'A');
            if(retOpe.isFail()) return retOpe;
            Return<Integer> retQ = upQuotation(dbserver.getConnection(),quotation);
            if(retQ.isFail()) return retQ;            
            quotation.upTotal(dbserver, recCotiza.getTotal());
        }
        
        return new Return(true);
    }
    
    
    public Return downFhETA(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Connection is null.");
        }
        String sql = "SELECT fhETA FROM " + MYSQL_AVATAR_TABLE + " WHERE fhETA IS NOT NULL AND id = " + getID();
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            fhETA = new Date();
            fhETA.setTime(rs.getDate(1).getTime());
            return new Return(true);
        }
        else
        {
            fhETA = null;
            return new Return(false,"No se encontro nigun registro para el ID = " + getID());
        }        
    }
       
    public Return downOwner2(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Connection is null.");
        }
        String sql = "SELECT ownerPerson2 FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getID();
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            owner2 = new Person(rs.getInt(1));
            owner2.download(connection);
            return new Return(true);
        }
        else
        {
            owner2 = null;
            return new Return(false,"No se encontro nigun registro para el ID = " + getID());
        }        
    }
        
    public Return downOwner(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Connection is null.");
        }
        String sql = "SELECT ownerPerson FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getID();
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            owner = new Person(rs.getInt(1));
            owner.download(connection);
            return new Return(true);
        }
        else
        {
            owner = null;
            return new Return(false,"No se encontro nigun registro para el ID = " + getID());
        }        
    }
    
    public static List<ServiceQuotation> getSamePO(Database db,core.bobeda.Business business) throws SQLException
    {
        if(db == null)
        {
            return null;
        }
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE poFile = " + business.getBobeda().getID();
        java.sql.Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<ServiceQuotation> list = new ArrayList<>();
        while(rs.next())
        {
            ServiceQuotation qs = new ServiceQuotation(rs.getInt(1));
            //if(qs.downQuotation(db.getConnection()).isFlag()) qs.getQuotation().download(db.getConnection());
            //if(qs.downPOFile(db).isFlag()) qs.getPOFile().download(db);
            list.add(qs);
        }
        return list;
    }
    
    public Boolean downState(Database db) throws SQLException 
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT estado FROM " + MYSQL_AVATAR_TABLE + " WHERE estado IS NOT NULL AND id = " + getID();
        java.sql.Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            state = new State(-1);
            state.select(db.getConnection(), new Module(6), rs.getString(1));
            return true;
        }
        else
        {
            state = null;
            return false;
        }        
    }
    
    public Boolean downPAutho(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT pAutho FROM " + MYSQL_AVATAR_TABLE + " WHERE pAutho > 0 AND id = " + getID();
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            pAutho = new Person(rs.getInt(1));
            return true;
        }
        else
        {
            pAutho = null;
            return false;
        }        
    }
    
    @Override
    public String toString()
    {
        return getFullFolio();
    }
    public static List<ServiceQuotation> searchByFolio(Database db,Office office,Folio folio, int limit) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE suc IS NOT NULL " ;
        if(folio.getSerie() != null)
        {
            sql = sql + " AND serie = '" + folio.getSerie() + "'" ;
        }
        if(folio.getNumber() > 0)
        {
            sql = sql + " AND folio = " + folio.getNumber();
        }
        
        sql +=  " LIMIT " + limit;
        
        System.out.println(sql);
        ResultSet rs = db.query(sql);
        ArrayList<ServiceQuotation> orders = new ArrayList<>();
        while(rs.next())
        {
            ServiceQuotation orden = new ServiceQuotation();
            orden.setID(rs.getInt(1));
            orders.add(orden);
        }
        return orders;
    }
    
    public static List<ServiceQuotation> searchByFolio(Database db,Office office,String text, int limit) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE suc = '" + office.getCode() + "' AND folio like'%" + text + "%' LIMIT " + limit;
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        ArrayList<ServiceQuotation> orders = new ArrayList<>();
        while(rs.next())
        {
            ServiceQuotation orden = new ServiceQuotation();
            orden.setID(rs.getInt(1));
            orders.add(orden);
        }
        return orders;
    }
    public String getFullFolio()
    {
        String fullfolio = null;
        if(getSerie() != null)
        {
            fullfolio = getSerie();
        }
        else
        {
            fullfolio = "";
        }
        //
        if(getFolio() != null)
        {
            fullfolio += getFolio();
        }
        return fullfolio;
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public String getFullFolio(Connection connection) throws SQLException
    {
        if(getSerie() == null)downSerie(connection);
        
        String fullfolio = null;
        if(getSerie() != null)
        {
            fullfolio = getSerie();
        }
        else
        {
            fullfolio = "";
        }
        //
        if(getFolio() != null)
        {
            fullfolio += getFolio();
        }
        return fullfolio;
    }
    
    /**
     * 
     * @param connection
     * @param serie
     * @return
     * @throws SQLException 
     */
    public Return<Integer> upSerie(Connection connection, String serie) throws SQLException 
    {
        if(getID() < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null");
        }
        if(serie == null)
        {
            return new Return<>(false,"Quotation is null.");
        }
        this.serie = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET serie='" + serie + "' WHERE id=" + getID();
        java.sql.Statement stmt = connection.createStatement();
        //System.out.println(sql);
        return new Return<>(true, stmt.executeUpdate(sql));
    }
    
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> downSerie(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT serie FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getID();
        java.sql.Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            serie = rs.getString(1);
            return new Return<>(true);
        }
        else
        {
            serie = null;
            return new Return<>(false,"No se encontro nigun registro para el ID = " + getID());
        }        
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> downPOFile(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT poFIle FROM " + MYSQL_AVATAR_TABLE + " WHERE poFIle > 0 AND id = " + getID();
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            poFile = new core.bobeda.Business(new core.bobeda.Vault(rs.getInt(1)));
            return new Return(true);
        }
        else
        {
            poFile = null;
            return new Return(false,"No se encontro nigun registro para el ID = " + getID());
        }        
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> downQuotation(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT quotation FROM " + MYSQL_AVATAR_TABLE + " WHERE quotation > 0 AND id = " + getID();
        java.sql.Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            quotation = new Quotation(rs.getInt(1));
            return new Return<>(true);
        }
        else
        {
            quotation = null;
            return new Return<>(false,"No se encontro nigun registro para el ID = " + getID());
        }        
    }
    
    public Return<Integer> upQuotation(Connection connection, Quotation quotation) throws SQLException 
    {
        if(getID() < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null");
        }
        if(quotation == null)
        {
            return new Return<>(false,"Quotation is null.");
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET quotation=" + quotation.getID() + " WHERE id=" + getID();
        java.sql.Statement stmt = connection.createStatement();
        //System.out.println(sql);
        return new Return<>(true, stmt.executeUpdate(sql));
    }
    
    public Throwable getFail()
    {
        return fail;
    }
    
    public ServiceQuotation(ServiceQuotation ord)
    {
        super(ord);
        this.technical = ord.technical;
        this.credential = ord.credential;
        this.progress = ord.progress;
        this.database = ord.database;
    }
    
    public int updateFin() throws SQLException 
    {
        java.sql.Timestamp fh = database.getTimestamp();
        state.select(database, State.Steps.CS_FIN);
        String sql = "UPDATE Orcom SET fhFin = ?, estado = ? WHERE BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmt = database.getConnection().prepareStatement(sql);
        pstmt.setTimestamp(1, fh);
        pstmt.setString(2, getState().getCode());
        pstmt.setString(3, credential.getBD());
        pstmt.setInt(4, folio);
        pstmt.setInt(5, ID);
        
        Value val;
        int flV = 0;
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("fhFin");
        val.setBefore("*");
        val.setAfter(String.valueOf(fh.getTime()));
        val.setBrief("Hora de Operación");
        val.setLlave("folio="+folio);
        flV += val.insert(database);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("estado");
        val.setBefore("*");
        val.setAfter(getState().getCode());
        val.setBrief("Bandera de Estado");
        val.setLlave("folio="+folio);
        flV += val.insert(database);
        
        return pstmt.executeUpdate();
    }
    
    public int updateSurtir() throws SQLException 
    {        
        java.sql.Timestamp  fh = database.getTimestamp();
        state.select(database, State.Steps.CS_ENTREGANDO);
        String sqlOp = "UPDATE Orcom SET fhSurtido = ?, estado = ?, sa = ? WHERE  BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmtOp = database.getConnection().prepareStatement(sqlOp);
        pstmtOp.setTimestamp(1, fh);
        pstmtOp.setString(2, getState().getCode());
        pstmtOp.setString(3, sa);
        pstmtOp.setString(4, credential.getBD());
        pstmtOp.setInt(5, folio);
        pstmtOp.setInt(6, ID);
        
        String sqlS = "SELECT estado, fhSurtido,sa FROM Orcom WHERE  BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmtS = database.getConnection().prepareStatement(sqlS);
        pstmtS.setString(1, credential.getBD());
        pstmtS.setInt(2, folio);
        pstmtS.setInt(3, ID);
        ResultSet rs = pstmtS.executeQuery();
        Estado estado = null;
        java.sql.Timestamp fhSurtido = null;
        String saold = null;
        if(rs.next())
        {
            estado = new Estado();
            estado.setCode(rs.getString("estado"));
            fhSurtido = rs.getTimestamp("fhSurtido");
            saold = rs.getString("sa");
        }
        
        Value val;
        int flV = 0;
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("fhSurtido");
        if(fhSurtido != null)
        {
            val.setBefore(String.valueOf(fhSurtido.getTime()));
        }
        val.setAfter(String.valueOf(fh.getTime()));
        val.setBrief("Hora de Operación");
        val.setLlave("folio="+folio);
        flV += val.insert(database);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("estado");
        if(estado != null)
        {
            val.setBefore(getState().getCode());
        }
        val.setAfter(getState().getCode());
        val.setBrief("Bandera de Estado");
        val.setLlave("folio="+folio);
        flV += val.insert(database);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("sa");
        if(saold != null)
        {
            val.setBefore(saold);
        }
        val.setAfter(sa);
        val.setBrief("Asignación de S.A.");
        val.setLlave("folio="+folio);
        flV += val.insert(database);        
        
        return pstmtOp.executeUpdate();
    }

    public int updateArribo() throws SQLException 
    {                 
        java.sql.Timestamp  fh = database.getTimestamp();
        state.select(database, State.Steps.CS_ALMACEN);
        
        String sqlA = "UPDATE Orcom SET fhArribo=?,estado=? WHERE  BD = ? and folio = ?  and ID = ?";
        PreparedStatement pstmtOp = database.getConnection().prepareStatement(sqlA);
        pstmtOp.setTimestamp(1, fh);
        pstmtOp.setString(2, getState().getCode());
        pstmtOp.setString(3, "bc.tj");
        pstmtOp.setInt(4, folio);
        pstmtOp.setInt(5, ID);
        
        String sqlOp = "SELECT estado, fhArribo FROM Orcom WHERE  BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmt = database.getConnection().prepareStatement(sqlOp);
        pstmt.setString(1, "bc.tj");
        pstmt.setInt(2, folio);
        pstmt.setInt(3, ID);
        ResultSet rs = pstmt.executeQuery();
        Estado estado = null;
        java.sql.Timestamp fhArribo = null;    
        if(rs.next())
        {
            estado = new Estado();
            estado.setCode(rs.getString("estado"));
            fhArribo = rs.getTimestamp("fhArribo");
        }  
        
        if(trace != null)
        {
            Value val;
            int flV = 0;
            val = new Value();
            val.setTraceID(trace.getTrace());
            val.setTable("Orcom");
            val.setField("fhArribo");
            if(fhArribo != null)
            {
                val.setBefore(String.valueOf(fhArribo.getTime()));
            }
            val.setAfter(String.valueOf(fh.getTime()));
            val.setBrief("Hora de Operación");
            val.setLlave("folio="+folio);
            flV += val.insert(database);
            val = new Value();
            val.setTraceID(trace.getTrace());
            val.setTable("Orcom");
            val.setField("estado");
            if(estado != null)
            {
                val.setBefore(estado.getCode());
            }
            val.setAfter(getState().getCode());
            val.setBrief("Bandera de Estado");
            val.setLlave("folio="+folio);
            flV += val.insert(database);
        }
        
        return pstmtOp.executeUpdate();
    }
    
    public int updateEdit() throws SQLException 
    {
        //Gurdando los datos
        state.select(database, State.Steps.CS_QUOTED);
        java.sql.Timestamp  fh = database.getTimestamp();
        String sqlOp = "UPDATE Orcom SET fhEdit = ?, estado = ? WHERE   BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmtOp = getServerDB().getConnection().prepareStatement(sqlOp);
        pstmtOp.setTimestamp(1, fh);
        pstmtOp.setString(2, state.getCode());
        pstmtOp.setString(3, credential.getBD());
        pstmtOp.setInt(4, folio);
        pstmtOp.setInt(5, ID);
        
        //Trace
        String sqltrace = "SELECT estado, fhEdit FROM Orcom WHERE  BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmttrace = getServerDB().getConnection().prepareStatement(sqltrace);
        pstmttrace.setString(1, credential.getBD());
        pstmttrace.setInt(2, folio);
        pstmttrace.setInt(3, ID);
        
        ResultSet rs = pstmttrace.executeQuery();
        Estado estado = null;
        java.sql.Timestamp fhEdit = null;        
        if(rs.next())
        {
            estado = new Estado();
            estado.setCode(rs.getString("estado"));
            fhEdit = rs.getTimestamp("fhEdit");
        }
        
        Value val;
        int flV = 0;
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("fhEdit");
        if(fhEdit != null)
        {
            val.setBefore(String.valueOf(fhEdit.getTime()));
        }
        val.setAfter(String.valueOf(fh.getTime()));
        val.setBrief("Hora de Edición");
        val.setLlave("folio="+folio);
        flV += val.insert(getServerDB());
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("estado");
        if(estado != null)
        {
            val.setBefore(estado.getCode());            
        }
        val.setAfter(Estado.STEP_TWO);
        val.setBrief("Bandera de Estado");
        val.setLlave("folio="+folio);
        flV += val.insert(getServerDB());
        
        return pstmtOp.executeUpdate();
    }
    
    public ServiceQuotation()
    {
        ;
    }
    public ServiceQuotation(int ID)
    {
        super.ID = ID;
    } 
    
    public ServiceQuotation(Integer ID,session.Credential cred)
    {
        super.ID = ID;
        credential = cred;
    }   
    
    public void setCredential(session.Credential cred)
    {
        this.credential = cred;
    }
    
    public SIIL.Server.Person getOwner2()
    {
        return owner2;
    }
    
    public SIIL.Server.Person getOwner()
    {
        return owner;
    }
    
    public SIIL.client.sales.Enterprise getEntreprise()
    {
        return company;
    }
    
    public SIIL.Server.Person getTechnical()
    {
        return technical;
    }
    
    public void setTechnical(SIIL.Server.Person tec)
    {
        technical = tec;
    }
    
    public boolean setFolio(String folio)
    {
        Integer f = 0;
        try
        {
            if(folio.matches("^[0-9]+$"))
            {
                f = Integer.valueOf(folio);
                super.setFolio(f);
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(NumberFormatException ex)
        {
            return false;            
        }
    }

    /**
     * @return the credential
     */
    public session.Credential getCredential() {
        return credential;
    }

    public Throwable fill(Database db,Credential cred, int id) 
    {
        credential = cred;
        database = db;
        Throwable ret = super.fill(db,cred,id);
        if(ret != null) return ret;
                
        String query = "SELECT technicalPerson,terminalComment,poFIle FROM Orcom WHERE BD = '" + cred.getBD() + "' and ID = " + id;
        Statement st;
        try 
        {
            st = (Statement) db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            //System.out.print(query);
            if(rs.next())
            {
                //System.out.println("<<");
                technical = new Person();
                ret = technical.fill(db, rs.getInt("technicalPerson"), cred.getBD());
                if(ret != null) return ret;
                note = rs.getString("terminalComment");
                if(rs.getInt(3) != 0)
                {
                    poFile = new core.bobeda.Business(new core.bobeda.Vault(rs.getInt(3))); 
                    poFile.download(db);
                }
                else
                {
                    poFile = null;
                }
            }
            else
            {
                return new java.lang.Error("No se encontraron datos para el Documento [" + query + "]");
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
        return null;
    }

    /**
     * @return the progress
     */
    public com.galaxies.andromeda.util.Progress getProgressObject() 
    {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgressObject(com.galaxies.andromeda.util.Progress progress) 
    {
        this.progress = progress;
    }

    /**
     * @return the serverDB
     */
    public Database getServerDB() {
        return database;
    }

    /**
     * @param serverDB the serverDB to set
     */
    public void setServerDB(Database serverDB) {
        this.database = serverDB;
    }

    public Throwable fillDetail(Database db, Credential cred, int id) 
    {
        Throwable fill;
        //si ya ha sido estainicializada solo completa
        if(id != getID())
        {
            fill = this.fill(db, cred, id);
            if(fill != null) return fill;
        }
        
        String sql = "SELECT fhFolio,fhEdit,fhAutho,fhETA,fhETAfl,fhSurtido,fhFin,fhArribo,terminalComment,poFile FROM Orcom WHERE ID = " + getID();
        Statement stmt;
        try 
        {
            stmt = (Statement) db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                fhCretion = new Date(rs.getTimestamp("fhFolio").getTime());
                if(rs.getTimestamp("fhEdit") != null) fhEdit = new Date(rs.getTimestamp("fhEdit").getTime());
                if(rs.getTimestamp("fhAutho") != null) fhAutho = new Date(rs.getTimestamp("fhAutho").getTime());
                if(rs.getTimestamp("fhETA") != null) fhETA = new Date(rs.getTimestamp("fhETA").getTime());
                if(rs.getTimestamp("fhETAfl") != null) fhETAfl = new Date(rs.getTimestamp("fhETAfl").getTime());
                if(rs.getTimestamp("fhArribo") != null) fhArribo = new Date(rs.getTimestamp("fhArribo").getTime());
                if(rs.getTimestamp("fhSurtido") != null) fhSurtir = new Date(rs.getTimestamp("fhSurtido").getTime());                
                if(rs.getTimestamp("fhFin") != null) fhEnd = new Date(rs.getTimestamp("fhFin").getTime()); 
                terminalComment = rs.getString("terminalComment");
                if(rs.getInt(10) != 0)
                {
                    poFile = new core.bobeda.Business(new core.bobeda.Vault(rs.getInt(10)));                    
                }
                else
                {
                    poFile = null;
                }
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ServiceQuotation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return super.fillDetail(db, cred, id);        
    }

    /**
     * @return the fhCretion
     */
    public Date getFhCretion() {
        return fhCretion;
    }

    /**
     * @param fhCretion the fhCretion to set
     */
    public void setFhCretion(Date fhCretion) {
        this.fhCretion = fhCretion;
    }

    /**
     * @return the fhEdit
     */
    public Date getFhEdit() {
        return fhEdit;
    }

    /**
     * @param fhEdit the fhEdit to set
     */
    public void setFhEdit(Date fhEdit) {
        this.fhEdit = fhEdit;
    }

    /**
     * @return the fhETAfl
     */
    public Date getFhETAfl() {
        return fhETAfl;
    }

    /**
     * @param fhETAfl the fhETAfl to set
     */
    public void setFhETAfl(Date fhETAfl) {
        this.fhETAfl = fhETAfl;
    }

    /**
     * @return the fhEnd
     */
    public Date getFhEnd() {
        return fhEnd;
    }

    /**
     * @param fhEnd the fhEnd to set
     */
    public void setFhEnd(Date fhEnd) {
        this.fhEnd = fhEnd;
    }

    /**
     * @return the fhSurtir
     */
    public Date getFhSurtir() {
        return fhSurtir;
    }

    /**
     * @param fhSurtir the fhSurtir to set
     */
    public void setFhSurtir(Date fhSurtir) {
        this.fhSurtir = fhSurtir;
    }   

    public void setTerminalComment(String comment) 
    {
        this.terminalComment = comment;
    } 
    
    public String getTerminalComment()
    {
        return this.terminalComment;
    }

    /**
     * @return the quotation
     */
    public Quotation getQuotation() {
        return quotation;
    }

    /**
     * @return the pAutho
     */
    public Person getPAutho() {
        return pAutho;
    }

    public boolean download(Database db) throws SQLException 
    {
        String sql = "SELECT pAutho,quotation,serie,poFile FROM " + MYSQL_AVATAR_TABLE + " WHERE pAutho > 0 AND id = " + getID();
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            pAutho = new Person(rs.getInt(1));
            quotation = new Quotation(rs.getInt(2));
            serie = rs.getString(3);
            if(rs.getInt(4) != 0)
            {
                poFile = new core.bobeda.Business(new core.bobeda.Vault(rs.getInt(4)));                    
            }
            else
            {
                poFile = null;
            }
            downState(db);
            return true;
        }
        else
        {
            pAutho = null;
            return false;
        }  
    }

    /**
     * @return the poFile
     */
    public core.bobeda.Business getPOFile() 
    {
        return poFile;
    }
    

    public Return upPOFile(Database connection, core.bobeda.Business poFile) throws SQLException 
    {
        if(getID() < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(false,"connection is null");
        }
        this.poFile = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET poFile=";
        if(poFile == null)
        {
            sql = sql + " NULL ";
        }
        else
        {
            sql = sql + poFile.getBobeda().getID() ;
        }
        sql = sql + " WHERE ID = " + getID();
        java.sql.Statement stmt = connection.getConnection().createStatement();
        System.out.println(sql);
        return new Return<>(true, stmt.executeUpdate(sql));
    }

    private void clean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Business getBusinesDocument() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getFolioInTable() 
    {
        return getFolio().toString();
    }

    @Override
    public boolean downloadDataVault(Database dbserver) throws SQLException
    {
        downFolio(dbserver.getConnection());
        return download(dbserver);
    }
}
