package com.kingtyphon.kaijucraft.util;

import java.util.Random;

public class ColorUtils {

    public static int colorToInt(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int colorToInt(int red, int green, int blue) {
        return colorToInt(255, red, green, blue); // Full opacity
    }

    public static int colorToInt(float alpha, float red, float green, float blue) {
        int a = Math.round(alpha * 255);
        int r = Math.round(red * 255);
        int g = Math.round(green * 255);
        int b = Math.round(blue * 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int colorToInt(float red, float green, float blue) {
        return colorToInt(1.0f, red, green, blue); // Full opacity
    }

    public static int colorToInt(float alpha, int red, int green, int blue) {
        int a = Math.round(alpha * 255);
        return (a << 24) | (red << 16) | (green << 8) | blue;
    }

    public static float getAlpha(int color) {
        return ((color >> 24) & 0xFF) / 255.0f;
    }

    public static float getRed(int color) {
        return ((color >> 16) & 0xFF) / 255.0f;
    }

    public static float getGreen(int color) {
        return ((color >> 8) & 0xFF) / 255.0f;
    }

    public static float getBlue(int color) {
        return (color & 0xFF) / 255.0f;
    }

    public static int getRandomColorInt() {
        return 1 + new Random().nextInt(16777215) | 536870912;
    }
}
