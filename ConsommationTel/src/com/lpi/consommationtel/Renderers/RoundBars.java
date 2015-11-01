/**
 * 
 */
package com.lpi.consommationtel.Renderers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.lpi.consommationtel.Jauge;

/**
 * @author lucien
 *
 */
public class RoundBars extends ColorBars
{
	public static final float RAYON = 3.0f;
	
	@Override
	protected void DessineFondJauge(Canvas canvas, final RectF r)
	{
		RectF rc = new RectF(r);
		Paint paint = new Paint() ;
		paint.setAntiAlias(false);
		paint.setStyle(Paint.Style.FILL);
		
		float ry = rc.height()/RAYON ;
		
		paint.setColor(CouleurFond);
		canvas.drawRoundRect(rc, ry, ry, paint);
	}

	@Override
	protected void DessineJauge(Canvas canvas, Jauge jauge, RectF rInfo)
	{
		Paint paint = new Paint();
		RectF rc = new RectF(rInfo);
		rc.right = rc.left + (rc.width() * jauge._Value);
		
		paint.setAntiAlias(false);
		paint.setStyle(Paint.Style.FILL);
		
		SetStyle(paint, jauge, rc);
		float ry = rc.height()/RAYON ;
		canvas.drawRoundRect(rc, ry, ry, paint);
	}
}
