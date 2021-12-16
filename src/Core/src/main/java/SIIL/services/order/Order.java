
package SIIL.services.order;

import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.core.Office;
import SIIL.trace.Trace;
import SIIL.trace.Value;
import core.FailResultOperationException;
import core.Searchable;
import core.bobeda.Archivo;
import core.bobeda.FTP;
import database.mysql.sales.Remision;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import process.Operational;
import process.Return;
import sales.Invoice;
import stock.Flow;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class Order implements Searchable
{
    public static final String MYSQL_AVATAR_TABLE = "ServicesOrder";    
    
        
    private static Return massAssociationUpFile(Database dbserver,FTP ftpServer,File file,Order orden) throws IOException, SQLException
    {        
        if(orden.getID() < 1)                
        {
            return new Return(false,"No se ha asignado un objeto.");
        }
        
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyy"); 
        FileInputStream inStr = new FileInputStream(file);
        String strDate = "";
        if(orden.getFhService() != null)
        {
            strDate = dt.format(orden.getFhService()) + " ";
        }
        else
        {
            strDate = "";
        }
        String strFolio = "";
        if(orden.getFolio() != null)
        {
            strFolio = orden.getFolio().toString();
        }
        else if(orden.getSA() > 0)
        {
            strFolio = String.valueOf(orden.getSA());
        }
        else
        {
            strFolio = "";
        }
        Return ret = Archivo.add(dbserver, ftpServer, file.getName(), inStr, orden.getEnterprise(), "Orden de Servicio Digitalizada :" + strDate + "#" + strFolio, Archivo.Type.ORDEN_SERVICIO, Archivo.Origen.INTERNO);
        inStr.close();
        if(ret.isFlag())
        {
            return orden.upArchivo(dbserver, (Archivo) ret.getParam(), null);
        }
        else
        {
            return ret;
        }
    }
    
    public static boolean massAssociationReadDirectory(Database dbserver, File directory,List<Association<Order>> associations,List<String> logger,Office office) throws SQLException
    {
        if(!directory.isDirectory()) return false;        
        File[] filesList = directory.listFiles();
        for (File file : filesList) 
        {
            if (file.isFile()) 
            {
                Return retFind = Order.findSA(dbserver,FilenameUtils.removeExtension(file.getName()),office);
                Association association = null;
                if(retFind.isFlag())
                {
                    Order order = (Order)retFind.getParam();
                    order.downFhService(dbserver);
                    order.downCompany(dbserver);
                    order.downFolio(dbserver);
                    order.downSA(dbserver);
                    order.downArchivo(dbserver);
                    association = new Association(file,true,order);                    
                }
                else
                {
                    logger.add("El archivo '" + file.getName() + "' será ignorado devido a " + retFind.getMessage());
                    association = new Association(file,false,null);
                }
                associations.add(association);
            }
            else if(file.isDirectory())
            {
                return massAssociationReadDirectory(dbserver,file,associations,logger,office);
            }
            else
            {
                return false;
            }
        }
        
        return true;
    }
    
    public static void massAssociationCleanDirectory(List<Association<Order>> associations) throws IOException
    {
        for(Association association : associations)
        {
            if(association.isActionable())
            {
                File file = association.getFile();
                Files.delete(file.toPath());
            }
        }
    }
    
    public static Return massAssociation(Database dbserver,FTP ftpServer,File directory,List<String> logger,List<Association<Order>> associations,Trace traceContext,Office office) throws IOException, SQLException
    {
        if(!directory.isDirectory())
        {
            return new Return(false, "'"+ directory + "' no es un directorio valido.");
        }
        
        int totalFile = 0;
        int asociatedFile = 0;
        
        for(Association<Order> association : associations)
        {       
            totalFile++;   
            File file = association.getFile();
            if(association.isActionable())
            {
                if(association.getObject().getID() < 1)                
                {
                    return new Return(false,"No se ha asignado un objeto.");
                }
                //logger.add("Procesando '" + file.getName() + "' ...");
                if(association.getObject().getArchivo() != null)//tiene archivo asociado?
                {
                    association.getObject().downSA(dbserver);
                    logger.add("La orden '" + association.getObject().getSA() + "' ya está asociada será ignorada.");
                    continue;
                }
                
                Return ret = massAssociationUpFile(dbserver,ftpServer,file,association.getObject());
                if(ret.isFail()) return ret;
                asociatedFile++;
                //logger.add("'" + file.getName() + "' Done.");
            }
        }
        return new Return(true," Se asociarion " + asociatedFile + " de un total de " + totalFile);
    }

    private static Return findSA(Database dbserver,String sa,Office office) throws SQLException 
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE sa = '" + sa + "' AND office = " + office.getID();
        //System.out.println(sql);
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            Order order = new Order(rs.getInt(1));
            if(rs.next())
            {
                return new Return(false,"El SA está duplicado.");
            }
            else
            {
                return new Return(true,order);
            }
        }
        else
        {
            return new Return(false,"El folio '" + sa + "' no esta asociado a alguana orden de servicio.");
        }
    }
    
    
    public boolean downArchivo(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT archivo FROM " + MYSQL_AVATAR_TABLE + " WHERE archivo IS NOT NULL AND id = " + id;
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.archivo = new Archivo(rs.getInt(1));
            return true;
        }
        else
        {
            this.archivo = null;
            return false;
        }
    }
    
    public Return upArchivo(Database db, Archivo archivo, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(archivo == null)
        {
            throw new InvalidParameterException("archivo es incorrecto.");
        }
        
        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET archivo = " + archivo.getID() + " WHERE id = " + id;        
        Statement stmt = db.getConnection().createStatement();  
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return new Return(true);
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else if(affected == 0)
        {
            return new Return(false,"Fallo : " + sql);
        }
        else
        {
            return new Return(false);
        }
    }
    
    public boolean download(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        
        String sql = "SELECT company,description,fhService,folio,horometro,itemFlow,quoteService,sa,technical,archivo FROM " + MYSQL_AVATAR_TABLE + " WHERE company IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        
        if(rs.next())
        {
            this.company = new Enterprise(rs.getInt(1));
            this.descripcion = rs.getString(2);
            this.fhService = rs.getDate(3);
            this.folio = rs.getInt(4);
            this.horometro = rs.getInt(5);
            this.itemFlow = new Flow(rs.getInt(6));
            this.itemFlow.download(db);
            this.quoteService = new SIIL.service.quotation.ServiceQuotation();
            this.quoteService.download(db);
            this.sa = rs.getInt(1);
            this.technical = new Person(rs.getInt(7));
            this.downType(db);
            this.archivo = new Archivo(rs.getInt(8));
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static List<Searchable> search(Database dbserver,Office office,String text,boolean activeSA) throws SQLException
    {
        List<Searchable> list = new ArrayList<>();        
        String table = MYSQL_AVATAR_TABLE;
        String sql = "SELECT id FROM " + table ;  
        //String whereSQL = " WHERE  ";
        if(activeSA == true)
        {
            List<Remision> lstR = Remision.search(dbserver, office, text, 0);
            for(Remision remision : lstR)
            {
                sql = "SELECT id FROM " + table;
                sql = sql + " WHERE  SalesRemision = " + remision.getID();
                //System.out.println(sql);
                ResultSet rs = dbserver.query(sql);
                while(rs.next())
                {
                    Order ord = new Order(rs.getInt(1));
                    ord.download(dbserver);
                    list.add(ord);                    
                }
            }            
        }        
        return list;
    }
    
    public boolean upDescripcion(Database db, String brief, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(brief == null)
        {
            throw new InvalidParameterException("Descripcion is es incorrecto.");
        }
        
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET description = '" + brief + "' WHERE id = " + id;        
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
       
    public boolean upFecha(Database db, Date fecha, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(fecha == null)
        {
            throw new InvalidParameterException("fecha is es incorrecto.");
        }
        
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET fhService = '" + new java.sql.Date(fecha.getTime()) + "' WHERE id = " + id;        
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public boolean upFolio(Database db, int folio, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(folio < 1)
        {
            throw new InvalidParameterException("folio is es incorrecto.");
        }
        
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET folio = " + folio + " WHERE id = " + id;        
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public boolean upMechanic(Database db, Person person, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(person == null)
        {
            throw new InvalidParameterException("person is null.");
        }
        else if(person.getpID() < 1)
        {
            throw new InvalidParameterException("person ID es invalido.");
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET technical = ";
        
        if(person == null)
        {
            sql += " NULL WHERE id = " + id;
        }
        else
        {
            sql += person.getpID() + " WHERE id = " + id;
        }
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public boolean upHorometro(Database db, int horometro, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(horometro == 0)
        {
            throw new InvalidParameterException("horometro is null.");
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET horometro = " + horometro + " WHERE id = " + id;        
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public boolean upType(Database db, Type type, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(type == null)
        {
            throw new InvalidParameterException("SA is null.");
        }
        
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET type = " ;
        
        if(type == Type.CORRECTIVE)
        {
            sql = sql + "'C'";
        }
        else if(type == Type.PREVENTIVE)
        {
            sql = sql + "'P'";
        }
        else
        {
            throw new FailResultOperationException("El Tipo es desconocido.");
        }
        sql = sql + " WHERE id = " + id; 
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public boolean upFlow(Database db, Flow flow, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(flow == null)
        {
            throw new InvalidParameterException("flow is null.");
        }
        else if(flow.getID() < 1)
        {
            throw new InvalidParameterException("flow ID es invalido.");
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET itemFlow = " + flow.getID() + " WHERE id = " + id;        
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public boolean upSA(Database db, Remision sa, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(sa == null)
        {
            throw new InvalidParameterException("SA is null.");
        }
        else if(sa.getID() < 1)
        {
            throw new InvalidParameterException("SA ID es invalido.");
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET sa = " + sa.getFolio() + ",SalesRemision = " + sa.getID() + " WHERE id = " + id;        
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public Boolean upQuotedService(Database db,ServiceQuotation orden, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(orden == null)
        {
            throw new InvalidParameterException("QuotedService is null.");
        }
        else if(orden.getID() < 1)
        {
            throw new InvalidParameterException("QuotedService ID es invalido.");
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET quoteService = " + orden.getID() + " WHERE id=" + id;        
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public Boolean upEnterprise(Database db, SIIL.client.sales.Enterprise enterprise, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(enterprise == null)
        {
            throw new InvalidParameterException("enterprise is null.");
        }
        else if(enterprise.getID() < 1)
        {
            throw new InvalidParameterException("enterprise ID es invalido.");
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET company = " + enterprise.getID() + " WHERE id=" + id;        
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public static int lastHorometro(Database db,Flow flow) throws SQLException
    {        
        String sql = "SELECT MAX(horometro)  FROM " + MYSQL_AVATAR_TABLE + " WHERE itemFlow = " + flow.getID();
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            return rs.getInt(1);
        }
        return 0;
    }
    
    public boolean downSA(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT sa FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.sa = rs.getInt(1);
            return true;
        }
        else
        {
            this.sa = -1;
            return false;
        }
    }
    
    public Boolean downFolio(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT folio FROM " + MYSQL_AVATAR_TABLE + " WHERE folio IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.folio = rs.getInt(1);
            return true;
        }
        else
        {
            this.folio = null;
            return false;
        }
    }
    
    public boolean downTechnical(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT technical FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.technical = new Person(rs.getInt(1));
            return true;
        }
        else
        {
            this.technical = null;
            return false;
        }
    }
    
    public boolean downQuoteService(Database db,session.Credential cred) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT quoteService FROM " + MYSQL_AVATAR_TABLE + " WHERE quoteService IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.quoteService = new SIIL.service.quotation.ServiceQuotation();
            this.quoteService.fillDetail(db, cred, rs.getInt(1));
            return true;
        }
        else
        {
            this.quoteService = null;
            return false;
        }
    }
    
    public boolean downType(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT type FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            switch (rs.getString(1)) 
            {
                case "C":
                    this.type = Type.CORRECTIVE;
                    break;
                case "P":
                    this.type = Type.PREVENTIVE;
                    break;
                default:
                    this.type = null;
            }
            return true;
        }
        else
        {
            this.type = null;
            return false;
        }
    }
    
    public boolean downItemFlow(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT itemFlow FROM " + MYSQL_AVATAR_TABLE + " WHERE itemFlow IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.itemFlow = new Flow(rs.getInt(1));
            return true;
        }
        else
        {
            this.itemFlow = null;
            return false;
        }
    }
    
    public boolean downFhService(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT fhService FROM " + MYSQL_AVATAR_TABLE + " WHERE fhService IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.fhService = rs.getDate(1);
            return true;
        }
        else
        {
            this.fhService = null;
            return false;
        }
    }
    
    public boolean downDescription(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT description FROM " + MYSQL_AVATAR_TABLE + " WHERE description IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.descripcion = rs.getString(1);
            return true;
        }
        else
        {
            this.descripcion = null;
            return false;
        }
    }
    
    public boolean downHorometro(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT horometro FROM " + MYSQL_AVATAR_TABLE + " WHERE horometro IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.horometro = rs.getInt(1);
            return true;
        }
        else
        {
            this.horometro = 0;
            return false;
        }
    }
    
    public boolean downCompany(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT company FROM " + MYSQL_AVATAR_TABLE + " WHERE company IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.company = new Enterprise(rs.getInt(1));
            return true;
        }
        else
        {
            this.company = null;
            return false;
        }
    }
    
    /**
     * @return the sa
     */
    public int getSA() 
    {
        return sa;
    }

    /**
     * @return the fhService
     */
    public Date getFhService() {
        return fhService;
    }

    /**
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * @return the itemFlow
     */
    public Flow getItemFlow() 
    {
        return itemFlow;
    }

    /**
     * @return the quoteService
     */
    public SIIL.service.quotation.ServiceQuotation getQuoteService() {
        return quoteService;
    }

    /**
     * @return the technical
     */
    public Person getTechnical() 
    {
        return technical;
    }

    /**
     * @return the folio
     */
    public Integer getFolio() {
        return folio;
    }

    @Override
    public String getIdentificator() 
    {
        return getFolio() + " - " + getFhService();
    }

    @Override
    public String getBrief() 
    {
        return   getDescripcion();
    }

    /**
     * @return the archivo
     */
    public Archivo getArchivo() {
        return archivo;
    }
    
    
    public enum Type
    {
        PREVENTIVE,
        CORRECTIVE
    }
    
    private int id;
    private Enterprise company;
    //private Titem titem;
    private Type type;
    private int horometro;
    private String descripcion;
    private int sa;
    private Date fhService;
    private Flow itemFlow;
    private SIIL.service.quotation.ServiceQuotation quoteService;
    private Person technical;
    private Integer folio;
    private Archivo archivo;
    
    
    
    /**
     * Asigna un ID al azar
     * @param db
     * @return
     * @throws SQLException 
     */
    public Boolean selectRandom(Database db) throws SQLException 
    {
        clean();
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return true;
        }
        else
        {
            throw new FailResultOperationException("No hay resultado");
        }
    }
    
    public Order(int id)
    {
        this.id = id;
    }
    
    /**
     * @return the company
     */
    public Enterprise getEnterprise() {
        return company;
    }
    public Company getCompany() {
        return company;
    }
    
    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the horometro
     */
    public int getHorometro() {
        return horometro;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }  
    
    private void clean() 
    {
        id = -1;
        company = null;
        descripcion = null;
        horometro = 0;
        itemFlow = null;
        type = null;
    }
    
    /**
     * 
     * @param database
     * @param fecha
     * @param flow
     * @param type
     * @param horometro
     * @param description
     * @param company
     * @param quoteService
     * @param sa
     * @param office
     * @param folio
     * @param person
     * @return
     * @throws SQLException 
     */
    public Boolean insert(Database database,Date fecha,Flow flow,Type type,int horometro,String description,Company company,SIIL.service.quotation.ServiceQuotation quoteService,Remision sa,Office office,Integer folio,Person person,Trace traceContext) throws SQLException
    {
        clean();
        if(getID() > 0)
        {
            return false;
        }
        if(type == null)
        {
            return false;
        }
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (fhService,itemFlow,`type`,horometro,office,folio" ;
        if(person != null && person.getpID() > 0)
        {
            sql = sql + ",technical";
        }
        if(sa != null && sa.getID() > 0)
        {
            sql = sql + ",sa,SalesRemision";
        }
        if(description != null  && description.length() > 0)
        {
            sql = sql + ",description";
        }
        if(company != null)
        {
            sql = sql + ",company";
        }
        if(quoteService != null)
        {
            sql = sql + ",quoteService";
        }
        sql = sql + ") VALUES('" + new java.sql.Date(fecha.getTime()) + "'," + flow.getID();
        Value val;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        int lfVal = 0;
        if(type == Type.CORRECTIVE)
        {
            sql = sql + ",'C'";
        }
        else if(type == Type.PREVENTIVE)
        {
            sql = sql + ",'P'";
        }
        sql = sql + "," + horometro;   
        
        val = new Value();
        val.setTraceID(traceContext.getTrace());
        val.setTable(MYSQL_AVATAR_TABLE);
        val.setAfter(Integer.toString(horometro));
        val.setField("horometro");
        val.setBrief("Hormetro capturado");  
        val.setLlave("folio="+folio+",fecha="+format.format(fecha));   
        lfVal = val.insert(database);
        sql = sql + "," + office.getID();
        sql = sql + "," + folio;
        if(person != null && person.getpID() > 0)
        {
            sql = sql + "," + person.getpID();
            //trace
            val = new Value();
            val.setTraceID(traceContext.getTrace());
            val.setTable(MYSQL_AVATAR_TABLE);
            person.fill(database, person.getpID(), person.getBD());
            val.setAfter(person.toString());
            val.setField("technical");
            val.setBrief("Â¿Quien hace la operaciÃ³n(Mecanico)?"); 
            val.setLlave("folio="+folio+",fecha="+format.format(fecha));   
            lfVal = val.insert(database);
        }
        if(sa != null && sa.getID() > 0)
        {
            sa.downFolio(database);
            sql = sql + "," + sa.getFolio() + "," + sa.getID();
            val = new Value();
            val.setTraceID(traceContext.getTrace());
            val.setTable(MYSQL_AVATAR_TABLE);
            val.setAfter(sa.getFolio());
            val.setField("sa");
            val.setBrief("SA");
            val.setLlave("folio="+folio+",fecha="+format.format(fecha));        
            lfVal = val.insert(database);
        }
        if(description != null  && description.length() > 0)
        {
            sql = sql + ",'" + description + "'";
        }
        if(company != null)
        {
            sql = sql + "," + company.getID();
            val = new Value();
            val.setTraceID(traceContext.getID());
            val.setTable(MYSQL_AVATAR_TABLE);
            company.complete(database);
            val.setAfter(company.toString());
            val.setField("company");
            val.setBrief("Localizacion Fisica de la operacion.");  
            val.setLlave("folio="+folio+",fecha="+format.format(fecha));        
            lfVal = val.insert(database);
        }
        if(quoteService != null)
        {
            sql = sql + "," + quoteService.getID();
        }
        sql = sql + ")";
        
        
        //System.out.println(sql);
        Statement stmt = database.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        int generated = -1;        
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
}
