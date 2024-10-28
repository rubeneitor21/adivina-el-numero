package ruben.garcalia.guessthenumber.realgame.drawutils;

import org.lwjgl.glfw.*;
import static org.lwjgl.opengl.GL11.*;

public class DrawUtilsRuben {
    
    public static void DrawRectFill(float x, float y, float width, float height, ColorRuben color) {
        glColor3f(color.r, color.g, color.b);
        glLineWidth(30);
        glBegin(GL_POLYGON);
            glVertex2f(x - width / 2, y - height / 2);
            glVertex2f(x + width / 2, y - height / 2);
            glVertex2f(x + width / 2, y + height / 2);
            glVertex2f(x - width / 2, y + height / 2);
        glEnd();
        glFlush();
    }

}
