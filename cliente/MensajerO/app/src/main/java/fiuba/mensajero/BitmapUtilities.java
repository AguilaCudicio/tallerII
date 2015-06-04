package fiuba.mensajero;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapUtilities {


    public static String bitmapToString(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    public static Bitmap stringToBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static Bitmap getResizedBitmap(String imgDecodableString, int maxSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(imgDecodableString,options);

        int scale = 1;
        if (options.outHeight > maxSize|| options.outWidth > maxSize) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(maxSize /
                    (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap b = BitmapFactory.decodeFile(imgDecodableString, o2);

        return b;
    }

}