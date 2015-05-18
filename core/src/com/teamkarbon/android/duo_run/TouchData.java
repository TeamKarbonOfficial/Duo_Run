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
    int pointerID;//Used to find out order of multi touches or just for tracking multiple touchDatas

    public TouchData()
    {
        active = false;
        isDragging = false;
        x = initialX = 0;
        y = initialY = 0;
    }
    public TouchData(float _x, float _y)
    {
        isDragging = false;
        x = initialX = _x;
        y = initialY = _y;
        active = true;
    }
    public void set(float _x, float _y)
    {
        //NOTE: Initial values can't be set!!!
        x = _x;
        y = _y;
        active = true;
    }
    public void deactivate()
    {
        active = false;
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
