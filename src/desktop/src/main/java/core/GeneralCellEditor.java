
package core;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Azael Reyes
 */
public class GeneralCellEditor  extends AbstractCellEditor implements TableCellEditor 
{
    protected EditorDelegate delegate;
    protected int clickCountToStart = 1;
    protected JComponent editorComponent;

    public GeneralCellEditor(final JTextField textField) 
    {
        editorComponent = textField;
        delegate = new GeneralCellEditor.EditorDelegate() 
        {
            @Override
            public void setValue(Object value) 
            {
                textField.setText((value != null) ? value.toString() : "");
            }

            @Override
            public Object getCellEditorValue() 
            {
                return textField.getText();
            }
        };
        textField.addActionListener(delegate);
    }
    
    public GeneralCellEditor(final JComboBox cbMechanic)
    {
        //this.clickCountToStart = 2;
        editorComponent = cbMechanic;
        cbMechanic.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        delegate = new EditorDelegate() 
        {
            @Override
            public void setValue(Object value) 
            {
                cbMechanic.setSelectedItem(value);
            }

            @Override
            public Object getCellEditorValue() 
            {
                return cbMechanic.getSelectedItem();
            }

            @Override
            public boolean shouldSelectCell(EventObject anEvent) 
            {
                if (anEvent instanceof MouseEvent) 
                {
                    MouseEvent e = (MouseEvent)anEvent;
                    return e.getID() != MouseEvent.MOUSE_DRAGGED;
                }
                return true;
            }
            
            @Override
            public boolean stopCellEditing() 
            {
                if (cbMechanic.isEditable()) 
                {
                    cbMechanic.actionPerformed(new ActionEvent(this, 0, ""));
                }
                return super.stopCellEditing();
            }
        };
        cbMechanic.addActionListener(delegate);       
    }
    
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
    {
        delegate.setValue(value);        
        return editorComponent;
    }  
    
    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#getCellEditorValue
     */
    @Override
    public Object getCellEditorValue() 
    {
        return delegate.getCellEditorValue();
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#isCellEditable(EventObject)
     */
    @Override
    public boolean isCellEditable(EventObject anEvent) 
    {
        return delegate.isCellEditable(anEvent);
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#shouldSelectCell(EventObject)
     */
    @Override
    public boolean shouldSelectCell(EventObject anEvent) 
    {
        return delegate.shouldSelectCell(anEvent);
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#stopCellEditing
     */
    @Override
    public boolean stopCellEditing() 
    {
        return delegate.stopCellEditing();
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#cancelCellEditing
     */
    public void cancelCellEditing() {
        delegate.cancelCellEditing();
    }
    

        protected class EditorDelegate implements ActionListener, ItemListener, Serializable 
        {

        /**  The value of this cell. */
        protected Object value;

       /**
        * Returns the value of this cell.
        * @return the value of this cell
        */
        public Object getCellEditorValue() {
            return value;
        }

       /**
        * Sets the value of this cell.
        * @param value the new value of this cell
        */
        public void setValue(Object value) {
            this.value = value;
        }

       /**
        * Returns true if <code>anEvent</code> is <b>not</b> a
        * <code>MouseEvent</code>.  Otherwise, it returns true
        * if the necessary number of clicks have occurred, and
        * returns false otherwise.
        *
        * @param   anEvent         the event
        * @return  true  if cell is ready for editing, false otherwise
        * @see #setClickCountToStart
        * @see #shouldSelectCell
        */
        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
            }
            return true;
        }

       /**
        * Returns true to indicate that the editing cell may
        * be selected.
        *
        * @param   anEvent         the event
        * @return  true
        * @see #isCellEditable
        */
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

       /**
        * Returns true to indicate that editing has begun.
        *
        * @param anEvent          the event
        */
        public boolean startCellEditing(EventObject anEvent) {
            return true;
        }

       /**
        * Stops editing and
        * returns true to indicate that editing has stopped.
        * This method calls <code>fireEditingStopped</code>.
        *
        * @return  true
        */
        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

       /**
        * Cancels editing.  This method calls <code>fireEditingCanceled</code>.
        */
       public void cancelCellEditing() {
           fireEditingCanceled();
       }

       /**
        * When an action is performed, editing is ended.
        * @param e the action event
        * @see #stopCellEditing
        */
        public void actionPerformed(ActionEvent e) {
            this.stopCellEditing();
        }

       /**
        * When an item's state changes, editing is ended.
        * @param e the action event
        * @see #stopCellEditing
        */
        public void itemStateChanged(ItemEvent e) {
            this.stopCellEditing();
        }
    }
}
