
package SIIL.artifact;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael
 */
@Deprecated
public class Artifact 
{

    private Word phase;
    public enum Word
    {
        alpha,
        beta,
        release,
        nothing
    }
    
    private Document document;
    
    /**
     * 
     * @return
     * @throws AmbiguosException 
     */
    @Deprecated
    public Word getPhase() throws AmbiguosException
    {
        NodeList nList = document.getElementsByTagName("phase");
        if(nList.getLength() > 1)
        {
            throw new AmbiguosException("Nodo 'phase' ambiguo");
        }
        else if(nList.item(0).getTextContent().equals("alpha"))
        {
            return Word.alpha;
        }
        else if(nList.item(0).getTextContent().equals("beta"))
        {
            return Word.beta;
        }
        else if(nList.item(0).getTextContent().equals("release"))
        {
            return Word.release;
        }
        else
        {
            return Word.nothing;
        }
    }    
    
    public Artifact(Word phase) throws DeployException
    {
        this.phase = phase;
    }

    @Deprecated
    public Artifact() throws DeployException
    {
        FileInputStream xml = null;
        try 
        {
            xml = new FileInputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "//deploy.xml");
        } 
        catch (FileNotFoundException ex) 
        {
            throw new DeployException();
        }
        read(xml);
    }
        
    public Artifact(FileInputStream xml)
    {
        read(xml);
    }

    private void read(FileInputStream xml) 
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try 
        {
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(xml);
            document.getDocumentElement().normalize();            
        }
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(Artifact.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) 
        {
            Logger.getLogger(Artifact.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(Artifact.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
