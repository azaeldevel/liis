
package core;

import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;

/**
 *
 * @author Azael Reyes
 */
public class IntegerInputVerifier extends InputVerifier
{
    private boolean opNothing;
    
    public IntegerInputVerifier(boolean  opNoting)
    {
        this.opNothing = opNoting;
    }
    
    @Override
    public boolean verify(JComponent input) 
    {
        if(input instanceof javax.swing.JTextField)
        {
            if(opNothing)
            {
                return ((javax.swing.JTextField) input).getText().matches("^([0-9]+)?$");
            }
            else
            {
                return ((javax.swing.JTextField) input).getText().matches("^[0-9]+$");
            }
        }
        return false;
    }
    
    @Override
    public boolean shouldYieldFocus(JComponent input) 
    {
        boolean v = verify(input);
        if(v)
        {
            if(input instanceof javax.swing.JTextField)
            {
                ((javax.swing.JTextField) input).setBackground(Color.WHITE);
            }
        }
        else
        {
            if(input instanceof javax.swing.JTextField)
            {
                ((javax.swing.JTextField) input).setBackground(new Color(255,102,102));
            }
        }
        return v;
    }
}
