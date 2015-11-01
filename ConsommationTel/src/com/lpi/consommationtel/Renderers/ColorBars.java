/**
 * 
 */
package com.lpi.consommationtel.Renderers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

import com.lpi.consommationtel.Jauge;
import com.lpi.consommationtel.JaugesInfos;
import com.lpi.consommationtel.R;

/**
 * @author lucien
 *
 */
public class ColorBars extends Renderer
{

	int CouleurFond;
	int CouleurStyle1;
	int CouleurStyle2;
	int CouleurStyle3;
	int CouleurDegrade1 ;
	int CouleurDegrade2 ;
	int MargeBarres ;
	
	/*
	 * (non-Javadoc)
	 * @see com.lpi.consommationtel.ConsotelWidgetProvider#ConstruitBitmap(android.content.Context,
	 * int, int, com.lpi.consommationtel.CartesSIM.CartesSIMManager)
	 */


	@Override
	protected void dessineBitmap(Context context, Canvas canvas, int largeur, int hauteur, JaugesInfos ji)
	{
		Rect re = new Rect(0, 0, largeur, hauteur);
		
		final int NbInformations = 3; // Cartes SIM + Batterie
		final int HauteurUneInfo = re.height() / NbInformations;
		RectF rInfo = new RectF(re.left, re.top, re.right, re.top + HauteurUneInfo);

		// SIM1
		DessineJaugeSIM(canvas, rInfo, ji._jaugeSIM1, ji._jaugeMois1);
		rInfo.offset(0, HauteurUneInfo);

		// SIM2
		DessineJaugeSIM(canvas, rInfo, ji._jaugeSIM2, ji._jaugeMois2);
		rInfo.offset(0, HauteurUneInfo);

		// Batterie
		DessineJaugeBatterie(canvas, ji._jaugeBatterie, rInfo);
	}

	protected void DessineFondJauge(Canvas canvas, final RectF r)
	{
		RectF rc = new RectF(r);
		Paint paint = new Paint() ;
		paint.setAntiAlias(false);
		paint.setStyle(Paint.Style.FILL);
		
		paint.setColor(CouleurFond);
		canvas.drawRect(rc, paint);
	}

	protected void DessineJauge(Canvas canvas, Jauge jauge, RectF rInfo)
	{
		Paint paint = new Paint();
		RectF rc = new RectF(rInfo);
		rc.right = rc.left + (rc.width() * jauge._Value);
		
		paint.setAntiAlias(false);
		paint.setStyle(Paint.Style.FILL);
		
		SetStyle(paint, jauge, rc);
		canvas.drawRect(rc, paint);
	}

	// Dessine les jauge: progression du mois/consommation du mois
	protected void DessineJaugeBatterie(Canvas canvas, Jauge jauge, RectF rInfo)
	{
		RectF rc = new RectF(rInfo);
		rc.inset(MargeBarres,MargeBarres);
		
		// Trace le fond de la jauge
		DessineFondJauge( canvas, rc ) ;
		rc.inset(MargeBarres,MargeBarres*2);
		
		// Progression du mois
		DessineJauge(canvas, jauge, rc);
		dessineTexteAligneDroite(canvas, jauge, rInfo) ;
		}

	protected void DessineJaugeSIM(Canvas canvas, final RectF rInfo, Jauge jaugeSIM, Jauge jaugeMois)
	{
		RectF R = new RectF(rInfo);
		R.inset(MargeBarres, MargeBarres);
		// Trace le fond de la jauge
		DessineFondJauge( canvas, R ) ;
		
		// Progression du mois
		R.inset(MargeBarres,MargeBarres) ;
		R.bottom = R.top + (R.height() / 2);
		DessineJauge(canvas, jaugeMois, R);
		
		R.offset(0, R.height());
		DessineJauge(canvas, jaugeSIM, R);
		
		dessineTexteAligneDroite(canvas, jaugeSIM, rInfo) ;
	}

	@Override
	protected void initStyle(Context context, Resources r)
	{
		super.initStyle(context, r);
		CouleurStyle1 = r.getColor(R.color.sim12);
		CouleurStyle2 = r.getColor(R.color.sim22);
		CouleurStyle3 = r.getColor(R.color.mois2);
		CouleurDegrade1 = r.getColor(R.color.degrade1);
		CouleurDegrade2 = r.getColor(R.color.degrade2);
		CouleurFond = r.getColor(R.color.fond_jauge);
		MargeBarres = r.getDimensionPixelSize(R.dimen.margeBarres) ;
	}

	protected void SetStyle(Paint paint, Jauge jauge, RectF rInfo)
	{
		int Couleur = Color.CYAN ;
		
		switch (jauge._StyleJauge)
		{
		case Jauge.STYLE_DEGRADE:
		Couleur = degrade( CouleurDegrade1, CouleurDegrade2, jauge._Value );
		break ;
		
		case Jauge.STYLE_JAUGE_1:
		Couleur = CouleurStyle1 ;
		break;
		
		case Jauge.STYLE_JAUGE_2:
		Couleur = CouleurStyle2 ;
		break;
		case Jauge.STYLE_JAUGE_3:
		Couleur = CouleurStyle3 ;
		break;
		
		default: assert false ;
		}
		
		paint.setColor(Couleur);
		LinearGradient s = new LinearGradient(rInfo.left, rInfo.top, rInfo.right, rInfo.top, degrade(Couleur, Color.BLACK, 0.4f ),degrade(Couleur, Color.WHITE, 0.4f ), TileMode.CLAMP);
		paint.setShader(s);
	}



}
