package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew on 25/11/2014.
 */
public class CustomButton {
    Vector2 pos, size;//Note: pos value is relative to the host CustomGUIBox..
    String text;
    Color color;
    boolean animateFlag;

    public CustomButton(Vector2 _pos, Vector2 _size, String _text, Color _color)
    {
        pos = _pos;
        size = _size;
        text = _text;
        color = _color;
        animateFlag = false;
    }

    public boolean isClicked(TouchData touchData, Vector2 hostPos)
    {
        if(touchData.active && touchData.x >= getGlobalPos(hostPos).x && touchData.x <= getGlobalPos(hostPos).x + size.x
                && touchData.y >= getGlobalPos(hostPos).y && touchData.y <= getGlobalPos(hostPos).y + size.y)
        {
            touchData.markHandled();
            touchData.deactivate();
            return true;
        }
        return false;
    }

    public void setColor(Color c) { color = c; }

    public Vector2 getGlobalPos(Vector2 hostPos)
    {
        return new Vector2(pos.x + hostPos.x, pos.y + hostPos.y);
    }
}
