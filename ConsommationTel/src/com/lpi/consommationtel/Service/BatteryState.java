/**
 * 
 */
package com.lpi.consommationtel.Service;

import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import com.lpi.consommationtel.Jauge;
import com.lpi.consommationtel.JaugesInfos;
import com.lpi.consommationtel.R;
import com.lpi.consommationtel.Renderers.Renderer;

/**
 * @author lucien
 *
 */

public class BatteryState
{
	public static final int BATTERIE_EN_CHARGE = 0;
	public static final int BATTERIE_ENDECHARGE = 1;
	public static final int BATTERIE_FROIDE = 8;
	public static final int BATTERIE_INCONNU = -1;
	public static final int BATTERIE_MORTE = 5;
	public static final int BATTERIE_PANNE = 7;
	public static final int BATTERIE_PASENCHARGE = 2;
	public static final int BATTERIE_PLEINE = 3;
	public static final int BATTERIE_SURCHAUFFE = 4;
	public static final int BATTERIE_SURTENSION = 6;
	
	static public int level = -1 ;
	static public String rawstatus = "" ; //$NON-NLS-1$
	static public int state = BATTERIE_INCONNU ;
	
	public static void FillJaugesInfo(Context context, JaugesInfos ji)
	{
		ji._jaugeBatterie = new Jauge() ;
		
		ji._jaugeBatterie._StyleJauge = Jauge.STYLE_DEGRADE ;
		ji._jaugeBatterie._Value	  =  level / 100.0f;
		ji._jaugeBatterie._Texte	  = new Vector<String>() ;
		ji._jaugeBatterie._Texte.add( getTexteStatusBatterie(context) ) ;
		ji._jaugeBatterie._Texte.add( level + "%") ; //$NON-NLS-1$
	}
	
	/**
	 * Retourne la couleur de la batterie en fonction de son etat et de son niveau de charge
	 * 
	 * @param context
	 * @param batteryStatus
	 * @param batteryLevel
	 * @return
	 */
	public static int getBatterieCouleur(Context context)
	{
		Resources r = context.getResources();

		switch (BatteryState.state)
		{
		case BATTERIE_EN_CHARGE:
		case BATTERIE_PASENCHARGE:
		case BATTERIE_ENDECHARGE:
		case BATTERIE_PLEINE:
			return Renderer.degrade(r.getColor(R.color.batterie_0), r.getColor(R.color.batterie_100),
					BatteryState.level / 100.0f);

		case BATTERIE_SURCHAUFFE:
		case BATTERIE_MORTE:
		case BATTERIE_SURTENSION:
		case BATTERIE_PANNE:
		case BATTERIE_FROIDE:
			return r.getColor(R.color.batterie_panne);
		default:
			return Color.MAGENTA;
		}
	}
	
	public static String getTexteStatusBatterie(Context context)
	{
		switch (BatteryState.state)
		{
		case BATTERIE_EN_CHARGE:
			return context.getString(R.string.labelCharge);
		case BATTERIE_PASENCHARGE:
			return context.getString(R.string.labelPasEnCharge);
		case BATTERIE_ENDECHARGE:
			return context.getString(R.string.labelDecharge);
		case BATTERIE_PLEINE:
			return context.getString(R.string.labelPleine);
		case BATTERIE_SURCHAUFFE:
			return context.getString(R.string.labelSurchauffe);
		case BATTERIE_MORTE:
			return context.getString(R.string.labelMorte);
		case BATTERIE_SURTENSION:
			return context.getString(R.string.labelSurtension);
		case BATTERIE_PANNE:
			return context.getString(R.string.labelPanne);
		case BATTERIE_FROIDE:
			return context.getString(R.string.labelFroide);
		default:
			return BatteryState.rawstatus;
		}

	}

}
