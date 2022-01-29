/*
 */
package SIIL.imports;

import SIIL.Server.Database;
import database.mysql.stock.Titem;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.servApp;
import SIIL.services.grua.Forklift;
import SIIL.services.grua.Battery;
import SIIL.services.grua.Charger;
import SIIL.services.grua.Movements;
import SIIL.services.grua.Resumov;
import SIIL.services.grua.Tipo;
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
import java.util.Locale;
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
        
        Return ret;
        Titem titem;
        Forklift forklift;
        Battery battery;
        Charger charger;
        for(List<String> row : result)
        {
            ret = new Return(true,"Init..");
            titem = null;
            if(Titem.isExist(dbserver, row.get(0)) <= 0)
            {
                switch(Titem.checkType(row.get(0)))
                {
                case FORKLIFT:
                        forklift = new Forklift(-1);
                        ret = forklift.insert(dbserver.getConnection(),row.get(0),"#",Import.NoImport,false,"#");
                        titem = forklift;
                    break;
                case BATTERY:
                        battery = new Battery(-1);
                        ret = battery.insert(dbserver.getConnection(),row.get(0),"#",Import.NoImport,false,"#");
                        titem = battery;
                    break;
                case CHARGER:
                        charger = new Charger(-1);
                        ret = charger.insert(dbserver.getConnection(),row.get(0),"#",Import.NoImport,false,"#");
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

                ret = titem.upMake(dbserver.getConnection(), row.get(5));
                if(ret.isFail()) 
                {
                    System.out.println(ret.getMessage());
                    return false;
                }

                ret = titem.upModel(dbserver.getConnection(), row.get(6));
                if(ret.isFail()) 
                {
                    System.out.println(ret.getMessage());
                    return false;
                }
                
                ret = titem.upSerie(dbserver.getConnection(), row.get(7));
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
        int counFailsResults = 0;
        for(int i = 0; i < result.size() ; i++)
        {
            if(result.get(i).size() < 6)
            {
                System.out.println("Tamaño menor a 3 : " + (i + 1));
                counFails++;
                continue;
            }
            else if(result.get(i).size() < 3)
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
            String strTitem;
            Titem.Type type;
            Movements mov;
            ArrayList<Enterprise> enterprisies;
            if(result.get(i).size() >= 3)
            {
                strClient = result.get(i).get(2).trim();
                if(!strClient.isEmpty() && !strClient.isBlank())
                {
                    enterprisies = search(result.get(i).get(2).trim());
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
                strTitem = result.get(i).get(5).trim();
                if(strTitem.isEmpty() || strTitem.isBlank()) 
                {
                    System.out.println("No hay datos en el campo de equipo : " + (i + 1));
                    counFails++;
                }
                else if(strTitem.compareTo("N/A") == 0) 
                {
                    System.out.println("No hay datos en el campo de equipo : " + (i + 1));
                    counFails++;
                }
                else
                {
                    type = Titem.checkType(strTitem);            
                    if(type == Titem.Type.UNKNOW)
                    {
                        System.out.println("El equipamiento es deconocido (" + strTitem + ") : " + (i + 1));
                        counFails++;
                    }
                }
            }
        }
        Collections.reverse(result);
        
        int counMovs = 0;
        ArrayList<String> NotFound = new ArrayList<>();
        Set<String> setNotFound = new HashSet(NotFound);
        ArrayList<String> NotMvoment = new ArrayList<>();
        Set<String> setNotMoviment = new HashSet(NotMvoment);        
        
        int idrow = 1;
        Battery battery;
        Charger charger;
        Forklift forklift;
        Return ret;
        String strTitem;
        Titem.Type type;
        String strBattery;
        Date date;
        List<Flow> titems;
        Flow forkFlow;
        Flow battFlow;
        Flow charFlow;
        String strSerie;
        String strTitemBaterry;
        String strTitemCharger;
        ArrayList<Enterprise> enterprisies;
        Movements mov;
        Office office;
        String strFolio;
        String strUso;
        Uso uso;
        String strNote;
        Tipo movT;
        for(List<String> row : result)
        {
            idrow++;
            battery = null;
            charger = null;
            forklift = null;
            //System.out.println("row.size() = " + row.size());
                        
            if(row.size() < 6)
            {
                //System.out.println(row);
                setNotMoviment.add(row.toString());
                counFailsResults++;
                continue;
            }
            
            if(row.size() < 5) 
            {
                counFailsResults++;
                continue;                
            }
            strTitem = row.get(5).trim();
            if(strTitem.isEmpty() || strTitem.isBlank()) 
            {
                counFailsResults++;
                continue;                
            }
            if(strTitem.compareTo("N/A") == 0) 
            {
                counFailsResults++;
                continue;                
            }
            type = Titem.checkType(strTitem);            
            if(type == Titem.Type.UNKNOW) 
            {
                counFailsResults++;
                continue;                
            }
                
            
            if(type == Titem.Type.FORKLIFT)
            {
                forklift = new Forklift(-1);
                //System.out.println("Searching " + row.get(5).trim() + " ...");
                forklift.searchForklift(dbserver, strTitem);
                if(forklift.getID() > 0)
                {
                    forklift.downNumber(dbserver.getConnection());
                    forklift.downSerie(dbserver);
                    forklift.downMake(dbserver.getConnection());
                    forklift.downModel(dbserver);
                }
                else
                {
                    /*
                    forklift.insert(dbserver.getConnection(), row.get(5).trim(), "Importacion Enero/22", Forklift.Import.NoImport, false, "");    
                    if(row.get(6).isEmpty() || row.get(6).isBlank()) forklift.upMake(dbserver.getConnection(), row.get(6).trim());
                    if(row.get(7).isEmpty() || row.get(7).isBlank()) forklift.upModel(dbserver.getConnection(), row.get(7).trim());
                    if(row.get(8).isEmpty() || row.get(8).isBlank()) forklift.upSerie(dbserver.getConnection(), row.get(8).trim());
                    forklift.downNumber(dbserver.getConnection());
                    forklift.downSerie(dbserver);
                    forklift.downMake(dbserver.getConnection());
                    forklift.downModel(dbserver);
                    */
                    System.out.println("No se encontro : " + strTitem);
                }
            }
            else if(type == Titem.Type.BATTERY)
            {
                if(row.size() >= 5)
                {
                    strBattery = row.get(5).trim();
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
                            /*
                            battery.insert(dbserver.getConnection(), row.get(5).trim(), "Importacion Enero/22", Forklift.Import.NoImport, false, "");
                            if(!row.get(6).isEmpty() || !row.get(6).isBlank()) battery.upMake(dbserver.getConnection(), row.get(6).trim());
                            if(!row.get(7).isEmpty() || !row.get(7).isBlank()) battery.upModel(dbserver.getConnection(), row.get(7).trim());
                            if(!row.get(8).isEmpty() || !row.get(8).isBlank()) battery.upSerie(dbserver.getConnection(), row.get(8).trim());
                            battery.downNumber(dbserver.getConnection());
                            battery.downSerie(dbserver);
                            battery.downMake(dbserver.getConnection());
                            battery.downModel(dbserver);  
                            */
                            System.out.println("No se encontro : " + strTitem);
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
                            /*
                            charger.insert(dbserver.getConnection(), row.get(5).trim(), "Importacion Enero/22", Forklift.Import.NoImport, false, "");
                            if(row.get(6).isEmpty() || row.get(6).isBlank()) charger.upMake(dbserver.getConnection(), row.get(6).trim());
                            if(row.get(7).isEmpty() || row.get(7).isBlank()) charger.upModel(dbserver.getConnection(), row.get(7).trim());
                            if(row.get(8).isEmpty() || row.get(8).isBlank()) charger.upSerie(dbserver.getConnection(), row.get(8).trim());
                            charger.downNumber(dbserver.getConnection());
                            charger.downSerie(dbserver);
                            charger.downMake(dbserver.getConnection());
                            charger.downModel(dbserver);
                            */
                            System.out.println("No se encontro : " + strTitem);
                        }
                    }
                }
            }
            
            
            date = convertDate(row.get(0).trim());
              
            titems = new ArrayList<>();
            
            if(type == Titem.Type.FORKLIFT)
            {
                if(forklift.getID() > 0)
                {
                    forkFlow = new Flow(-1);
                    strSerie = forkFlow.getSerie() == null? "" : forkFlow.getSerie();
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
                    battFlow = new Flow(battery.getID());
                    strSerie = battFlow.getSerie() == null? "" : battFlow.getSerie();
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
                    charFlow = new Flow(charger.getID());
                    strSerie = charFlow.getSerie() == null? "" : charFlow.getSerie();
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
                counFailsResults++;
                continue;
            }
                        
            if(type == Titem.Type.FORKLIFT)
            {
                if(row.size() >= 9)
                {
                    strTitemBaterry = row.get(9).trim();
                    type = Titem.checkType(strTitemBaterry);
                    if(!strTitemBaterry.isEmpty() && !strTitemBaterry.isBlank() && type != Titem.Type.UNKNOW)
                    {
                        if(type == Titem.Type.BATTERY)
                        {
                            battery = new Battery(-1);
                            ret = battery.search(dbserver.getConnection(), strTitem.trim());
                            if(battery.getID() > 0)
                            {
                                battFlow = new Flow(battery.getID());
                                strSerie = battFlow.getSerie() == null? "" : battFlow.getSerie();
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
                    }
                }
                
                if(row.size() >= 10)
                {
                    strTitemCharger = row.get(10).trim();
                    type = Titem.checkType(strTitemCharger);
                    if(!strTitemCharger.isEmpty() && !strTitemCharger.isBlank() && type != Titem.Type.UNKNOW)
                    {
                        if(type == Titem.Type.CHARGER)
                        {
                            charger = new Charger(-1);
                            charger.search(dbserver.getConnection(), strTitem.trim());
                            if(charger.getID() > 0)
                            {
                                charFlow = new Flow(charger.getID());
                                strSerie = charFlow.getSerie() == null? "" : charFlow.getSerie();
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
                    }
                }
            }
            
            enterprisies = search(row.get(2));
            if(enterprisies.size() == 1)
            {
                mov = new Movements(-1);
                office = new Office(dbserver,1);
                strFolio = row.get(0).isEmpty() || row.get(0).isBlank() ?  "0" : row.get(1);
                ret = mov.insert(dbserver, office, date, strFolio, titems);
                mov.upImported(dbserver, true);
                /*if(!ret.isFlag())
                {
                    System.out.println("Fallo de movimiento 1 en : " + idrow);
                    counFailsResults++;
                    continue;
                }*/
                ret = mov.upCompany(dbserver.getConnection(), enterprisies.get(0));
                /*if(!ret.isFlag())
                {
                    System.out.println("Fallo de movimiento 2 : " + idrow);
                    counFailsResults++;
                    continue;
                }*/
                strUso = getStringUso(row.get(15));
                if(!strUso.isEmpty() && !strUso.isBlank() )
                {
                    uso = new Uso(-1);
                    if(uso.selectCode(dbserver,strUso))
                    {
                        uso.download(dbserver.getConnection());
                        mov.upUso(dbserver.getConnection(), uso);
                    }
                    else
                    {
                        System.out.println("Fallo de movimiento 3 : " + idrow );
                        counFailsResults++;
                        continue;
                    }
                }
                if(row.size() >= 14)
                {
                    strNote = row.get(13);
                    if(!strNote.isBlank() && !strNote.isEmpty())
                    {
                        mov.upNote(dbserver.getConnection(), strNote);
                    }
                    else if(row.size() >= 17)
                    {
                        strNote = row.get(16);
                        if(!strNote.isBlank() && !strNote.isEmpty())
                        {
                            mov.upNote(dbserver.getConnection(), strNote);
                        }
                    }
                }
                
                movT = new Tipo(convertTipo(row.get(4)));
                if(movT.getID() > 0)
                {
                    movT.download(dbserver.getConnection());
                    mov.upTMov(dbserver.getConnection(), movT);
                }
                else
                {
                    System.out.println("Tipo desconocido : " + row.get(4));
                }
                
                counMovs++;
            }
        }
        
        String message = "Total Procesados : " + result.size() + "/" + counMovs + "\n";
        message += "Fallos : " + counFails + " / " + counFailsResults;
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
    
    int convertTipo(String tipo)
    {
        if(tipo.compareTo("CAMBIO") == 0 || tipo.compareTo("Cambio") == 0  || tipo.compareTo("cambio") == 0 )
        {
            return 3;
        }
        else if(tipo.compareTo("CANCELADA") == 0 || tipo.compareTo("Cancelad") == 0  || tipo.compareTo("cancelada") == 0 )
        {
            return 5;
        }
        else if(tipo.compareTo("ENTRADA") == 0 || tipo.compareTo("Entrada") == 0  || tipo.compareTo("entrada") == 0 )
        {
            return 1;
        }
        else if(tipo.compareTo("MOVIMIENTO") == 0 || tipo.compareTo("Movimiento") == 0  || tipo.compareTo("movimiento") == 0 )
        {
            return 3;
        }
        else if(tipo.compareTo("SALIDA") == 0 || tipo.compareTo("Salida") == 0  || tipo.compareTo("salida") == 0 )
        {
            return 2;
        }
        else
        {
            return -1;
        }
    }    
    
    Date convertDate(String strDate)
    {
        if(strDate.isEmpty() || strDate.isBlank()) return new Date();
        
        Date date;
        
        Locale englishLocale = new Locale("en", "US");
        
        try 
        {
            //System.out.println(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",englishLocale);
            date = sdf.parse(strDate);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(date));
            //System.out.println(sdf.format(date));
            return date;
        } 
        catch (ParseException ex) 
        {
                    
        }
                
        try 
        {
            //System.out.println(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy",englishLocale);
            date = sdf.parse(strDate);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(date));
            //System.out.println(sdf.format(date));
            return date;
        } 
        catch (ParseException ex) 
        {
                    
        }
        
        
        /*try 
        {
            //System.out.println(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy",englishLocale);
            date = sdf.parse(strDate);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(date));
            //System.out.println(sdf.format(date));
            return date;
        } 
        catch (ParseException ex) 
        {
                    
        }*/
        
        try 
        {
            //System.out.println(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("F-MMM-yy",englishLocale);
            date = sdf.parse(strDate);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(date));
            //System.out.println(sdf.format(date));
            return date;
        } 
        catch (ParseException ex) 
        {
                    
        }
        
        Locale spanishLocale = new Locale("es", "ES");
        
        
        try 
        {
            //System.out.println(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",spanishLocale);
            date = sdf.parse(strDate);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(date));
            //System.out.println(sdf.format(date));
            return date;
        } 
        catch (ParseException ex) 
        {
                    
        }
                
        try 
        {
            //System.out.println(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy",spanishLocale);
            date = sdf.parse(strDate);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(date));
            //System.out.println(sdf.format(date));
            return date;
        } 
        catch (ParseException ex) 
        {
                    
        }
        
                
        /*try 
        {
            //System.out.println(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy",spanishLocale);
            date = sdf.parse(strDate);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(date));
            //System.out.println(sdf.format(date));
            return date;
        } 
        catch (ParseException ex) 
        {
                    
        }*/
        
        try 
        {
            //System.out.println(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("F-MMM-yy",spanishLocale);
            date = sdf.parse(strDate);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(date));
            //System.out.println(sdf.format(date));
            return date;
        } 
        catch (ParseException ex) 
        {
                    
        }
        
        System.out.println(strDate);
        return new Date();
    }    
    
    public String getStringUso(String uso)
    {
        if(uso.compareTo("DISPONIBLE") == 0 || uso.compareTo("Disponible") == 0 )
        {
            return "disp";
        }
                
        if(uso.compareTo("RENTA CORTO PLAZO") == 0 || uso.compareTo("Renta Corto Plazo") == 0 || uso.compareTo("renta corto plazo") == 0  || uso.compareTo("Renta") == 0)
        {
            return "rtacp";
        }
                             
        if(uso.compareTo("REPARCION") == 0 || uso.compareTo("reparacion") == 0   || uso.compareTo("Repracion") == 0 )
        {
            return "rep";
        }
        
        if(uso.compareTo("PRESTAMO") == 0 || uso.compareTo("Prestamo") == 0   || uso.compareTo("prestamo") == 0 )
        {
            return "pres";
        }
        
        if(uso.compareTo("RENTA OPCION COMPRA") == 0 || uso.compareTo("Renta Opcion Compra") == 0   || uso.compareTo("renta opcion compra") == 0 )
        {
            return "rtaoc";
        }        
        
        if(uso.compareTo("VENTA") == 0 || uso.compareTo("Venta") == 0   || uso.compareTo("Venta") == 0 )
        {
            return "vta";
        } 
        
        if(uso.compareTo("MOVIMIENTO") == 0 || uso.compareTo("Movimiento") == 0   || uso.compareTo("movimiento") == 0 )
        {
            return "mov";
        } 
        
        if(uso.compareTo("BAJA") == 0 || uso.compareTo("Baja") == 0   || uso.compareTo("baja") == 0 )
        {
            return "baja";
        }
        
        if(uso.compareTo("TALLER PINTURA") == 0 || uso.compareTo("Taller Pintura") == 0   || uso.compareTo("taller pintura") == 0 )
        {
            return "tpint";
        }
        
        if(uso.compareTo("CUBRIENDO") == 0 || uso.compareTo("Cubriendo") == 0   || uso.compareTo("cubriendo") == 0 )
        {
            return "corr";
        }
        
        if(uso.compareTo("OTRAS") == 0 || uso.compareTo("Otras") == 0   || uso.compareTo("otras") == 0 )
        {
            return "otras";
        }
        
        if(uso.compareTo("AJUSTE") == 0 || uso.compareTo("Ajuste") == 0   || uso.compareTo("ajuste") == 0 )
        {
            return "aj";
        }
                
        if(uso.compareTo("CANCELADA") == 0 || uso.compareTo("Cancela") == 0   || uso.compareTo("cacelada") == 0 )
        {
            return "";
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
        if(uso.compareTo("CUBRIR RENTA") == 0 || uso.compareTo("RENTA NUEVA") == 0 || uso.compareTo("Renta Nueva") == 0  || uso.compareTo("Renta Nueva") == 0)
        {
            return "rtacp";
        }
        */
        
        return "";
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
        Collections.reverse(result);
        
        Office office = new Office(dbserver,1);
        int countTotal = 0;
        Resumov resumov;
        String strTitem;
        Titem.Type type;
        Battery battery,addBattery;
        Charger charger,addCahrger;
        Forklift forklift;
        Titem titem;
        Return ret;
        Flow forkFlow;
        Flow battFlow;
        Flow charFlow;
        String strUso;
        Uso uso;
        Enterprise enterprise;
        ArrayList<Enterprise> enterprisies;
        Date date;
        String strNote;
        String strForklift;
        String strBattery;
        String strCharger;
        String strSerie;
        String strTitemCharger;
        for(List<String> row : result)
        {
            battery = null;
            charger = null;
            forklift = null;
            addBattery = null;
            addCahrger = null;
            titem = null;
            //System.out.println("row.size() = " + row.size());
            
            if(row.size() < 5) continue;
            strTitem = row.get(5).trim();
            if(strTitem.isEmpty() || strTitem.isBlank()) continue;
            type = Titem.checkType(strTitem.trim());
            if(type == Titem.Type.UNKNOW) continue;
            
            if(type == Titem.Type.FORKLIFT)
            {
                if(!strTitem.isEmpty() && !strTitem.isBlank())
                {
                    forklift = new Forklift(-1);
                    ret = forklift.searchForklift(dbserver, strTitem);
                    if(forklift.getID() > 0)
                    {
                        forklift.downNumber(dbserver.getConnection());
                        forklift.downSerie(dbserver);
                        forklift.downMake(dbserver.getConnection());
                        forklift.downModel(dbserver);
                    }
                    else
                    {
                        System.out.println("no esta registrado el elemento '" + strTitem + "'");
                        titem = null;
                        continue;
                    }
                    titem = forklift;
                }
                else
                {
                    titem = null;
                }
            }
            else if(type == Titem.Type.BATTERY)
            {
                if(row.size() >= 5)
                {
                    if(!strTitem.isEmpty() && !strTitem.isBlank())
                    {
                        battery = new Battery(-1);
                        ret = battery.search(dbserver.getConnection(), strTitem);
                        if(battery.getID() > 0)
                        {
                            battery.downNumber(dbserver.getConnection());
                            battery.downSerie(dbserver);
                            battery.downMake(dbserver.getConnection());
                            battery.downModel(dbserver);
                        }
                        else
                        {
                            System.out.println("no esta registrado el elemento '" + strTitem + "'");
                            titem = null;
                            continue;
                        }
                        titem = battery;
                    }
                    else
                    {
                        titem = null;
                    }
                }
                else
                {
                    titem = null;
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
                        if(charger.getID() > 0)
                        {
                            charger.downNumber(dbserver.getConnection());
                            charger.downSerie(dbserver);
                            charger.downMake(dbserver.getConnection());
                            charger.downModel(dbserver);
                        }
                        else
                        {
                            System.out.println("no esta registrado el elemento '" + strTitem + "'");
                            titem = null;
                            continue;
                        }
                        titem = charger;
                    }
                    else
                    {
                        titem = null;
                    }
                }
                else
                {
                    titem = null;
                }
            }
            else
            {
                continue;
            }
            
            System.out.println("Prev 1 " + strTitem);
            if(titem == null) continue;
            
            System.out.println("Prev 2 " + titem.getNumber());
            resumov = new Resumov(-1);
            if(resumov.find(dbserver,titem)) continue;
            
            System.out.println("Running " + titem.getNumber());            
            date = convertDate(row.get(0).trim());
            forkFlow = null;
            battFlow = null;
            charFlow = null;
            
            //System.out.print("Titem '" + row.get(5).trim() + "' : ");
            if(type == Titem.Type.FORKLIFT)
            {
                if(row.size() >= 5)
                {
                    type = Titem.checkType(strTitem);
                    if(!strTitem.isEmpty() && !strTitem.isBlank() && type != Titem.Type.UNKNOW)
                    {
                        if(type == Titem.Type.FORKLIFT)
                        {
                            if(forklift.getID() > 0)
                            {
                                forkFlow = new Flow(-1);
                                //strSerie = forkFlow.getSerie() == null? "" : forkFlow.getSerie();
                                forkFlow.selectItemNumber(dbserver,strTitem);
                                if(forkFlow.getID() > 0) 
                                {
                                    if(forkFlow.downItem(dbserver))
                                    {
                                        forkFlow.getItem().downNumber(dbserver.getConnection());
                                        forkFlow.getItem().downMake(dbserver.getConnection());
                                        forkFlow.getItem().downModel(dbserver);
                                        forkFlow.getItem().downSerie(dbserver);
                                    }
                                    else
                                    {
                                        System.out.println("Fallo la descarga del item " + strTitem);
                                        continue;
                                    }
                                }
                                else
                                {
                                    System.out.println("No se encontro en la base de datos " + strTitem);
                                    continue;
                                }
                            }
                            else
                            {
                                System.out.println("No es un montacargas " + strTitem);
                                continue;
                            }
                        }
                        else
                        {
                            System.out.println("No es un montacargas " + strTitem);
                            continue;
                        }
                    }
                    else
                    {
                        System.out.println("Campo vacio " + strTitem);
                        continue;
                    }
                }
                else
                {
                    System.out.println("Tamano insuficiente " + strTitem);
                    continue;
                }
                
                if(row.size() >= 9)
                {
                    strBattery = row.get(9).trim();
                    type = Titem.checkType(strBattery);
                    if(!strBattery.isEmpty() && !strBattery.isBlank() && type == Titem.Type.BATTERY)
                    {
                        //System.out.print(", B : '" + strTitemBaterry + "'");
                            battery = new Battery(-1);
                            ret = battery.search(dbserver.getConnection(), strBattery);
                            if(battery.getID() > 0)
                            {
                                battFlow = new Flow(battery.getID());
                                //strSerie = battFlow.getSerie() == null? "" : battFlow.getSerie();
                                battFlow.selectItemNumber(dbserver,strBattery);
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
                                else
                                {
                                    battFlow = null;
                                }
                            }
                    }
                }
                
                if(row.size() >= 10)
                {
                    strTitemCharger = row.get(10).trim();
                    type = Titem.checkType(strTitemCharger);
                    if(!strTitemCharger.isEmpty() && !strTitemCharger.isBlank() && type != Titem.Type.UNKNOW)
                    {
                        //System.out.println(", C : '" + strTitemCharger + "'");
                        if(type == Titem.Type.CHARGER)
                        {
                            charger = new Charger(-1);
                            ret = charger.search(dbserver.getConnection(), strTitemCharger);
                            if(charger.getID() > 0)
                            {
                                charFlow = new Flow(-1);
                                //strSerie = charFlow.getSerie() == null? "" : charFlow.getSerie();
                                charFlow.selectItemNumber(dbserver,strTitemCharger);
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
                                else
                                {
                                    charFlow = null;
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                continue;
            }
            //System.out.println("");
            
            strUso = getStringUso(row.get(15));
            uso = new Uso(-1);
            //System.out.println("uso = '" + row.get(15) + "'");
            if(!strUso.isEmpty() && !strUso.isBlank())
            {
                if(uso.selectCode(dbserver,strUso))
                {                    
                    ret = uso.download(dbserver.getConnection());
                    if(ret.isFail())
                    {
                        throw new RuntimeException("Fallo el uso '" + row.get(15) + "'.");
                    }
                }
                else
                {
                    throw new RuntimeException("Fallo el uso '" + row.get(15) + "'.");
                }
            }
            else
            {
                //throw new RuntimeException("Fallo el uso '" + row.get(15) + "'.");
                continue;
            }
                
            enterprise = null;
            enterprisies = search(row.get(2).trim());
            if(enterprisies.size() == 1)
            {
                enterprise = enterprisies.get(0);
            }            
            else
            {
                continue;
            }
            
            if(resumov.insert(dbserver, office, uso, enterprise, forkFlow, date, null)) 
            {
                if(battFlow != null)
                {
                    //System.out.println("Agregando bateria.");
                    resumov.upBattery(dbserver.getConnection(), battFlow, null);
                } 
                if(charFlow != null)
                {
                    resumov.upCharger(dbserver.getConnection(), charFlow, null);
                }
                countTotal++;
            }
            resumov.upImported(dbserver, true);
            if(row.size() >= 14)
            {
                strNote = row.get(13);
                if(!strNote.isBlank() && !strNote.isEmpty())
                {
                    resumov.upNote(dbserver.getConnection(), strNote,null);
                }
                else if(row.size() >= 17)
                {
                    strNote = row.get(16);
                    if(!strNote.isBlank() && !strNote.isEmpty())
                    {
                        resumov.upNote(dbserver.getConnection(), strNote,null);
                    }
                }
            }           
        }
        
        try 
        {
            dbserver.commit();
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
        
        String message = "Hoja de renta : " + countTotal;
        JOptionPane.showMessageDialog(null,message);
        
        return true;        
    }
}
