package com.lpi.consommationtel.Renderers;

import android.content.Context;
import android.content.res.Resources;

import com.lpi.consommationtel.R;

public class BlueBars extends MonoBars
{
	@Override
	protected void initStyle(Context context, Resources r )
	{
		super.initStyle(context, r);
		CouleurStyle1 = r.getColor(R.color.bleu_jauge);
		CouleurStyle2 = r.getColor(R.color.bleu_jauge);
		CouleurStyle3 = r.getColor(R.color.bleu_jauge);
		CouleurFond = r.getColor(R.color.bleu_fond);
	}
}
