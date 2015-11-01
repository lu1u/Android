package com.lpi.consommationtel.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryChangeReceiver extends BroadcastReceiver {
	
	public static String TAG = "BatteryReceiver"; //$NON-NLS-1$
	
	@SuppressWarnings("nls")
	private static void GetBatteryLevel(Context context, Intent intent) {
		int rawlevel = intent.getIntExtra("level", -1); //$NON-NLS-1$
		int scale = intent.getIntExtra("scale", -1); //$NON-NLS-1$
		int level = -1;
		if (rawlevel >= 0 && scale > 0) 
		{
			level = (rawlevel * 100) / scale;
		}
			
		BatteryState.level = level;
		
		final int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,BatteryManager.BATTERY_STATUS_UNKNOWN);
		switch( status )
		{
		case BatteryManager.BATTERY_STATUS_CHARGING : 		BatteryState.state = BatteryState.BATTERIE_EN_CHARGE ; break ;
		case BatteryManager.BATTERY_STATUS_DISCHARGING :	BatteryState.state = BatteryState.BATTERIE_ENDECHARGE; break ;
		case BatteryManager.BATTERY_STATUS_NOT_CHARGING :	BatteryState.state = BatteryState.BATTERIE_PASENCHARGE; break ;
		case BatteryManager.BATTERY_STATUS_FULL : 			BatteryState.state = BatteryState.BATTERIE_PLEINE; break ;
		default : BatteryState.state = BatteryState.BATTERIE_INCONNU;
					
		}
		
		final int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,BatteryManager.BATTERY_HEALTH_UNKNOWN);
		switch( health)
		{
		case BatteryManager.BATTERY_HEALTH_OVERHEAT : 	BatteryState.state = BatteryState.BATTERIE_SURCHAUFFE ; break ;
		case BatteryManager.BATTERY_HEALTH_DEAD : 	BatteryState.state = BatteryState.BATTERIE_MORTE ; break ;
		case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE : 	BatteryState.state = BatteryState.BATTERIE_SURTENSION ; break ;
		case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE : 	BatteryState.state = BatteryState.BATTERIE_PANNE ; break ;
		case BatteryManager.BATTERY_HEALTH_COLD : 	BatteryState.state = BatteryState.BATTERIE_FROIDE ; break ;
		default: // Pas de panne detectee
		}
		BatteryState.rawstatus = "[" + status + "][" + health ; //$NON-NLS-2$
		}

	public static void Refresh(Context context) {
		Intent intent = context.getApplicationContext().registerReceiver(null,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		GetBatteryLevel( context, intent ) ;
	}
	

	@SuppressWarnings("static-method")
	private void Notification(Context context) {
		Intent i = new Intent();
		i.setAction(ConsommationTelephoneService.ACTION_CHANGED);
		i.putExtra(ConsommationTelephoneService.TYPE,ConsommationTelephoneService.TYPE_BATTERY);
		i.putExtra(ConsommationTelephoneService.BATTERY_LEVEL, BatteryState.level);
		i.putExtra(ConsommationTelephoneService.BATTERY_STATE, BatteryState.state);
		i.putExtra(ConsommationTelephoneService.BATTERY_RAWSTATE, BatteryState.rawstatus);
		context.sendBroadcast(i);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// Log.debug( "BatteryChangeReceiver:" + intent.getAction()) ;
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
			int oldL = BatteryState.level ;
			int oldS = BatteryState.state ;
			GetBatteryLevel(context, intent);
			if ( oldL != BatteryState.level || oldS != BatteryState.state)
				Notification(context);
		}
	}
}