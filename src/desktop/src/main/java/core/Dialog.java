
package core;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXDialog;

/**
 *
 * @author Azael Reyes
 */
public class Dialog extends JXDialog 
{
    public void setContent(DialogContent contentPane)
    {        
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - ((Container)contentPane).getWidth()) / 3);
        int y = (int) ((dimension.getHeight() - ((Container)contentPane).getHeight()) / 3);
        super.setLocation(x,y);
        super.setContentPane((Container)contentPane);
        super.setSize(((Container)contentPane).getPreferredSize());
        super.setModal(true);
        contentPane.setDialog(this);
        super.setVisible(true);        
    }

    public Dialog(JComponent content) 
    {
        super(content);
    }
}
