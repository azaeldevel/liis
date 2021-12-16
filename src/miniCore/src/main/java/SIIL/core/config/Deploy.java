
package SIIL.core.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class Deploy 
{
    enum Phase
    {
        ALPHA,
        BETARELEASE,
        RELEASE,
        UNKNOW
    }
    enum Platform
    {
        LINUX,
        WINDOWS
    }
    
    private Phase phase;
    
    public static Platform checkPlatform() throws Exception
    {
        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("windows"))
        {
            return Platform.WINDOWS;              
        }
        else if (OS.contains("linux"))
        {
            return Platform.LINUX;            
        }
        else
        {
            throw new Exception("No Hay soporte para el sistema el SO '" + OS + "'");
        }
    
    }
    
    /**
     * @return the stage
     */
    public Phase getPhase() 
    {
        return phase;
    }
    
    public Deploy(String file)
    {
        parseXML(file);
    }
    
    private void parseXML(String file) 
    {
        File inputFile = new File(file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try 
        {
            dBuilder = dbFactory.newDocumentBuilder();
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try 
        {
            doc = (Document) dBuilder.parse(inputFile);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        doc.getDocumentElement().normalize();
        //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("deploy");
        Node nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element eElement = (Element) nNode;
            String textContent = eElement.getElementsByTagName("phase").item(0).getTextContent();
            if(textContent.equals("alpha"))
            {
                phase = Phase.ALPHA;
            }
            else if(textContent.equals("betarelease"))
            {
                phase = Phase.BETARELEASE;
            }
            else if(textContent.equals("release"))
            {
                phase = Phase.RELEASE;
            }
            else
            {
                phase = Phase.UNKNOW;
            }
        }
    }
    
}
