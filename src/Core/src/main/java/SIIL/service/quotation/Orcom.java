
package SIIL.service.quotation;

import SIIL.Server.Database;
import SIIL.Server.MySQL;
import SIIL.Server.Person;
import SIIL.core.Office;
import SIIL.client.sales.Enterprise;
import session.Credential;
import session.User;
import SIIL.core.Badable;
import SIIL.trace.Value;
import java.sql.Statement;
import core.FailResultOperationException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import process.Return;
import process.State;

/**
 * Manejador viejo para tabla Orcom, bacwark compatible
 * @version 3.0
 * @author Azael Reyes
 */
@Deprecated
public class Orcom implements Badable
{
    private static final String MYSQL_AVATAR_TABLE = "Orcom";
    protected static final String MYSQL_AVATAR_TABLE_BACKWARD_BD = "bc.tj";
    
    protected String note;
    protected String BD;
    protected String serie;
    protected Date fhArribo;
    protected String factura;
    protected Date fhAutho;   
    //protected String sucursal;
    protected Office sucursal;
    //Backward compatibles>>>
    protected int ID;
    protected Integer folio;
    protected State state;//protected Estado estado;
    protected SIIL.Server.Person owner;
    protected SIIL.Server.Person owner2;
    protected SIIL.client.sales.Enterprise company;
    protected Date fhETA;
    protected String sa;
    protected session.User creator;
    protected SIIL.trace.Trace trace;
    
