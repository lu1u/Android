package com.lpi.consommationtel.Renderers;

import android.content.Context;
import android.content.res.Resources;

import com.lpi.consommationtel.R;

public class MonoBars extends ColorBars
{
	@Override
	protected void initStyle(Context context, Resources r)
	{
		super.initStyle(context, r);
		CouleurStyle1 = r.getColor(R.color.mono_jauge);
		CouleurStyle2 = CouleurStyle1 ;
		CouleurStyle3 = CouleurStyle1 ;
		CouleurFond = r.getColor(R.color.mono_fondjauge);
	}
}
