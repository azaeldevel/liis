/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIIL.artifact;

/**
 *
 * @author Azael
 */
public class UnsupportedPahseExeception extends UnsupportedOperationException
{
    public UnsupportedPahseExeception()
    {
        super("Soporte para fase no implementado.");
    }
}
