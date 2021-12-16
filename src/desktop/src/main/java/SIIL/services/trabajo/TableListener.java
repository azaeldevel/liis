
package SIIL.services.trabajo;

import java.awt.Container;
import java.awt.Point;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Azael Reyes
 */
public class TableListener  implements ListSelectionListener 
{	
    private JTable rowHeadersTable;
    private JTable userTable;
    private JViewport userTableViewPort;
    private JViewport rowHeadersViewPort;
 
    public TableListener(JTable rowHeadersTable, JTable userTable) 
    {
	this.userTable = userTable;
	this.rowHeadersTable = rowHeadersTable; 
	Container p = userTable.getParent();
	userTableViewPort = (JViewport) p; 
	Container p2 = rowHeadersTable.getParent();
	rowHeadersViewPort = (JViewport) p2;
	Point newPosition = userTableViewPort.getViewPosition();
	rowHeadersViewPort.setViewPosition(newPosition); 
	// userTableViewPort.setViewPosition(newPosition); 
	rowHeadersTable.getSelectionModel().addListSelectionListener(this);
	userTable.getSelectionModel().addListSelectionListener(this); 
    }
 
    public void valueChanged(ListSelectionEvent e) 
    {
	System.out.println("ListSelectionEvent");
 
	if (e.getSource() == userTable.getSelectionModel()) 
	{
            rowHeadersTable.getSelectionModel().removeListSelectionListener(this);
            rowHeadersTable.getSelectionModel().clearSelection();
 
            int[] rows = userTable.getSelectedRows();
 
            for (int i = 0; i < rows.length; i++) 
            {
		System.out.println("adding row selection to rowHeaders table : " + rows[i]);
		rowHeadersTable.getSelectionModel().addSelectionInterval(rows[i], rows[i]); 
            }
            int[] iarr = rowHeadersTable.getSelectedRows();
            rowHeadersTable.getSelectionModel().addListSelectionListener(this);
	} 
	else if(e.getSource() == rowHeadersTable.getSelectionModel()) 
	{
            System.out.println("in rowHeadersTable");
            System.out.println("e.getValueIsAdjusting() " + e.getValueIsAdjusting());
 
            boolean isColumnSelectionAllowed = userTable.getColumnSelectionAllowed();
            boolean isRowSelectionAllowed = userTable.getRowSelectionAllowed();
            boolean isCellSelectionAllowed = userTable.getCellSelectionEnabled();
 
            userTable.getSelectionModel().removeListSelectionListener(this);
            userTable.getSelectionModel().clearSelection();
 
            int[] rows = rowHeadersTable.getSelectedRows();
 
            if (isRowSelectionAllowed && !isCellSelectionAllowed && !isColumnSelectionAllowed) 
            {
		for (int i = 0; i < rows.length; i++) 
                {
                    userTable.addRowSelectionInterval(rows[i], rows[i]);
                    System.out.println("NI? - viewPort1 y = " + userTableViewPort.getViewPosition().y + " viewPort2 y = " + rowHeadersViewPort.getViewPosition().y);
                    userTableViewPort.setViewPosition(rowHeadersViewPort.getViewPosition());
 
                }
            } 
            else 
            {
		// looks cleaner
		userTableViewPort.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE); 
		System.out.println(""); 
		for (int i = 0; i < rows.length; i++) 
                {
                    if (i == 0) 
                    {
			// need to create row first with change selection
			userTable.changeSelection(rows[i], 0, false, false);
			userTable.changeSelection(rows[i], userTable.getColumnCount(), false, true);
 
                    } 
                    else 
                    {
			userTable.addRowSelectionInterval(rows[i], rows[i]);
                    }
		}
            }
            // re-adding the listener to the user table
            userTable.getSelectionModel().addListSelectionListener(this);
	} 
	else 
	{
            System.out.println("no table selestion model");
	}
    }
}
