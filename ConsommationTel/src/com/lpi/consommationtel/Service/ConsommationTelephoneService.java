/**
 * 
 */
package com.lpi.consommationtel.Service;


import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.lpi.consommationtel.R;

/**
 * @author lucien
 * 
 */
public class ConsommationTelephoneService extends Service
{
	public static final String ACTION_CHANGED = "com.lpi.consommationtel.changed"; //$NON-NLS-1$
	public static final String ACTION_CONFIG = "com.lpi.consommationtel.config"; //$NON-NLS-1$

	public static final String TAG = "ConsoService"; //$NON-NLS-1$
	public static final String TYPE = "type"; //$NON-NLS-1$
	public static final int TYPE_BATTERY = 2;
	public static final int TYPE_CALL = 1;
	public static final int TYPE_SMS = 0;

	public static final String BATTERY_LEVEL = "level"; //$NON-NLS-1$
	public static final String BATTERY_STATE = "state"; //$NON-NLS-1$
	public static final String BATTERY_RAWSTATE = "rstate"; //$NON-NLS-1$

	/***
	 * Affichage de message d'erreur
	 * 
	 * @param localizedMessage
	 */
	public static void Erreur(Context context, String localizedMessage)
	{
		Log.e(TAG, localizedMessage);
		Toast t = Toast.makeText(context, localizedMessage, Toast.LENGTH_LONG);
		t.show();
	}

	/***
	 * Affiche un message dans la zone de notification
	 * 
	 * @param context
	 * @param message
	 *            a afficher
	 */
	public static void Notification(Context context, String message)
	{
		if (message == null)
			return;
		if (context == null)
			return;

		try
		{
			Builder builder = new Builder(context);
			builder.setContentTitle(context.getResources().getString(R.string.app_name))
					.setContentText(message)
			.setTicker(message)
			.setSmallIcon(R.drawable.ic_stat_icon_notification_consommation)
			.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
			.setAutoCancel(true)
			.setPriority(0)
			.setAutoCancel(true);
			Notification notification = builder.build();
			NotificationManager notificationManger = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManger.notify(0, notification);
		} catch (Exception e)
		{
			Erreur(context, e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

	public static void Start(Context context)
	{
		try
		{
			Intent intent = new Intent(context, ConsommationTelephoneService.class);
			context.startService(intent);

		} catch (Exception e)
		{
			Erreur(context, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private BatteryChangeReceiver batteryReceiver;
	private CallObserver _callObserver ;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(batteryReceiver);
	}

	/***
	 * Creation du service, creer les divers surveillants: - SMS - Appels telephoniques
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		try
		{
			// Appel telephonique
			//CallObserver m_CallObserver = new CallObserver(this);
			//getContentResolver().registerContentObserver(android.provider.CallLog.Calls.CONTENT_URI, true,
			//		m_CallObserver);
			_callObserver = new CallObserver(this);
			TelephonyManager yourmanager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		    yourmanager.listen(_callObserver, PhoneStateListener.LISTEN_CALL_STATE);
			// Envoi d'un SMS

			// Changement d etat de la batterie
			IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			batteryReceiver = new BatteryChangeReceiver();
			registerReceiver(batteryReceiver, intentFilter);
		} catch (Exception e)
		{
			Erreur(this, e.getLocalizedMessage() + "\n" + e.getStackTrace().toString()); //$NON-NLS-1$
		}

		return START_STICKY;

	}

}
