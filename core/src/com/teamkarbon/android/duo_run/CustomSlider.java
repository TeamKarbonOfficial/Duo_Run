package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew on 24/12/2014
 */
public class CustomSlider {
	//Left/right pos is the range of the center of the slidable
	//NOTE: All values are in pixels...
    Vector2 pos, size, leftPos, rightPos;//Note: pos value is pos of relative to the host CustomGUIBox.
	Vector2 sliderPos;//Origin of slider button is bottom left. Slider pos is relative to the slider BAR, not the GUI BOX
    Texture sliderBarPic;
    Texture sliderButtonPic;
	float sliderPercent;
    float value;
    float min;
    float max;
    String text;
    Color color;
    boolean animateFlag;

	//Length: length of the slider leftPos + length = rightPos
    public CustomSlider(Vector2 leftmost, Vector2 _size, String _text, Color _color, float _sliderPercent, Texture _sliderBarPic, Texture _sliderButtonPic)
    {
        leftPos = leftmost;
        size = _size;
        pos = new Vector2((leftmost.x + size.x) / 2, leftmost.y);
		rightPos = new Vector2(leftmost.x, leftmost.y).add(size.x, 0);
        text = _text;
        color = _color;
		sliderPercent = _sliderPercent;
		sliderPos = new Vector2(leftPos.x + (sliderPercent / 100f * size.x), leftPos.y);
        animateFlag = false;
        min = 0;
        max = 100;
        value = min + (max * sliderPercent / 100f);
        sliderBarPic = _sliderBarPic;
        sliderButtonPic = _sliderButtonPic;
    }

    public CustomSlider(Vector2 leftmost, Vector2 _size, String _text, Color _color, float _sliderPercent, float _min, float _max, Texture _sliderBarPic, Texture _sliderButtonPic)
    {
        leftPos = leftmost;
        size = _size;
        pos = new Vector2((leftmost.x + size.x) / 2, leftmost.y);
        rightPos = new Vector2(leftmost).add(size.x, 0);
        text = _text;
        color = _color;
        sliderPercent = _sliderPercent;
        sliderPos = new Vector2(leftPos.x + (sliderPercent / 100f * size.x), leftPos.y);
        animateFlag = false;
        min = _min;
        max = _max;
        value = min + (max * sliderPercent / 100f);
        sliderBarPic = _sliderBarPic;
        sliderButtonPic = _sliderButtonPic;
    }

    public boolean isSliderClicked(TouchData touchData, Vector2 hostPos)
    {
		float tempXmin = getGlobalBottomLeftPos(hostPos).x;
        float tempXmax = getGlobalBottomRightPos(hostPos).x;
		float tempY = getGlobalSliderPos(hostPos).y;
        if((touchData.active || touchData.isDragging) && touchData.x >= tempXmin && touchData.x <= tempXmax
		   && touchData.y >= tempY && touchData.y <= tempY + size.y)
        {
            return true;
        }
        return false;
    }
	
	public void moveSlider(Vector2 rawPos, Vector2 hostPos)
	{
		sliderPos.x = rawPos.x - (getSliderButtonSize().x / 2f) - leftPos.x - hostPos.x;
		sliderPercent = sliderPos.x * 100f / (size.x - getSliderButtonSize().x);

        //Do some clamping
        if(sliderPercent > 100f) moveSlider(100f);
        if(sliderPercent < 0f) moveSlider(0f);
	}
	
	public void moveSlider(float _sliderPercent)
	{
        //Do some clamping
        if(_sliderPercent > 100f) _sliderPercent = 100f;
        if(_sliderPercent < 0f) _sliderPercent = 0f;

		sliderPercent = _sliderPercent;
		sliderPos.x = (sliderPercent / 100f * (size.x - getSliderButtonSize().x));
	}

    public float getPercent()
    {
        return sliderPercent;
    }

    public float getValue()
    {
        return value;
    }

    public Vector2 getSliderButtonSize()
    {
        return new Vector2(size.x / 10f, size.y);
    }

    public void setColor(Color c) { color = c; }

    public Vector2 getGlobalPos(Vector2 hostPos)
    {
        return new Vector2(pos.x + hostPos.x, pos.y + hostPos.y);
    }

    public Vector2 getGlobalBottomLeftPos(Vector2 hostPos)
    {
        return new Vector2(leftPos.x + hostPos.x, leftPos.y + hostPos.y);
    }

    public Vector2 getGlobalBottomRightPos(Vector2 hostPos)
    {
        return new Vector2(rightPos.x + hostPos.x, rightPos.y + hostPos.y);
    }
	
	public Vector2 getGlobalSliderPos(Vector2 hostPos)
	{
		return new Vector2(leftPos.x + hostPos.x + sliderPos.x, leftPos.y + hostPos.y);
	}
}
