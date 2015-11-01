/**
 * 
 */
package com.lpi.consommationtel.Renderers;

import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.NinePatchDrawable;

import com.lpi.consommationtel.Jauge;
import com.lpi.consommationtel.JaugesInfos;
import com.lpi.consommationtel.Preferences;
import com.lpi.consommationtel.R;

/**
 * @author lucien
 *
 */
public abstract class Renderer
{
	public static final int OMBRE_DX = 1 ;
	public static final int OMBRE_DY = 1 ;
	public static final float RAYON_OMBRE = 1.0f ;
	
	/**
	 * Calcule un carre contenu dans un rectangle et ayant le meme centre
	 * @param r
	 * @return
	 */
	protected static RectF carreCentre(RectF r)
	{
		float cx = r.centerX();
		float cy = r.centerY();
		float largeur = Math.min(r.height(), r.width()) / 2.0f;

		return new RectF(cx - largeur, cy - largeur, cx + largeur, cy + largeur);
	}
	
	public static String concatene( Vector<String> v, String separator)
	{
		StringBuffer sb = new StringBuffer() ;
		for (int i = 0; i < v.size()-1; i++)
		{
			sb.append(v.get(i)) ;
			sb.append(separator) ;
		}
		
		if ( v.size() > 0)
			sb.append(v.get(v.size()-1)) ;
		
		return sb.toString();
	}

	/**
	 * Calcule une couleur degradee entre deux couleurs
	 * 
	 * @param color1
	 * @param color2
	 * @param f
	 * @return
	 */
	public static int degrade(int color1, int color2, float f)
	{
		final float a1 = Color.alpha(color1);
		final float r1 = Color.red(color1);
		final float g1 = Color.green(color1);
		final float b1 = Color.blue(color1);

		final float a2 = Color.alpha(color2);
		final float r2 = Color.red(color2);
		final float g2 = Color.green(color2);
		final float b2 = Color.blue(color2);

		return Color.argb((int) (a1 + (a2 - a1) * f), (int) (r1 + (r2 - r1) * f), (int) (g1 + (g2 - g1) * f),
				(int) (b1 + (b2 - b1) * f));
	}
	
	/**
	 * Dessine le texte en ajustant la taille a l'espace disponible
	 * 
	 * @param canvas
	 * @param texte
	 * @param rInfo
	 */
	public static void dessineTexteAligneDroite(Canvas canvas, Jauge jauge, RectF r)
	{
		Paint paint = new Paint();
		paint.setShadowLayer(RAYON_OMBRE, OMBRE_DX, OMBRE_DY, Color.BLACK);
		paint.setAntiAlias(true);
		RectF rInfo = new RectF(r);
		rInfo.inset(8, 4);
		
		final String texte = concatene(jauge._Texte, " ") ; //$NON-NLS-1$
		final int texteSize = getTailleTexte( r, texte ) ;
		
		paint.setTextSize(texteSize);
		FontMetrics fm = paint.getFontMetrics();
		final float HauteurTexte = fm.ascent + fm.descent + fm.leading ;
		
		paint.setTextAlign(Align.RIGHT);
		float X = rInfo.right;
		float Y = rInfo.top + rInfo.height() + HauteurTexte/2.0f ;

		paint.setColor(Color.WHITE);
		canvas.drawText(texte, X, Y, paint);
	}
	
	/***
	 * Calcule une taille de texte pour que celui ci tienne dans le rectangle donne
	 * @param r
	 * @param s
	 * @return
	 */
	public static int getTailleTexte( RectF r, String s)
	{
		Paint paint = new Paint();
		int texteSize = (int)( r.height() / 2);
		paint.setTextSize(texteSize);
		
		final int longueur = s.length();
		Rect rBounds = new Rect() ;
		paint.getTextBounds(s, 0, longueur, rBounds);
		
		int LargeurTexte = rBounds.width() ;
		int HauteurTexte = rBounds.height() ;
		
		while ((texteSize>2) && ((LargeurTexte>r.width()) || (HauteurTexte>r.height())))
		{
			texteSize--;
			paint.setTextSize(texteSize);
			paint.getTextBounds(s, 0, longueur, rBounds);
			
			LargeurTexte = rBounds.width() ;
			HauteurTexte = rBounds.height() ;
		}
		
		return texteSize ;
	}
	
	public Bitmap construitBitmap(Context context, int Largeur, int Hauteur, JaugesInfos ji)
	{
		initStyle(context, context.getResources()) ;
		
		Bitmap bm = Bitmap.createBitmap(Largeur, Hauteur, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bm);
		
		if (Largeur > Hauteur)
		{
			dessineFond( context, canvas, Largeur, Hauteur, ji ) ;
			dessineBitmap(context, canvas, Largeur, Hauteur, ji);
		}
		else
		{
			int C = Math.min(Largeur, Hauteur) / 2;
			Matrix m = new Matrix();
			m.setRotate(90, C, C);
			canvas.setMatrix(m);
			dessineFond( context, canvas, Hauteur, Largeur, ji ) ;
			dessineBitmap(context, canvas, Hauteur, Largeur, ji);
		}

		return bm;
	}
	protected abstract void dessineBitmap(Context context, Canvas canvas, int Largeur, int Hauteur, JaugesInfos ji);
	
	protected static  void dessineFond(Context context, Canvas canvas, int Largeur, int Hauteur, JaugesInfos ji)
	{
		if ( Preferences._DessineFond )
		{
			NinePatchDrawable npd = (NinePatchDrawable) context.getResources().getDrawable(R.drawable.appwidget_dark_bg);
			npd.setBounds(0, 0, Largeur, Hauteur );
			npd.draw(canvas);
		}
	}

	protected void initStyle(Context context, Resources resources)
	{
	}
}
