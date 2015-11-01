package com.lpi.consommationtel.Renderers;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.lpi.consommationtel.Jauge;

public class FlatBars extends ColorBars
{
	@Override
	protected void SetStyle(Paint paint, Jauge jauge, RectF rInfo)
	{
		int Couleur = Color.CYAN ;
		
		switch (jauge._StyleJauge)
		{
		case Jauge.STYLE_DEGRADE:
		Couleur = degrade( CouleurStyle1, CouleurStyle2, jauge._Value );
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
	}
}
