/**
 * 
 */
package com.lpi.consommationtel.CartesSIM;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

/**
 * @author lucien
 *
 */
public class InputFilterMinMax implements InputFilter {

	    private Context _c ;
	    private int _min, _max; //paramets that you send to class
	   
	    public InputFilterMinMax(Context c, int min, int max) {
	    	_c = c ; 
	       _min = min;
	       _max = max;
	    }

	    @SuppressWarnings("nls")
		@Override
	    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {   
	        try {
	        	String startString = dest.toString().substring(0, dstart);
	            String insert = source.toString();
	            String endString = dest.toString().substring(dend);
	            String parseThis = startString+insert+endString;
	            int input = Integer.parseInt (parseThis);
	        	
	            if (isInRange(_min, _max, input))
	                return null;
	        } catch (NumberFormatException nfe) { return null ;}   
	        
	        Toast.makeText(_c, "Min: " + _min + ", _max: " + _max, Toast.LENGTH_SHORT).show() ;
	        return "";
	    }

	    private boolean isInRange(int a, int b, int c) {
	        return b > a ? c >= a && c <= b : c >= b && c <= a;
	    }
	}
