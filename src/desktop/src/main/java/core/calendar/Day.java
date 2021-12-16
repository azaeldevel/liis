
package core.calendar;

import SIIL.Server.Database;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Azael Reyes
 */
public class Day extends JButton
{    
    private static final Color ORANGE = new Color(255,157,102);
    private IMarking marking;    
    private JPanel panel;
    private Date date;
    private Database db;
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if(marking != null & getDate() != null)
        {
            ((Marking)marking).setDB(db);
            if(marking.isMarkRed(date) && panel != null)
            {
                Color oldColor = g.getColor();
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                Point point = new Point();
                SwingUtilities.convertPoint(this,point,panel);
                int x1 = point.x + (getBounds().width / 5);
                int y1 = point.y + 5;
                int x2 = point.x + 4*(getBounds().width / 5);
                int y2 = point.y + 5; 
                Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);             
                g2d.draw(line);                
                g.setColor(oldColor);                
            }
            else if(marking.isMarkYellow(date) && panel != null)
            {
                Color oldColor = g.getColor();
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.ORANGE);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                Point point = new Point();
                SwingUtilities.convertPoint(this,point,panel);
                int x1 = point.x + (getBounds().width / 5);
                int y1 = point.y + 5;
                int x2 = point.x + 4*(getBounds().width / 5);
                int y2 = point.y + 5; 
                Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);             
                g2d.draw(line);                
                g.setColor(oldColor);                
            }
            else if(marking.isMarkGray(date) && panel != null)
            {
                Color oldColor = g.getColor();
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.GRAY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                Point point = new Point();
                SwingUtilities.convertPoint(this,point,panel);
                int x1 = point.x + (getBounds().width / 5);
                int y1 = point.y + 5;
                int x2 = point.x + 4*(getBounds().width / 5);
                int y2 = point.y + 5; 
                Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);              
                g2d.draw(line);                
                g.setColor(oldColor);
            }
        }
    }
    
    public Day()
    {
        super();
    }
    
    public Day(String text)
    {
        super(text);
    }

    /**
     * @param marking the marking to set
     */
    public void setMarking(IMarking marking) {
        this.marking = marking;
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param db the db to set
     */
    public void setDB(Database db) {
        this.db = db;
    }
}
