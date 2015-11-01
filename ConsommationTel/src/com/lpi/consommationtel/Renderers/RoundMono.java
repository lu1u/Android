/**
 * 
 */
package com.lpi.consommationtel.Renderers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.RectF;

import com.lpi.consommationtel.Jauge;
import com.lpi.consommationtel.R;

/**
 * @author lucien
 * 
 */
public class RoundMono extends Round
{
	@Override
	protected void initStyle(Context context, Resources r)
	{
		super.initStyle(context, r);
		CouleurStyle1 = r.getColor(R.color.RondMonoSIM1);
		CouleurStyle2 = r.getColor(R.color.RondMonoSIM2);
		CouleurJaugeMois = r.getColor(R.color.RondMonoMois);
		CouleurMilieuJauge = r.getColor(R.color.RondTransparentMilieux);
	}
	
	@Override
	protected void SetStyle(Paint paint, Jauge jauge, RectF rInfo)
	{
		if ( jauge._StyleJauge == Jauge.STYLE_DEGRADE )
		{
			paint.setColor(CouleurJaugeMois);
		}
		else
			super.SetStyle(paint, jauge, rInfo);
	}
}
