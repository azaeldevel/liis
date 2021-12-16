
package SIIL.services.grua;

import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.core.Office;
import SIIL.trace.Trace;
import core.FailResultOperationException;
import core.PlainTitem;
import database.mysql.sales.Remision;
import database.mysql.stock.Titem;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import process.Return;
import stock.Flow;

/**
 *
 * @author Azael
 */
public class Captura<T>
{
    public enum Owner
    {
        NONE,
        SIIL,
        CLIENT
    }
    
    private Owner owner;
    private String folio;
    private Date fecha;
    private Tipo tipo;
    private Uso uso;
    private String sa;
    private Company company;
    private String firma;
    private Office office;
    private List<T> hequis;
    private String note;
    private boolean note_his;
    private boolean note_res;
    private Remision remision;
    
    
    /**
     * 
     * @param database
     * @param traceContext
     * @param operatorOffice
     * @return 
     * @throws java.sql.SQLException 
     */
    public Boolean create(Database database,Trace traceContext,Office operatorOffice) throws SQLException
    {
        
        if(tipo == null)
        {
            throw new InvalidParameterException("tipo is null");
        }
        else if(tipo.getID() < 1)
        {
            throw new InvalidParameterException("ID de tipo incorrecto " + tipo.getID());
        }
        if(tipo.getID() != 5)
        {
            if(hequis == null)
            {
                throw new InvalidParameterException("La lista de equipos es nula");
            }
            else if(hequis.isEmpty())
            {
                throw new InvalidParameterException("La lista de equipos esta vacia");
            }
        }
        if(office == null)
        {
            throw new InvalidParameterException("office es null.");
        }
        else if(office.getID() < 1)
        {
            throw new InvalidParameterException("El ID de officina es incorrecto.");
        }       
        if(uso == null && tipo.getID() != 4 && tipo.getID() != 5)
        {
            throw new InvalidParameterException("uso es null");
        }
        /*else if(uso.getID() < 1)
        {
            throw new InvalidParameterException("uso es tiene ID incorreecto " + uso.getID()); 
        }*/
        if(fecha == null)
        {
            throw new InvalidParameterException("fecha is null");
        }
        if(folio == null)
        {
            throw new InvalidParameterException("folio is null");
        }
        else if(!validFolio(folio))
        {
            throw new InvalidParameterException("El folio tiene formato incorrecto.");
        }
        if(owner == null)
        {
            throw new InvalidParameterException("owner is null");
        }
        else if(owner == Owner.NONE)
        {
            throw new InvalidParameterException("owner is None.");
        }    
        if(folio == null)
        {
            throw new InvalidParameterException("Deve asinarse un folio");
        }
        else if(folio.length() < 1)
        {
            throw new InvalidParameterException("Deve asinarse un folio");
        }   
        if(company == null && tipo.getID() != 4 && tipo.getID() != 5)
        {
            throw new InvalidParameterException("company es null.");
        }
        /*else if(company.getID() < 1)
        {
            throw new InvalidParameterException("company tiene formato incorrecto.");
        }*/
        
        Resumov resumov = new Resumov(-1);
        //Para la operaciones de sucursal solo se pueden afectar equipos de dicha sucursal
        if(operatorOffice.getType().equals("s") && owner == Owner.SIIL)
        {
            for(T hequi: hequis)
            {
                if(resumov.find(database,(Flow)hequi))
                {
                    Office sucHequi = resumov.getOffice(database);
                    Throwable th = sucHequi.download(database.getConnection());
                    if(th != null)
                    {
                        throw new FailResultOperationException(th.getMessage());
                    }
                    if(!operatorOffice.getCode().equals(sucHequi.getCode()))
                    {
                        throw new FailResultOperationException("El equipo '" + ((Flow)hequi).getItem().getNumber() + "' pertenece ha '" + sucHequi.getName() + "'");
                    }
                }
            }
        }
        
        Uso disponible = new Uso(1);//disponible
        Return retNU = disponible.download(database.getConnection());
        if(retNU.isFail()) throw new FailResultOperationException("Fallo descarga de uso para returno");
        Tipo newTipo = new Tipo(1); //entrada
        Return<Integer> retNT = newTipo.download(database.getConnection());   
        Company companyOld = null; 
        Uso usoOld = null;
        
        /*Company newComp = null;
        if(operatorOffice.getID() == 1)//tijuana
        {
            newComp = new Company(4606);
        }
        else if(operatorOffice.getID() == 2)//mexicali
        {
            newComp = new Company(4604);                            
        }
        else//default(tijuana)
        {
            newComp = new Company(4606);                                                        
        }
        if(!newComp.download(database)) throw new FailResultOperationException("Falló la descarga de la empresa");*/
        
        if(retNT.isFail())  throw new FailResultOperationException("Fallo descarga det ipo para returno");
        resumov = new Resumov(-1);            
        if(owner == Owner.SIIL && tipo.getID() != 5)
        {
            Return ret1 = null;
            for(Object hequi : hequis)
            {
                if(resumov.find(database,(Flow)hequi))
                {
                    companyOld = resumov.getCompany(database);//recordar la empresa en la hoja de renta antes de la operacion.
                    usoOld = resumov.getUso(database); 
                    if(!resumov.upOffice(database,office,traceContext)) throw new FailResultOperationException("Falló la escritura de la officina en Hoja de Renta.");
                    if(tipo.getID() == 4 || (tipo.getID() == 1 && uso.getID() == 5))//si es retorno o (Entrada - Reparacion)
                    {
                        if(!resumov.upUso(database,disponible,traceContext)) throw new FailResultOperationException("Falló la escrutura de Uso en hoja de renta");
                        if(!resumov.upCompany(database,company,traceContext)) throw new FailResultOperationException("Falló la escritua de la empresa en Hoja de renta.");                      
                    }
                    else
                    {
                        resumov.upUso(database,uso,traceContext);
                        resumov.upCompany(database,company,traceContext);
                    }
                    if(!resumov.upFecha(database,fecha,traceContext)) throw new FailResultOperationException("Falló al escribir la fecha en la Hoja de renta");                    
                    if(Forklift.isForklift(database,(Titem)((Flow)hequi).getItem()))
                    {//Si es Montacarga agregar Aditamento.
                        //resumov.removeAditamento(database,(Flow)hequi);//elimina refrencias previas
                        if(usoOld == null) throw new FailResultOperationException("Uso descopnocido en hoja de renta"); 
                        if(!resumov.extractAditamento(database, office, usoOld, companyOld, (Flow)hequi, fecha, traceContext)) throw new FailResultOperationException("Falló la extracion de aditamento"); 
                        if(!resumov.upAditamento(database,(Flow)hequi,true)) throw new FailResultOperationException("Falló al limpar aditamentos"); 
                    }
                    /*else if(Battery.isBattery(database,(Titem)((Flow)hequi).getItem()) | Charger.isCharger(database,(Titem)((Flow)hequi).getItem()))
                    {
                        
                    }*/
                    else //es aditamento
                    {
                        resumov.removeDuplicated(database,(Flow)hequi);
                    }
                    
                    if(note != null)
                    {
                        if(note.length() > 0)
                        {
                            if(note_res)
                            {
                                Return<Integer> retUp = resumov.upNote(database.getConnection(),note,traceContext);
                                if(retUp.isFail()) throw new FailResultOperationException("Falló escritura de Nota en hoja de renta");
                            }
                        }
                    }
                    else
                    {
                        if(note_res)
                        {
                            Return<Integer> retUp = resumov.upNote(database.getConnection(),"",traceContext);
                            if(retUp.isFail()) throw new FailResultOperationException("Falló escritura de Nota en hoja de renta");
                        }
                    }
                }
                else if(Battery.isBattery(database,(Titem)((Flow)hequi).getItem()) | Charger.isCharger(database,(Titem)((Flow)hequi).getItem()))
                {
                    Resumov refResumov = Resumov.findLinked(database, (Flow) hequi);
                    if(refResumov == null)
                    {
                        Resumov newResumov = new Resumov(-1);
                        if(!newResumov.insert(database, office, uso, company, ((Flow)hequi), fecha, traceContext))  throw new FailResultOperationException("Fallo la generacion del registro nuevo para el aditamento enlazado '" + ((Flow)hequi).getItem().getNumber() + "'");                        
                    }
                    else
                    {
                        if(!extractAditamento(database, hequi, traceContext, uso, company)) throw new FailResultOperationException("Falló extraccion de Aditamento");                        
                    }
                }
                else
                {//si no existe en hoja de renta
                    throw new FailResultOperationException("no se encontro el elemento '" + ((Flow)hequi).getItem().getNumber() + "' no existe en la hoja de renta");
                }
            }
        }
        
        //
        Movements movements = new Movements(-1);
        Return ret2 = null;
        if(tipo.getID() == 5)
        {
            ret2 = movements.cancel(database, office,fecha,folio);
            return ret2.isFlag();
        }
        else if(owner == Owner.SIIL)
        {
            ret2 = movements.insert(database, office,fecha,folio,(List<Flow>)hequis, new Flow(-1));
            if(ret2.getStatus() == Return.Status.FAIL)  throw new FailResultOperationException("Fallo la operacion de Agregar registro a historial de movimentos.");
        }
        else if(owner == Owner.CLIENT)
        {
            if(!movements.insert(database, office,fecha,folio,(List<PlainTitem>) hequis,new PlainTitem())) throw new FailResultOperationException("Fallo la operacion de Agregar registro a historial de movimentos.");
        }
        if(tipo.getID() == 4)
        {
            ret2 = movements.upUso(database.getConnection(),usoOld);//el mismo que en la hoja de renta
        }
        else
        {
            ret2 = movements.upUso(database.getConnection(),uso);
        }
        if(ret2.isFail()) throw new FailResultOperationException("Fallo la escitura de Uso");
        if(tipo.getID() == 4)
        {
            ret2 = movements.upTMov(database.getConnection(),newTipo);
        }
        else
        {
            ret2 = movements.upTMov(database.getConnection(),tipo);
        }
        if(ret2.isFail())  throw new FailResultOperationException("Fallo la escritura de Fecha");;
        Return<Integer> retUp = null;
        if(firma != null && firma.length() > 0)
        {
            retUp = movements.upFirma(database.getConnection(),firma);
            if(retUp.isFail()) throw new FailResultOperationException("Fallo escritura de Firma");
        }        
        if(remision != null && remision.getID() > 0)
        {
            retUp = movements.upSA(database.getConnection(),remision);
            if(retUp.isFail())  throw new FailResultOperationException("Fallo escritura de SA:" + retUp.getMessage());
        }
        retUp = movements.upOwner(database.getConnection(),Owner.SIIL);
        if(retUp.isFail()) throw new FailResultOperationException("Fallo escitura de Propietario");;        
        if(tipo.getID() == 4)
        {
            retUp = movements.upCompany(database.getConnection(),companyOld);
            if(retUp.isFail())  throw new FailResultOperationException("Fallo escritura de empresa");;
        }
        else
        {
            if(company != null && company.getID() > 0)
            {
                retUp = movements.upCompany(database.getConnection(),company);
                if(retUp.isFail())  throw new FailResultOperationException("Fallo escritura de empresa");
            }
        }
        
        if(note != null)
        {
            if(note.length() > 0)
            {
                if(note_his)
                {
                    retUp = movements.upNote(database.getConnection(),note);
                    if(retUp.isFail())  throw new FailResultOperationException("Falló escritura de Nota en Movimietos");
                }
            }
        }
        
        return true;        
    }

