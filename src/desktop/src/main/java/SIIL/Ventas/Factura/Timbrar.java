

package SIIL.Ventas.Factura;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Azael
 */

public class Timbrar 
{

    // Existen otras formas de leer 
    public String readFile(String ruta) {
        try {
            FileInputStream fstream = new FileInputStream(ruta);
            DataInputStream entrada = new DataInputStream(fstream);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;
            StringBuffer sB = new StringBuffer();
            while ((strLinea = buffer.readLine()) != null) {
                sB.append(strLinea).append("\n");
            }
            entrada.close();
            return sB.toString();
        } catch (Exception e) {
        }
        return null;
    }
    
}