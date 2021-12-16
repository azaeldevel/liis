
package database.mysql.sales;

import SIIL.CN.Sucursal;
import SIIL.CN.Tables.CN60;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.services.order.Association;
import SIIL.trace.Trace;
import core.Searchable;
import core.bobeda.Archivo;
import core.bobeda.FTP;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import process.Return;
import process.State;

/**
 *
 * @author Azael Reyes
 */
public class Remision  extends Operational  implements Searchable
{
    public static final String MYSQL_AVATAR_TABLE = "SalesRemision";
    private Archivo archivoOS;
    

    
    public boolean downOrdenesArchivo(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT archivoOS FROM " + MYSQL_AVATAR_TABLE + " WHERE archivoOS IS NOT NULL AND id = " + id;
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.archivoOS = new Archivo(rs.getInt(1));
            return true;
        }
        else
        {
            this.archivoOS = null;
            return false;
        }
    }
    
    public static void massAssociationCleanDirectory(List<Association<Remision>> associations) throws IOException
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
    
    private static Return massAssociationUpFile(Database dbserver,FTP ftpServer,File file,Remision remision,Office office,Person person,Trace traceContext) throws IOException, SQLException, ParseException
    {
        if(remision.getID() < 1)                
        {
            return new Return(false,"No se ha asignado un objeto.");
        }
        
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyy"); 
        FileInputStream inStr = new FileInputStream(file);
        Return ret = Archivo.add(dbserver, ftpServer, file.getName(), inStr, remision.getEnterprise(), "Orden de Servicio Digitalizada : " + dt.format(remision.getFhFolio() ) + " #" + remision.getFolio().toString(), Archivo.Type.ORDEN_SERVICIO, Archivo.Origen.INTERNO);
        inStr.close();
        if(ret.isFlag())
        {
            if(remision.upArchivoOS(dbserver, (Archivo) ret.getParam()))
            {
                return new Return(true);
            }
            else
            {
                return new Return(false,"Falló la operacion de agregar archivo a la bobeda.");
            }
        }
        else
        {
            return ret;
        }
    }
    
    public static Return massAssociation(Database dbserver,FTP ftpServer,File directory,List<String> logger,List<Association<Remision>> associations,Trace traceContext,Office office,Person person,Object progress) throws IOException, SQLException, ParseException
    {
        if(!directory.isDirectory())
        {
            return new Return(false, "'"+ directory + "' no es un directorio valido.");
        }
        
        int totalFile = 0;
        int asociatedFile = 0;
        
        int oneStep = 100/associations.size();        
        //if(progress != null) progress.setProgress(0, "Comenzando...");
        int progressStep = 0;
            
        for(Association<Remision> association : associations)
        {       
            progressStep += oneStep;
            totalFile++;
            File file = association.getFile();
            if(association.isActionable())
            {
                //if(progress != null) progress.setProgress(oneStep, "Presesando " + association.getObject().getFolio());
                if(association.getObject().getID() < 1)                
                {
                    return new Return(false,"No se ha asignado un objeto.");
                }
                //logger.add("Procesando '" + file.getName() + "' ...");
                if(association.getObject().getArchivoOS() != null)//Â¿tiene archivo asociado?
                {
                    association.getObject().downFolio(dbserver);
                    logger.add("La orden '" + association.getObject().getFolio() + "' ya estÃ¡ asociada serÃ¡ ignorada.");
                    continue;
                }
                
                Return ret = massAssociationUpFile(dbserver,ftpServer,file,association.getObject(),office,person,traceContext);
                if(ret.isFail()) return ret;
                asociatedFile++;
                //logger.add("'" + file.getName() + "' Done.");
            }
        }
        return new Return(true,"Se asociarion " + asociatedFile + " de un total de " + totalFile);
    }
    
    public static boolean massAssociationReadDirectory(Database dbserver, File directory,List<Association<Remision>> associations,List<String> logger,Office office) throws SQLException
    {
        if(!directory.isDirectory()) return false;        
        File[] filesList = directory.listFiles();
        for (File file : filesList) 
        {
            if (file.isFile()) 
            {
                Return retFind = Remision.exist(dbserver, office, FilenameUtils.removeExtension(file.getName()), Type.SalesRemision);
                Association<Remision> association = null;
                if(retFind.isFlag())
                {
                    Remision invoice = new Remision((int)retFind.getParam());
                    invoice.download(dbserver);
                    invoice.downCompany(dbserver);
                    invoice.downFolio(dbserver);
                    invoice.downSerie(dbserver);
                    association = new Association(file,true,invoice);                    
                }
                else
                {
                    logger.add("El archivo '" + file.getName() + "' será ignorado devido a que no nay folio para asociar.");
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
    
    public boolean upArchivoOS(Database db, Archivo archivo) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET archivoOS = " + archivo.getID() + " WHERE id = " + id;
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
    
    public static Return generate(Database db,Office office,String[] folios,Person operator) throws SQLException, IOException, ParseException
    {
        List<Remision> ls = new ArrayList<>();
        
        for(String folio : folios)
        {
            Return retOp = exist(db, office, folio, Type.SalesRemision);
            Remision remision = null;
            if(retOp.isFlag())
            {
                remision = new Remision((int)retOp.getParam());
            }
            else
            {
                Return retRem = fromCN2(db, office, folio, operator);
                if(retRem.isFail())
                {
                    return retRem;
                }
                else
                {
                    remision = (Remision) retRem.getParam();
                }
            }
            ls.add(remision);
        }
        return new Return(true,ls);
    }
    
    public static List<Searchable> search(Database db,Office office,String text) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + "_Resolved WHERE  folio LIKE '%" + text + "%' ";
        if(office != null)
        {
            sql += " AND office = " + office.getID() ;
        }
        ArrayList<Searchable> lst = new ArrayList<>();
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            Remision remision = new Remision(rs.getInt(1));
            remision.download(db);
            lst.add(remision);
        }        
        return lst;
    }
    
    public static Return fromCN2(Database db,Office office,String folio,Person operator) throws IOException, ParseException, SQLException
    {
        if(!CN60.isWorking())
        {
            return new Return(false,"No ha base de dataos de CN60.");
        }        
        SIIL.CN.Tables.FACTURA tbFactura = null;
        switch (office.getCode()) 
        {
            case "bc.tj":
                tbFactura = new SIIL.CN.Tables.FACTURA(Sucursal.BC_Tijuana);
                break;
            case "bc.mx":
                tbFactura = new SIIL.CN.Tables.FACTURA(Sucursal.BC_Mexicali);
                break;
            case "bc.ens":
                tbFactura = new SIIL.CN.Tables.FACTURA(Sucursal.BC_Ensenada);
                break;
            default:
                return new Return(false,"Base de taos Cn60 desconocida.");
        }
        
        SIIL.CN.Records.FACTURA recFactura = tbFactura.readWhere(folio);
        if(recFactura != null)
        {//si existe el folio
            Remision remision = new Remision(-1);
            String numberCleint = recFactura.getClientNumber();
            Enterprise enterprise = Enterprise.find(db,numberCleint);
            if(enterprise != null)
            {
                enterprise.download(db);
            }
            else
            {
                tbFactura.close();
                return new Return(false,"No se encontro el cliente '" + numberCleint + "'  en Tools.");
            }
            Return ret = remision.insert(db, office, new State(1), operator, recFactura.getFecha(),enterprise, Integer.parseInt(folio), office.getSerieOffice(Office.Platform.CN60), "SR");           
            if(ret.isFlag())
            {
                //System.out.println("Se importo Remision desde CN60.");
                remision.upFlag(db.getConnection(), 'A');
                tbFactura.close();
                return new Return(true,remision);
            }
            else
            {
                tbFactura.close();
                return ret;
            }
        }
        else
        {
            tbFactura.close();
            return new Return(false,"No se encontro el folio '" + folio + "'  en CN60.");
        }
    }
    
    public static Remision fromCN(Database db,Office office,String folio,Person operator) throws IOException, ParseException, SQLException
    {
        if(!CN60.isWorking())
        {
            return null;
        }        
        SIIL.CN.Tables.FACTURA tbFactura = null;
        switch (office.getCode()) 
        {
            case "bc.tj":
                tbFactura = new SIIL.CN.Tables.FACTURA(Sucursal.BC_Tijuana);
                break;
            case "bc.mx":
                tbFactura = new SIIL.CN.Tables.FACTURA(Sucursal.BC_Mexicali);
                break;
            case "bc.ens":
                tbFactura = new SIIL.CN.Tables.FACTURA(Sucursal.BC_Ensenada);
                break;
            default:
                return null;
        }
        
        SIIL.CN.Records.FACTURA recFactura = tbFactura.readWhere(folio);
        if(recFactura != null)
        {//si existe el folio
            Remision remision = new Remision(-1);
            String numberCleint = recFactura.getClientNumber();
            Enterprise enterprise = Enterprise.find(db,numberCleint);
            if(enterprise != null)
            {
                enterprise.download(db);
            }
            else
            {
                tbFactura.close();
                return null;
            }
            Return ret = remision.insert(db, office, new State(1), operator, recFactura.getFecha(),enterprise, Integer.parseInt(folio), office.getSerieOffice(Office.Platform.CN60), "SR");
            remision.upFlag(db.getConnection(), 'A');
            if(ret.isFlag())
            {
                //System.out.println("Se importo Remision desde CN60.");
                tbFactura.close();
                return remision;
            }
            else
            {
                tbFactura.close();
                return null;
            }
        }
        else
        {
            tbFactura.close();
            return null;
        }
    }
    
    public static List<Remision> search(Database db,Office office,String text,int limit) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + "_Resolved WHERE  folio LIKE '%" + text + "%' ";
        if(office != null)
        {
            sql += " AND office = " + office.getID() ;
        }
        ArrayList<Remision> lst = new ArrayList<>();
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            Remision remision = new Remision(rs.getInt(1));
            lst.add(remision);
        }        
        return lst;
    }

    /**
     * 
     * @param db
     * @return
     * @throws SQLException 
     */
    public Boolean selectRandom(Database db) throws SQLException 
    {
        clean();      
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
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
            this.id = -1;
            return false;
        }
    }
    
    public Remision(int id) {
        super(id);
    }
    
    @Override
    public Return insert(Database db,Office office,State state,Person operator,java.util.Date date,Enterprise company,int folio,String serie,String type) throws SQLException
    {
        if(getID() > 0)
        {
            return new Return(false, "Bab parameter."); 
        }
        if(db == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        if(company == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        else if(company.getNumber() == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        
        Return insertRet = super.insert(db,office, state, operator, date, company, folio, serie, type);
        if(insertRet.isFail()) return insertRet;        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id) VALUES(" + super.getID() + ")";
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return(false, "No unico registro insertado.");  
        }
        else
        {
            return insertRet;
        }     
    }

    private void clean() 
    {
        ;
    }

    @Override
    public String getIdentificator() 
    {
        return getFullFolio();
    }

    @Override
    public String getBrief() 
    {
        return "";
    }

    /**
     * @return the archivoOS
     */
    public Archivo getArchivoOS() {
        return archivoOS;
    }
    
}
