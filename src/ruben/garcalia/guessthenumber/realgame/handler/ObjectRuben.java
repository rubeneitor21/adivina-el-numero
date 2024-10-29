/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruben.garcalia.guessthenumber.realgame.handler;

import ruben.garcalia.guessthenumber.realgame.drawutils.ColorRuben;
import ruben.garcalia.guessthenumber.realgame.drawutils.DrawUtilsRuben;

import org.lwjgl.glfw.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.openvr.Texture;

public class ObjectRuben {
    
    public float x;
    public float y;
    public float width;
    public float height;
    public ColorRuben color;
    public String name;
    public Texture texture;
    
    public ObjectRuben(String name, float x, float y, float width, float height, ColorRuben color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.name = name;
    }
    
    public ObjectRuben(String name, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = new ColorRuben(1f, 1f, 1f, 1f);
        this.name = name;
    }
    
    public void update() {
        // La logica que me de por poner supongo
        draw();
    }
    
    public void draw() {
        DrawUtilsRuben.DrawRectFill(x, y, width, height, color);
    }
}
