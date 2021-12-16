
package sales;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class TimbreFiscal 
{
    private int responseCode;
    private String responseMessage;
    private String token;
    private URL url;
    private String strURLCancel;
    private String strURLStamp;
    private String strURLCancel32;
    private String strURLStamp32;
    private String strURLCancel33;
    private String strURLStamp33;
    
    public enum XML
    {
        v32,
        v33
    }
    
    public String getToken()
    {
        return token;
    }
    
    public TimbreFiscal()
    {
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server(); 
        try 
        { 
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        } 
        catch (ParserConfigurationException | SAXException | IOException ex) 
        {
            Logger.getLogger(TimbreFiscal.class.getName()).log(Level.SEVERE, null, ex);
        }
        token = serverConfig.getTokenDiversa();
        strURLCancel = serverConfig.getCancelDiversa32();
        strURLStamp = serverConfig.getStampDiversa32();
        strURLCancel33 = serverConfig.getCancelDiversa33();
        strURLStamp33 = serverConfig.getStampDiversa33();        
    }
        
    public boolean cancelar(String rfc,String uuid,XML version) throws FileNotFoundException, MalformedURLException, IOException 
    {
        //Creamos una nueva instancia del objeto URL con la direcci—n del servicio de timbrado
        String strUrl = "";
        if(version == XML.v32)
        {
            strUrl += strURLCancel32;
            strURLCancel = strURLCancel32;
        }
        else if(version == XML.v33)
        {
            strUrl += strURLCancel33;
            strURLCancel = strURLCancel33;
        }
        strUrl += rfc + "/" + uuid;
	url = new URL(strUrl);
	
	/* Inicializamos un nuevo objeto HttpURLConnection que nos servira para realizar la petici—n HTTP */
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	
	// Indicamos que el methodo HTTP a utilizar es POST
	connection.setRequestMethod("POST");
	
	/* Agregamos un header a la petici—n indicando el token que utilizaremos, en este caso el de 
	* prueba 'ABCD1234'. Este debe ser modificado una vez que querramos utilizar nuestra propia cuenta
	* para timbrar */
	connection.setRequestProperty("x-auth-token", token);
        
	// Configuramos la conexi—n para permitirnos recibir y enviar informaci—n
	connection.setUseCaches(false);
	connection.setDoInput(true);
	connection.setDoOutput(true);
        			
	// Obtenemos el codigo de respuesta del servidor
	responseCode = connection.getResponseCode();
	responseMessage = connection.getResponseMessage();
			
	//System.out.println(String.format("Timbre: %s", stamp.toString()));
        if(responseCode == 200)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean timbrar(File xml,XML version) throws FileNotFoundException, MalformedURLException, IOException 
    {	
        //Por facilidad leemos el CFD a timbrar del archivo cfd.xml que se encuentra en la carpeta resources
        String cfd = new Scanner(xml).useDelimiter("\\Z").next();
	
        //Creamos una nueva instancia del objeto URL con la direcci—n del servicio de timbrado
        if(version == XML.v32)
        {
            url = new URL(strURLStamp32);
            strURLStamp = strURLStamp32;
        }
        else if(version == XML.v33)
        {
            url = new URL(strURLStamp33);
            strURLStamp = strURLStamp33;
        }
	
	/* Inicializamos un nuevo objeto HttpURLConnection que nos servira para realizar la petici—n HTTP */
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	
	// Indicamos que el methodo HTTP a utilizar es POST
	connection.setRequestMethod("POST");
	
	/* Agregamos un header a la petici—n indicando el token que utilizaremos, en este caso el de 
	* prueba 'ABCD1234'. Este debe ser modificado una vez que querramos utilizar nuestra propia cuenta
	* para timbrar */
	connection.setRequestProperty("x-auth-token", token);
        
	// Configuramos la conexi—n para permitirnos recibir y enviar informaci—n
	connection.setUseCaches(false);
	connection.setDoInput(true);
	connection.setDoOutput(true);
        
	// Utilizando una instancia de DataOutputStream enviamos el CFD a timbrar.
	DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
	dataOutputStream.writeBytes(cfd);
	dataOutputStream.flush();
	dataOutputStream.close();
			
	// Obtenemos el codigo de respuesta del servidor
	responseCode = connection.getResponseCode();
	responseMessage = connection.getResponseMessage();
			

	/* Recuperamos el resultado del timbrado utilizando una instancia de InputStream y lo almacenamos en la variable stamp */
	InputStream inputStream = connection.getInputStream();
        if(responseCode == 200)
        {
            OutputStream out = new FileOutputStream(xml);
            IOUtils.copy(inputStream,out);
            inputStream.close();
            out.flush();
            out.close();            
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @return the responseCode
     */
    public int getResponseCode() 
    {
        return responseCode;
    }

    /**
     * @return the responseMessage
     */
    public String getResponseMessage() 
    {
        return responseMessage;
    }

    /**
     * @return the url
     */
    public URL getURL() 
    {
        return url;
    }

    /**
     * @return the strURLCancel
     */
    public String getStrURLCancel() 
    {
        return strURLCancel;
    }

    /**
     * @return the strURLStamp
     */
    public String getStrURLStamp() 
    {
        return strURLStamp;
    }
}
