
package process;

import java.util.ArrayList;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public final class Convinator 
{
    private ArrayList<Double> options;//valores posbles para operando
    private int cantTermino;//cantidad de terminos por operacion
    private ArrayList<ArrayList<Double>> generatedArrays;
    
    public int factorial(int numero) 
    {
        if (numero == 0) return 1;
        return numero * factorial(numero - 1);
    }
    
    public int calcularPermutacion()
    {
        return factorial(options.size())/factorial(options.size()-cantTermino);
    }
    
    public int calcularCombinacion()
    {
        return factorial(options.size())/(factorial(options.size() - cantTermino)) * (factorial(cantTermino));
    }
    
    public void generarCombinacion2()
    {
        generatedArrays = new ArrayList<>();
        ArrayList<Double> comb = null;
        int r = 3, n = options.size();
        
                
    }
    
    public void generarCombinacion()
    {
        generatedArrays = new ArrayList<>();
        ArrayList<Double> comb = null;
        int r = 0;
                
        for(int jr = 1; jr < options.size(); jr++)
        {
            for(int i = jr - 1; i < options.size(); i = i + jr)
            {
                comb = new ArrayList<>();
                if((options.size() - i)  > cantTermino)
                {
                    for(int j = 0; j < cantTermino; j++)
                    {
                        comb.add(options.get(j + i).doubleValue());
                    }
                }
                generatedArrays.add(comb);
            }
        }
    }

    private void clean() 
    {
        for(int i = options.size() - 2; i > 0 ; i--)
        {
            for(int j = i + 1; j < options.size() ; j++)
            {
                if(Double.compare(options.get(i).doubleValue(), options.get(j).doubleValue()) == 0)
                {
                    options.remove(j);
                    i--;
                }
            }
        }
    }
    
    public Convinator(ArrayList<Double> op, int cantTermino)
    {
        this.options = op;
        this.cantTermino = cantTermino;        
    }
    
    public void print()
    {
        for(ArrayList<Double> comb : generatedArrays)
        {
            
            for(int i = 0; i < comb.size(); i++)
            {
                System.out.print(comb.get(i));
                System.out.print(" + ");
            }
            System.out.println("");
        }
    }
}
