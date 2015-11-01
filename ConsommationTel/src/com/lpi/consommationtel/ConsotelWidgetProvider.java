/**
 * 
 */
package com.lpi.consommationtel;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lpi.consommationtel.CartesSIM.CartesSIMManager;
import com.lpi.consommationtel.Renderers.Renderer;
import com.lpi.consommationtel.Service.BatteryChangeReceiver;
import com.lpi.consommationtel.Service.BatteryState;
import com.lpi.consommationtel.Service.ConsommationTelephoneService;

/**
 * @author lucien
 * 
 */
public class ConsotelWidgetProvider extends AppWidgetProvider
{
	public static String CLICK_ACTION = "com.lpi.consotel.CLICK"; //$NON-NLS-1$
	public static String REFRESH_ACTION = "com.lpi.consotel.REFRESH"; //$NON-NLS-1$

	public static final String TAG = "ConsotelProvider"; //$NON-NLS-1$

	static public void UpdateAllWidgets(Context context)
	{
		AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
		ComponentName widgetComponent = new ComponentName(context, ConsotelWidgetProvider.class);
		int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);

		Intent update = new Intent(context, ConsotelWidgetProvider.class);
		update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
		update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		context.sendBroadcast(update);
	}

	private void AfficheConfiguration(Context context, Intent intent)
	{
		int WidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		Intent myIntent = new Intent(context, Configuration.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		myIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WidgetId);
		context.startActivity(myIntent);
	}

	/***
	 * Rapporte une erreur detectee par le programme
	 * @param context
	 * @param e
	 */
	@SuppressWarnings("nls")
	public void Erreur(Context context, Exception e)
	{
		String message = ""; //$NON-NLS-1$
		if (e == null)
		{
			message = "null exception ?!?";//$NON-NLS-1$
		} else
		{
			StringBuffer stack = new StringBuffer();

			StackTraceElement[] st = e.getStackTrace();
			for (int i = 0; i < st.length; i++)
			{
				String line = st[i].getMethodName() + ":" + st[i].getLineNumber();
				Log.e(TAG, line);
				stack.append(line);
				stack.append("\n");//$NON-NLS-1$
			}
			message = "Erreur " + e.getLocalizedMessage() + "\n" + stack.toString(); //$NON-NLS-1$
		}
		Log.e(TAG, message);
		Toast t = Toast.makeText(context, "Erreur\n" + message + "\n", Toast.LENGTH_LONG);//$NON-NLS-1$
		t.show();

	}

	private Renderer GetRendererFromPreferences(Context c)
	{
		
		try
		{
			Class<?> cls = Class.forName(Preferences._RendererClass);
			return (Renderer) cls.newInstance();
		} catch (Exception e)
		{
			Erreur(c, e);
			return null ;
		} 
	}

	/***
	 * Prendre en compte le changement de configuratioh
	 * 
	 * @param context
	 * @param intent
	 */
	private void HandleConfigure(Context context, Intent intent)
	{
		// Comme les cartes SIM sont rechargees a chaque Update, il suffit de rafraichir l'affichage
		UpdateAllWidgets(context);
	}

	private void HandleDateChanged(Context context, Intent intent)
	{
		UpdateAllWidgets(context);
	}

	private void HandleResize(Context context, Intent intent)
	{
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName widgetComponent = new ComponentName(context, getClass());
		int[] widgetIds = appWidgetManager.getAppWidgetIds(widgetComponent);
		appWidgetManager.updateAppWidget(widgetIds, new RemoteViews(context.getPackageName(), R.layout.widgetlayout));
	}

	/*
	 * (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onAppWidgetOptionsChanged(android .content.Context,
	 * android.appwidget.AppWidgetManager, int, android.os.Bundle)
	 */
	@Override
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions)
	{
		Update(context);
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
	}

	// @Override
	@Override
	public void onReceive(Context context, Intent intent)
	{
		final String action = intent.getAction();
		// Log.d(TAG, "OnReceive " + action );
		if (action.equals(Intent.ACTION_DATE_CHANGED))
			HandleDateChanged(context, intent);
		else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE))
			AfficheConfiguration(context, intent);
		else if (action.equals(ConsommationTelephoneService.ACTION_CHANGED))
			ServiceUpdate(context, intent);
		else if (action.contentEquals("com.motorola.blur.home.ACTION_SET_WIDGET_SIZE")) //$NON-NLS-1$
			HandleResize(context, intent);
		else if (action.equals(ConsommationTelephoneService.ACTION_CONFIG))
			HandleConfigure(context, intent);
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		try
		{
			if (BatteryState.level == -1)
				BatteryChangeReceiver.Refresh(context);

			ConsommationTelephoneService.Start(context);

			// Update each of the widgets with the remote adapter
			for (int i = 0; i < appWidgetIds.length; ++i)
			{
				final int appWidgetId = appWidgetIds[i];
				Preferences.LitPreferences(context, appWidgetId);
				
				CartesSIMManager cartesSIM = new CartesSIMManager(context, appWidgetId);

				Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
				final int minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
				final int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
				final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
				RegisterOnClickListener(context, rv, appWidgetIds, appWidgetId);

				if (minWidth > 0 && minHeight > 0)
				{
					// / Register an onClickListener
					JaugesInfos ji = new JaugesInfos();
					cartesSIM.FillJaugesInfo(context, ji);
					BatteryState.FillJaugesInfo(context, ji);
					
					Renderer renderer = GetRendererFromPreferences(context) ;
					rv.setImageViewBitmap(R.id.imageView1, renderer.construitBitmap(context, minWidth, minHeight, ji));
				}

				appWidgetManager.updateAppWidget(appWidgetId, rv);

			}
			super.onUpdate(context, appWidgetManager, appWidgetIds);
		} catch (Exception e)
		{
			Erreur(context, e);
		}
	}

	protected void RegisterOnClickListener(Context context, RemoteViews rv, int[] appWidgetIds, int appWidgetId)
	{
		Intent intent = new Intent(context, getClass());
		intent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
		//intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
	}

	private void ServiceUpdate(Context context, Intent intent)
	{
		switch (intent.getIntExtra(ConsommationTelephoneService.TYPE, -1))
		{
		case ConsommationTelephoneService.TYPE_CALL:
			break;

		case ConsommationTelephoneService.TYPE_BATTERY:
			BatteryState.level = intent.getIntExtra(ConsommationTelephoneService.BATTERY_LEVEL, -1);
			BatteryState.state = intent.getIntExtra(ConsommationTelephoneService.BATTERY_STATE, -1);
			BatteryState.rawstatus = intent.getStringExtra(ConsommationTelephoneService.BATTERY_RAWSTATE);
			break;

		default:
			BatteryChangeReceiver.Refresh(context);
			break;
		}
		
		Update( context);
		
	}

	private void Update(Context context)
	{
		AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
		ComponentName widgetComponent = new ComponentName(context, getClass());
		int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);

		Intent update = new Intent(context, getClass());
		update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
		update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		context.sendBroadcast(update);
	}

}
