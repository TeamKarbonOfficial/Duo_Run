package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew on 7/12/2014.
 */
public class CheckBox {
    Vector2 pos, size;//Note: pos value is relative to the host CustomGUIBox, and is the pos of the bottom left of the text.
    String text;
    Color color;//Color when depressed.
    boolean isChecked;

    public CheckBox(Vector2 _pos, Vector2 _size, String _text, Color _color, boolean _isChecked)
    {
        pos = _pos;
        size = _size;
        text = _text;
        color = _color;
        isChecked = _isChecked;
    }

    //Default unchecked init.
    public CheckBox(Vector2 _pos, Vector2 _size, String _text, Color _color)
    {
        pos = _pos;
        size = _size;
        text = _text;
        color = _color;
        isChecked = false;
    }

    public boolean flip()
    {
        isChecked = !isChecked;
        return isChecked;
    }

    public Vector2 getGlobalPos(Vector2 hostPos)
    {
        return new Vector2(pos.x + hostPos.x, pos.y + hostPos.y);
    }

    //This Vector2 value is the place to draw the clickable check box.
    public Vector2 getGlobalBoxPos(Vector2 hostPos, BitmapFont font, CustomGUIBox guiBoxHost)
    {
        return this.getGlobalPos(hostPos).add(font.getBounds(text).width + guiBoxHost.pwidth(5f), 0f);
    }
}
