
package SIIL.core.config;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
/**
 *
 * @author Azael Reyes
 */
public class Server 
{
    private MysqlDataSource datasource;
    private int speechPort;
    private String speechHost; 
    private HashMap<String,String> passwords;
    private boolean flag;
    private String ftpHost;
    private int ftpPort;
    private String ftpUser;
    private String ftpPasss;
    private String ftpBase;
    private HashMap<String,String> workspace;
    private String tokenDiversa;
    private String prefiFileDiversa;
    private String passwordDiversa;
    private String stampDiversa32;
    private String cancelDiversa32;
    private String stampDiversa33;
    private String cancelDiversa33;
    private SQLServerDataSource sqlsComercial;
    private String phase;
    
    public String getPhase()
    {
        return phase;
    }
    
    public SQLServerDataSource getComercial()
    {
        return sqlsComercial;
    }
    private boolean validateXMLSchema(String xsdPath, String xmlPath)
    {
        try 
        {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        }
        catch (IOException e)
        {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
        catch(SAXException e1)
        {
            System.out.println("SAX Exception: " + e1.getMessage());
            return false;
        }
        
        return true;	
    }
    
    public Server()
    {
    }

    public void loadFile(String file) throws ParserConfigurationException, SAXException, IOException 
    {
        flag = false;
        passwords = new HashMap<String,String>();
        workspace = new HashMap<String,String>();
        passwords.put("956A-981GLP97", "123456ar");
        passwords.put("956A-981GLP97-dev", "123456");
        passwords.put("956A-981GLP98", "R3sP41Do5");
        passwords.put("956A-9719MOP7", "12345678a");
        passwords.put("956A-9719MOPA", "1234567cd");
        passwords.put("956A-9719GOPA", "Trycer2000");
        passwords.put("956A-971XJOPA", "65094%?¡wrs");
        parseXML(file);
    }
    
    public MysqlDataSource getDataSource()
    {
        return this.datasource;
    }
    public MysqlDataSource getMySQLDS()
    {
        return this.datasource;
    }

    public int getSpeechPort() 
    {
        return this.speechPort;
    }
    
    public String getSpeechHost()
    {
        return this.speechHost;
    }

    private boolean parseXML(String directory) throws ParserConfigurationException, SAXException, IOException 
    {
        String OS = System.getProperty("os.name").toLowerCase();
        File inputFile;
        File inputFileXSD;
        
        if (OS.contains("windows"))
        {
            inputFile = new File(directory + "\\server.xml");
            inputFileXSD = new File(directory + "\\server.xsd");              
        }
        else if (OS.contains("linux") || OS.contains("Unix"))
        {
            inputFile = new File(directory + "/server.xml");
            inputFileXSD = new File(directory + "/server.xsd"); 
            
        }
        else
        {
            throw new ParserConfigurationException("No Hay soporte para el sistema el SO '" + OS + "'");
        }
                              
        
        boolean isValid = validateXMLSchema(inputFileXSD.getAbsolutePath(),inputFile.getAbsolutePath());         
        if(!isValid)
        {
            throw new ParserConfigurationException("El formto del XML no es valido");
        }
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try 
        {
            dBuilder = dbFactory.newDocumentBuilder();
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        Document doc = null;
        doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("mysql");
        Node nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element eElement = (Element) nNode;
            datasource = new MysqlDataSource();
            datasource.setUser(eElement.getElementsByTagName("user").item(0).getTextContent());
            String pass = this.passwords.get(eElement.getElementsByTagName("code").item(0).getTextContent());
            //System.out.println(">>>> Password:"  + pass);
            datasource.setPassword(pass);
            datasource.setDatabaseName(eElement.getElementsByTagName("database").item(0).getTextContent());
            datasource.setServerName(eElement.getElementsByTagName("host").item(0).getTextContent());            
        }
        nList = doc.getElementsByTagName("speech");
        nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element eElement = (Element) nNode;   
            speechPort = Integer.valueOf(eElement.getElementsByTagName("port").item(0).getTextContent());
            speechHost = eElement.getElementsByTagName("host").item(0).getTextContent();
            flag=true;
        }
        nList = doc.getElementsByTagName("ftp");
        nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element eElement = (Element) nNode;   
            ftpHost = eElement.getElementsByTagName("host").item(0).getTextContent();
            ftpPort = Integer.valueOf(eElement.getElementsByTagName("port").item(0).getTextContent());
            ftpUser = eElement.getElementsByTagName("user").item(0).getTextContent();
            ftpPasss = this.passwords.get(eElement.getElementsByTagName("code").item(0).getTextContent());
            ftpBase = eElement.getElementsByTagName("base").item(0).getTextContent();
            flag=true;
        }
        nList = doc.getElementsByTagName("workspace");
        nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element eElement = (Element) nNode;   
            getWorkspace().put("failmail",eElement.getElementsByTagName("failmail").item(0).getTextContent().replace("\\", "\\\\"));
            flag=true;
        }
        nList = doc.getElementsByTagName("diversa");
        nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element eElement = (Element) nNode;   
            tokenDiversa = eElement.getElementsByTagName("token").item(0).getTextContent();
            passwordDiversa = this.passwords.get(eElement.getElementsByTagName("code").item(0).getTextContent());
            prefiFileDiversa = eElement.getElementsByTagName("prefixFile").item(0).getTextContent();
            cancelDiversa32 = eElement.getElementsByTagName("cancelService").item(0).getTextContent();
            stampDiversa32 = eElement.getElementsByTagName("stampService").item(0).getTextContent();
            cancelDiversa33 = eElement.getElementsByTagName("cancelService33").item(0).getTextContent();
            stampDiversa33 = eElement.getElementsByTagName("stampService33").item(0).getTextContent();
            flag=true;            
        }
        
        nList = doc.getElementsByTagName("comercial");
        nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element eElement = (Element) nNode;
            sqlsComercial = new SQLServerDataSource();
            sqlsComercial.setUser(eElement.getElementsByTagName("user").item(0).getTextContent());
            String pass = this.passwords.get(eElement.getElementsByTagName("code").item(0).getTextContent());
            sqlsComercial.setPassword(pass);
            sqlsComercial.setDatabaseName(eElement.getElementsByTagName("database").item(0).getTextContent());
            sqlsComercial.setServerName(eElement.getElementsByTagName("serverName").item(0).getTextContent());            
        }
        nList = doc.getElementsByTagName("phase");
        nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element eElement = (Element) nNode;
            phase = eElement.getTextContent();
        }
        return true;
    }

    /**
     * @return the flag
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * @return the ftpHost
     */
    public String getFtpHost() {
        return ftpHost;
    }

    /**
     * @return the ftpPort
     */
    public int getFtpPort() {
        return ftpPort;
    }

    /**
     * @return the ftpUser
     */
    public String getFtpUser() {
        return ftpUser;
    }

    /**
     * @return the ftpPasss
     */
    public String getFtpPasss() {
        return ftpPasss;
    }

    /**
     * @return the ftpBase
     */
    public String getFtpBase() {
        return ftpBase;
    }

    /**
     * @return the workspace
     */
    public HashMap<String,String> getWorkspace() {
        return workspace;
    }

    /**
     * @return the tokenDiversa
     */
    public String getTokenDiversa() {
        return tokenDiversa;
    }

    /**
     * @return the prefiFileDiversa
     */
    public String getPrefiFileDiversa() {
        return prefiFileDiversa;
    }

    /**
     * @return the passwordDiversa
     */
    public String getPasswordDiversa() {
        return passwordDiversa;
    }

    /**
     * @return the stampDiversa
     */
    public String getStampDiversa32() {
        return stampDiversa32;
    }

    /**
     * @return the cancelDiversa
     */
    public String getCancelDiversa32() {
        return cancelDiversa32;
    }

    /**
     * @return the stampDiversa33
     */
    public String getStampDiversa33() {
        return stampDiversa33;
    }

    /**
     * @return the cancelDiversa33
     */
    public String getCancelDiversa33() {
        return cancelDiversa33;
    }
}
