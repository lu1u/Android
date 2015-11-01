/**
 * 
 */
package com.lpi.consommationtel;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;

/**
 * @author lucien
 * 
 */
public class Configuration extends Activity
{
	int AppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	/**
	 * 
	 * @return
	 */
	private String GetTypeAffichage()
	{
		final int selected = ((Spinner) findViewById(R.id.spinnerRenderer)).getSelectedItemPosition();
		final String[] values = getResources().getStringArray(R.array.renderers_values);
		return values[selected];
	}

	/**
	 * 
	 * @param v
	 */
	public void OnCancel(View v)
	{
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetId);
		setResult(RESULT_CANCELED, resultValue);
		finish();
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setResult(RESULT_CANCELED);
		// Inflate our UI from its XML layout description.
		setContentView(R.layout.configure);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null)
			AppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		else
			AppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

		Preferences.LitPreferences(this, AppWidgetId);

		((Switch) findViewById(R.id.switchFondTransparent)).setChecked(!Preferences._DessineFond);
		// ((TextView)findViewById(R.id.textViewWidgetID)).setText(Integer.toString(AppWidgetId));
		((EditText) findViewById(R.id.editTextSIM1Name)).setText(Preferences._SIM1Name);
		((EditText) findViewById(R.id.editSIM1Minutes)).setText(Integer.toString(Preferences._SIM1NbMinutes));
		RemplitDebutAbo( R.id.spinnerDebutAbo1, R.layout.spinner_view_sim_1, Preferences._SIM1DebutAbo ) ;
		
		
		((EditText) findViewById(R.id.EditTextSIM2Name)).setText(Preferences._SIM2Name);
		((EditText) findViewById(R.id.EditTextSIM2Minutes)).setText(Integer.toString(Preferences._SIM2NbMinutes));
		RemplitDebutAbo( R.id.spinnerDebutAbo2, R.layout.spinner_view_sim_2, Preferences._SIM1DebutAbo ) ;
		((ScrollView) findViewById(R.id.scrollView1)).setScrollY(0);
		((ScrollView) findViewById(R.id.scrollView1)).setScrollX(0);
		SetTypeAffichage();
	}

	/**
	 * Remplir un spinner avec les jours du mois
	 * @param spinnerdebutabo1
	 * @param _SIM1DebutAbo
	 */
	private void RemplitDebutAbo(int spinnerId, int spinnerViewId, int debutAbo)
	{
		Spinner sp = (Spinner)findViewById(spinnerId) ;
		
		ArrayAdapter<String> array = new ArrayAdapter<String>(this,spinnerViewId, R.id.texteView1);
		for ( int i = 1; i < 32; i++)
			array.add(Integer.toString(i));
		sp.setAdapter(array);
		sp.setSelection(debutAbo-1); // Les jours commencent à 0, les dates à 1
	}

	/**
	 * 
	 * @param v
	 */
	public void OnOK(View v)
	{
		Preferences._RendererClass = GetTypeAffichage();
		Preferences._DessineFond = !((Switch) findViewById(R.id.switchFondTransparent)).isChecked();

		Preferences._SIM1Name = ((EditText) findViewById(R.id.editTextSIM1Name)).getText().toString();
		Preferences._SIM1NbMinutes = Integer.parseInt(((EditText) findViewById(R.id.editSIM1Minutes)).getText().toString());
		Preferences._SIM1DebutAbo = ((Spinner)findViewById(R.id.spinnerDebutAbo1)).getSelectedItemPosition() + 1 ;
		Preferences._SIM2Name = ((EditText) findViewById(R.id.EditTextSIM2Name)).getText().toString();
		Preferences._SIM2NbMinutes = Integer.parseInt(((EditText) findViewById(R.id.EditTextSIM2Minutes)).getText().toString());
		Preferences._SIM2DebutAbo = ((Spinner)findViewById(R.id.spinnerDebutAbo2)).getSelectedItemPosition() + 1 ;
		
		Preferences.EcritPreferencesGlobales(this, AppWidgetId);
		
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetId);
		setResult(RESULT_OK, resultValue);
		ConsotelWidgetProvider.UpdateAllWidgets(this);
		finish();
	}

	/**
	 * Selectionne le type d'affichage dans le spinner qui permet de choisir
	 */
	private void SetTypeAffichage()
	{
		final String[] values = getResources().getStringArray(R.array.renderers_values);
		for (int i = 0; i < values.length; i++)
		{
			if (values[i].equals(Preferences._RendererClass))
			{
				((Spinner) findViewById(R.id.spinnerRenderer)).setSelection(i);
				return;
			}
		}

		((Spinner) findViewById(R.id.spinnerRenderer)).setSelection(0);
	}


}
