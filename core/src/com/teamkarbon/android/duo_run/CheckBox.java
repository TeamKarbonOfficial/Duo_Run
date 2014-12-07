package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew on 7/12/2014.
 */
public class CheckBox {
    Vector2 pos, size;//Note: pos value is relative to the host CustomGUIBox..
    String text;
    Color color;
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
}
