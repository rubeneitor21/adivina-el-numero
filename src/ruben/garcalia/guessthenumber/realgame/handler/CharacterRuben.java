/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruben.garcalia.guessthenumber.realgame.handler;

import java.util.HashMap;
import java.util.Map;
import ruben.garcalia.guessthenumber.realgame.drawutils.ColorRuben;

import static org.lwjgl.glfw.GLFW.*;

public class CharacterRuben extends ObjectRuben {
    
    private Map<Integer, Boolean> keys = new HashMap<>();
    
    public CharacterRuben(String name, String texturePath, float x, float y, float width, float height, ColorRuben color) {
        super(name, texturePath, x, y, width, height, color);
    }
    
    public CharacterRuben(String name, String texturePath, float x, float y, float width, float height) {
        super(name, texturePath, x, y, width, height);
    }
    
    @Override
    public void update() {
        float newX = 0f;
        float newY = 0f;
        
        if (keys.get(GLFW_KEY_UP) != null && keys.get(GLFW_KEY_UP)) {
            newY += 0.01f;
        }
        if (keys.get(GLFW_KEY_DOWN) != null && keys.get(GLFW_KEY_DOWN)) {
            newY -= 0.01f;
        }
        if (keys.get(GLFW_KEY_RIGHT) != null && keys.get(GLFW_KEY_RIGHT)) {
            newX += 0.01f;
        }
        if (keys.get(GLFW_KEY_LEFT) != null && keys.get(GLFW_KEY_LEFT)) {
            newX -= 0.01f;
        }
        
        this.x += newX;
        this.y += newY;
        
        if (newX != 0 || newY != 0) {
            this.flipX = newX > 0;
//            this.flipY = newY > 0;
            createVertex();
        }
        
        draw();
    }
    
    public void getInputs(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            this.keys.put(key, true);
        }
        if (action == GLFW_RELEASE) {
            this.keys.put(key, false);
        }
        
        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_J) {
                this.flipX = !this.flipX;
            }
            else if (key == GLFW_KEY_L) {
                this.flipY = !this.flipY;
            }
        }
    }
}