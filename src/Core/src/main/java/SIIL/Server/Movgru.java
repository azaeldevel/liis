
package SIIL.Server;

import java.util.ArrayList;

/**
 * Distribucion de Equipos
 * @author areyes
 */
public class Movgru extends SIIL.Server.Movimiento
{
    public boolean write(Database conn,ArrayList<SIIL.Server.Titem> ts) throws Exception 
    {
        
        String fields = "";
        String values = "";
        //
        int flMov = super.insert(conn,ts);
       
        //
        String sql = " Movgru(" + fields + ") VALUES(" + values + ")";
        int flMovgru = conn.insert(sql);

        int flForklift = 0; 
        int flBattery = 0;        
        int flCharger = 0;
        int flRes = 0;
        int flMovtem = 0;
        /*if(getOwner().equals("SIIL"))
        {    
            String sqlMovtem = " Movtem(titem,movid) VALUES(" + forklift.getId() + "," + getId() + ")";
            flMovtem = conn.insert(sqlMovtem);
            String upAcc;

            //Accesorios del montacargas.
            if(battery != null)
            {
                if(battery.getId() > 0)
                {
                    sqlMovtem = " Movtem(titem,movid) VALUES(" + battery.getId() + "," + getId() + ")";
                    flBattery += conn.insert(sqlMovtem);
                    upAcc = " Forklift SET battery =" + battery.getId() + " WHERE item =" + forklift.getId() ;
                    flBattery += conn.update(upAcc);
                }
            }
            if(charger != null)
            {
                if(charger.getId() > 0)
                {
                    sqlMovtem = " Movtem(titem,movid) VALUES(" + charger.getId() + "," + getId() + ")";
                    flCharger += conn.insert(sqlMovtem);
                    upAcc = " Forklift SET charger = " + charger.getId() + " WHERE item =" + forklift.getId() ;
                    flCharger += conn.update(upAcc);
                }
            }
            
            String sqlRes = " Resumov SET tipo='" + super.getUso() + "' , compID =" + getClient().getId() + "  WHERE forklift = " + getForklift().getId();
            flRes = conn.update(sqlRes);
        }*/
        

        
        //Validacion de banderas;        
        if( flRes == 1 && (flForklift == 1 || flForklift == 0) && flMov == 1 && flMovgru == 1 && flMovtem == 1 && (flBattery == 0 || flBattery == 2) && (flCharger == 0 || flCharger ==2))return true;
        return false; 
    }


}
