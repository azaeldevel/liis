/*
 */
package SIIL.imports;

import SIIL.Server.Database;
import database.mysql.stock.Titem;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.services.grua.Forklift;
import SIIL.services.grua.Battery;
import SIIL.services.grua.Charger;
import SIIL.services.grua.Movements;
import SIIL.services.grua.Resumov;
import SIIL.services.grua.Uso;
import database.mysql.stock.Item;
import database.mysql.stock.Titem.Import;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import process.Return;
import stock.Flow;

/**
 *
 * @author Azael
 */
public class LoteGrua 
{
    private final static String COMMA_DELIMITER = ",";
    private SIIL.core.config.Server serverConfig;
    private Database dbserver;
    
    public LoteGrua(Database dbserver)
    {
        this.dbserver = dbserver;    
    }
    
    public boolean loadClients(String clients) throws FileNotFoundException, IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new FileReader(clients));
        
        List<List<String>> result = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) 
        {
            String[] values = line.split(COMMA_DELIMITER);
            result.add(Arrays.asList(values));
        }

        //System.out.println(result);
        for(List<String> row : result)
        {
            Enterprise enterprise = Enterprise.find(dbserver, row.get(1));
            Return<Integer>  ret = null;
            if(enterprise == null)
            {
                //System.out.println("Adding client " + row.get(1));
                enterprise = new Enterprise();
                String name = row.get(0);
                //if(row.size() > 2) name += row.get(2);
                ret = enterprise.insert(dbserver.getConnection(),row.get(1),name);   
                if(ret.isFail()) return false;
            }
        }
                
        return true;
    }
    
    public boolean loadActivos(String activos) throws FileNotFoundException, IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new FileReader(activos));
        
        List<List<String>> result = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) 
        {
            String[] values = line.split(COMMA_DELIMITER);
            result.add(Arrays.asList(values));
        }
        
        for(List<String> row : result)
        {
            Return ret = new Return(true,"Init..");
            Titem titem = null;
            if(Titem.isExist(dbserver, row.get(0)) == -1)
            {
                switch(Titem.checkType(row.get(0)))
                {
                case FORKLIFT:
                        Forklift forklift = new Forklift(-1);
                        ret = forklift.insert(dbserver.getConnection(),row.get(0),"#",Import.NoImport,false,row.get(4));
                        titem = forklift;
                    break;
                case BATTERY:
                        Battery battery = new Battery(-1);
                        ret = battery.insert(dbserver.getConnection(),row.get(0),"#",Import.NoImport,false,row.get(4));
                        titem = battery;
                    break;
                case CHARGER:
                        Charger charger = new Charger(-1);
                        ret = charger.insert(dbserver.getConnection(),row.get(0),"#",Import.NoImport,false,row.get(4));
                        titem = charger;
                    break;
                    default :
                        continue;
                }       
                if(ret.isFail()) 
                {
                    System.out.println(ret.getMessage());
                    return false;
                }

                ret = titem.upMake(dbserver.getConnection(), row.get(2));
                if(ret.isFail()) 
                {
                    System.out.println(ret.getMessage());
                    return false;
                }

                ret = titem.upModel(dbserver.getConnection(), row.get(3));
                if(ret.isFail()) 
                {
                    System.out.println(ret.getMessage());
                    return false;
                }
                
                ret = titem.upSerie(dbserver.getConnection(), row.get(4));
                if(ret.isFail()) 
                {
                    System.out.println(ret.getMessage());
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public boolean loadMovements(String movs) throws FileNotFoundException, IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new FileReader(movs));
        
        List<List<String>> result = new ArrayList<>();
        //List<List<String>> result2 = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) 
        {
            String[] values = line.split(COMMA_DELIMITER);
            result.add(Arrays.asList(values));
        }
        int counFails = 1;
        
        for(int i = 0; i < result.size() ; i++)
        {
            if(result.get(i).size() < 3)
            {
                System.out.println("Tamaño menor a 3 : " + (i + 1));
                counFails++;
                continue;
            }
            else if(result.get(i).size() < 5)
            {
                System.out.println("Tamaño menor a 5 : " + (i + 1));
                counFails++;
                continue;
            }
            else if(result.get(i).size() < 9)
            {
                System.out.println("Tamaño menor a 9 : " + (i + 1));
                counFails++;
                continue;
            }
            
            String strClient;
            if(result.get(i).size() >= 3)
            {
                strClient = result.get(i).get(2).trim();
                if(!strClient.isEmpty() && !strClient.isBlank())
                {
                    ArrayList<Enterprise> enterprisies = search(result.get(i).get(2).trim());
                    if(enterprisies.size() != 1)
                    {
                        System.out.println("Cliente ambiguo o incomprehensible : " + (i + 1));
                        counFails++;
                    }
                }
                else
                {
                    System.out.println("Campo vacio : " + (i + 1));
                    counFails++;
                }
            }  
            
            if(result.get(i).size() >= 5)        
            {
                String strTitem = result.get(i).get(5).trim();
                if(strTitem.isEmpty() || strTitem.isBlank()) 
                {
                    System.out.println("No hay dfatos en el campo de equipo : " + (i + 1));
                    counFails++;
                }
                else
                {
                    Titem.Type type = Titem.checkType(strTitem);            
                    if(type == Titem.Type.UNKNOW)
                    {
                        System.out.println("El equipaniento es deconocido : " + (i + 1));
                        counFails++;
                    }
                }
            }
            
        }
        Collections.reverse(result);
        
        int countTotal=0, countFound=0, counMovs = 0;
        ArrayList<String> NotFound = new ArrayList<>();
        Set<String> setNotFound = new HashSet(NotFound);
        ArrayList<String> NotMvoment = new ArrayList<>();
        Set<String> setNotMoviment = new HashSet(NotMvoment);        
        /*for(List<String> row : result)
        {
            //System.out.println("row.size() = " + row.size());
            if(row.size() < 3) continue;
            
            countTotal++;
            
            ArrayList<Enterprise> enterprisies = search(row.get(2).trim());
            if(enterprisies.size() == 1)
            {
                countFound++;
                continue;
            }
            
            setNotFound.add(row.get(2).trim());            
        }*/
        int idrow = 1;
        for(List<String> row : result)
        {
            idrow++;
            Battery battery = null;
            Charger charger = null;
            Forklift forklift = null;
            Charger addCharger = null;
            Battery addBattery = null;
            //System.out.println("row.size() = " + row.size());
            
            Return ret;
            
            if(row.size() < 6)
            {
                //System.out.println(row);
                setNotMoviment.add(row.toString());
                continue;
            }
            
            
            if(row.size() < 5) continue;
            String strTitem = row.get(5).trim();
            if(strTitem.isEmpty() || strTitem.isBlank()) continue;
            Titem.Type type = Titem.checkType(strTitem);            
            if(type == Titem.Type.UNKNOW) continue;
            
            if(type == Titem.Type.FORKLIFT)
            {
                forklift = new Forklift(-1);
                //System.out.println("Searching " + row.get(5).trim() + " ...");
                forklift.searchForklift(dbserver, row.get(5).trim());
                if(forklift.getID() > 0)
                {
                    forklift.downNumber(dbserver.getConnection());
                    forklift.downSerie(dbserver);
                    forklift.downMake(dbserver.getConnection());
                    forklift.downModel(dbserver);
                }
                else
                {
                    forklift.insert(dbserver.getConnection(), row.get(5).trim(), "Importacion Dic/20", Forklift.Import.NoImport, false, "");    
                    if(row.get(6).isEmpty() || row.get(6).isBlank()) forklift.upMake(dbserver.getConnection(), row.get(6).trim());
                    if(row.get(7).isEmpty() || row.get(7).isBlank()) forklift.upModel(dbserver.getConnection(), row.get(7).trim());
                    if(row.get(8).isEmpty() || row.get(8).isBlank()) forklift.upSerie(dbserver.getConnection(), row.get(8).trim());
                    forklift.downNumber(dbserver.getConnection());
                    forklift.downSerie(dbserver);
                    forklift.downMake(dbserver.getConnection());
                    forklift.downModel(dbserver);
                }
            }
            else if(type == Titem.Type.BATTERY)
            {
                if(row.size() >= 5)
                {
                    String strBattery = row.get(5).trim();
                    if(!strBattery.isEmpty() && !strBattery.isBlank())
                    {
                        battery = new Battery(-1);
                        ret = battery.search(dbserver.getConnection(), strBattery);
                        if(battery.getID() > 0)
                        {
                            battery.downNumber(dbserver.getConnection());
                            battery.downSerie(dbserver);
                            battery.downMake(dbserver.getConnection());
                            battery.downModel(dbserver);    
                        }
                        else
                        {
                            battery.insert(dbserver.getConnection(), row.get(5).trim(), "Importacion Dic/20", Forklift.Import.NoImport, false, "");
                            if(row.get(6).isEmpty() || row.get(6).isBlank()) battery.upMake(dbserver.getConnection(), row.get(6).trim());
                            if(row.get(7).isEmpty() || row.get(7).isBlank()) battery.upModel(dbserver.getConnection(), row.get(7).trim());
                            if(row.get(8).isEmpty() || row.get(8).isBlank()) battery.upSerie(dbserver.getConnection(), row.get(8).trim());
                            battery.downNumber(dbserver.getConnection());
                            battery.downSerie(dbserver);
                            battery.downMake(dbserver.getConnection());
                            battery.downModel(dbserver);    
                        }
                    }
                }
            }
            else if(type == Titem.Type.CHARGER)
            {
                if(row.size() >= 5)
                {
                    if(!row.get(5).isEmpty() && !row.get(5).isBlank())
                    {
                        charger = new Charger(-1);
                        ret = charger.search(dbserver.getConnection(), row.get(5).trim());
                        if(charger.getID() > 0)
                        {
                            charger.downNumber(dbserver.getConnection());
                            charger.downSerie(dbserver);
                            charger.downMake(dbserver.getConnection());
                            charger.downModel(dbserver);
                        }
                        else
                        {
                            charger.insert(dbserver.getConnection(), row.get(5).trim(), "Importacion Dic/20", Forklift.Import.NoImport, false, "");
                            if(row.get(6).isEmpty() || row.get(6).isBlank()) charger.upMake(dbserver.getConnection(), row.get(6).trim());
                            if(row.get(7).isEmpty() || row.get(7).isBlank()) charger.upModel(dbserver.getConnection(), row.get(7).trim());
                            if(row.get(8).isEmpty() || row.get(8).isBlank()) charger.upSerie(dbserver.getConnection(), row.get(8).trim());
                            charger.downNumber(dbserver.getConnection());
                            charger.downSerie(dbserver);
                            charger.downMake(dbserver.getConnection());
                            charger.downModel(dbserver);
                        }
                    }
                }
            }
            
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            String strDate = row.get(0).trim();
            Date date = null;
            if(strDate.isEmpty() || strDate.isBlank()) 
            {
                try 
                {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(row.get(0).trim());
                } 
                catch (ParseException ex) 
                {
                    Logger.getLogger(LoteGrua.class.getName()).log(Level.SEVERE, null, ex);
                    date = new Date();
                }
            }
            else
            {
                date = new Date();
            }  
            List<Flow> titems = new ArrayList<>();
            
            if(type == Titem.Type.FORKLIFT)
            {
                if(forklift.getID() > 0)
                {
                    Flow forkFlow = new Flow(-1);
                    String strSerie = forkFlow.getSerie() == null? "" : forkFlow.getSerie();
                    forkFlow.insert(dbserver.getConnection(), date, false, "", forklift);
                    if(forkFlow.getID() > 0)
                    {
                        if(forkFlow.downItem(dbserver))
                        {
                            forkFlow.getItem().downNumber(dbserver.getConnection());
                            forkFlow.getItem().downMake(dbserver.getConnection());
                            forkFlow.getItem().downModel(dbserver);
                            forkFlow.getItem().downSerie(dbserver);
                        }
                        titems.add(forkFlow);
                    }
                }
            }
            else if(type == Titem.Type.BATTERY)
            {
                if(battery.getID() > 0)
                {
                    Flow battFlow = new Flow(battery.getID());
                    String strSerie = battFlow.getSerie() == null? "" : battFlow.getSerie();
                    battFlow.insert(dbserver.getConnection(), date, false, "", battery);
                    if(battFlow.getID() > 0) 
                    {
                        if(battFlow.downItem(dbserver))
                        {
                            battFlow.getItem().downNumber(dbserver.getConnection());
                            battFlow.getItem().downMake(dbserver.getConnection());
                            battFlow.getItem().downModel(dbserver);
                            battFlow.getItem().downSerie(dbserver);
                        }
                        titems.add(battFlow);
                    }
                }
            }
            else if(type == Titem.Type.CHARGER)
            {
                //System.out.println("Charger '" + row.get(5) + "'");
                if(charger.getID() > 0)
                {
                    Flow charFlow = new Flow(charger.getID());
                    String strSerie = charFlow.getSerie() == null? "" : charFlow.getSerie();
                    charFlow.insert(dbserver.getConnection(), date, false, "", charger);
                    if(charFlow.getID() > 0)
                    {
                        if(charFlow.downItem(dbserver))
                        {
                            charFlow.getItem().downNumber(dbserver.getConnection());
                            charFlow.getItem().downMake(dbserver.getConnection());
                            charFlow.getItem().downModel(dbserver);
                            charFlow.getItem().downSerie(dbserver);
                        }
                        titems.add(charFlow);
                    }
                }
            }
            else
            {
                continue;
            }
            
            
            ArrayList<Enterprise> enterprisies = search(row.get(2));
            if(enterprisies.size() == 1)
            {
                Movements mov = new Movements(-1);
                Office office = new Office(dbserver,1);
                String strFolio = row.get(0).isEmpty() || row.get(0).isBlank() ?  "0" : row.get(1);
                ret = mov.insert(dbserver, office, date, strFolio, titems);
                if(!ret.isFlag())
                {
                    System.out.println("Fallo de movimiento en : " + idrow);
                    continue;
                }
                ret = mov.upCompany(dbserver.getConnection(), enterprisies.get(0));
                if(!ret.isFlag())
                {
                    System.out.println("Fallo de movimiento : " + idrow);
                    continue;
                }
                String strUso = getStringUso(row.get(3));
                Uso uso = new Uso(-1);
                if(uso.selectCode(dbserver,strUso))
                {
                    uso.download(dbserver.getConnection());
                    mov.upUso(dbserver.getConnection(), uso);
                }
                else
                {
                    System.out.println("Fallo de movimiento : " + idrow );
                    continue;
                }
                if(row.size() >= 12)
                { 
                    String strNote = row.get(11);
                    if(strNote.isBlank() && strNote.isEmpty())
                    {
                        mov.upNote(dbserver.getConnection(), strNote);
                    }
                }
                counMovs++;
            }
        }
        
        String message = "Total Procesados : " + result.size() + "/" + counMovs + "\n";
        message += "Fallos : " + counFails;
        JOptionPane.showMessageDialog(null,message);
        
        System.out.println("Fallos de cliente");
        for(String str : setNotFound)
        {
            System.out.println(str);
        }
        System.out.println("");
        System.out.println("Fallos de Movimientos");
        for(String str : setNotMoviment)
        {
            System.out.println(str);
        }
        System.out.println("Fallos de Movimientos detectados : " + counFails);
        
        return true;
    }
    
    public String getStringUso(String uso)
    {
        if(uso.compareTo("DISPONIBLE") == 0 || uso.compareTo("Disponible") == 0 )
        {
            return "disp";
        }
                
        if(uso.compareTo("CUBRIR RENTA") == 0 || uso.compareTo("RENTA NUEVA") == 0 || uso.compareTo("Renta Nueva") == 0  || uso.compareTo("Renta Nueva") == 0)
        {
            return "rtacp";
        }
                             
        if(uso.compareTo("REPARCION") == 0 || uso.compareTo("reparacion") == 0   || uso.compareTo("VIENE A REPARACION") == 0 || uso.compareTo("Reparacion de equipo") == 0 || uso.compareTo("Evaluacion de Bateria") == 0 || uso.compareTo("Reparacion de equipo") == 0)
        {
            return "rep";
        }
                         
        /*
        if(uso.compareTo("PRESTAMO") == 0 || uso.compareTo("Prestamo de equipo") == 0)
        {
            return "renta";
        }
                         
        if(uso.compareTo("TERMINOI DE RENTA") == 0 || uso.compareTo("Termino de renta") == 0)
        {
            return "renta";
        }                      
        if(uso.compareTo("ENTREGA DE CARHGADOR") == 0 || uso.compareTo("Entrega de Cargador") == 0)
        {
            return "otras";
        }
        */
        
        return "otras";
    }
    public ArrayList<Enterprise> search(String search)
    {
        ArrayList<Enterprise> enterprisies = Enterprise.listing(search,3,dbserver,"bc.tj");
        if(enterprisies.size() == 1)
        {
            return enterprisies;
        }
            
            enterprisies = Enterprise.patron( search,3,dbserver,"bc.tj");
            if(enterprisies.size() == 1)
            {
                return enterprisies;
            }
            
            String[] words = search.split(" ");
            enterprisies = Enterprise.listing(words[0],3,dbserver,"bc.tj");
            if(enterprisies.size() == 1)
            {
                return enterprisies;
            }
            
            if(words.length > 1)
            {
                enterprisies = Enterprise.listing(words[1],3,dbserver,"bc.tj");
                if(enterprisies.size() == 1)
                {
                    return enterprisies;
                }
            }
            
            if(words.length > 1)
            {
                String strName2 = words[0] + " " + words[1];
                enterprisies = Enterprise.listing(strName2,3,dbserver,"bc.tj");
                if(enterprisies.size() == 1)
                {
                    return enterprisies;
                }
            }           
            
            if(words.length > 2)
            {                
                String strName2 = words[0] + " " + words[1] + " " + words[2];
                enterprisies = Enterprise.listing(strName2,3,dbserver,"bc.tj");
                if(enterprisies.size() == 1)
                {
                    return enterprisies;
                }
            }
            
            return enterprisies;
    }
    
    
    public boolean loadResumov(String fn) throws FileNotFoundException, IOException, SQLException
    {
        BufferedReader br = new BufferedReader(new FileReader(fn));
        
        List<List<String>> result = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) 
        {
            String[] values = line.split(COMMA_DELIMITER);
            result.add(Arrays.asList(values));
        }
        
        Office office = new Office(dbserver,1);
        int countTotal = 0;
        for(List<String> row : result)
        {
            Resumov resumov = new Resumov(-1);
            Battery battery = null;
            Charger charger = null;
            Forklift forklift = null;
            //System.out.println("row.size() = " + row.size());
            
            Return ret;
            
            
            if(row.size() < 5) continue;
            String strTitem = row.get(5).trim();
            if(strTitem.isEmpty() || strTitem.isBlank()) continue;
            Titem.Type type = Titem.checkType(strTitem.trim());
            if(type == Titem.Type.UNKNOW) continue;
            
            if(type == Titem.Type.FORKLIFT)
            {
                if(!row.get(5).trim().isEmpty() && !row.get(5).trim().isBlank())
                {
                    forklift = new Forklift(-1);
                    ret = forklift.searchForklift(dbserver, row.get(5).trim());
                    if(ret.isFlag())
                    {
                        forklift.downNumber(dbserver.getConnection());
                        forklift.downSerie(dbserver);
                        forklift.downMake(dbserver.getConnection());
                        forklift.downModel(dbserver);
                    }
                    else
                    {
                        System.out.println("no esta registrado el elemento '" + row.get(5)+ "'");
                        continue;
                    }
                }
            }
            else if(type == Titem.Type.BATTERY)
            {
                if(row.size() >= 5)
                {
                    String strBattery = row.get(5).trim();
                    if(!strBattery.isEmpty() && !strBattery.isBlank())
                    {
                        battery = new Battery(-1);
                        ret = battery.search(dbserver.getConnection(), strBattery);
                        if(ret.isFlag())
                        {
                            battery.downNumber(dbserver.getConnection());
                            battery.downSerie(dbserver);
                            battery.downMake(dbserver.getConnection());
                            battery.downModel(dbserver);
                        }
                        else
                        {
                            System.out.println("no esta registrado el elemento '" + row.get(5)+ "'");
                            continue;
                        }
                    }
                }
            }
            else if(type == Titem.Type.CHARGER)
            {
                if(row.size() >= 5)
                {
                    if(!strTitem.isEmpty() && !strTitem.isBlank())
                    {
                        charger = new Charger(-1);
                        ret = charger.search(dbserver.getConnection(), strTitem.trim());
                        if(ret.isFlag())
                        {
                            charger.downNumber(dbserver.getConnection());
                            charger.downSerie(dbserver);
                            charger.downMake(dbserver.getConnection());
                            charger.downModel(dbserver);
                        }
                        else
                        {
                            System.out.println("no esta registrado el elemento '" + row.get(5)+ "'");
                            continue;
                        }
                    }
                }
            }
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            String strDate = row.get(0).trim();
            Date date = null;
            if(strDate.isEmpty() || strDate.isBlank()) 
            {
                try 
                {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(row.get(0).trim());
                } 
                catch (ParseException ex) 
                {
                    Logger.getLogger(LoteGrua.class.getName()).log(Level.SEVERE, null, ex);
                    date = new Date();
                }
            }
            else
            {
                date = new Date();
            }
            
            Flow forkFlow = null;
            Flow battFlow = null;
            Flow charFlow = null;
            if(type == Titem.Type.FORKLIFT)
            {
                forkFlow = new Flow(-1);
                    if(forkFlow.selectTitemNumber(dbserver,row.get(5).trim()))
                    {
                        if(forkFlow.getID() > 0)
                        {
                            if(forkFlow.downItem(dbserver))
                            {
                                forkFlow.getItem().downNumber(dbserver.getConnection());
                                forkFlow.getItem().downMake(dbserver.getConnection());
                                forkFlow.getItem().downModel(dbserver);
                                forkFlow.getItem().downSerie(dbserver);
                                //titems.add(forkFlow);
                                if(resumov.find(dbserver,forkFlow)) 
                                {
                                    continue;
                                }//si ya existe no agregar
                            }
                        }
                    }
                    else
                    {
                        throw new RuntimeException("No se encontro el articulo '" + row.get(5) + "'.");
                    }
            }
            else if(type == Titem.Type.BATTERY)
            {
                    battFlow = new Flow(-1);
                    if(battFlow.selectTitemNumber(dbserver,row.get(5).trim()))
                    {
                        if(battFlow.getID() > 0)
                        {
                            if(battFlow.downItem(dbserver))
                            {
                                battFlow.getItem().downNumber(dbserver.getConnection());
                                battFlow.getItem().downMake(dbserver.getConnection());
                                battFlow.getItem().downModel(dbserver);
                                battFlow.getItem().downSerie(dbserver);
                            }
                        }
                        if(resumov.find(dbserver,battFlow)) continue; //si ya existe no agregar
                    }
                    else
                    {
                        throw new RuntimeException("No se encontro el articulo '" + row.get(5) + "'.");
                    }
            }
            else if(type == Titem.Type.CHARGER)
            {
                    charFlow = new Flow(-1);
                    if(charFlow.selectTitemNumber(dbserver,row.get(5).trim()))
                    {
                        if(charFlow.getID() > 0)
                        {
                            if(charFlow.downItem(dbserver))
                            {
                                charFlow.getItem().downNumber(dbserver.getConnection());
                                charFlow.getItem().downMake(dbserver.getConnection());
                                charFlow.getItem().downModel(dbserver);
                                charFlow.getItem().downSerie(dbserver);
                            }
                        }
                        if(resumov.find(dbserver,charFlow)) continue; //si ya existe no agregar
                    }
                    else
                    {
                        throw new RuntimeException("No se encontro el articulo '" + row.get(5) + "'.");
                    }
            }
            
            String strUso = getStringUso(row.get(3));
            Uso uso = new Uso(-1);
            if(uso.selectCode(dbserver,strUso))
            {
                uso.download(dbserver.getConnection());
            }
            Enterprise enterprise = null;
            ArrayList<Enterprise> enterprisies = search(row.get(2).trim());
            if(enterprisies.size() == 1)
            {
                enterprise = enterprisies.get(0);
            }            
            else
            {
                continue;
            }
            
            if(resumov.insert(dbserver, office, uso, enterprise, forkFlow, date, null)) countTotal++;
        }
        String message = "Hoja de renta : " + countTotal;
        JOptionPane.showMessageDialog(null,message);
        
        return true;        
    }
}
