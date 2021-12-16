
package sales;

//import SAT.CatalogoTest;
import SIIL.CN.Tables.CN60;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.services.order.Association;
import core.Renglon;
import core.bobeda.Archivo;
import core.bobeda.FTP;
import core.bobeda.Vault;
import database.mysql.sales.Remision;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
//import mx.bigdata.sat.cfdi.CFDI;
//import mx.bigdata.sat.cfdi.schema.Comprobante;
//import mx.bigdata.sat.cfdi.v33.schema.CTipoFactor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import process.Moneda;
import process.Return;
import stock.Flow;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class InvoiceTest 
{
    private static final boolean FL_COMMIT = true;
    private static PrivateKey key; 
    private static X509Certificate cert;
  
    /*@BeforeClass public static void loadKeys()
    {
        
        try 
        {
            key = Invoice.privateKey();
            cert = Invoice.certificado();
        }
        catch (IOException | ParserConfigurationException | SAXException ex) {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
    }*/
    
    public InvoiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class Factura.
     */
    @Test
    public void testLoadFromCN()
    {
        //System.out.println("load");
        /*Invoice factura = null;
        try 
        {
            factura = factura.loadFromCN(new File(CN60.DIR_CN60_TJ), "A", 29520);
            //System.out.println(factura.getComprobante().getFolio() + " - " + factura.getComprobante().getFecha() + " - " + factura.getComprobante().getReceptor().getNombre());
        }
        catch (Exception ex) 
        {
            //Logger.getLogger(FacturaTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(factura == null)
        {
            fail("No hay Factura");
        }*/
    }

    @Test public void testValidateVerify()
    {
        /*
        try 
        {
            Comprobante comprobante = 
            CFDv32 cfd = new CFDv32(comprobante);
            cfd.sellar(key, cert);
            cfd.validar();
            cfd.verificar();
            FileOutputStream out = new FileOutputStream("C:\\Users\\Azael Reyes\\Desktop\\temp\\cfd.xml");
            cfd.guardar(out);
            out.flush();
            out.close();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            //fail(ex.getMessage());
        }
        */
    }
    
    @Test public void testDiversa()
    {
        /*
        File file = new File("C:\\Users\\Azael Reyes\\Desktop\\temp\\cfd.diversa.xml");
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            dbserver.close();
            return;
        }
        FTP ftpServer = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = ftpServer.connect(serverConfig);
        }
        catch (IOException ex) 
        {
            //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            dbserver.close();
            return;
        }
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low; 
        List<core.Renglon> renglones = new ArrayList<>();
        Flow flow = new Flow(-1);    
        Flow flow2 = new Flow(-1);
        
        process.State state = new process.State(-1);
        Person operator = new Person();
        Enterprise enterprise = new Enterprise(-1);
        Invoice invoice = new Invoice();
        Office office = new Office(1);
        core.Renglon reng1 = new core.Renglon();
        core.Renglon reng2 = new core.Renglon();
        try 
        {
            state.selectRandom(dbserver.getConnection());
            state.download(dbserver);
            operator.selectRandom(dbserver.getConnection());
            operator.download(dbserver);
            enterprise.selectRandom(dbserver.getConnection());
            enterprise.download(dbserver);
            office.selectRandom(dbserver.getConnection());  
            office.download(dbserver.getConnection());
            Return retinvo = invoice.insert(dbserver, office, state, operator, date, enterprise, Result, office.getSerieOffice(Office.Platform.TOOLS), "SI");            
            if(retinvo.isFail())
            {
                fail(retinvo.getMessage());
                dbserver.close();
                return;
            }
            flow.select(dbserver.getConnection(), 14962);
            flow.download(dbserver);
            flow.getItem().downDescription(dbserver.getConnection());
            flow.getItem().downNumber(dbserver.getConnection());
            reng1.add(flow);
            reng1.upCostSale(dbserver, 100.0, Moneda.USD);
            renglones.add(reng1);
            flow2.select(dbserver.getConnection(), 7230);
            flow2.download(dbserver);
            flow2.getItem().downDescription(dbserver.getConnection());
            flow2.getItem().downNumber(dbserver.getConnection());
            reng2.add(flow2);
            reng2.upCostSale(dbserver, 100.0, Moneda.USD);
            renglones.add(reng2);
            Return retInset = Renglon.insert(dbserver, invoice, renglones);
            if(retInset.isFail())
            {
                fail(retInset.getMessage());
                dbserver.close();
                return;
            }
            reng1.download(dbserver,invoice);
            reng1.downCostSale(dbserver);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            dbserver.close();
            return;
        }
        
        HashMap<String,Object> params = new HashMap<>();
        params.put("Serie","F");
        params.put("Folio","123456");
        params.put("FormaPago","02");
        params.put("CondicionesDePago","Credito a 20 dias");
        params.put("TipoCambio","1");
        params.put("LugarExpedicion","22356");
        params.put("Confirmacion","aB1cD");
        params.put("Emisor.Nombre","PHARMA PLUS SA DE CV");
        params.put("Emisor.RFC","AAA010101AAA");
        params.put("Emisor.RegimenFiscal","601");
        params.put("Relacionados.TipoRelacion","06");
        params.put("Relacionado.UUID","a0452045-89cb-4792-9cc0-153f21faab7f");
        params.put("Receptor.RFC","AAA010101AAA");
        params.put("Receptor.Nombre","Juan Perez Perez");
        params.put("Receptor.NumRegIdTrib","ResidenteExtranjero1");
        params.put("TipoFactor",mx.bigdata.sat.cfdi.v33.schema.CTipoFactor.TASA);
        params.put("TasaOCuota","0.160000");
        params.put("Impuesto","002");
        params.put("Impuestos.Traslados.Impuesto","002");
        params.put("Impuestos.Traslados.CTipoFactor",mx.bigdata.sat.cfdi.v33.schema.CTipoFactor.TASA);
        params.put("Impuestos.Traslados","0.160000");
        
        CFDI retinv = null;
        try 
        {
            retinv = invoice.createCFDI33(dbserver, renglones, file, params);            
        }
        catch (Exception ex) 
        {
            fail(ex.getMessage());
            dbserver.close();
            return;
        }
        
        if(retinv == null)
        {
            fail("Falló la creacion del comprobante");
            return;
        }
        
        TimbreFiscal timbrar = new TimbreFiscal();
        try 
        {
            if(!timbrar.timbrar(file,TimbreFiscal.XML.v33)) 
            {
                fail(timbrar.getResponseMessage() + " -- " + timbrar.getStrURLStamp());
                return;
            }
        }
        catch (MalformedURLException ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        catch (IOException ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(timbrar.getResponseCode() + " : " +  timbrar.getResponseMessage() + " -- " + timbrar.getStrURLStamp() + " TOKEN : " + timbrar.getToken());
            dbserver.close();
            return;
        }
        Return retXML = null;
        try 
        {
            if(!ftpServer.isExist(Vault.Type.FACTURA_XML,Vault.Origen.INTERNO, enterprise.getNumber()))
            {
                if(!ftpServer.addSubdirectory(Vault.Type.FACTURA_XML,Vault.Origen.INTERNO, enterprise.getNumber()))
                {
                    fail("No se pudo crear el subdirectio de cliente");
                    return;
                }
            }
            retXML = Vault.add(dbserver, ftpServer,invoice.getFullFolio() + ".xml",new FileInputStream(file),enterprise ,"XML de prueba.", Vault.Type.FACTURA_XML, Vault.Origen.INTERNO);
            if(retXML.isFail())
            {
                fail(retXML.getMessage());
            }
            if(!invoice.upXML(dbserver, (Archivo)retXML.getParam()))
            {
                fail("Fallo la insercion en BMD del XML.");
                return;
            }
            if(!invoice.downXMLArchivo(dbserver))
            {
                fail("No se descargo XML.");
                return;
            }            
        }
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        
        boolean retXMLD = false;
        try 
        {
            retXMLD = invoice.getXMLArchivo().download(dbserver, ftpServer, file);
            Return ret = invoice.cancelar(dbserver,null);
            if(ret.isFail())
            {
                fail(ret.getMessage());
                return;
            }
        } 
        catch (Exception ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        


        if((retinv != null && retXML.isFlag() && retXMLD ) && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if((retinv == null || retXML.isFail() || retXMLD == false ) && FL_COMMIT == false)
        {
            try 
            {
                dbserver.rollback();
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
                return;
            }
        }
        
        dbserver.close();*/
      
    }
    /**
     * Test of getSerie method, of class Factura.
     */
    @Test
    public void testGetSerie() 
    {
        System.out.println("getSerie");
    }

    /**
     * Test of getFolio method, of class Factura.
     */
    @Test
    public void testGetFolio() 
    {
        System.out.println("getFolio");        
    }

    /**
     * Test of getXML method, of class Factura.
     */
    @Test
    public void testGetXML() {
        System.out.println("getXML");
    }

    /**
     * Test of getPDF method, of class Factura.
     */
    @Test
    public void testGetPDF() {
        System.out.println("getPDF");
    }

    /**
     * Test of createComprobante method, of class Invoice.
     */
    @Test
    public void testCreateComprobante() 
    {
        System.out.println("createComprobante");

    }

    /**
     * Test of getFullFolio method, of class Invoice.
     */
    @Test
    public void testGetFullFolio() {
        System.out.println("getFullFolio");
    }


    /**
     * Test of getFilledFolio method, of class Invoice.
     */
    @Test
    public void testGetFilledFolio() {
        System.out.println("getFilledFolio");
    }

    /**
     * Test of getComprobante method, of class Invoice.
     */
    @Test
    public void testGetComprobante() {
        System.out.println("getComprobante");
    }

    /**
     * Test of getEnterprise method, of class Invoice.
     */
    @Test
    public void testGetEnterprise() 
    {
        System.out.println("getEnterprise");
    }
    
    /**
     * Test of upEmail method, of class Invoice.
     */
    @Test
    public void testUpEmail() 
    {
        System.out.println("upEmail");
    }

    /**
     * Test of insert method, of class Invoice.
     */
    @Test
    public void testInsert() throws Exception {
        System.out.println("insert");
    }

    /**
     * Test of fromCN method, of class Invoice.
     */
    @Test
    public void testFromCN()
    {
        System.out.println("fromCN");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        Invoice invoice = new Invoice(-1);
        Office office = new Office(1);
        String folio = "29500";
        Person person = new Person(-1);
        Return ret = null;
        try 
        {
            office.download(dbserver.getConnection());
            person.selectRandom(dbserver.getConnection());
            ret = invoice.fromCN(dbserver, office, folio, person);            
        } 
        catch (IOException | ParseException | SQLException ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        

        if(ret.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(ret.isFlag() && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();*/
    }

    /**
     * Test of addToBobeda method, of class Invoice.
     */
    @Test
    public void testAddToBobeda() 
    {
        System.out.println("addToBobeda");
    }

    /**
     * Test of loadFiles method, of class Invoice.
     */
    @Test
    public void testLoadFiles() 
    {
        System.out.println("loadFiles");
    }

    /**
     * Test of loadPDF method, of class Invoice.
     */
    @Test
    public void testLoadPDF() 
    {
        System.out.println("loadPDF");
    }

    /**
     * Test of loadXML method, of class Invoice.
     */
    @Test
    public void testLoadXML()
    {
        System.out.println("loadXML");
    }

    /**
     * Test of loadFromCN method, of class Invoice.
     */
    @Test
    public void testLoadFromCN_4args()
    {
        System.out.println("loadFromCN");
    }

    /**
     * Test of loadFromCN method, of class Invoice.
     */
    @Test
    public void testLoadFromCN_3args()
    {
        System.out.println("loadFromCN");
    }

    /**
     * Test of getRFC method, of class Invoice.
     */
    @Test
    public void testGetRFC() {
        System.out.println("getRFC");
    }

    /**
     * Test of upOrdenes method, of class Invoice.
     */
    @Test
    public void testUpOrdenes() 
    {
        System.out.println("upOrdenes");
    }

    /**
     * Test of asociarArchivo method, of class Invoice.
     */
    @Test
    public void testAsociarArchivo()
    {
        System.out.println("asociarArchivo");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        FTP ftpServer = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = ftpServer.connect(serverConfig);
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        if(!expResult)
        {
            fail("Fallo la conexion al servidor de archivos");
        }
        Return ret = null;
        try 
        {
            Office office = new Office(1);
            office.download(dbserver.getConnection());
            Invoice invoice = new Invoice(-1);
            invoice.selectRandom(dbserver.getConnection());
            invoice.downFolio(dbserver);
            invoice.downCompany(dbserver);
            invoice.getEnterprise().download(dbserver);
            invoice.download(dbserver);
            ArrayList<Remision> ls = new ArrayList<>();
            Remision rem = new Remision(-1);
            rem.selectRandom(dbserver);
            if(!Invoice.existRemision(dbserver, rem))
            {
                rem.download(dbserver);
                ls.add(rem);
            }
            rem = new Remision(-1);
            rem.selectRandom(dbserver);
            if(!Invoice.existRemision(dbserver, rem))
            {
                rem.download(dbserver);
                ls.add(rem);
            }
            rem = new Remision(-1);
            rem.selectRandom(dbserver);
            if(!Invoice.existRemision(dbserver, rem))
            {
                rem.download(dbserver);
                ls.add(rem);
            }
            if(ls.size() == 0)
            {
                fail("No hay remisoines para asignar a la factura");
            }
            
            File file = new File("kit.pdf");
            FileInputStream in = new FileInputStream(file);
                        
            ret = new Return(true);//ret = Invoice.asociarArchivo(dbserver, ftpServer, office, invoice, ls, in);            
        }
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        

        if(ret.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(ret.isFlag() && FL_COMMIT == false)
        {            
            try 
            {                
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else
        {
            try 
            {
                fail(ret.getMessage());
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();*/
    }

    /**
     * Test of upPDF method, of class Invoice.
     */
    @Test
    public void testUpPDF()
    {
        System.out.println("upPDF");
        
    }

    /**
     * Test of upXML method, of class Invoice.
     */
    @Test
    public void testUpXML() 
    {
        System.out.println("upXML");
    }

    /**
     * Test of massAssociation method, of class Invoice.
     */
    @Test
    public void testMassAssociation()   
    {
        System.out.println("massAssociation");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        FTP ftpServer = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = ftpServer.connect(serverConfig);
        }
        catch (IOException ex) 
        {
            //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        File directory = new File("C:\\Users\\Azael Reyes\\Desktop\\factura");
        List<String> logger = new ArrayList<>();
        Return ret = null;
        try 
        {
            Office office = new Office(1);
            office.download(dbserver.getConnection());
            List<Association<Invoice>> associations = new ArrayList<>();
            Invoice.massAssociationReadDirectory(dbserver, directory, associations, logger, office);
            ret = Invoice.massAssociation(dbserver, ftpServer, directory, logger, associations, null, office,null,null);
            for(String str : logger)
            {
                //System.out.println(str);
            }
            //Order.massAssociationCleanDirectory(associations);
        }
        catch (ParseException | IOException | SQLException ex) 
        {
            //Logger.getLogger(OrderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret.isFlag() && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + affected);
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(ret.isFlag() && FL_COMMIT == false)
        {
            try 
            {
                dbserver.rollback();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
                return;
            }
            //fail("Cant. Reg. incorrectos : " + affected);
        }
        dbserver.close();*/
    }

    /**
     * Test of massAssociationReadDirectory method, of class Invoice.
     */
    @Test
    public void testMassAssociationReadDirectory() 
    {
        System.out.println("massAssociationReadDirectory");
    }

    /**
     * Test of asociarArchivo method, of class Invoice.
     */
    @Test
    public void testAsociarArchivo_5args() 
    {
        System.out.println("asociarArchivo");
    }

    /**
     * Test of downOrdenesArchivo method, of class Invoice.
     */
    @Test
    public void testDownOrdenesArchivo() 
    {
        System.out.println("downOrdenesArchivo");
    }

    /**
     * Test of selectRandom method, of class Invoice.
     */
    @Test
    public void testSelectRandom() 
    {
        System.out.println("selectRandom");        
    }

    /**
     * Test of existRemision method, of class Invoice.
     */
    @Test
    public void testExistRemision() 
    {
        System.out.println("existRemision");
    }

    /**
     * Test of asociarArchivo method, of class Invoice.
     */
    @Test
    public void testAsociarArchivo_6args() 
    {
        System.out.println("asociarArchivo");
    }

    /**
     * Test of getOrdenesArchivo method, of class Invoice.
     */
    @Test
    public void testGetOrdenesArchivo() {
        System.out.println("getOrdenesArchivo");
    }    

    /**
     * Test of cancelarNotif method, of class Invoice.
     */
    @Test
    public void testCancelarNotif() 
    {
        System.out.println("cancelarNotif");
        
        /*
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        FTP ftpServer = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = ftpServer.connect(serverConfig);
        }
        catch (IOException ex) 
        {
            //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        Return ret = null;
        Invoice invoice = new Invoice();
        Office office = new Office(1);
        try 
        {
            office.download(dbserver.getConnection());
            ret = Invoice.exist(dbserver,  "A", "29520", Operational.Type.SalesInvoice);
            if(ret.isFlag())
            {
                invoice = new Invoice((int)ret.getParam());
                invoice.download(dbserver.getConnection());
                invoice.downFolio(dbserver);
                invoice.downSerie(dbserver);
                if(invoice.downXMLArchivo(dbserver))
                {
                    boolean retXMLDown = invoice.getXMLArchivo().download(dbserver, ftpServer, new File(System.getProperty("java.io.tmpdir")));
                }
                else
                {
                    fail("No hay XML en bobeda.");
                    return;
                }
                if(invoice.downPDFArchivo(dbserver))
                {
                    boolean retPDFDown = invoice.getPDFArchivo().download(dbserver, ftpServer, new File(System.getProperty("java.io.tmpdir")));
                }
                File file = new File(invoice.getXMLArchivo().getDownloadFileName());
                if(invoice.leerComprobante(file) == null)
                {
                    fail("Fallo la lectura del XML.");
                    return;
                }                
                if(invoice.downCompany(dbserver))
                {                    
                    invoice.getEnterprise().downMailFact(dbserver);
                    ret = invoice.cancelarNotif(dbserver, invoice.getEnterprise().getEmailFactura(office));
                    if(ret.isFail())
                    {
                        fail(ret.getMessage());
                        return;
                    }
                }
                else
                {
                    fail("Fallo la descarga de informacion de cliente");
                    return;
                }
            }
            else
            {
                fail(ret.getMessage());
                return;
            }
            
        } 
        catch (AddressException | SQLException ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
                    return;
        } 
        catch (Exception ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
                    return;
        }
        

        if(ret.isFlag() && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + affected);
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(ret.isFlag() && FL_COMMIT == false)
        {
            try 
            {
                dbserver.rollback();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
                return;
            }
            //fail("Cant. Reg. incorrectos : " + affected);
        }
        
        dbserver.close();
        ftpServer.close();*/
    }

    /**
     * Test of downSatEstado method, of class Invoice.
     */
    @Test
    public void testDownSatEstado() 
    {
        System.out.println("downSatEstado");
    }

    /**
     * Test of getSatEstado method, of class Invoice.
     */
    @Test
    public void testGetSatEstado() 
    {
        System.out.println("getSatEstado");
    }

    /**
     * Test of upSatEstado method, of class Invoice.
     */
    @Test
    public void testUpSatEstado() 
    {
        System.out.println("upSatEstado");
    }

    /**
     * Test of search method, of class Invoice.
     */
    @Test
    public void testSearch() 
    {
        System.out.println("search");
    }

    /**
     * Test of leerComprobante method, of class Invoice.
     */
    @Test
    public void testLeerComprobante() 
    {
        System.out.println("leerComprobante");
    }

    /**
     * Test of cancelar method, of class Invoice.
     */
    @Test
    public void testCancelar() 
    {
        System.out.println("cancelar");
    }

    /**
     * Test of extracUUID method, of class Invoice.
     */
    @Test
    public void testExtracUUID() 
    {
        System.out.println("extracUUID");
    }

    /**
     * Test of downXMLArchivo method, of class Invoice.
     */
    @Test
    public void testDownXMLArchivo() 
    {
        System.out.println("downXMLArchivo");
    }

    /**
     * Test of downPDFArchivo method, of class Invoice.
     */
    @Test
    public void testDownPDFArchivo() 
    {
        System.out.println("downPDFArchivo");
    }

    /**
     * Test of massAssociationCleanDirectory method, of class Invoice.
     */
    @Test
    public void testMassAssociationCleanDirectory() 
    {
        System.out.println("massAssociationCleanDirectory");
    }

    /**
     * Test of calcularSubTotal method, of class Invoice.
     */
    @Test
    public void testCalcularSubTotal() {
        System.out.println("calcularSubTotal");
    }

    /**
     * Test of calcularIVA method, of class Invoice.
     */
    @Test
    public void testCalcularIVA() {
        System.out.println("calcularIVA");
    }

    /**
     * Test of calcularTotal method, of class Invoice.
     */
    @Test
    public void testCalcularTotal() 
    {
        System.out.println("calcularTotal");
    }

    /**
     * Test of privateKey method, of class Invoice.
     */
    @Test
    public void testPrivateKey() 
    {
        System.out.println("privateKey");
    }

    /**
     * Test of certificado method, of class Invoice.
     */
    @Test
    public void testCertificado()
    {
        System.out.println("certificado");
    }

    /**
     * Test of getPDFArchivo method, of class Invoice.
     */
    @Test
    public void testGetPDFArchivo() {
        System.out.println("getPDFArchivo");
    }

    /**
     * Test of getXMLArchivo method, of class Invoice.
     */
    @Test
    public void testGetXMLArchivo() {
        System.out.println("getXMLArchivo");
    }
}
