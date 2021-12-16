
package SIIL.panel.notif;

import SIIL.Server.MySQL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Azael
 */
public class Notification extends org.jdesktop.swingx.JXTaskPane
{
    private javax.swing.JTextArea tx;
    private javax.swing.JScrollPane scroll;
    private Message msg;
    
    public String getComment()
    {
        return msg.getComment();
    }
    /**
     * 
     * @param menssage 
     */
    public void setComment(String menssage)
    {
        msg.setComment(menssage);
        tx.setText(menssage);
    }
    
    /**
     * 
     * @param title 
     */
    public void setTitle(String title)
    {
        super.setTitle(title);
        msg.setTitle(title);
    }
    
    /**
     * 
     * @return 
     */
    public String getTitle()
    {
        return msg.getTitle();
    }
    
    
    
    public Notification()
    {
        scroll = new javax.swing.JScrollPane();
        getContentPane().add(scroll);
        tx = new javax.swing.JTextArea();
        tx.setEditable(false);
        tx.setColumns(20);
        tx.setRows(5);
        scroll.setViewportView(tx);
        msg = new Message();
    }
    
    
    public int insert(MySQL conn) throws SQLException,Exception
    {
        msg.createID(conn);
        int r = msg.insert(conn);
        
        return r;
    }
}
