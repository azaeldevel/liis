
package database.mysql.purchases.order;

import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.core.Office;
import SIIL.service.quotation.OrdenTransito;
import SIIL.service.quotation.ServiceQuotation;
import core.Renglon;
import database.mysql.sales.Quotation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import process.Document;
import process.Return;
import process.Row;
import process.State;
import session.Credential;
import stock.Allocated;
import stock.Container;
import stock.Flow;

/**
 *
 * @author Azael Reyes
 */
public class PO extends database.mysql.purchases.Operational implements process.Purchases
{
    private static final String MYSQL_AVATAR_TABLE = "PurchasesOrder";
    public static final String TYPE = "PO";
    public static final String MODULE = "PO";
    
    private Date fhETA;
    
    
    /**
     * Verifica si todos los articulos han sido surtidos
     * @param db
     * @return 
     * @throws java.sql.SQLException 
     */
    public boolean isAssorted(Database db) throws SQLException
    {
        List<Renglon> list = Renglon.select(db, this, Renglon.RenglonExclusion.SIIL_Y_SEVICICOS);
        for(Renglon renlgon : list)
        {
            if(renlgon.isArrived(db))
            {
                continue;
            }
            else
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 
     * @param dbserver
     * @return
     * @throws SQLException 
     */
    public boolean isFullArrive(Database dbserver) throws SQLException
    {
        if(isAssorted(dbserver))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private boolean allocateArrive(Database db, List<Renglon> renglones) throws SQLException
    {
        Container container = new Container(200);
        Allocated allocated = null;
        for(Renglon renglon: renglones)
        {
            if(!renglon.upEstado(db, Flow.Estado.LIBRE)) return false;
            if(!renglon.upPurchaseArrival(db, db.getDateToday())) return false;
            List<Row> rows = renglon.getRows();
            for(Row row : rows)
            {
                allocated = new Allocated(-1);
                if(allocated.insert(db.getConnection(), container, row.getItem()).isFail())
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean autoArrive(Database db, Credential credential, List<Renglon> renNotS) throws SQLException
    {
        ArrayList<Quotation> qs = selectQuotations(db);
        
        if(qs != null)
        {
            if(renNotS != null)
            {                    
                allocateArrive(db, renNotS);
            } 
        
            ArrayList<ServiceQuotation> sqs = ServiceQuotation.selectServiceQuotation(db, qs);
            for(ServiceQuotation sq : sqs)
            {
                OrdenTransito ot = new OrdenTransito(sq.getID()); 
                ot.setServerDB(db);
                ot.downSerie(db.getConnection());
                ot.downFolio(db.getConnection());
                ot.download(db);  
                ot.downOwner(db);
                ot.downOwner2(db);
                ot.downQuotation(db.getConnection());
                ot.setCredential(credential);
                ot.downState(db);
                if(isAssorted(db))
                {
                    ot.run();
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public ArrayList<Quotation> selectQuotations(Database db) throws SQLException
    {
        String sql = "SELECT DISTINCT (stockflow.quotation) FROM processoperationalrows,stockflow WHERE processoperationalrows.item = stockflow.id AND stockflow.quotation IS NOT NULL AND processoperationalrows.op = " + getID();
        ArrayList<Quotation> qs = new ArrayList<>();
        System.out.println(sql);
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            Quotation q = new Quotation(rs.getInt(1));
            qs.add(q);
        }  
        if(qs.size() > 0) return qs;
        return null;
    }
    
        
    public boolean upETA(Database db, Date fecha) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET eta = '" + new java.sql.Date(fecha.getTime()) + "' WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        int ret = stmt.executeUpdate(sql);
        if(ret == 1)
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
     * @param db
     * @return 
     * @throws java.sql.SQLException 
     */
    @Override
    public Return download(Database db) throws SQLException
    {
        String sql = "SELECT eta FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);        
        if(rs.next())
        {
            fhETA = rs.getDate(1);
        }        
        
        rs.close();
        return super.download(db);
    }
    
    /**
     * 
     * @param connection
     * @param office
     * @param state
     * @param operator
     * @param date
     * @param serie
     * @param folio
     * @param provider
     * @return
     * @throws SQLException 
     */
    @Override
    public Return insert(Database connection,Office office,State state,Person operator,Date date,database.mysql.purchases.Provider provider,int folio,String serie,String type) throws SQLException
    {
        if(connection == null)
        {
            new Return(false, "NO se genero ID");
        }
        Return insertRet = super.insert(connection,office, state, operator, date, provider,folio,serie,type);
        if(insertRet.isFail()) return insertRet;
        clean();

        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id) VALUES(" + super.getID()+ ")";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return(false, "Deamasiados registros insertados"); 
        }
        return insertRet;    
    }

    public PO(int id) 
    {
        super(id);
    }

    @Override
    public Document getDocumentoContable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Document getDocumentTrace() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    private void clean() 
    {
        ;
    }

    /**
     * @return the fhETA
     */
    public Date getFhETA() {
        return fhETA;
    }

}
