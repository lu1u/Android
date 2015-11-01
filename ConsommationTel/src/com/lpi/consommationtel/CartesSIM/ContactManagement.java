/**
 * 
 */
package com.lpi.consommationtel.CartesSIM;

/**
 * @author lucien
 * 
 */
public class ContactManagement
{
	@SuppressWarnings("nls")
	public static final String[] NUMEROS_GRATUITS =
	{ "666", "+33695600011" };
	public static final String[] NUMEROS_SPECIAUX =
	{};
	public static final String TAG = "ContactManagement"; //$NON-NLS-1$

//	static boolean EstNumeroGratuit(String number)
//	{
//		for (String numero : NUMEROS_GRATUITS)
//			if (numero.equals(number))
//				return true;
//
//		return false;
//	}
//
//	static boolean EstNumeroSpecial(String number)
//	{
//		for (String numero : NUMEROS_SPECIAUX)
//			if (numero.equals(number))
//				return true;
//
//		return false;
//	}

	/**
	 * Retrouve le nom du contact a partir de son numero
	 * 
	 * @param a
	 * @param numero
	 * @return
	 * @throws Exception
	 */
//	public static String GetContact(Context a, String numero)
//	{
//		String res = numero;
//
//		try
//		{
//			// Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(numero));
//			Cursor c = a.getContentResolver().query(
//					Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(numero)), new String[]
//					{ PhoneLookup.DISPLAY_NAME }, null, null, null);
//			// a.startManagingCursor(c) ;
//			c.moveToFirst();
//			res = c.getString(c.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
//			c.close();
//
//		} catch (Exception e)
//		{
//			if (numero.startsWith("+33")) //$NON-NLS-1$
//			{
//				numero = "0" + numero.substring(3); //$NON-NLS-1$
//				return GetContact(a, numero);
//			} else
//				res = numero;
//		}
//
//		return res;
//
//	}

	/***
	 * Retourne l'image associee a un contact, ou null
	 * 
	 * @param context
	 * @param phoneNumber
	 * @return
	 */
//	public static Bitmap getPhotoUri(Context context, String phoneNumber)
//	{
//		Cursor contact = null ;
//		Bitmap res ;
//		
//		try
//		{
//			Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
//			Uri photoUri = null;
//			ContentResolver cr = context.getContentResolver();
//			contact = cr.query(phoneUri, new String[] { BaseColumns._ID }, null, null, null);
//			
//			contact.moveToFirst() ;
//			final long userId = contact.getLong(contact.getColumnIndex(BaseColumns._ID));
//			contact.close();
//			photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);
//
//			InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, photoUri);
//			
//			res = BitmapFactory.decodeStream(input);
//			input.close();
//		} catch (Exception e)
//		{
//			// En cas d'erreur, on retourne une bitmap par defaut
//			res = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_report_image);
//		}
//
//		if ( contact != null)
//			contact.close() ;
//		return res ;
//	}

}
