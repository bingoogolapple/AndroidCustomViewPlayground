package cn.bingoogolapple.acvp.imageprocessing.demo1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class ImageHelper {

    /**
     * @param bitmap     图像
     * @param hue        色调
     * @param saturation 饱和度
     * @param lum        亮度
     * @return
     */
    public static Bitmap handleImageEffect(Bitmap bitmap, float hue, float saturation, float lum) {
        // 传进来的bitmap默认是不可修改的
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(buildCMCF(hue, saturation, lum));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return bmp;
    }

    private static ColorMatrixColorFilter buildCMCF(float hue, float saturation, float lum) {
        ColorMatrix hueColorMatrix = new ColorMatrix();
        // 设置三个颜色的色相，0、1、2分别表示红绿蓝
        hueColorMatrix.setRotate(0, hue);
        hueColorMatrix.setRotate(1, hue);
        hueColorMatrix.setRotate(2, hue);

        ColorMatrix saturationColorMatrix = new ColorMatrix();
        saturationColorMatrix.setSaturation(saturation);

        ColorMatrix lumColorMatrix = new ColorMatrix();
        lumColorMatrix.setScale(lum, lum, lum, 1);

        // 将前面设置的三个ColorMatrix进行柔和
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.postConcat(hueColorMatrix);
        colorMatrix.postConcat(saturationColorMatrix);
        colorMatrix.postConcat(lumColorMatrix);
        return new ColorMatrixColorFilter(colorMatrix);
    }

    public static Bitmap handleImagePixelNegative(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int color;
        int r, g, b, a;

        int[] oldPxs = new int[width * height];
        int[] newPxs = new int[width * height];
        bitmap.getPixels(oldPxs, 0, width, 0, 0, width, height);
        for (int i = 0; i < oldPxs.length; i++) {
            color = oldPxs[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }

            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }

            newPxs[i] = Color.argb(a, r, g, b);

        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(newPxs, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap handleImagePixelOldPhoto(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int color;
        int r, g, b, a;

        int[] oldPxs = new int[width * height];
        int[] newPxs = new int[width * height];
        bitmap.getPixels(oldPxs, 0, width, 0, 0, width, height);
        for (int i = 0; i < oldPxs.length; i++) {
            color = oldPxs[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            r = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            g = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            b = (int) (0.272 * r + 0.534 * g + 0.131 * b);

            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }

            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }

            newPxs[i] = Color.argb(a, r, g, b);

        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(newPxs, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap handleImagePixelRelief(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int color;
        int beforeColor;
        int r, g, b, a;
        int r1, g1, b1;

        int[] oldPxs = new int[width * height];
        int[] newPxs = new int[width * height];
        bitmap.getPixels(oldPxs, 0, width, 0, 0, width, height);
        for (int i = 1; i < oldPxs.length; i++) {
            beforeColor = oldPxs[i - 1];
            r = Color.red(beforeColor);
            g = Color.green(beforeColor);
            b = Color.blue(beforeColor);
            a = Color.alpha(beforeColor);

            color = oldPxs[i];
            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);

            r = (r - r1 + 127);
            g = (g - g1 + 127);
            b = (b - b1 + 127);

            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }

            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }

            newPxs[i] = Color.argb(a, r, g, b);

        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(newPxs, 0, width, 0, 0, width, height);
        return bmp;
    }
}