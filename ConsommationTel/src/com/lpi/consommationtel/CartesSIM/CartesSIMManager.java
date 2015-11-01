/**
 * 
 */
package com.lpi.consommationtel.CartesSIM;

import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;

import com.lpi.consommationtel.Jauge;
import com.lpi.consommationtel.JaugesInfos;
import com.lpi.consommationtel.Preferences;
import com.lpi.consommationtel.R;

/**
 * @author lucien
 * 
 */
public final class CartesSIMManager 
{
	//static private final String[] COLONNES_COMPTE_CARTES_SIM ={ "simid" };
	//private static final String PREF_COULEURS = "Couleur";
	//private static final String PREF_TRANSPARENT = "Transparent";
	//private static final String TAG = "CartesSIMManager";
	private Vector<CarteSIM> _cartesSIM;
	public ViewPager _ViewPager;

	public CartesSIMManager(Context context, int WidgetId)

	{
		Resources r = context.getResources();

		// Recuperer la liste des cartes SIM depuis le journal d'appel
		try
		{
			Preferences.LitPreferences(context, WidgetId);

			_cartesSIM = new Vector<CarteSIM>();
			CarteSIM carte = new CarteSIM();
			JaugeColorInfo color = new JaugeColorInfo(r.getColor(R.color.sim11), r.getColor(R.color.sim12),
					r.getColor(R.color.sim13));

			carte.setID(context, "1", color); //$NON-NLS-1$
			carte._dateDebutAbonnement = Preferences._SIM1DebutAbo;
			carte._nbMinutes = Preferences._SIM1NbMinutes;
			carte._name = Preferences._SIM1Name;
			_cartesSIM.add(carte);

			carte = new CarteSIM();
			color = new JaugeColorInfo(r.getColor(R.color.sim21), r.getColor(R.color.sim22), r.getColor(R.color.sim23));
			carte.setID(context, "2", color); //$NON-NLS-1$
			carte._dateDebutAbonnement = Preferences._SIM2DebutAbo;
			carte._nbMinutes = Preferences._SIM2NbMinutes;
			carte._name = Preferences._SIM2Name;
			_cartesSIM.add(carte);

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void FillJaugesInfo(Context context, JaugesInfos ji)
	{
		ji._jaugeSIM1 = new Jauge() ;
		ji._jaugeMois1 = new Jauge() ;
		RemplitJaugeInfo(context, ji._jaugeSIM1, ji._jaugeMois1, getCarte(0)) ;
		ji._jaugeSIM1._StyleJauge = Jauge.STYLE_JAUGE_1 ;
		
		ji._jaugeSIM2 = new Jauge() ;
		ji._jaugeMois2 = new Jauge() ;
		RemplitJaugeInfo(context, ji._jaugeSIM2, ji._jaugeMois2, getCarte(1)) ;
		ji._jaugeSIM2._StyleJauge = Jauge.STYLE_JAUGE_2 ;
		
	}

	public CarteSIM getCarte(int i)
	{

		if (i >= _cartesSIM.size())
			return null;

		return _cartesSIM.elementAt(i);
	}

	public int getNbCartes()
	{
		return _cartesSIM.size();
	}




	public String[] getSIMNames()
	{
		String[] result = new String[_cartesSIM.size()];
		for (int i = 0; i < _cartesSIM.size(); i++)
		{
			CarteSIM carte = _cartesSIM.elementAt(i);
			result[i] = carte._name;
		}
		return result;

	}

	@SuppressWarnings("static-method")
	private void RemplitJaugeInfo(Context context, Jauge jaugeSIM, Jauge jaugeMois, CarteSIM carte)
	{
		final int	NbMinutesConsommees = carte.NbMinutesConsommees(context) ;
		jaugeSIM._Value = (float)NbMinutesConsommees / (float)carte._nbMinutes ;
		jaugeSIM._Texte = new Vector<String>() ;
		jaugeSIM._Texte.add(  carte._name  ) ;
		jaugeSIM._Texte.add( NbMinutesConsommees+ "/"  + carte._nbMinutes ); //$NON-NLS-1$
		
		
		jaugeMois._StyleJauge = Jauge.STYLE_JAUGE_3 ;
		final int	NbJoursDepuisDebutMois = carte.NbJoursDepuisDebutMois(context) ;
		final int	NbJourMois = carte.NbJoursMois( context) ;
		jaugeMois._Value = (float)NbJoursDepuisDebutMois / (float)NbJourMois ;
		jaugeMois._Texte = new Vector<String>() ;
		jaugeMois._Texte.add(  NbJoursDepuisDebutMois + "j/"  + NbJourMois);//$NON-NLS-1$
	}

}
