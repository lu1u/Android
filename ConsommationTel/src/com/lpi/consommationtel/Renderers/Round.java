/**
 * 
 */
package com.lpi.consommationtel.Renderers;

import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;

import com.lpi.consommationtel.Jauge;
import com.lpi.consommationtel.JaugesInfos;
import com.lpi.consommationtel.R;

/**
 * @author lucien
 * 
 */
public class Round extends Renderer
{
	protected static int CouleurMilieuJauge;
	protected static int CouleurJaugeMois;
	protected static int CouleurStyle1;
	protected static int CouleurStyle2;
	protected static int MargeJauge;
	protected static int LargeurLigneEpaisse;
	protected static int LargeurLigneFine;
	protected static int TailleTexte;

	public static final int OMBRE_DX = 1;
	public static final int OMBRE_DY = 1;
	public static final float RAYON_OMBRE = 1.0f;

	/*
	 * (non-Javadoc)
	 * @see com.lpi.consommationtel.ConsotelWidgetProvider#ConstruitBitmap(android.content.Context,
	 * int, int, com.lpi.consommationtel.JaugesInfos)
	 */
	@Override
	protected void dessineBitmap(Context context, Canvas canvas, int largeur, int hauteur, JaugesInfos ji)
	{
		Rect re = new Rect(0, 0, largeur, hauteur);

		final int NbInformations = 3; // Cartes SIM + Batterie
		final int LargeurUneInfo = re.width() / NbInformations;
		RectF rInfo = new RectF(re.left, re.top, re.left + LargeurUneInfo, re.bottom);

		// SIM1
		DessineJaugeSIM(canvas, rInfo, ji._jaugeSIM1, ji._jaugeMois1);
		rInfo.offset(rInfo.width(), 0);

		// SIM2
		DessineJaugeSIM(canvas, rInfo, ji._jaugeSIM2, ji._jaugeMois2);
		rInfo.offset(rInfo.width(), 0);

		// Batterie
		DessineJaugeBatterie(canvas, ji._jaugeBatterie, rInfo);
	}

	private void DessineJaugeBatterie(Canvas canvas, Jauge jauge, RectF rInfo)
	{
		DessineJaugeRonde(canvas, rInfo, jauge, LargeurLigneEpaisse, true, jauge._Texte);
	}

	protected void DessineJaugeRonde(Canvas canvas, final RectF re, Jauge jauge, int LargeurLigne, boolean dessineFond, Vector<String> vTexte)
	{
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(LargeurLigne);
		RectF RCercle = carreCentre(re);
		RCercle.inset(MargeJauge, MargeJauge);

		final float CentreX = RCercle.centerX();
		final float CentreY = RCercle.centerY();

		if (dessineFond)
		{
			paint.setColor(CouleurMilieuJauge);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(RCercle.centerX(), RCercle.centerY(), (RCercle.width() + LargeurLigne) / 2, paint);

			paint.setColor(0x44FFFFFF);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(RCercle, 0, 360.0f, false, paint);
			paint.setColor(CouleurJaugeMois);
		}

		float Angle = jauge._Value * 360.0f;

		//if (jauge._StyleJauge == Jauge.STYLE_DEGRADE)
		{
			canvas.save(Canvas.MATRIX_SAVE_FLAG);
			canvas.rotate(-90, CentreX, CentreY);
			SetStyle(paint, jauge, RCercle);
			
			
			canvas.drawArc(RCercle, 0.0f, Angle, false, paint);
			canvas.restore();
			paint.setShader(null);
		}
		
		paint.setShader(null);
		paint.setStrokeWidth(0);
		paint.setColor(CouleurJaugeMois);
		if (vTexte != null)
			DessineTexteCentre(canvas, paint, RCercle, vTexte);
	}

	private  void DessineJaugeSIM(Canvas canvas, final RectF rInfo, Jauge jaugeSIM, Jauge jaugeMois)
	{
		Paint paint = new Paint();
		paint.setAntiAlias(false);
		SetStyle(paint, jaugeSIM, rInfo);
		RectF re = new RectF(rInfo);
		Vector<String> vTexte = new Vector<String>();
		vTexte.add(jaugeSIM._Texte.get(0));
		vTexte.add(jaugeSIM._Texte.get(1));

		DessineJaugeRonde(canvas, re, jaugeSIM, LargeurLigneEpaisse, true, vTexte);
		DessineJaugeRonde(canvas, re, jaugeMois, LargeurLigneFine, false, null);
	}

	/***
	 * Dessine un ensemble de lignes de textes, centrees dans un rectangle
	 * 
	 * @param canvas
	 * @param paint
	 * @param rCercle
	 * @param v
	 */
	protected static void DessineTexteCentre(Canvas canvas, Paint paint, RectF rCercle, Vector<String> v)
	{
		final int NbLignes = v.size();
		// Calculer la hauteur totale des lignes
		FontMetrics fm = paint.getFontMetrics();
		final float HauteurLignes = fm.ascent * NbLignes;

		final float X = rCercle.centerX();
		float Y = rCercle.centerY() - (HauteurLignes / 2.0f) + fm.ascent;

		Y += fm.ascent * ((NbLignes - 1.0f) / 2.0f);
		Rect bounds = new Rect();
		paint.setShadowLayer(RAYON_OMBRE, OMBRE_DX, OMBRE_DY, Color.BLACK);
		paint.setTextSize(TailleTexte);

		for (int i = 0; i < NbLignes; i++)
		{
			String texte = v.get(i);
			paint.getTextBounds(texte, 0, texte.length(), bounds);
			canvas.drawText(texte, X - bounds.width() / 2, Y, paint);
			Y -= fm.ascent;
		}

		paint.setShadowLayer(0, 0, 0, 0);
	}

	@Override
	protected void initStyle(Context context, Resources r)
	{
		super.initStyle(context, r);
		CouleurStyle1 = r.getColor(R.color.RondSIM1);
		CouleurStyle2 = r.getColor(R.color.RondSIM2);
		CouleurJaugeMois = r.getColor(R.color.RondMois);
		CouleurMilieuJauge = r.getColor(R.color.RondTransparentMilieux);
		MargeJauge = r.getDimensionPixelSize(R.dimen.margeJaugesRondes);
		LargeurLigneEpaisse = r.getDimensionPixelSize(R.dimen.ligneEpaisseJaugesRondes);
		LargeurLigneFine = r.getDimensionPixelSize(R.dimen.ligneFineJaugesRondes);
		TailleTexte = r.getDimensionPixelSize(R.dimen.tailleTexteJaugesRondes);
	}

	protected void SetStyle(Paint paint, Jauge jauge, RectF rInfo)
	{
		int Couleur = Color.MAGENTA;

		switch (jauge._StyleJauge)
		{
		case Jauge.STYLE_DEGRADE:
		{
			paint.setShader(new SweepGradient(rInfo.centerX(), rInfo.centerY(), Color.RED, Color.GREEN));
			break;
		}
		case Jauge.STYLE_JAUGE_1:
			Couleur = CouleurStyle1;
			break;

		case Jauge.STYLE_JAUGE_2:
			Couleur = CouleurStyle2;
			break;
		case Jauge.STYLE_JAUGE_3:
			Couleur = CouleurJaugeMois;
			break;
		default: assert false ;
		
		}

		paint.setColor(Couleur);
	}

}
