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
    
    public float speed = 1f;
    
    public CharacterRuben(String name, String texturePath, float x, float y, float width, float height, ColorRuben color) {
        super(name, texturePath, x, y, width, height, color);
    }
    
    public CharacterRuben(String name, String texturePath, float x, float y, float width, float height) {
        super(name, texturePath, x, y, width, height);
    }

    @Override
    public void update(double deltaTime) {
        float newX = 0f;
        float newY = 0f;
        
        if (keys.get(GLFW_KEY_UP) != null && keys.get(GLFW_KEY_UP)) {
            newY += speed;
        }
        if (keys.get(GLFW_KEY_DOWN) != null && keys.get(GLFW_KEY_DOWN)) {
            newY -= speed;
        }
        if (keys.get(GLFW_KEY_RIGHT) != null && keys.get(GLFW_KEY_RIGHT)) {
            newX += speed;
        }
        if (keys.get(GLFW_KEY_LEFT) != null && keys.get(GLFW_KEY_LEFT)) {
            newX -= speed;
        }
        
        this.x += newX * deltaTime;
        this.y += newY * deltaTime;
        
        if (newX != 0) {
            this.flipX = newX > 0;
        }
        
        if (newX != 0 || newY != 0) {
            createVertex();
        }
        
        elapsedTime += deltaTime;
        if (elapsedTime >= 1/fps) {
            textureIndex++;
            elapsedTime = 0;
        }

        if (textureIndex >= textureBuffer.size()) {
            textureIndex = 0;
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
    }
}
