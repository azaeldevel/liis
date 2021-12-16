
package SIIL.CN.Engine;

/**
 *
 * @author Azael Reyes
 */
public class Clause 
{
    private int record;
    private Object value;
    private Operator op;

    public Clause(int record,Operator operator, Object value)
    {
        this.record = record;
        this.value = value;
        this.op = operator;
    }
    /**
     * @return the record
     */
    public int getRecord() {
        return record;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return the op
     */
    public Operator getOp() {
        return op;
    }
    
}
