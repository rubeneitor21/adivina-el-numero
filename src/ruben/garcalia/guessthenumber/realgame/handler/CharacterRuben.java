/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruben.garcalia.guessthenumber.realgame.handler;

import ruben.garcalia.guessthenumber.realgame.drawutils.ColorRuben;

public class CharacterRuben extends ObjectRuben {
    public CharacterRuben(String name, float x, float y, float width, float height, ColorRuben color) {
        super(name, x, y, width, height, color);
    }
    
    public CharacterRuben(String name, float x, float y, float width, float height) {
        super(name, x, y, width, height);
    }
    
    @Override
    public void update(long window, int key, int scancode, int action, int mods) {
        draw();
    }
}
