package com.teamkarbon.android.duo_run;


import com.badlogic.gdx.math.Vector2;

public class TouchData
{
    float x, y;
    boolean active;
    boolean isDragging;
    public TouchData()
    {
        active = false;
        isDragging = false;
        x = 0;
        y = 0;
    }
    public TouchData(float _x, float _y)
    {
        x = _x;
        y = _y;
        active = true;
    }
    public void set(float _x, float _y)
    {
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
}
