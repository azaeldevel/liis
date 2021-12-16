
package SIIL.Servicios.Orden;

import java.util.Timer;


/**
 *
 * @author Azael
 */
public class ListTask extends java.util.TimerTask
{
    Read list;
    Timer timer;
            
    public ListTask(Read list,int sec)
    {
        this.list = list;
        timer = new Timer();
        timer.scheduleAtFixedRate(this, sec * 1000,sec * 1000);
    }

    @Override
    public void run() 
    {
        list.reloadTable();
    }
}
