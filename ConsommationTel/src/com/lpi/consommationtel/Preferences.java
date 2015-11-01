/**
 * 
 */
package com.lpi.consommationtel;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author lucien
 *
 */
@SuppressWarnings("nls")
public class Preferences
{
	public static String	_RendererClass = "ColorRenderer" ; //$NON-NLS-1$
	
	public static boolean	_DessineFond = true ;	
	// SIM 1
	public static int		_SIM1DebutAbo = 25 ;
	public static String	_SIM1Name = "SIM1" ;
	public static int		_SIM1NbMinutes = 120 ;
	
	// SIM2
	public static int		_SIM2DebutAbo = 25 ;
	public static String	_SIM2Name = "SIM2" ;
	public static int		_SIM2NbMinutes = 120 ;
	
	public static final String PREF_DESSINEFOND			= "dessineFond" ;
	
	public static final String PREF_SIM1_DEBUTABO		= "SIM1debutabo" ;
	public static final String PREF_SIM1_NAME			= "SIM1Name" ;
	public static final String PREF_SIM1_NBMINUTES		= "SIM1NbMInutes" ;
	
	public static final String PREF_SIM2_DEBUTABO		= "SIM2debutabo" ;
	public static final String PREF_SIM2_NAME			= "SIM2Name" ;
	public static final String PREF_SIM2_NBMINUTES		= "SIM2NbMInutes" ;
	
	public static final String PREF_TYPEAFFICHAGE		= "TypeAffichage" ;
	public static final String PREFERENCES				= "com.lpi.consommationtel.Renderers.ColorBars" ;
	
	public static void EcritPreferencesGlobales( Context a, int WidgetId ) 
	{
		SharedPreferences settings = a.getSharedPreferences( PREFERENCES + WidgetId, Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(PREF_DESSINEFOND, _DessineFond) ;
		editor.putString( PREF_TYPEAFFICHAGE, _RendererClass);
		
		editor.putString( PREF_SIM1_NAME, _SIM1Name );
		editor.putInt( PREF_SIM1_NBMINUTES, _SIM1NbMinutes);
		editor.putInt( PREF_SIM1_DEBUTABO, _SIM1DebutAbo);
		
		editor.putString( PREF_SIM2_NAME, _SIM2Name );
		editor.putInt( PREF_SIM2_NBMINUTES, _SIM2NbMinutes);
		editor.putInt( PREF_SIM2_DEBUTABO, _SIM2DebutAbo);
		
		editor.commit();
	}

	public static void LitPreferences( Context a, int WidgetId)
	{
		SharedPreferences settings 				= a.getSharedPreferences( PREFERENCES + WidgetId, Context.MODE_PRIVATE);
		_DessineFond							= settings.getBoolean( PREF_DESSINEFOND, _DessineFond);
		_RendererClass							= settings.getString( PREF_TYPEAFFICHAGE, _RendererClass );
		
		_SIM1Name								= settings.getString( PREF_SIM1_NAME, _SIM1Name );
		_SIM1NbMinutes							= settings.getInt( PREF_SIM1_NBMINUTES, _SIM1NbMinutes);
		_SIM1DebutAbo							= settings.getInt( PREF_SIM1_DEBUTABO, _SIM1DebutAbo);
		
		_SIM2Name								= settings.getString( PREF_SIM2_NAME, _SIM2Name );
		_SIM2NbMinutes							= settings.getInt( PREF_SIM2_NBMINUTES, _SIM2NbMinutes);
		_SIM2DebutAbo							= settings.getInt( PREF_SIM2_DEBUTABO, _SIM2DebutAbo);
	}
}
