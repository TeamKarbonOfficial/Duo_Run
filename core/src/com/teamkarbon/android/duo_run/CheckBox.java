package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Matthew on 7/12/2014.
 */
public class CheckBox {
    //Note: size value is the size of the check box only, not including the text..
    Vector2 pos, size;//Note: pos value is relative to the host CustomGUIBox, and is the pos of the bottom left of the text.
    String text;
    Color color;//Color when depressed.
    boolean fingerOff;//Only reset in CustomGUIBox.resetTouchListForGUI()
    boolean isChecked;

    public CheckBox(Vector2 _pos, Vector2 _size, String _text, Color _color, boolean _isChecked)
    {
        pos = _pos;
        size = _size;
        text = _text;
        color = _color;
        isChecked = _isChecked;
        fingerOff = true;
    }

    //Default unchecked init.
    public CheckBox(Vector2 _pos, Vector2 _size, String _text, Color _color)
    {
        pos = _pos;
        size = _size;
        text = _text;
        color = _color;
        isChecked = false;
        fingerOff = true;
    }
    
    //host: the host CustomGUIBox
    //font: the font used by the host
    public boolean isClicked(ArrayList<TouchData> touchList, TouchData touchData, CustomGUIBox host, BitmapFont font)
    {
        if(touchData.active && touchData.x >= getGlobalBoxPos(host, font).x && touchData.x <= getGlobalBoxPos(host, font).x + size.x
                            && touchData.y >= getGlobalBoxPos(host, font).y && touchData.y <= getGlobalBoxPos(host, font).y + size.y
                            && fingerOff)
        {
            touchData.markHandled();
            touchData.deactivate();
            touchData.isDragging = false;

            fingerOff = false;

            return true;
        }
        return false;
    }

    public boolean flip()
    {
        isChecked = !isChecked;

        //Flip colours as well...
        if(isChecked) color = color.add(0, 0.3f, 0, 0);
        else          color = color.sub(0, 0.3f, 0, 0);

        return isChecked;
    }

    public Vector2 getGlobalPos(Vector2 hostPos)
    {
        return new Vector2(pos.x + hostPos.x, pos.y + hostPos.y);
    }

    public void setFingerOff()
    {
        fingerOff = true;
    }

    //This Vector2 value is the place to draw the clickable check box.
    public Vector2 getGlobalBoxPos(CustomGUIBox guiBoxHost, BitmapFont font)
    {
        return new Vector2( this.getGlobalPos(guiBoxHost.pos) ).add(font.getBounds(text).width + guiBoxHost.pwidth(3f), -guiBoxHost.pheight(8f));
    }
}
