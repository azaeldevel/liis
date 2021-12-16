
package SIIL.Server;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/**
 * @version 1.0
 * @since Diciembre 8, 2014
 * @author Azael Reyes
 */
public class Movimiento 
{
    private String BD;
    private String folio;
    private Date fhMov;
    protected String tmov;
    private String uso;
    private SIIL.Server.Company comp;
    private String firma;
    private String note;
    private String ItemClass;
    private String sa;
    private String owner;
    private float horometro;
    private String sucursal;
    private String objetivo;

    public enum Command
    {
        Recuperacion,
        Reparacion,
    }
    
    private Messages retornoTitem(Titem titem, MySQL conn) 
    {
        try 
        {
            String tclass = titem.toString().split("@")[0];
            String strSQL = "";
            strSQL = "SELECT folio,compNumber,id,tmov,uso FROM Movements_Resolved WHERE ( tmov='sal' or  tmov='ent' ) and numeco = '" + titem.getNumber() + "' ORDER BY fhMov DESC LIMIT 2 ";
            Statement stmt = (Statement)conn.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(strSQL);
            if(rs.next())
            {
                uso = rs.getString("uso");//se asigna el mismo uso del anterior movimiento
                tmov = "ent";
                comp = new SIIL.Server.Company();
                comp.setBD(BD);
                comp.setNumber(rs.getString("compNumber"));
                if(rs.getString("tmov").equals("sal"))
                {
                    String regID1 = rs.getString("id");
                    if(rs.next())//verifiando que halla una entreda previa a la ultima salida
                    {
                        if(rs.getString("tmov").equals("ent"))
                        {
                            return Messages.Successful;
                        }
                        else
                        {
                            System.out.println("Inconsistencia entre el " + regID1 + " y " + rs.getString("id") + " [" + strSQL + "]" );
                            return Messages.Inconsistent;
                        }
                    }
                }
                return Messages.Successful;
            }
            else
            {
                System.out.println("No hay registro de salida para la consulta ["  + strSQL + "]");
                return Messages.NoSalida;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Messages.Nada;
    }

    public int Return(Database conn, Forklift forklift,ArrayList<Titem> titems,SIIL.Server.Movimiento.Command cmd,boolean  rnote) 
    {
        try 
        {
            String strSQL = "";
            if(SIIL.Server.Movimiento.Command.Recuperacion == cmd || SIIL.Server.Movimiento.Command.Reparacion == cmd)
            {
                strSQL = "SELECT folio,compNumber,id,tmov,uso,suc FROM Movements_Resolved WHERE ( tmov='sal' or  tmov='ent' ) and numeco = '" + forklift.getNumber() + "' ORDER BY fhMov DESC LIMIT 2 ";
                Statement stmt = (Statement)conn.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(strSQL);
                if(rs.next())
                {
                    if(SIIL.Server.Movimiento.Command.Recuperacion == cmd)
                    {
                        uso = rs.getString("uso");//se asigna el mismo uso del anterior movimiento
                    }
                    else if(SIIL.Server.Movimiento.Command.Reparacion == cmd)
                    {
                        uso = "rep" ;
                    }
                    else
                    {
                        ;
                    }
                    
                    if(owner.equals("SIIL"))
                    {
                        tmov = "ent";
                    }
                    else if(owner.equals("client"))
                    {
                        tmov = "sal";
                    }
                    if(comp == null)
                    {
                        comp = new SIIL.Server.Company();
                        comp.setBD(BD);
                        comp.setNumber(rs.getString("compNumber"));                        
                    }
                    //sino se usa el cleinte capturado
                    
                    if(owner.equals("SIIL"))
                    {
                        if(rs.getString("tmov").equals("sal"))
                        {//deve existir una salida previa
                            /*String regID1 = rs.getString("id");
                            if(rs.next())//verifiando que halla una entreda previa a la ultima salida
                            {
                                if(rs.getString("tmov").equals("ent"))
                                {
                                    //return true;
                                }
                                else
                                {
                                    System.out.println("Inconsistencia entre el " + regID1 + " y " + rs.getString("id") + " [" + strSQL + "]" );
                                    return 0;
                                }
                            }*/
                        }
                        else
                        {
                            System.err.println("No existe registro de salida para el movimienti");
                            return 0;
                        }
                    }
                    else if(owner.equals("client"))
                    {
                        if(rs.getString("tmov").equals("ent"))
                        {
                            String regID1 = rs.getString("id");
                            if(rs.next())//verifiando que halla una entreda previa a la ultima salida
                            {
                                if(rs.getString("tmov").equals("sal"))
                                {
                                    //return true;
                                }
                                else
                                {
                                    System.out.println("Inconsistencia entre el " + regID1 + " y " + rs.getString("id") + " [" + strSQL + "]" );
                                    return 0;
                                }
                            }
                        }
                    }
                    //return true;
                }
                else
                {
                    System.err.println("No hay registro de salida para la consulta ["  + strSQL + "]");
                    return 0;
                }
            }
            /*else if(SIIL.Server.Movimiento.Command.Reparacion == cmd)
            {
                tmov = "ent";
                uso = "rep";
                comp = new SIIL.Server.Company();
                comp.setBD(BD);
                comp.setNumber("999");
            }*/
            else
            {
                System.err.println("Comando desconocido "  + cmd );
                return 0;
            }                       
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int count = 0;
        try {
            count += insert(conn, titems);
        } catch (Exception ex) {
            Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Resumov rmov = new Resumov();
        boolean flB = true;
        boolean flC = true;
        
           if(forklift.getBattery() != null)
            {
                flB = forklift.linkBattery(conn);
                if(forklift.getBattery().getCharger() != null)
                {
                    //flC = forklift.getBattery().linkCharger(conn);
                }
            }
        rmov.setForklift(forklift);
        if(comp != null)
        {
            comp.setNumber("999");
            rmov.setCompany(comp);
        }
        rmov.setUso("disp");
        rmov.setSucursal(sucursal);
        if(rnote)
        {
            rmov.setNote(note);
        }
        else
        {
            rmov.setNote("");
        }
        rmov.setFhMov(fhMov);
        count += rmov.update(conn);
        return count;
    }

    public int Return(Database conn, Battery battery,ArrayList<Titem> titems,SIIL.Server.Movimiento.Command cmd) 
    {        
        int count = 0;
        try {
            count += insert(conn, titems);
        } catch (Exception ex) {
            Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public int Return(Database conn, ArrayList<Titem> titems) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public enum Messages
    {
        NoSalida,
        Successful,
        Nada,
        Inconsistent
    }
    /**
     * @return the mov
     */
    public String getFolio() 
    {
        return folio;
    }

    /**
     * @param mov the mov to set
     */
    public void setFolio(String mov) 
    {
        this.folio = mov;
    }

    /**
     * @return the tmov
     */
    public String getTmov() {
        return tmov;
    }

    /**
     * @param tmov the tmov to set
     */
    public void setTmov(String tmov) {
        this.tmov = tmov;
    }

    /**
     * @return the uso
     */
    public String getUso() {
        return uso;
    }

    /**
     * @param uso the uso to set
     */
    public void setUso(String uso) {
        this.uso = uso;
    }

    /**
     * @return the sa
     */
    public String getSa() {
        return sa;
    }

    /**
     * @param sa the sa to set
     */
    public void setSA(String sa) {
        this.sa = sa;
    }

    /**
     * @return the client
     */
    public SIIL.Server.Company getCompany() {
        return comp;
    }

    /**
     * @param client the client to set
     */
    public void setCompany(SIIL.Server.Company client) {
        this.comp = client;
    }

    /**
     * @return the firma
     */
    public String getFirma() {
        return firma;
    }

    /**
     * @param firma the firma to set
     */
    public void setFirma(String firma) {
        this.firma = firma;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }


    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public void setFhMov(Date fh)
    {
        fhMov = fh;
    }
    public void setFhMov(String fh) throws ParseException 
    {
        fhMov = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH).parse(fh);
    }

    public String covertUso(JComboBox cbusomov) 
    {
        switch(cbusomov.getSelectedIndex())
        {
            case 0:
                break;
            case 1:
                return "disp";
            case 2:
                return "rtacp";
            case 3:
                return "pres";
            case 4:   
                return "rtaoc";
            case 5:    
                return "rep";
            case 6:    
                return "vta";  
            case 7:    
                return "mov";  
        }
        return "";
    }
    public void setUso(JComboBox cbusomov) 
    {
        switch(cbusomov.getSelectedIndex())
        {
            case 0:
                break;
            case 1:
                uso = "disp";
                break;
            case 2:
                uso = "rtacp";               
                break;
            case 3:
                uso = "pres";               
                break;
            case 4:   
                uso = "rtaoc";            
                break;
            case 5:    
                uso = "rep";           
                break;
            case 6:    
                uso = "vta";           
                break;
            case 7:    
                uso = "mov";           
                break;
        }
    }

    public Messages setTmov(JComboBox cbtmov,MySQL conn) 
    {
        switch(cbtmov.getSelectedIndex())
        {
            case 0:                
                break;
            case 1:
                tmov = "ent";
                break;
            case 2:
                tmov = "sal";                
                break;
            case 3:
                tmov = "mov";                
                break;
            case 4:
                //return retornoTitem((Titem)titems.get(0),conn);                   
        }
        return Messages.Nada;
    }
 
    public int insert(Database conn, ArrayList<SIIL.Server.Titem> titems)
    {
        try
        {
        String fields = "";
        String values = "";
        
        if(BD != null)
        {
            fields = "BD";
            values = "'" + BD + "'";
        }
        else
        {
            throw new Exception("Se deve de especificar la Base de Datos");
        }
        

        //
        if(folio != null)
        {
            fields = fields + ",folio";
            values = values + ",'" + folio + "'";
        }   
        //
        if(fhMov != null)
        {
            fields = fields + ",fhMov";
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            values = values + ",STR_TO_DATE('" + df.format(fhMov) + "','%d/%m/%Y')";
        }        
        else
        {
            throw new Exception("Se deve de especificar la fecha");
        }
        //
        if(tmov != null)
        {
            fields = fields + ",tmov";
            values = values + ",'" + tmov + "'";
        }
        
        //
        if(uso != null)
        {
            if(fields.length()>0 || values.length()>0)
            {
                fields = fields + ",uso";
                values = values + ",'" + uso + "'"; 
            }
            else
            {
                fields = fields + "uso";
                values = values + "'" + uso + "'"; 
            }
        }
        
        if(sa != null)
        {
            fields = fields + ",sa";
            values = values + ",'" + sa + "'";
        }
        
        //
        if(comp != null)
        {
            //fields = fields + ",compBD";
            //values = values + ",'" + client.getBD() + "'";
            fields = fields + ",compNumber";
            values = values + ",'" + comp.getNumber() + "'";
        }
        
        //
        if(ItemClass != null)
        {
            fields = fields + ",movClass";
            values = values + ",'" + ItemClass + "'";
        }
        
        if(firma != null)
        {
            fields = fields + ",firma";
            values = values + ",'" + firma + "'";
        }
        
        if(sucursal != null)
        {
            fields = fields + ",suc";
            values = values + ",'" + sucursal + "'";
        }            
        
        if(note != null && note.length() > 0)
        {
            fields = fields + ",note";
            values = values + ",\"" + note + "\"";
        }
        
        if(owner != null)
        {
            if(owner.equals("SIIL"))
            {
                /*if(horometro > 0 )
                {
                    fields = fields + ",horometro";
                    values = values + "," + horometro + "";
                }*/
                /*if(forklift.getNumber().length() > 0)
                {
                   fields = fields + ",forklift";
                   values = values + ",'" + forklift.getNumber() + "'";
                }*/
            }
            if(owner.length() > 0 )
            {
                fields = fields + ",owner";
                values = values + ",'" + owner + "'";
            }
        }
        
        String sql = " Movements(" + fields + ") VALUES(" + values + ")";
        int id = conn.insert(sql,Statement.RETURN_GENERATED_KEYS);
        int count;
        if(id > 0) 
        {
            count = 1;
        }
        else //uno del inser previo
        {
            count = 0;
        }
        for(int i = 0; i < titems.size(); i++)
        {
            Titem t = (Titem)titems.get(i);
            String strSQlitem = " Movtitems(mov,numeco,marca,modelo,serie,titemClass,horometro) VALUES(" + id  + ",";
            if(t.getNumber() != null)
            {
                strSQlitem = strSQlitem + "'" + t.getNumber() + "',";
            }
            else
            {
                strSQlitem = strSQlitem + "NULL,";
            }
            if(t.getMarca() != null )
            {
                strSQlitem = strSQlitem + "'" + t.getMarca() + "',";
            }
            else
            {
                strSQlitem = strSQlitem + "NULL,";
            }
            if(t.getModelo()!= null )
            {
                strSQlitem = strSQlitem + "'" + t.getModelo() + "',";
            }
            else
            {
                strSQlitem = strSQlitem + "NULL,";
            }
            if(t.getSerie()!= null )
            {
                strSQlitem = strSQlitem + "'" + t.getSerie() + "',";
            }
            else
            {
                strSQlitem = strSQlitem + "NULL,";
            }
            strSQlitem = strSQlitem + "'" + t.getItemClass() + "',";
            if(t.getItemClass().equals("forklift") && ((SIIL.Server.Forklift)t).getHoromentro() > 0)
            {
                strSQlitem = strSQlitem + ((SIIL.Server.Forklift)t).getHoromentro() + ")";
            }
            else
            {
                strSQlitem = strSQlitem + "NULL)";
            }
            if(t.getItemClass() == null)
            {
                System.out.println(" class name: " + t.toString());
            }
            t.setSucursal(sucursal);
            count = count + t.updateSucursal(conn);
            count = count + conn.insert(strSQlitem);
        }
        return count;
        }
        catch(Exception ex)
        {
            Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }


    /**
     * @return the BD
     */
    public String getBD() {
        return BD;
    }

    /**
     * @param BD the BD to set
     */
    public void setBD(String BD) {
        this.BD = BD;
    }

    /**
     * @return the ItemClass
     */
    public String getItemClass() {
        return ItemClass;
    }

    /**
     * @param ItemClass the ItemClass to set
     */
    public void setItemClass(String ItemClass) {
        this.ItemClass = ItemClass;
    }

    /**
     * @return the horometro
     */
    public float getHorometro() {
        return horometro;
    }

    /**
     * @param horometro the horometro to set
     */
    public void setHorometro(float horometro) {
        this.horometro = horometro;
    }

    /**
     * @return the sucursal
     */
    public String getSucursal() {
        return sucursal;
    }

    /**
     * @param sucursal the sucursal to set
     */
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }


    /**
     * @return the objetivo
     */
    public String getObjetivo() {
        return objetivo;
    }

    /**
     * @param objetivo the objetivo to set
     */
    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }
   
}