    public Return<Integer> select(Connection connection,Office office,String serie,Integer folio) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT ID FROM  " + MYSQL_AVATAR_TABLE + " WHERE suc = '" + office.getCode() + "' AND serie ='" + serie + "' AND folio=" + folio + " ORDER BY RAND() LIMIT 1";
        java.sql.Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            ID = rs.getInt(1);
            return new Return<>(true);
        }
        else
        {
            ID = -1;
            return new Return<>(false);
        }
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @param office
     * @return
     * @throws SQLException 
     */
    public Return<Integer> selectRandom(Connection connection,Office office) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT ID FROM  " + MYSQL_AVATAR_TABLE + " WHERE suc = '" + office.getCode() + "' ORDER BY RAND() LIMIT 1";
        java.sql.Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            ID = rs.getInt(1);
            return new Return<>(true);
        }
        else
        {
            ID = -1;
            return new Return<>(false);
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> downOffice(Database connection) throws SQLException
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT suc FROM " + MYSQL_AVATAR_TABLE + " WHERE ID = " + ID;
        java.sql.Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {        
            sucursal = new Office(connection, rs.getString(1));
            return new Return<>(true);
        }
        else
        {
            sucursal = null;
            return new Return<>(false,"No se encontro nigun registro para el ID = " + ID);
        }   
    }

    public Return<Integer> downFolio(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT folio FROM " + MYSQL_AVATAR_TABLE + " WHERE ID = " + ID;
        java.sql.Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            folio = rs.getInt(1);
            return new Return<>(true);
        }
        else
        {
            folio = -1;
            return new Return<>(false,"No se encontro nigun registro para el ID = " + ID);
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
        String sql = "SELECT iD FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY ID DESC LIMIT 0, 1";
        java.sql.Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            ID = rs.getInt(1);
            return new Return(true);
        }
        else
        {
            ID = -1;
            return new Return(false);
        }
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> selectRandom(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT ID FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        java.sql.Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            ID = rs.getInt(1);
            return new Return<>(true);
        }
        else
        {
            ID = -1;
            return new Return<>(false);
        }
    }
    
    public Orcom(Orcom orcom)
    {
        this.note = orcom.note;
        this.BD = orcom.BD;
        this.serie = orcom.serie;
        this.fhArribo = orcom.fhArribo;
        this.factura = orcom.factura;
        this.fhAutho = orcom.fhAutho;
        this.sucursal = orcom.sucursal;
        this.ID = orcom.ID;
        this.folio = orcom.folio;
        this.state = orcom.state;//this.estado = orcom.estado;
        this.owner = orcom.owner;
        this.company = orcom.company;
        this.fhETA = orcom.fhETA;
        this.sa = orcom.sa;
        this.creator = orcom.creator;
        this.trace = orcom.trace;
    }
    
    public Orcom()
    {
    }
    
    @Deprecated
    public ResultSet SELECT(MySQL conn, String fields, String where, int limit) throws SQLException
    {
        String sql = "SELECT " + fields + " FROM Orcom ";
        if(where != null && !where.isEmpty())
        {
            sql = sql + " WHERE " + where;
        }
        if(limit > 0)
        {
            sql = sql + " LIMIT " + limit;
        }
        
        Statement stmt = (Statement) conn.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        return rs;
    }

    @Deprecated
    public  int insertDoc(MySQL conn) throws SQLException 
    {
        java.sql.Timestamp fhFolio = conn.getTimestamp();
        String sql = "INSERT INTO Orcom(compBD,compNumber,folio,BD,estado,fhFolio,docType,ownerName,suc,creator,ownerUser) VALUES(?,?,?,?,?,?,'C',?,?,?,?)";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setString(1, getCompany().getBD());
        pstmt.setString(2, getCompany().getNumber());
        pstmt.setInt(3, folio);
        pstmt.setString(4, BD);
        pstmt.setString(5, "docedit"); 
        pstmt.setTimestamp(6, fhFolio);        
        pstmt.setNull(7, 0);
        pstmt.setString(8, getOffice());
        pstmt.setString(9, creator.getAlias());
        pstmt.setString(10, creator.getAlias());
        
        Value val;
        int flV = 0;
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter("docedit");
        val.setField("estado");
        val.setBrief("Bandera de Estado");        
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(String.valueOf(folio));
        val.setField("folio");
        val.setBrief("Folio del Documento");     
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(String.valueOf(fhFolio.getTime()));
        val.setField("fhFolio");
        val.setBrief("Fecha del Documento");     
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(owner.toString());
        val.setField("ownerName");
        val.setBrief("Nombre del Responsable");     
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(sucursal.getCode());
        val.setField("suc");
        val.setBrief("Sucursal");     
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(creator.getAlias());
        val.setField("creator");
        val.setBrief("Creador");     
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
        
        return pstmt.executeUpdate();
    }

    /**
     * @return the comp
     */
    public SIIL.Server.Company getCompany() 
    {
        return company;
    }

    /**
     * @param comp the comp to set
     */
    public void setCompany(SIIL.client.sales.Enterprise comp) 
    {
        this.company = comp;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) 
    {
        this.note = note;
    }

    /**
     * @return the estado
     */
    public State getState() {
        return state;
    }

    /**
     * @param estado the estado to set
     */
    public void setState(State state) 
    {
        this.state = state;
    }

    @Deprecated
    public int updateAutho(Database conn,Person operator) throws SQLException 
    {
        java.sql.Timestamp  fh = conn.getTimestamp();
        state.select(conn,State.Steps.CS_ETA);
        String sqlOp = "UPDATE Orcom SET estado = ?, fhAutho = ?, pAutho = ? WHERE  BD = ? and folio = ? and ID = ?";
        System.out.println(sqlOp);
        PreparedStatement pstmtOp = conn.getConnection().prepareStatement(sqlOp);
        pstmtOp.setString(1, getState().getCode()); 
        pstmtOp.setTimestamp(2, fh);
        pstmtOp.setInt(3, operator.getpID());
        pstmtOp.setString(4, BD);
        pstmtOp.setInt(5, folio);
        pstmtOp.setInt(6, ID);     
        
        String sqlT = "SELECT estado, fhAutho FROM Orcom WHERE  BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmtT = conn.getConnection().prepareStatement(sqlT);
        pstmtT.setString(1, BD);
        pstmtT.setInt(2, folio);
        pstmtT.setInt(3, ID);
        ResultSet rs = pstmtT.executeQuery();
        Estado estado = null;
        java.sql.Timestamp fhAutho = null;        
        if(rs.next())
        {
            estado = new Estado();
            estado.setCode(rs.getString("estado"));
            fhAutho = rs.getTimestamp("fhAutho");
        }
        
        Value val;
        int flV = 0;
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("fhAutho");
        if(fhAutho != null)
        {
            val.setBefore(String.valueOf(fhAutho.getTime()));
        }
        val.setAfter(String.valueOf(fh.getTime()));
        val.setBrief("Hora de Autorización");
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
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
        flV += val.insert(conn);
        
        return pstmtOp.executeUpdate();
    }
    
    @Deprecated
    public int updateETA(Database conn) throws SQLException 
    {        
        java.sql.Timestamp  fh = conn.getTimestamp();
        state.select(conn,State.Steps.CS_TRANS);
        String sqlOp = "UPDATE Orcom SET fhETA=?,estado=?,fhETAfl=? WHERE  BD = ? and folio = ?  and ID = ?";
        PreparedStatement pstmtOp = conn.getConnection().prepareStatement(sqlOp);
        pstmtOp.setDate(1,new java.sql.Date(fhETA.getTime()));
        pstmtOp.setString(2, state.getCode());
        pstmtOp.setTimestamp(3, fh);
        pstmtOp.setString(4, BD);
        pstmtOp.setInt(5, folio);
        pstmtOp.setInt(6, ID);
        
        
        String sqlE = "SELECT estado, fhETA, fhETAfl  FROM Orcom WHERE  BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmtE = conn.getConnection().prepareStatement(sqlE);
        pstmtE.setString(1, BD);
        pstmtE.setInt(2, folio);
        pstmtE.setInt(3, ID);
        
        ResultSet rs = pstmtE.executeQuery();
        Estado estado = null;
        java.sql.Timestamp fhETAold = null;    
        java.sql.Timestamp fhETAfl = null;     
        if(rs.next())
        {
            estado = new Estado();
            estado.setCode(rs.getString("estado"));
            fhETAold = rs.getTimestamp("fhETA");
            fhETAfl = rs.getTimestamp("fhETAfl");
        }
        
        Value val;
        int flV = 0;
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("fhETAfl");
        if(fhETAfl != null)
        {
            val.setBefore(String.valueOf(fhETAfl.getTime()));
        }
        val.setAfter(String.valueOf(fh.getTime()));
        val.setBrief("Hora de Operación");
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
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
        flV += val.insert(conn);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("fhETA");
        if(fhETAold != null)
        {
            val.setBefore(String.valueOf(new java.sql.Date(fhETAold.getTime()).getTime()));
        }
        val.setAfter(String.valueOf(new java.sql.Date(fhETA.getTime()).getTime()));
        val.setBrief("Fecha de ETA");
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
        
        return pstmtOp.executeUpdate();
    }
    
    @Deprecated
    public int updateSurtir(MySQL conn) throws SQLException 
    {
        String sql = "SELECT estado, fhSurtido,sa FROM Orcom WHERE  BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setString(1, BD);
        pstmt.setInt(2, folio);
        pstmt.setInt(3, ID);
        ResultSet rs = pstmt.executeQuery();
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
        
        java.sql.Timestamp  fh = conn.getTimestamp();
        estado.setCode("pedsur");
        sql = "UPDATE Orcom SET fhSurtido = ?, estado = ?, sa = ? WHERE  BD = ? and folio = ? and ID = ?";
        pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setTimestamp(1, fh);
        pstmt.setString(2, getState().getCode());
        pstmt.setString(3, sa);
        pstmt.setString(4, BD);
        pstmt.setInt(5, folio);
        pstmt.setInt(6, ID);
        
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
        flV += val.insert(conn);
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
        flV += val.insert(conn);
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
        flV += val.insert(conn);        
        
        return pstmt.executeUpdate();
    }
    
    @Deprecated
    public int updateFin(MySQL conn) throws SQLException 
    {
        java.sql.Timestamp fh = conn.getTimestamp();
        //estado.setCode("pedfin");
        String sql = "UPDATE Orcom SET fhFin = ?, estado = ? WHERE BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setTimestamp(1, fh);
        pstmt.setString(2, getState().getCode());
        pstmt.setString(3, BD);
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
        flV += val.insert(conn);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("estado");
        val.setBefore("*");
        val.setAfter(getState().getCode());
        val.setBrief("Bandera de Estado");
        val.setLlave("folio="+folio);
        flV += val.insert(conn);
        
        return pstmt.executeUpdate();
    }

    /**
     * @return the fhin
     */
    public Date getFhETA() {
        return this.fhETA;
    }

    /**
     * @param fhin the fhin to set
     */
    public void setFhETA(Date fhin) {
        this.fhETA = fhin;
    }

    /**
     * @return the confirmArribo
     */
    public Date getConfirmArribo() {
        return fhArribo;
    }

    /**
     * @param confirmArribo the confirmArribo to set
     */
    public void setConfirmArribo(Date confirmArribo) 
    {
        this.fhArribo = confirmArribo;
    }

    /**
     * @return the factura
     */
    public String getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(String factura) {
        this.factura = factura;
    }

    /**
     * @return the fhAutho
     */
    public Date getFhAutho() {
        return fhAutho;
    }

    /**
     * @param fhAutho the fhAutho to set
     */
    public void setFhAutho(Date fhAutho) {
        this.fhAutho = fhAutho;
    }

    /**
     * @return the serie
     */
    public String getSerie() {
        return serie;
    }

    /**
     * @param serie the serie to set
     */
    public void setSerie(String serie) {
        this.serie = serie;
    }

    /**
     * @return the folio
     */
    public Integer getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public void setDocClave(String doc) 
    {
        /*String[] split = doc.split("-");
        serie = split[0];*/
        folio = Integer.parseInt(doc);
    }

    @Deprecated
    public String getDocClave() 
    {
        return String.valueOf(folio);
    }
    @Deprecated
    public int updateEdit(MySQL conn) throws SQLException 
    {
        String sql = "SELECT estado, fhEdit FROM Orcom WHERE  BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setString(1, BD);
        pstmt.setInt(2, folio);
        pstmt.setInt(3, ID);
        
        ResultSet rs = pstmt.executeQuery();
        Estado estado = null;
        java.sql.Timestamp fhEdit = null;        
        if(rs.next())
        {
            estado = new Estado();
            estado.setCode(rs.getString("estado"));
            fhEdit = rs.getTimestamp("fhEdit");
        }
        
        java.sql.Timestamp  fh = new java.sql.Timestamp(new Date().getTime());
        estado.setCode("docpen");
        sql = "UPDATE Orcom SET fhEdit = ?, estado = ? WHERE   BD = ? and folio = ? and ID = ?";
        pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setTimestamp(1, fh);
        pstmt.setString(2, getState().getCode());
        pstmt.setString(3, BD);
        pstmt.setInt(4, folio);
        pstmt.setInt(5, ID);
        
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
        flV += val.insert(conn);
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
        flV += val.insert(conn);
        
        return pstmt.executeUpdate();
    }

    public int updateCancel(Database db,String comment) throws SQLException 
    {
        java.sql.Timestamp  fh = db.getTimestamp();
        //estado.setCode("cancel");
        String sqlOp = "UPDATE Orcom SET estado = ?, fhFin = ?, terminalComment= ? WHERE   BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmtOp = db.getConnection().prepareStatement(sqlOp);
        pstmtOp.setString(1, Estado.transitionCodeForCancel);
        pstmtOp.setTimestamp(2, fh);
        pstmtOp.setString(3, comment );
        pstmtOp.setString(4, BD);
        pstmtOp.setInt(5, folio);
        pstmtOp.setInt(6, ID);
        
        String sql = "SELECT estado, fhFin FROM Orcom WHERE  BD = ? and folio = ? and ID = ?";
        PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
        pstmt.setString(1, BD);
        pstmt.setInt(2, folio);
        pstmt.setInt(3, ID);
        ResultSet rs = pstmt.executeQuery();
        Estado estado = null;
        java.sql.Timestamp fhFin = null;    
        if(rs.next())
        {
            estado = new Estado();
            estado.setCode(rs.getString("estado"));
            fhFin = rs.getTimestamp("fhFin");
        }
        
        Value val;
        int flV = 0;
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("fhFinfl");
        if(fhFin != null)
        {
            val.setBefore(String.valueOf(fhFin.getTime()));
        }
        val.setAfter(String.valueOf(fh.getTime()));
        val.setBrief("Hora de Operación");
        val.setLlave("folio="+folio);
        val.setLlave("folio="+folio);
        flV += val.insert(db);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setField("estado");
        if(estado != null)
        {
            val.setBefore(estado.getCode());
        }
        val.setAfter(Estado.transitionCodeForCancel);
        val.setBrief("Bandera de Estado");
        val.setLlave("folio="+folio);
        flV += val.insert(db);
        
        return pstmtOp.executeUpdate();    
    }

    /**
     * @return the ownerName
     */
    @Deprecated
    public SIIL.Server.Person getOwnerName() 
    {
        return owner;
    }

    /**
     * @param owner the ownerName to set
     */
    public void setOwner(SIIL.Server.Person owner) {
        this.owner = owner;
    }

    /**
     * @param owner the ownerName to set
     */
    public void setOwner2(SIIL.Server.Person owner2) {
        this.owner2 = owner2;
    }
    
    /**
     * @return the sa
     */
    public String getSA() {
        return sa;
    }

    /**
     * @param sa the sa to set
     */
    public void setSA(String sa) {
        this.sa = sa;
    }

    public boolean existKey(MySQL conn) 
    {
        String sql = "SELECT BD,folio FROM Orcom WHERE BD = ? and folio = ?";
        PreparedStatement stmt;
        
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, BD);
            stmt.setInt(2, folio);
            ResultSet rs = stmt.executeQuery();
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
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * @return the creator
     */
    public String getCreatorString() {
        return creator.toString();
    }    
    /**
     * @return the creator
     */
    public session.User getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(session.User creator) 
    {
        this.creator = creator;
    }
    
    public boolean downCompany(Database db) 
    {
        String sql = "SELECT compBD,compNumber FROM Orcom WHERE BD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' AND folio = " + folio;
                
        try 
        {
            java.sql.Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                company = new Enterprise();
                company.setBD(rs.getString(1));
                company.setNumber(rs.getString(2));
                company.complete(db);
                return true;
            }
            else
            {
                company = null;
                return false;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /*@Deprecated
    public void downCompany(MySQL conn) 
    {
        String sql = "SELECT compBD,compNumber FROM Orcom WHERE BD = ? and folio = ?";
        PreparedStatement stmt;
        
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, BD);
            stmt.setInt(2, folio);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                company = new Enterprise();
                company.setBD(rs.getString(1));
                company.setNumber(rs.getString(2));
                company.complete(conn);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Orcom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    /**
     * @return the trace
     */
    public SIIL.trace.Trace getTrace() {
        return trace;
    }

    /**
     * @param trace the trace to set
     */
    public void setTrace(SIIL.trace.Trace trace) {
        this.trace = trace;
    }


    Throwable fill(Database db,Credential cred, Integer id) 
    {
        @SuppressWarnings("UnusedAssignment")
        Throwable ret;
        @SuppressWarnings("UnusedAssignment")
        String compNumber;
        @SuppressWarnings("UnusedAssignment")
        String creator;
        @SuppressWarnings("UnusedAssignment")
        String ownerName;
        int ownerPerson = -1;
        
        String query = "SELECT folio,suc,sa,compNumber,creator,ownerName,ownerPerson,estado,fhAutho,fhETA,serie FROM Orcom WHERE BD = '" + cred.getBD() + "' and ID = " + id;
        Statement st;
        try 
        {
            st = (Statement) db.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next())
            {
                //
                compNumber = rs.getString("compNumber");
                creator = rs.getString("creator");
                ownerName = rs.getString("ownerName");
                ownerPerson = rs.getInt("ownerPerson");
                //
                ID = id;
                BD = cred.getBD();
                folio = rs.getInt("folio");
                sucursal = new Office(db,rs.getString("suc"));
                sa = rs.getString("sa");
                serie = rs.getString("serie");
                if( rs.getDate("fhETA") != null)
                {
                    fhETA = new Date();
                    fhETA.setTime(rs.getDate("fhETA").getTime());
                }
                else
                {
                    fhETA = null;
                }
                state = new State(-1);
                Throwable fill = state.fill(db, rs.getString("estado"));
            }
            else
            {
                return new java.lang.Error("No se encontraron datos para el Documento [" + query + "]");
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
        
        company = new Enterprise();
        ret = company.fill(db,cred,compNumber);
        owner = new Person();
        ret = owner.fill(db,BD,ownerName,ownerPerson);
        if(ret != null) return ret;
        
        this.creator = new User(); 
        ret = this.creator.fill(db, creator, cred.getBD());
        if(ret != null) return ret;        
        
        return null;
    }

    //Implementeacion de Recognizable
    /**
     * @return the id
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setID(int id) {
        this.ID = id;
    }
    
    @Override
    public String getBD() {
        return BD;
    }

    /**
     * @param BD the BD to set
     */
    @Override
    public void setBD(String BD) 
    {
        this.BD = BD;
    }

    public Office getSucursal()
    {
        return sucursal;
    }
    /**
     * @return the sucursal
     */
    @Override
    public String getOffice() {
        return sucursal.getCode();
    }

    /**
     * @param sucursal the sucursal to set
     */
    @Override
    public void setOffice(String sucursal) {
        throw new FailResultOperationException("La asignacion manual de sucuarsal esta desclaseficada.");
    }
    
    @Override
    public boolean isInMatrix()
    {
        return this.sucursal.equals("bc.tj");
    }
    
    @Override
    public boolean isInSubsidiary()
    {
        return !this.sucursal.equals("bc.tj");
    }    

    Throwable fillDetail(Database db, Credential cred, int id) 
    {
        return null;
    }

    private void clean() 
    {
        this.note = null;
        this.BD = null;
        this.serie = null;
        this.fhArribo = null;
        this.factura = null;
        this.fhAutho = null;
        this.sucursal = null;
        this.ID = -1;
        this.folio = null;
        this.state = null;
        this.owner = null;
        this.company = null;
        this.fhETA = null;
        this.sa = null;
        this.creator = null;
        this.trace = null;
    }
}
