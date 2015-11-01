/**
 * 
 */
package com.lpi.consommationtel.CartesSIM;

import java.util.Calendar;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewPager;

/**
 * @author lucien
 * 
 */
public final class CarteSIM extends Fragment {
	static private final String[] COLONNES_COMPTE_APPELS = {
			android.provider.CallLog.Calls.DURATION,/*
													 * android.provider.CallLog. Calls.DATE,
													 */
			android.provider.CallLog.Calls.NUMBER };
	//static private final String[] COLONNES_DERNIER_APPEL = { android.provider.CallLog.Calls.DURATION };
	public final static String SIM_COULEUR1 = "couleur1"; //$NON-NLS-1$
	public final static String SIM_COULEUR2 = "couleur2"; //$NON-NLS-1$
	public final static String SIM_DEBUT = "simdebut"; //$NON-NLS-1$
	public final static String SIM_ID = "simid"; //$NON-NLS-1$
	public final static String SIM_NAME = "simname"; //$NON-NLS-1$
	public final static String SIM_NBMINUTES = "simnbminutes"; //$NON-NLS-1$
	public static final String TAG = "CarteSIM"; //$NON-NLS-1$
	private static void MoisPrecedent(Calendar date) {
		if (date.get(Calendar.MONTH) > Calendar.JANUARY)
			date.add(Calendar.MONTH, -1);
		else {
			date.add(Calendar.YEAR, -1);
			date.set(Calendar.MONTH, Calendar.DECEMBER);
		}

		if (date.get(Calendar.DAY_OF_MONTH) > date
				.getActualMaximum(Calendar.DAY_OF_MONTH))
			date.set(Calendar.DAY_OF_MONTH,
					date.getActualMaximum(Calendar.DAY_OF_MONTH));
	}
	public JaugeColorInfo _colorInfo ;

	public int _dateDebutAbonnement;
	public String _name;

	public int _nbMinutes;
	//private View _rootView;
	public String _simID;

	public ViewPager _ViewPager;



	

	private Calendar getMoisPrecedent(Calendar D) {
		Calendar Date = (Calendar) D.clone();

		if (Date.get(Calendar.DAY_OF_MONTH) >= _dateDebutAbonnement) {
			// Log.d( TAG,
			// "Le debut de l'abonnement est le meme mois qu'aujourd'hui" ) ;
			Date.set(Calendar.DAY_OF_MONTH, _dateDebutAbonnement);
		} else {
			MoisPrecedent(Date);
			//int NbJrMois = Date.getActualMaximum(Calendar.DAY_OF_MONTH);

			Date.set(
					Calendar.DAY_OF_MONTH,
					Math.min(_dateDebutAbonnement,
							Date.getActualMaximum(Calendar.DAY_OF_MONTH)));
		}

		Date.set(Calendar.HOUR_OF_DAY, 0);
		Date.set(Calendar.MINUTE, 0);
		Date.set(Calendar.SECOND, 0);

		return Date;
	}

	
	/**
	 * Charger les donnees depuis
	 */
	/*private void LoadOptions(Context c) {
		SharedPreferences settings = c.getSharedPreferences(TAG + _simID,
				Context.MODE_WORLD_READABLE);
		if (settings == null)
			return;
		_name = settings.getString(SIM_NAME, _name);
		_dateDebutAbonnement = settings.getInt(SIM_DEBUT, _dateDebutAbonnement);
		_nbMinutes = settings.getInt(SIM_NBMINUTES, _nbMinutes);
		// couleurs
	}
*/
	public int NbJoursDepuisDebutMois(Context context) {
		Calendar now = Calendar.getInstance();
		Calendar DateChangementAbonnement = getMoisPrecedent(now);
		long nbMilli = now.getTimeInMillis()
				- DateChangementAbonnement.getTimeInMillis();
		// Passer au premier jour des la premiere seconde
		nbMilli += (1000 * 60 * 60 * 24) / 1;

		return (int) (nbMilli / (1000 * 60 * 60 * 24));
	}

	public int NbJoursMois(Context context) {
		Calendar now = Calendar.getInstance();
		Calendar DateChangementAbonnement = getMoisPrecedent(now);
		return DateChangementAbonnement.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Calcule le nombre de minutes consommees depuis le jour du debut du mois
	 * d'abonnement
	 * 
	 * @param c
	 * @return
	 */
	@SuppressWarnings("nls")
	public int NbMinutesConsommees(Context c) {
		int NbSecTelephone = 0;
		Calendar DateChangementAbonnement = getMoisPrecedent(Calendar
				.getInstance());
		String NotInFree = " NOT IN (" ;
		for ( int i = 0; i < ContactManagement.NUMEROS_GRATUITS.length; i++)
			NotInFree += "'" + ContactManagement.NUMEROS_GRATUITS[i] + "'" + (i<ContactManagement.NUMEROS_GRATUITS.length-1 ? "," : ")") ;
		
		try {
			Cursor cur = c.getContentResolver().query(
					android.provider.CallLog.Calls.CONTENT_URI,
					COLONNES_COMPTE_APPELS,
					android.provider.CallLog.Calls.DATE + " >= " + DateChangementAbonnement.getTimeInMillis() 
					+ " AND " + android.provider.CallLog.Calls.TYPE + " = " + android.provider.CallLog.Calls.OUTGOING_TYPE
					+ " AND simid = '" + _simID + "'" 
					+ " AND " + android.provider.CallLog.Calls.NUMBER + NotInFree, null,
					android.provider.CallLog.Calls.DATE + " DESC");

			// final int DateCol =
			// cur.getColumnIndex(android.provider.CallLog.Calls.DATE) ;
			//final int NumCol = cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
			final int DureeCol = cur
					.getColumnIndex(android.provider.CallLog.Calls.DURATION);

			while (cur.moveToNext()) {
				// C'est un message sortant
				// long callDate = cur.getLong(DateCol);
				//String numero = cur.getString(NumCol);
				long duration = cur.getLong(DureeCol);

				//if (!ContactManagement.EstNumeroGratuit(numero))
				//	if (!ContactManagement.EstNumeroSpecial(numero))
						NbSecTelephone += duration;
			}

			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NbSecTelephone / 60;
	}

	@SuppressWarnings("nls")
	public void setID(Context context, String SimID, JaugeColorInfo colorInfo) {
		_simID = SimID;
		_name = "SIM " + SimID;
		_dateDebutAbonnement = 26;
		_nbMinutes = 121;
		_colorInfo = colorInfo ;
		//LoadOptions(context);
	}
}