    /**
     * Si el Aditamento tiene algun enlaze se borra y se crea un nuevo registro
     * para el aditamento
     * @param database
     * @param hequi
     * @param traceContext
     * @param newUso
     * @param newComp
     * @throws SQLException 
     */
    private Boolean extractAditamento(Database database, Object hequi, Trace traceContext, Uso newUso, Company newComp) throws SQLException 
    {
        Resumov refResumov = Resumov.findLinked(database, (Flow) hequi);
        if(refResumov != null)
        {
            if(Battery.isBattery(database,(Titem)((Flow)hequi).getItem()))
            {
                refResumov.upBattery(database.getConnection(), null, traceContext);
            }
            else if(Charger.isCharger(database,(Titem)((Flow)hequi).getItem()))
            {
                refResumov.upCharger(database.getConnection(), null, traceContext);
            }
            Resumov newResumov = new Resumov(-1);
            if(!newResumov.insert(database, office, refResumov.getUso(database), refResumov.getCompany(database), ((Flow)hequi), fecha, traceContext))  throw new FailResultOperationException("Fallo la generacion del registro nuevo para el aditamento enlazado '" + ((Flow)hequi).getItem().getNumber() + "'");
            if(tipo.getID() == 4 || (tipo.getID() == 1 && uso.getID() == 5))
            {
                if(!newResumov.upUso(database,newUso,traceContext)) return false;//throw new FailResultOperationException("Falló la escrutura de Uso en hoja de renta");
                if(!newResumov.upCompany(database,newComp,traceContext)) return false;//throw new FailResultOperationException("Falló la escritua de la empresa en Hoja de renta.");
            }
            return true;
        }
        return false;
    }
    
