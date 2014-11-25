package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew on 25/11/2014.
 */
public class CustomButton {
    Vector2 pos, size;
    String text;
    Color color;

    public CustomButton(Vector2 _pos, Vector2 _size, String _text, Color _color)
    {
        pos = _pos;
        size = _size;
        text = _text;
        color = _color;
    }

    public boolean isClicked(TouchData touchData)
    {
        if(touchData.active && touchData.x >= pos.x && touchData.x <= pos.x + size.x
                && touchData.y >= pos.y && touchData.y <= pos.y + size.y)
        {
            return true;
        }
        return false;
    }
}
