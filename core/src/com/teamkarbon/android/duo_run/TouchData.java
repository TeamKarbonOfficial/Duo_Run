package com.teamkarbon.android.duo_run;


import com.badlogic.gdx.math.Vector2;

public class TouchData
{
    float x, y;
    float initialX, initialY;
    boolean active;//True on touchDown, false when isDragging or on touchUp.
    boolean isDragging;//When true, do not delete from touchList when active is false!
                       // Used to make sure the correct Force is set to false in the event
                       // of a drag from one end to the other of the screen.
                       // Use initialX and initialY to check origin of the TouchData.

    boolean handled;//To make sure that this touchData has been handled before it's called inactive in a concurrent unsynchronized lousy thread :P
    int pointerID;//Used to find out order of multi touches or just for tracking multiple touchDatas

    public TouchData()
    {
        active = false;
        isDragging = false;
        x = initialX = 0;
        y = initialY = 0;
        handled = false;
    }
    public TouchData(float _x, float _y)
    {
        active = true;
        isDragging = false;
        x = initialX = _x;
        y = initialY = _y;
        handled = false;
    }
    public TouchData(TouchData touchData)
    {
        active = touchData.active;
        isDragging = touchData.isDragging;
        x = initialX = touchData.x;
        y = initialY = touchData.y;
        handled = touchData.handled;
    }

    public void set(float _x, float _y)
    {
        //NOTE: Initial values can't be set!!!
        x = _x;
        y = _y;
        active = true;
        handled = false;
    }
    public void deactivate()
    {
        /*if(handled)*/ active = false;
    }
    public void markHandled()
    {
        handled = true;
    }

    public Vector2 asVector2()
    {
        return new Vector2(x, y);
    }

    public Vector2 initialAsVector2()
    {
        return new Vector2(initialX, initialY);
    }
}