    /**
     * @return the owner
     */
    public Owner getOwner() 
    {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * @return the folio
     */
    public String getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the tipo
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }    
    public void setTipo(String tipo) 
    {
    }
    

    /**
     * @return the uso
     */
    public Uso getUso() {
        return uso;
    }

    /**
     * @param uso the uso to set
     */
    public void setUso(Uso uso) {
        this.uso = uso;
    }

    /**
     * @return the sa
     */
    public String getSa() {
        return sa;
    }

    /**
     * @param remision
     */
    public void setSA(Remision remision) 
    {
        this.sa = String.valueOf(remision.getFolio());
        this.remision = remision;
    }

    /**
     * @return the company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(Company company) {
        this.company = company;
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
     * @return the oficina
     */
    public Office getOficina() {
        return office;
    }

    /**
     * @param oficina the oficina to set
     */
    public void setOficina(Office oficina) {
        this.office = oficina;
    }

    /**
     * @return the titems
     */
    public List<T> getHequis() {
        return hequis;
    }

    /**
     * @param titems the titems to set
     */
    public void setHequis(List<T> titems) {
        this.hequis = titems;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * 
     * @param note the note to set
     * @param historial
     * @param resumov
     */
    public void setNote(String note, boolean historial, boolean resumov) 
    {
        this.note_his = historial;
        this.note_res = resumov;
        this.note = note;
    }

    private boolean validFolio(String folio) 
    {
        if(folio == null) return false;
        if(folio.length() > 0) return true;        
        return false;
    }
}
