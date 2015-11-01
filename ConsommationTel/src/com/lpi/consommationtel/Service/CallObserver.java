package com.lpi.consommationtel.Service;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/*
 * public class CallObserver extends ContentObserver { Context context ; public CallObserver(
 * Context c) { super(new Handler()); context = c ; }
 * @Override public boolean deliverSelfNotifications() { return false; } // *** Envoi ou reception
 * d'un appel // *
 * @Override public void onChange(boolean selfChange) { Intent i = new Intent() ; i.setAction(
 * ConsommationTelephoneService.ACTION_CHANGED ) ; i.putExtra( ConsommationTelephoneService.TYPE,
 * ConsommationTelephoneService.TYPE_CALL ) ; context.sendBroadcast(i) ; } }
 */

public class CallObserver extends PhoneStateListener
{
	private boolean _decroche = false;
	private Context context;

	public CallObserver(Context c)
	{
		context = c;
		_decroche = false;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber)
	{
		super.onCallStateChanged(state, incomingNumber);
		switch (state)
		{
		case TelephonyManager.CALL_STATE_OFFHOOK:
			_decroche = true;
			break;

		case TelephonyManager.CALL_STATE_RINGING:
		case TelephonyManager.CALL_STATE_IDLE:
			if (_decroche)
			{
				Intent i = new Intent();
				i.setAction(ConsommationTelephoneService.ACTION_CHANGED);
				i.putExtra(ConsommationTelephoneService.TYPE, ConsommationTelephoneService.TYPE_CALL);
				context.sendBroadcast(i);
			}
			_decroche = false;
			break;
		default:
			break;
		}
	}

}
