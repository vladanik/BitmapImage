import java.io.*;
import java.lang.*;

public class BitmapImage {
    static int width;
    static int height;
    static String[][] bitmap;

    BitmapImage(int width, int height) {
        this.width = width;
        this.height = height;
        bitmap = new String[width][height];
    }

    void setPixelColor(Pixel p, RGB rgb) {
        bitmap[p.getX()][p.getY()] = rgb.toString();
    }

    static void saveToFile(String fname) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(fname + ".ppm");
        out.println("P3");
        out.println(width + " " + height);
        out.println(255);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                out.println(bitmap[j][i]);
            }
        }
        out.close();
    }

    void fillBitmap(int w, int h) {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                setPixelColor(new Pixel(j, i), new RGB(255, 255, 255));
            }
        }
    }

    void drawLine(int x1, int y1, int x2, int y2, int colorR, int colorG, int colorB) {
        int s = (x2 - x1) * (y2 - y1);
        if (s < 0) {
            s *= -1;
        }
        double ooo = (double)(x2 - x1) / (y2 - y1);
        if (ooo < 0) {
            ooo *= -1;
        }
        setPixelColor(new Pixel(x1, y1), new RGB(colorR, colorG, colorB));
        if (x1 > x2 && y1 < y2) {
            for (int i = y1 + 1; i < y2; i++) {
                for (int j = x1 - 1; j > x2 ; j--) {
                    if (ooo == ((double)(x1 - j) / (i - y1))) {
                        setPixelColor(new Pixel(j, i), new RGB(colorR, colorG, colorB));
                    }
                }
            }
        } else if (x1 < x2 && y1 > y2) {
            for (int i = y1 - 1; i > y2; i--) {
                for (int j = x1 + 1; j < x2 ; j++) {
                    if (ooo == ((double)(j - x1) / (y1 - i))) {
                        setPixelColor(new Pixel(j, i), new RGB(colorR, colorG, colorB));
                    }
                }
            }
        } else if (x1 > x2 && y1 > y2) {
            for (int i = y1 - 1; i > y2; i--) {
                for (int j = x1 - 1; j > x2 ; j--) {
                    if (ooo == ((double)(x1 - j) / (y1 - i))) {
                        setPixelColor(new Pixel(j, i), new RGB(colorR, colorG, colorB));
                    }
                }
            }
        } else if (x1 == x2 && y1 < y2) {
            for (int i = y1 + 1; i < y2; i++) {
                setPixelColor(new Pixel(x1, i), new RGB(colorR, colorG, colorB));
            }
        } else if (x1 == x2 && y1 > y2) {
            for (int i = y1 - 1; i > y2; i--) {
                setPixelColor(new Pixel(x1, i), new RGB(colorR, colorG, colorB));
            }
        } else if (x1 < x2 && y1 == y2) {
            for (int i = x1 + 1; i < x2; i++) {
                setPixelColor(new Pixel(i, y1), new RGB(colorR, colorG, colorB));
            }
        } else if (x1 > x2 && y1 == y2) {
            for (int i = x1 - 1; i > x2; i--) {
                setPixelColor(new Pixel(i, y1), new RGB(colorR, colorG, colorB));
            }
        } else if (x1 == x2 && y1 == y2) {

        }
        else {
            for (int i = y1 + 1; i < y2; i++) {
                for (int j = x1 + 1; j < x2 ; j++) {
                    if (ooo == ((double)(j - x1) / (i - y1))) {
                        setPixelColor(new Pixel(j, i), new RGB(colorR, colorG, colorB));
                    }
                }
            }
        }
        setPixelColor(new Pixel(x2, y2), new RGB(colorR, colorG, colorB));
    }
    void drawRectangle(int x1, int y1, int x2, int y2, int colorR, int colorG, int colorB) {
        for (int i = y1; i <= y2; i++) {
            for (int j = x1; j <= x2; j++) {
                if (i == y1 || i == y2 || j == x1 || j == x2) {
                    setPixelColor(new Pixel(j, i), new RGB(colorR, colorG, colorB));
                }
            }
        }
    }
    void fillRectangle(int x1, int y1, int x2, int y2, int colorR, int colorG, int colorB) {
        for (int i = y1; i <= y2; i++) {
            for (int j = x1; j <= x2; j++) {
                if (i != y1 || i != y2 || j != x1 || j != x2) {
                    setPixelColor(new Pixel(j, i), new RGB(colorR, colorG, colorB));
                }
            }
        }
    }
    void drawTriangle(int xA, int yA, int xB, int yB, int xC, int yC, int colorR, int colorG, int colorB) {
        drawLine(xA, yA, xB, yB, colorR, colorG, colorB);
        drawLine(xB, yB, xC, yC, colorR, colorG, colorB);
        drawLine(xC, yC, xA, yA, colorR, colorG, colorB);
    }
    void fillTriangle(int xA, int yA, int xB, int yB, int xC, int yC, int colorR, int colorG, int colorB) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (inTriangle(j, i, xA, yA, xB, yB, xC, yC)) {
                    setPixelColor(new Pixel(j, i), new RGB(colorR, colorG, colorB));
                }
            }
        }
    }
    boolean inTriangle(int x, int y, int xA, int yA, int xB, int yB, int xC, int yC) {
        boolean b = false;
        int[] xp = {xA, xB, xC};
        int[] yp = {yA, yB, yC};
        int np = 3;
        int j = 2;
        for (int i = 0; i < np; i++) {
            if (((yp[i] <= y && y < yp[j]) || (yp[j] <= y && y < yp[i])) && (x > (xp[j] - xp[i]) * (y - yp[i]) / (yp[j] - yp[i]) + xp[i])) {
                b = !b;
            }
            j = i;
        }
        return b;
    }
    void drawCircle(int x, int y, int r, int colorR, int colorG, int colorB) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int xp = j - x;
                if (xp < 0) {
                    xp *= -1;
                }
                int yp = i - y;
                if (yp < 0) {
                    yp *= -1;
                }
                if (Math.pow(xp, 2) + Math.pow(yp, 2) == Math.pow(r, 2)) {
                    setPixelColor(new Pixel(j, i), new RGB(colorR, colorG, colorB));
                }
            }
        }
    }
    void fillCircle(int x, int y, int r, int colorR, int colorG, int colorB) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int xp = j - x;
                if (xp < 0) {
                    xp *= -1;
                }
                int yp = i - y;
                if (yp < 0) {
                    yp *= -1;
                }
                if (Math.pow(xp, 2) + Math.pow(yp, 2) < Math.pow(r, 2)) {
                    setPixelColor(new Pixel(j, i), new RGB(colorR, colorG, colorB));
                }
            }
        }
    }
    void drawStar(int x, int y) {
        drawTriangle(x, y, x - 15, y + 30, x + 15, y + 30, 100, 93, 0);
        fillTriangle(x, y, x - 15, y + 30, x + 15, y + 30, 100, 93, 0);
        drawCircle(x, y, 10, 232, 216, 0);
        fillCircle(x, y, 10, 232, 216, 0);
        drawTriangle(x, y - 30, x - 6, y - 10, x + 6, y - 10, 232, 216, 0);
        fillTriangle(x, y - 30, x - 6, y - 10, x + 6, y - 10, 232, 216, 0);
        drawTriangle(x + 6, y - 10,x + 26, y - 10, x + 10, y + 4, 232, 216, 0);
        fillTriangle(x + 6, y - 10,x + 26, y - 10, x + 10, y + 4, 232, 216, 0);
        drawTriangle(x - 6, y - 10,x - 26, y - 10, x - 10, y + 4, 232, 216, 0);
        fillTriangle(x - 6, y - 10,x - 26, y - 10, x - 10, y + 4, 232, 216, 0);
        drawTriangle(x, y + 10, x + 10, y + 4, x + 13, y + 17, 232, 216, 0);
        fillTriangle(x, y + 10, x + 10, y + 4, x + 13, y + 17, 232, 216, 0);
        drawTriangle(x, y + 10, x - 10, y + 4, x - 13, y + 17, 232, 216, 0);
        fillTriangle(x, y + 10, x - 10, y + 4, x - 13, y + 17, 232, 216, 0);
    }


    public static void main(String[] args) throws FileNotFoundException {
//        BitmapImage polish = new BitmapImage(800, 500);
//        BitmapImage german = new BitmapImage(800, 500);
//        BitmapImage czech = new BitmapImage(800, 500);
//        BitmapImage russian = new BitmapImage(800, 500);
        BitmapImage choinka = new BitmapImage(600, 800);

        choinka.fillBitmap(600, 800);

        choinka.drawRectangle(285, 540, 315, 590, 78, 52, 0);
        choinka.fillRectangle(285, 540, 315, 590, 78, 52, 0);

        choinka.drawTriangle(300,440,200,540,400,540, 0, 155, 0);
        choinka.fillTriangle(300,440,200,540,400,540, 0, 155, 0);
        choinka.drawTriangle(300,380,200,480,400,480, 0, 155, 0);
        choinka.fillTriangle(300,380,200,480,400,480, 0, 155, 0);
        choinka.drawTriangle(300,320,200,420,400,420, 0, 155, 0);
        choinka.fillTriangle(300,320,200,420,400,420, 0, 155, 0);
        choinka.drawTriangle(300,260,220,360,380,360, 0, 155, 0);
        choinka.fillTriangle(300,260,220,360,380,360, 0, 155, 0);

        choinka.drawCircle(280, 330, 10, 255, 0, 0);
        choinka.fillCircle(280, 330, 10, 255,0,0);
        choinka.drawCircle(265, 460, 10, 255, 255, 0);
        choinka.fillCircle(265, 460, 10, 255,255,0);
        choinka.drawCircle(335, 395, 10, 0, 0, 255);
        choinka.fillCircle(335, 395, 10, 0,0,255);

        choinka.drawStar(300, 240);

        choinka.saveToFile("choinka");

        /*for (int i = 0; i < polish.height / 2; i++) {
            for (int j = 0; j < polish.width; j++) {
                polish.setPixelColor(new Pixel(j, i), new RGB(255, 255, 255));
            }
        }
        for (int i = polish.height / 2; i < polish.height; i++) {
            for (int j = 0; j < polish.width; j++) {
                polish.setPixelColor(new Pixel(j, i), new RGB(255, 0, 0));
            }
        }
        polish.saveToFile("polish");

        for (int i = 0; i < german.height / 3; i++) {
            for (int j = 0; j < german.width; j++) {
                german.setPixelColor(new Pixel(j, i), new RGB(10, 10, 10));
            }
        }
        for (int i = german.height / 3; i < (german.height / 3) * 2; i++) {
            for (int j = 0; j < german.width; j++) {
                german.setPixelColor(new Pixel(j, i), new RGB(255, 0, 0));
            }
        }
        for (int i = (german.height / 3) * 2; i < german.height; i++) {
            for (int j = 0; j < german.width; j++) {
                german.setPixelColor(new Pixel(j, i), new RGB(255, 255, 0));
            }
        }
        german.saveToFile("german");

        for (int i = 0; i < czech.height / 2; i++) {
            for (int j = 0; j < czech.width; j++) {
                czech.setPixelColor(new Pixel(j, i), new RGB(255, 255, 255));
            }
        }
        for (int i = czech.height / 2; i < czech.height; i++) {
            for (int j = 0; j < czech.width; j++) {
                czech.setPixelColor(new Pixel(j, i), new RGB(255, 0, 0));
            }
        }
        for (int i = 0; i < czech.height / 2; i++) {
            for (int j = 0; j < czech.width; j++) {
                if (j <= i) {
                    czech.setPixelColor(new Pixel(j, i), new RGB(0, 0, 255));
                }
            }
        }
        for (int i = czech.height / 2; i < czech.height; i++) {
            for (int j = 0; j < czech.width; j++) {
                if (j <= czech.height - i) {
                    czech.setPixelColor(new Pixel(j, i), new RGB(0, 0, 255));
                }
            }
        }
        czech.saveToFile("czech");

        for (int i = 0; i < russian.height / 3; i++) {
            for (int j = 0; j < russian.width; j++) {
                russian.setPixelColor(new Pixel(j, i), new RGB(255, 255, 255));
            }
        }
        for (int i = russian.height / 3; i < (russian.height / 3) * 2; i++) {
            for (int j = 0; j < russian.width; j++) {
                russian.setPixelColor(new Pixel(j, i), new RGB(0, 0, 255));
            }
        }
        for (int i = (russian.height / 3) * 2; i < russian.height; i++) {
            for (int j = 0; j < russian.width; j++) {
                russian.setPixelColor(new Pixel(j, i), new RGB(255, 0, 0));
            }
        }
        russian.saveToFile("russian");*/
    }
}
