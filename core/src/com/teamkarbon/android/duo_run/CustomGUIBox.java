package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/*
    A simple box used as a DialogBox, Level Select or a Game Mode select.
*/
public class CustomGUIBox {
    /*
        Note: Size of DialogBoxTexture200.png is 200x200
              0, 0 in the Box is the bottom left corner.
     */

    SpriteBatch batch;
    Vector2 pos;
    Vector2 size;
    Texture DialogPic;
    String[] options;
    Color color;
    String DialogMessage;
    BoxType boxType;
    TouchData touchData;

    Vector2 MessagePosition;
    ArrayList<CustomButton> buttons;

    private CustomButton tempButton;

    ArrayList<CheckBox> checkBoxes;
	ArrayList<CustomSlider> sliders;

    private float count;
    private float buttonsCount;
    private Vector2 tempPos;
    private Vector2 tempSize;

    public CustomGUIBox(SpriteBatch _batch, String _DialogMessage, Vector2 _pos, Vector2 _size, Texture _DialogPic,
                        String[] _options, Color _color, BoxType _boxType)
    {
        batch = _batch;
        pos = _pos;
        size = _size;
        DialogPic = _DialogPic;
        options = _options;
        color = _color;
        DialogMessage = _DialogMessage;
        boxType = _boxType;
        tempButton = null;

        count = 0;
        tempPos = new Vector2();
        tempSize = new Vector2();

        buttons = new ArrayList<CustomButton>();
        sliders = new ArrayList<CustomSlider>();

        if(boxType == BoxType.MODESELECT)
        {
            count = 0;
            if(options.length <= 3) {//Single line for all buttons
                for (String s : options) {
                    //tempPos is position of buttons where (0, 0) is the bottom left of the gui box
                    tempPos = new Vector2();
                    tempPos.x = pwidth(10f + (80 * (count / (float) options.length)));
                    tempPos.y = pheight(10f);
                    tempSize.set(pwidth(80f / (float) options.length), pheight(45f));

                    buttons.add(new CustomButton(tempPos, tempSize, options[(int) count], invert(color).sub(0.1f, 0.1f, 0.1f, 0f)));
                    Gdx.app.debug("Button Pos", buttons.get((int) count).pos.x + ", " + buttons.get((int) count).pos.y);
                    count++;
                }
            }
            else if(options.length == 4)//2 x 2
            {
                for(String s : options) {
                    Vector2 tempPos = new Vector2();
                    if(count < 2) { // first and second boxes above
                        tempPos.x = pwidth(10f + (count / 2f) * 80f);
                        tempPos.y = pheight(50f);
                    }
                    else // The third and fourth below
                    {
                        tempPos.x = pwidth(10f + ((count - 2f) / 2f) * 80f);
                        tempPos.y = pheight(10f);
                    }
                    tempSize.set(pwidth(70f / 2), pheight(35f));

                    buttons.add(new CustomButton(tempPos, tempSize, options[(int) count], invert(color).sub(0.1f, 0.1f, 0.1f, 0f)));
                    //Gdx.app.debug("Button Pos", buttons.get((int) count).pos.x + ", " + buttons.get((int) count).pos.y);
                    count++;
                }
            }

            buttonsCount = options.length;
        }
        if(boxType == BoxType.CHECKBOX)
        {
            checkBoxes = new ArrayList<CheckBox>();
            count = 0;
            buttonsCount = 0;
            for(String s : options)
            {
                tempPos.x = pwidth(10f);
                tempPos.y = pheight(90f - (count * 15f));
                tempSize.set(pwidth(10f), pheight(10f));

                checkBoxes.add(new CheckBox(tempPos, tempSize, s, new Color(0.2f + (count * 0.1f), 0.2f + (count * 0.1f), 0.2f, 0.6f)));
            }
        }
    }

