
package process;

import SIIL.CN.Engine.Clause;
import SIIL.CN.Engine.Operator;
import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Tables.ARTICULO;
import SIIL.CN.Tables.CLIENTE;
import SIIL.CN.Tables.CN60;
import SIIL.CN.Tables.COTIZACI;
import SIIL.CN.Tables.COTIZA_R;
import SIIL.CN.Tables.PROVEEDO;
import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.services.grua.Battery;
import SIIL.services.grua.Charger;
import SIIL.services.grua.Forklift;
import SIIL.services.grua.Mina;
import database.mysql.purchases.Provider;
import database.mysql.sales.Quotation;
import database.mysql.stock.Titem;
import database.mysql.stock.Item;
import database.mysql.stock.Service;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import stock.Flow;
import stock.Refection;
import SIIL.CN.Sucursal;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class ImportsCN 
{
    public final String CN_SERIE = "CN";
    private int countCNQuote;
    
    public boolean generateTitemFlow(Database database,ArrayList<String> logger,int longTest)
    {
        String sql = "SELECT Titem.id,Titem.Marca as oldMarca,Titem.Modelo as oldModelo,Titem.Serie FROM StockItem,Titem WHERE StockItem.id = Titem.id AND Titem.Marca IS NOT NULL AND Titem.Modelo IS NOT NULL AND Titem.Serie IS NOT NULL";
        try 
        {
            Statement stmt = database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Titem titem = null;
            Flow flow = null;
            int counUp = 0;
            //logger = new ArrayList<>();
            while(rs.next())
            {
                titem = new Titem(rs.getInt(1));
                titem.upMake(database.getConnection(), rs.getString(2));
                titem.upModel(database.getConnection(), rs.getString(3));
                flow = new Flow(-1);
                flow.insert(database.getConnection(), new Date(), true, rs.getString(4),titem);
                logger.add(rs.getString(4) + " Flowing.");
                counUp++;
            }
            sql = "UPDATE Titem SET Marca = NULL, Modelo = NULL, Serie = NULL WHERE Titem.id > 0 AND Titem.Marca IS NOT NULL AND Titem.Modelo IS NOT NULL AND Titem.Serie IS NOT NULL";
            int countNULL = stmt.executeUpdate(sql);
            if(counUp == countNULL)
            {
                //System.out.println("Se actualizarón " + counUp);
                return true;
            }
            else
            {
                //System.err.println(" " + counUp + " != " + countNULL);
                return false;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ImportsCN.class.getName()).log(Level.SEVERE, null, ex);
            logger.add("Falló");
            return false;
        }        
    }
    
    public boolean importProvider(Database database,ArrayList<String> logger,int longTest)
    {
        if(!CN60.isWorking())
        {
            return false;
        }
        PROVEEDO tbProvedo = null;
        try 
        {
            tbProvedo = new PROVEEDO(Sucursal.BC_Tijuana);
        }
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        
        int last = tbProvedo.getRecordsCount(), length = 100;
        List<DBFRecord> list = null;
        Boolean flagContinue = false;
        while(last > 0)
        {
            int begin = last - length;
            list = tbProvedo.readBlock(begin,last);
            flagContinue = importProviderNext(database,list,logger,longTest);
            if(flagContinue == null) return false;//error
            if(flagContinue) return true;
            if(begin < length) length = last;            
            last -= length;
        }
        return true;
    }
    
    /**
     * 
     * @param dbserver
     * @param logger
     * @param longTest
     * @return
     * @throws SQLException 
     */
    public boolean  importLinkTitems(Database dbserver,ArrayList<String> logger,int longTest) throws SQLException
    {
        String tableUpdateT = Titem.MYSQL_AVATAR_TABLE_BACKWARD_TITEM;        
        boolean flC = importLinkTitemsDetail(dbserver,SIIL.services.grua.Charger.MYSQL_AVATAR_TABLE, tableUpdateT);        
        if(!flC) return false;
        boolean flB = importLinkTitemsDetail(dbserver,SIIL.services.grua.Battery.MYSQL_AVATAR_TABLE, tableUpdateT);        
        if(!flB) return false;
        boolean flF = importLinkTitemsDetail(dbserver,SIIL.services.grua.Forklift.MYSQL_AVATAR_TABLE,  tableUpdateT);        
        if(!flF) return false;
        
        
        return true;
    }

    private boolean importLinkTitemsDetail(Database dbserver, String tableUpdateTFinal, String tableUpdateT) throws SQLException 
    {
        String sql = "SELECT number FROM " + tableUpdateTFinal + " WHERE id = 0 OR id IS NULL";//Todas las queno nueten id en titem
        ResultSet rs = null;
        System.out.println(" Buscando ... " + sql);
        rs = dbserver.query(sql);
        String numberStr = "";
        String updateSQlT,updateSQlFinal = "";
        int numberID = 0;
        while (rs.next()) 
        {
            numberStr = rs.getString(1);
            System.out.println(numberStr + " procesiong.");
            numberID = Item.select(dbserver.getConnection(),numberStr);
            if(numberID  < 1) 
            {
                System.out.println(numberStr + " no existe Item.");
                continue;
            }
            updateSQlT = "UPDATE " + tableUpdateT + " SET id = " + numberID + " WHERE BD = '" + Titem.MYSQL_AVATAR_TABLE_BACKWARD_BD + "' AND number = '" + numberStr + "'";
            updateSQlFinal = "UPDATE " + tableUpdateTFinal + " SET id = " + numberID + " WHERE BD = '" + Titem.MYSQL_AVATAR_TABLE_BACKWARD_BD + "' AND number = '" + numberStr + "'";
            Statement stmt = dbserver.getConnection().createStatement();
            System.out.println(tableUpdateT);
            System.out.println(updateSQlFinal);
            int effectedT = stmt.executeUpdate(updateSQlT);
            int effectedFinal  = stmt.executeUpdate(updateSQlFinal);
            if (effectedT != 1 & effectedFinal != 1)
            {
                System.err.println("La cantidad de registro afectados para actulizar la bateria '" + numberStr + "' es incorrecto.");
                return false;
            }
            else
            {
                System.out.println(numberStr + " Link Done.");
            }
        }
        return true;
    }
    /**
     * Lee la bsase de datos del CN par aimportar los clientes nuevos que detecte
     * @param database
     * @param logger
     * @param longTest
     * @return 
     */
    public boolean importClients(Database database,ArrayList<String> logger,int longTest)
    {
        if(!CN60.isWorking())
        {
            return false;
        }
        CLIENTE tbCliente = null;
        try 
        {
            tbCliente = new CLIENTE(Sucursal.BC_Tijuana);
        }
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        
        int last = tbCliente.getRecordsCount(), length = 100;
        List<DBFRecord> list = null;
        Boolean flagContinue = false;
        while(last > 0)
        {
            int begin = last - length;
            list = tbCliente.readBlock(begin,last);
            //if(list == list) return true;
            flagContinue = importClientsNext(database, list, logger, longTest);
            if(flagContinue == null) return false;//error
            if(flagContinue) return true;
            if(begin < length) length = last;            
            last -= length;
        }        
        tbCliente.close();
        return true;
    }
        
    protected SIIL.CN.Records.ARTICULO getCN60Descripcion(ARTICULO tbArticulo, String number) 
    {
        List<DBFRecord> list = tbArticulo.readWhere(new Clause(0,Operator.TRIMEQUAL,number),1,'R');
        if(list.size() > 0)
        {
            return new SIIL.CN.Records.ARTICULO(list.get(0));
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 
     * @param orden
     * @param database
     * @return true si la operacion se realizo false si no.
     * @throws SQLException
     * @throws IOException 
     */
    @Deprecated
    public boolean importCNQuotation(ServiceQuotation orden,Database database) throws SQLException, IOException
    {
        if(!CN60.isWorking())
        {
            return false;
        }
        
        //Importacion de renglones desde en CN
        orden.downOffice(database);
        COTIZACI tbCotiza = null;
        try 
        {
            if(null != orden.getOffice())
            switch (orden.getOffice()) 
            {
                case "bc.tj":
                    tbCotiza = new COTIZACI(Sucursal.BC_Tijuana);
                    break;
                case "bc.mx":
                    tbCotiza = new COTIZACI(Sucursal.BC_Mexicali);
                    break;
                case "bc.ens":
                    tbCotiza = new COTIZACI(Sucursal.BC_Ensenada);
                    break;
                default:
                    return false;
            }
        } 
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        COTIZA_R tbCotiza_r = null;
        try 
        {
            if(null != orden.getOffice())
            switch (orden.getOffice()) 
            {
                case "bc.tj":
                    tbCotiza_r = new COTIZA_R(Sucursal.BC_Tijuana);
                    break;
                case "bc.mx":
                    tbCotiza_r = new COTIZA_R(Sucursal.BC_Mexicali);
                    break;
                case "bc.ens":
                    tbCotiza_r = new COTIZA_R(Sucursal.BC_Ensenada);
                    break;
                default:
                    return false;
            }
        } 
        catch (IOException ex) 
        {
            /*
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
            );*/
            return false;
        }
        ARTICULO tbArticulo = null;
        try 
        {
            if(null != orden.getOffice())
            switch (orden.getOffice()) 
            {
                case "bc.tj":
                    tbArticulo = new ARTICULO(Sucursal.BC_Tijuana);
                    break;
                case "bc.mx":
                    tbArticulo = new ARTICULO(Sucursal.BC_Mexicali);
                    break;
                case "bc.ens":
                    tbArticulo = new ARTICULO(Sucursal.BC_Ensenada);
                    break;
                default:
                    return false;
            }
        } 
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        
        SIIL.CN.Records.COTIZACI recCotiza = tbCotiza.readWhere(orden.getFolio());
        if(recCotiza == null) return false;//no existe la cotizacion
        
        System.out.println("Folio : " + recCotiza.getFolio());
        List<SIIL.CN.Records.COTIZA_R> renglones = recCotiza.readWhere(tbCotiza_r);
        List<Item> items = new ArrayList<>();
        //Registrar En BD
        this.countCNQuote = renglones.size();
        for(SIIL.CN.Records.COTIZA_R r : renglones)
        {
            String number,description,unidad;
            number = r.getNumber();
            
            Item item = null;
            Return<Integer> ret = Item.isExist(database.getConnection(),number);
            if(ret.isFail())
            {
                //si no existe enconces
                //Descargar la descripcion desde el CN60
                SIIL.CN.Records.ARTICULO art = getCN60Descripcion(tbArticulo,number);
                description = art.getDescription();
                unidad = art.getReciben();
                unidad = unidad.trim().toLowerCase();                
                if(unidad.equals("servicio"))
                {
                    item = new Service(-1);
                    Return<Integer> retIse = ((Service)item).insert(database.getConnection(),number, description,Item.Type.SERVICIO,false,null); 
                }
                else if(unidad.equals("pieza") | unidad.equals("piezas") | unidad.equals("pza.") | unidad.equals("pieza.") | unidad.equals("piesa")  | unidad.equals("piesas") | unidad.equals("piezas.") | unidad.equals("pza") | unidad.equals("cajas")  | unidad.equals("caja") | unidad.equals("cubeta") | unidad.equals("cubeta."))
                {
                    item = new Refection(-1);
                    Return<Integer> retIse = ((Refection)item).insert(database.getConnection(),number, description,false,null);
                }
                else
                {
                    throw new InvalidParameterException("Tipo de elemento desconocido : " + number + " - " + unidad);
                }
                item.temporalCantidad = r.getCantidad();
                unidad = art.getReciben();
                item.upUnidad(database, unidad);
                items.add(item);
            }
            else
            {
                //ya existe.
                Return<Integer> retnmew = Refection.isExist(database.getConnection(), number);
                if(retnmew.isFlag())
                {
                    item = new Refection(retnmew.getParam());
                }
                else if(Service.isExist(database.getConnection(), number).isFlag())
                {
                    item = new Service(Service.isExist(database.getConnection(),number).getParam());
                }
                else
                {
                    item = new Item(Item.isExist(database.getConnection(),number).getParam());
                }
                item.temporalCantidad = r.getCantidad();
                //item.upUnidad(database, unidad);
                items.add(item);
                continue;
            }           
        }
        
        Quotation oper = null;
        orden.downSerie(database.getConnection());
        if(orden.getSerie() == null) return false;
        orden.downQuotation(database.getConnection());
        if(orden.getQuotation() != null)
        {            
            oper = orden.getQuotation();
            oper.upTotal(database, recCotiza.getTotal());
        }
        else
        {
            oper = new Quotation(-1);
            orden.downCompany(database);
            String serie = "";
            Return retOpe = oper.insert(database,new Office(database, orden.getOffice()), new State(1),orden.getCredential().getUser(),database.getTimestamp(), (Enterprise) orden.getCompany(),orden.getFolio(),orden.getSucursal().getSerieOffice(Office.Platform.CN60),"SQ",orden);
            oper.upFlag(database.getConnection(), 'A');
            if(retOpe.isFail()) return false;
            Return<Integer> retQ = orden.upQuotation(database.getConnection(),oper);
            oper.upTotal(database, recCotiza.getTotal());
        }
        
        //Comproposito de saber cuales estan en la base de datos actualmente
        Map<String,Row> actualRows = new HashMap<>();
        List<Row> rows = Row.select(database.getConnection(), oper);
        for(Row row : rows)
        {
            row.downItem(database.getConnection());
            row.getItem().downItem(database.getConnection());
            row.getItem().getItem().downNumber(database.getConnection());
            actualRows.put(row.getItem().getItem().getNumber(), row);
        }
        
        //Agregar las piezas que faltan al flow
        Flow flow = null;
        for(Item item : items)
        {
            item.downNumber(database.getConnection());
            if(!actualRows.containsKey(item.getNumber()))//verificar si existe el item correspondiente al flow.
            {
                for(int i = 0; i < item.temporalCantidad; i++)
                {
                    flow = new Flow(-1);
                    Return<Integer> retFlow = flow.insert(database.getConnection(), database.getTimestamp(), false, null, item);
                    flow.upQuotation(database.getConnection(), oper);
                    //flow.upCantidad(database,(int)item.temporalCantidad);
                    //flow.downItem(database);
                    Row row = new Row(-1);
                    row.insert(database.getConnection(), oper, flow);
                    item.downNumber(database.getConnection());
                    actualRows.put(item.getNumber(), row) ;//se registran las que se estan agregando.
                }
            }
        }
        
        //Ya que se cuales son las actuales elimino el todas las que estan en el CN, las que quedan son las que hay que eliminar del tool.
        for(SIIL.CN.Records.COTIZA_R r : renglones)
        {
            Row row = actualRows.remove(r.getNumber());//eliminar todas las que estan en el CN
        }
        
        //las que quedan son las que sobran y se deven eliminar
        Collection<Row> deletedRows = actualRows.values();
        for(Row row : deletedRows)
        {
            row.delete(database.getConnection());
        }
        
        tbArticulo.close();
        tbCotiza.close();
        tbCotiza_r.close();
        return true;        
    }

    /**
     * @return the countCNQuote
     */
    public int getCountCNQuote() {
        return countCNQuote;
    }

    private boolean importClientsNext(Database database,List<DBFRecord> list,ArrayList<String> log,int longTest) 
    {
        int counlongTest = 0;
        for(int i = list.size() - 1; i >= 0; i--)
        {
            counlongTest++;
            DBFRecord record = list.get(i);
            String number = record.getString(0).trim();  
            String name = record.getString(1).trim();            
            name = name.replaceAll("'", " ");
            if(number == null) continue;
            if(number.length() < 2) continue;
            int ret = 0;
            try 
            {
                ret = Titem.isExist(database, number);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ImportsCN.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            
            if(ret > 0)
            {
                continue;//ya existe pasar al siguiente
            }
            //System.out.println(record.getString(0) + " -- " + record.getString(1));
            Describe.Type type = Describe.getParse(number);
            String lg = "";
            if(null != type)
            switch (type) 
            {
                case Battery:
                    Battery battery = new Battery(-1);
                    try
                    {
                        lg = "Agregando Bateria " + number + " ... ";
                        battery.insert(database.getConnection(),number,name,Titem.importType(database, number),false,"N/A");
                        Flow flow = new Flow(-1);
                        flow.insert(database.getConnection(), new Date(), true, "?",battery);
                        lg += "Done";
                        counlongTest = 0;
                        log.add(lg);
                    }
                    catch (SQLException ex)
                    {
                        lg += "Fail : " + number + " -- " + ex.getMessage();
                        log.add(lg);
                        Logger.getLogger(ImportsCN.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case Charger:
                    Charger charger = new Charger(-1);
                    try
                    {
                        lg = "Agregando Cargador " + number + " ... ";
                        charger.insert(database.getConnection(),number,name,Titem.importType(database, number),false,"N/A");
                        Flow flow = new Flow(-1);
                        flow.insert(database.getConnection(), new Date(), true, "?",charger);
                        lg += "Done";
                        counlongTest = 0;
                        log.add(lg);
                    }
                    catch (SQLException ex)
                    {
                        //Logger.getLogger(ImportsCN.class.getName()).log(Level.SEVERE, null, ex);
                        lg += "Fail : " + number + " -- " + ex.getMessage();
                        log.add(lg);
                    }
                    break;
                case Forklift:
                    Forklift forklift = new Forklift(-1);
                    try
                    {
                        lg = "Agregando Montacargas " + number + " ... ";
                        forklift.insert(database.getConnection(),number,name,Titem.importType(database, number),false,"N/A");
                        Flow flow = new Flow(-1);
                        flow.insert(database.getConnection(), new Date(), true, "?",forklift);
                        lg += "Done";
                        counlongTest = 0;
                        log.add(lg);
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(ImportsCN.class.getName()).log(Level.SEVERE, null, ex);
                        lg += "Fail : " + number + " -- " + ex.getMessage();
                        log.add(lg);
                    }
                    break;
                case Mina:
                    Mina mina = new Mina(-1);
                    try
                    {
                        lg = "Agregando Mina " + number + " ... ";
                        mina.insert(database.getConnection(),number,name,Titem.importType(database, number),false,"N/A");
                        Flow flow = new Flow(-1);
                        flow.insert(database.getConnection(), new Date(), true, "?",mina);
                        lg += "Done";
                        counlongTest = 0;
                        log.add(lg);
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(ImportsCN.class.getName()).log(Level.SEVERE, null, ex);
                        lg += "Fail : " + number + " -- " + ex.getMessage();
                        log.add(lg);
                    }
                    break;
                case Company:
                    break;
                default:
                    lg = "Desconosido " + number + " ... ";
                    log.add(lg);
                    break;
            }
            
            if(type == Describe.Type.Company || type == Describe.Type.Forklift || type == Describe.Type.Battery || type == Describe.Type.Charger || type == Describe.Type.Mina)
            {
                if(!Company.checkExistCo(database,number))
                {
                    Enterprise company = new Enterprise(-1);
                    try 
                    {
                        lg = "Agregando Cliente " + number + " ...";
                        company.insert(database.getConnection(), number, name);
                        lg += " Done";
                        counlongTest = 0;
                        company.upRFC(database.getConnection(), record.getString(5).replaceAll("\\s+",""));
                    }
                    catch (SQLException ex) 
                    {
                        //Logger.getLogger(ImportsCN.class.getName()).log(Level.SEVERE, null, ex);
                        lg += "Fail : " + number + " -- " + ex.getMessage();
                    }
                    log.add(lg);
                }
            }            
        }
        if(counlongTest > longTest) return true;
        return false;
    }
    
    private boolean importProviderNext(Database database,List<DBFRecord> list,ArrayList<String> log,int longTest) 
    {
        int counlongTest = 0;
        for(int i = list.size() - 1; i >= 0; i--)
        {
            counlongTest++;
            DBFRecord record = list.get(i);
            String number = record.getString(0).trim();
            if(number == null) continue;
            if(number.length() < 2) continue;
            int ret = 0;
            try 
            {
                ret = Provider.isExist(database, number);
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ImportsCN.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }            
            if(ret > 0)
            {
                continue;//ya existe pasar al siguiente
            }                   
            
            Provider provider = new Provider(-1);
            Return<Integer> ret2 = null;
            try 
            {
                ret2 = provider.insert(database.getConnection(),record.getString(0).trim(),record.getString(1));
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ImportsCN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(counlongTest > longTest) return true;
        return false;
    }
}
