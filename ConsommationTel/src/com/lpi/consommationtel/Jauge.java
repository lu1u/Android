/**
 * 
 */
package com.lpi.consommationtel;

import java.util.Vector;

/**
 * @author lucien
 *
 */
public class Jauge
{
	public static final	int	STYLE_DEGRADE = 6 ;	// Degrade en fonction de la valeur
	public static final int	STYLE_JAUGE_1 = 1 ;	// Couleur de jauge 1: ex SIM1
	public static final int	STYLE_JAUGE_2 = 2 ;	// Couleur de jauge 2: ex SIM2
	
	public static final int	STYLE_JAUGE_3 = 3 ;	// Couleur de jauge 3: ex progression du mois
	public int		_StyleJauge ;
	public Vector<String> 	_Texte ;
	public float	_Value ;
	
	
	public String getLigne(int i){ return _Texte.get(i);}
}
