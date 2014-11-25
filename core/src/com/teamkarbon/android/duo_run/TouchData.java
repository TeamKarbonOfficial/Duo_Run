package com.teamkarbon.android.duo_run;


public class TouchData
{
    float x, y;
    boolean active;
    public TouchData()
    {
        active = false;
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
}
