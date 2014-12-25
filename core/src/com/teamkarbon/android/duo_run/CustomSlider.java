package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew on 24/12/2014
 */
public class CustomSlider {
	//Left/right pos is the range of the center of the slidable
	//NOTE: All values are in pixels...
    Vector2 pos, size, leftPos, rightPos;//Note: pos value is pos of relative to the host CustomGUIBox.
	Vector2 sliderPos;//Origin of slider button is centre!
	int sliderPercent;
    String text;
    Color color;
    boolean animateFlag;

	//Length: length of the slider leftPos + length = rightPos
    public CustomSlider(Vector2 leftmost, float length, Vector2 _size, String _text, Color _color, float _sliderPercent)
    {
        pos = new Vector2((leftmost.x + length) / 2, leftmost.y);
		leftPos = leftmost;
		rightPos = leftmost.add(length, 0);
        size = _size;
        text = _text;
        color = _color;
		sliderPercent = _sliderPercent
		sliderPos = new Vector2(leftPos.x + (sliderPercent / 100f * length), leftPos.y);
        animateFlag = false;
    }

    public boolean isSliderClicked(TouchData touchData, Vector2 hostPos)
    {
		float tempX = getGlobalSliderPos(hostPos).x;
		float tempY = getGlobalSliderPos(hostPos).y;
        if(touchData.active && touchData.x >= tempX && touchData.x <= tempX + size.x
		   && touchData.y >= tempY && touchData.y <= tempY + size.y)
        {
            return true;
        }
        return false;
    }
	
	public void moveSlider(Vector2 _sliderPos)
	{
		sliderPos = _sliderPos;
		sliderPercent = ((sliderPos.x - leftPos.x) / length * 100f);
	}
	
	public void moveSlider(float _sliderPercent)
	{
		sliderPercent = _sliderPercent;
		sliderPos.x = leftPos.x + (sliderPercent / 100f * length);
	}

    public void setColor(Color c) { color = c; }

    public Vector2 getGlobalPos(Vector2 hostPos)
    {
        return new Vector2(pos.x + hostPos.x, pos.y + hostPos.y);
    }
	
	public Vector2 getGlobalSliderPos(Vector2 hostPos)
	{
		return new Vector2(leftPos.x + hostPos.x + sliderPos.x, leftPos.y + hostPos.y);
	}
}