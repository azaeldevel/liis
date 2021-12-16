
package SIIL.panel.notif;

/**
 *
 * @author Azael
 */
public class Panel 
{
    private org.jdesktop.swingx.JXTaskPaneContainer container;
    
    /**
     * 
     * @param c 
     */
    public Panel(org.jdesktop.swingx.JXTaskPaneContainer c)
    {
        container = c;
    }

    public void add(Notification notif) 
    {
        container.add(notif);
    }
}
