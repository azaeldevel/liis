
package SIIL.artifact;

import java.io.FileNotFoundException;

/**
 *
 * @author Azael
 */
public class DeployException extends FileNotFoundException
{
    public DeployException()
    {
        super("No se encontro el archivo de configuracion de despliegue.");
    }
}