    public CustomButton DrawAndUpdate(BitmapFont font, TouchData touchData)
    {
        tempButton = null;//Set null to default.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.setColor(color);
        batch.draw(DialogPic, pos.x, pos.y, size.x, size.y);

        if(boxType == BoxType.MODESELECT)
        {
            /*
                options list are used as game modes in this BoxType
             */

            //Set font size
            if(604f / font.getBounds(DialogMessage.trim()).width < 10f)
                font.setScale(604f / font.getBounds(DialogMessage.trim()).width);
            else
                font.setScale(10f);

            //Check if the message ends with a colon, then offset the drawn header to the left so it looks more aesthetically pleasing :P
            if(String.valueOf(DialogMessage.trim().charAt(DialogMessage.trim().length() - 1)).equals(":"))
            {
                font.setColor(new Color(1f, 1f, 1f, 0.6f));//Just set it to white first :P
                font.draw(batch, DialogMessage, pos.x + pwidth(10f),
                        pos.y + size.y - pheight(10f));
            } else {
                font.setColor(new Color(1f, 1f, 1f, 0.6f));//Just set it to white first :P
                font.draw(batch, DialogMessage, pos.x + pwidth(50f) - (font.getBounds(DialogMessage).width / 2f),
                        pos.y + size.y - pheight(10f));
            }

            for(CustomButton b : buttons)
            {
                batch.setColor(b.color);
                batch.draw(DialogPic, b.getGlobalPos(this.pos).x, b.getGlobalPos(this.pos).y, b.size.x, b.size.y);
                //draws the text at the centre
                font.setScale(1.4f);
                font.draw(batch, b.text, b.getGlobalPos(this.pos).x + (b.size.x / 2f) - (font.getBounds(b.text).width / 2f),
                        b.getGlobalPos(this.pos).y + (b.size.y / 2f) - (font.getBounds(b.text).height / 2f));
                //Single touch capabilities for now...
                if(b.isClicked(touchData, this.pos)) tempButton = b;
                Gdx.app.debug("BPOS", b.getGlobalPos(this.pos).x + ", " + b.getGlobalPos(this.pos).y + "::" + b.text);
            }

            return tempButton;
        }
        else if (boxType == BoxType.CHECKBOX) {

            if (400f / font.getBounds(DialogMessage.trim()).width < 8f)
                font.setScale(400f / font.getBounds(DialogMessage.trim()).width);
            else
                font.setScale(8f);

            //Check if the message ends with a colon, then offset the drawn header to the left so it looks more aesthetically pleasing :P
            if (String.valueOf(DialogMessage.trim().charAt(DialogMessage.trim().length() - 1)).equals(":")) {
                font.setColor(new Color(1f, 1f, 1f, 0.6f));//Just set it to white first :P
                font.draw(batch, DialogMessage, pos.x + pwidth(10f),
                        pos.y + size.y - pheight(5f));
            } else {
                font.setColor(new Color(1f, 1f, 1f, 0.6f));//Just set it to white first :P
                font.draw(batch, DialogMessage, pos.x + pwidth(50f) - (font.getBounds(DialogMessage).width / 2f),
                        pos.y + size.y - pheight(5f));
            }

            for (CheckBox c : checkBoxes) {
                //Draw the check box
                batch.setColor(c.color);

                font.setScale(1.4f);
                batch.draw(DialogPic, c.getGlobalBoxPos(this, font).x, c.getGlobalBoxPos(this, font).y,
                        c.size.x, c.size.y);

                //Draw the text
                font.draw(batch, c.text, c.getGlobalPos(this.pos).x, c.getGlobalPos(this.pos).y);

                //Check if clicked
                if(c.isClicked(touchData, this, font))
                {
                    c.flip();
                }
            }

            //TODO: Allow for this mode to have buttons as well. (The back button is still needed)
            for(CustomButton b : buttons)
            {
                batch.setColor(b.color);
                batch.draw(DialogPic, b.getGlobalPos(this.pos).x, b.getGlobalPos(this.pos).y, b.size.x, b.size.y);

                //draws the text at the centre
                font.setScale(1.4f);
                font.draw(batch, b.text, b.getGlobalPos(this.pos).x + (b.size.x / 2f) - (font.getBounds(b.text).width / 2f),
                        b.getGlobalPos(this.pos).y + (b.size.y / 2f) + (font.getBounds(b.text).height / 2f));

                //Single touch capabilities for now...
                if(b.isClicked(touchData, this.pos)) tempButton = b;
            }
			
			for(CustomSlider s : sliders)
			{
                font.setScale(1.4f);

                //Draw slider bar
                Vector2 temp = s.getGlobalBottomLeftPos(this.pos);
				font.draw(batch, s.text, temp.x - pwidth(4), temp.y + pheight(20));
                batch.draw(s.sliderBarPic, temp.x, temp.y, s.size.x, s.size.y);

                //Draw slider button
                temp = s.getGlobalSliderPos(this.pos);
                Vector2 temp2 = s.getSliderButtonSize();
                batch.draw(s.sliderButtonPic, temp.x, temp.y, temp2.x, temp2.y);

                //Draw slider percentage
                temp = s.getGlobalBottomRightPos(this.pos);
                font.draw(batch, String.valueOf(s.getPercent()).concat("%"), temp.x, temp.y + pheight(10));

                //Debug
                temp = sliders.get(0).getGlobalSliderPos(this.pos);
                font.draw(batch, "tpos: " + touchData.x + ", " + touchData.y + "; sp1: " + temp.x + ", " + temp.y +
                        ", drag: " + touchData.isDragging + ", act: " + touchData.active, 100, 100);

                //Check if slider clicked
                if(s.isSliderClicked(touchData, this.pos))
                {
                    s.moveSlider(touchData.asVector2(), this.pos);//No need for percent; these are raw values...
                }
			}

            return tempButton;
        }

        return null;
    }

    public ArrayList<CheckBox> getCheckBoxes()
    {
        return checkBoxes;
    }

    //TODO: Make sure this works
    public void addButton(String buttonText)
    {
        buttonsCount ++;
        if(boxType == BoxType.CHECKBOX) {
            buttons.add(new CustomButton(percent(10f + (buttonsCount - 1f) * 40f, 10f), percent(20f, 20f), buttonText, invert(this.color)));
        }
    }
	
	public void addSlider(String sliderText, Texture _sliderBarPic, Texture _sliderButtonPic, float yPos)
	{
        //TODO: Make sure this works
		sliders.add(new CustomSlider(percent(10, yPos), percent(70, 12), sliderText, Color.WHITE, 50,_sliderBarPic, _sliderButtonPic));
	}

    public CustomSlider getSlider(String sliderText)
    {
        for(CustomSlider s : sliders)
        {
            if(s.text.equals(sliderText))
                return s;
        }
        return null;
    }

    public void Translate(Vector2 translation)
    {
        pos.x += translation.x;
        pos.y += translation.y;
    }

    public enum BoxType
    {
        NORMAL, CHECKBOX, MODESELECT
    }

    //This set of pwidth and pheight is regarding the local size.x and size.y values
    public float pwidth(float percent)
    {
        return ((percent / 100f) * this.size.x);
    }

    public float pheight(float percent)
    {
        return ((percent / 100f) * this.size.y);
    }

    public Vector2 percent(float x, float y)
    {
        return new Vector2(pwidth(x), pheight(y));
    }

    private Color invert(Color c){
        Color temp = new Color();
        temp.a = c.a;
        temp.r = 1f - c.r;
        temp.g = 1f - c.g;
        temp.b = 1f - c.b;
        return temp;
    }
}
