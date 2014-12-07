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

    private float count;
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

        if(boxType == BoxType.MODESELECT)
        {
            buttons = new ArrayList<CustomButton>();
            count = 0;
            if(options.length <= 3) {//Single line for all buttons
                for (String s : options) {
                    //tempPos is position of buttons where (0, 0) is the bottom left of the gui box
                    tempPos.x = pwidth(10f + (count / (float) options.length) * 80f);
                    tempPos.y = pheight(10f);
                    tempSize.set(pwidth(70f / (float) options.length), pheight(45f));

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
                        tempPos.x = pwidth(10f + (count / 2) * 80f);
                        tempPos.y = pheight(50f);
                    }
                    else // The third and fourth below
                    {
                        tempPos.x = pwidth(10f + (count / 2f) * 80f);
                        tempPos.y = pheight(10f);
                    }
                    tempSize.set(pwidth(70f / 2), pheight(35f));

                    buttons.add(new CustomButton(tempPos, tempSize, options[(int) count], invert(color).sub(0.1f, 0.1f, 0.1f, 0f)));
                    Gdx.app.debug("Button Pos", buttons.get((int) count).pos.x + ", " + buttons.get((int) count).pos.y);
                    count++;
                }
            }
        }
        if(boxType == BoxType.CHECKBOX)
        {
            checkBoxes = new ArrayList<CheckBox>();
            count = 0;
            for(String s : options)
            {
                tempPos.x = pwidth(10f);
                tempPos.y = pheight(85f - (count * 15f));
                tempSize.set(pwidth(10f), pheight(10f));

                checkBoxes.add(new CheckBox(tempPos, tempSize, s, new Color(0.2f + (count * 0.1f), 0.2f + (count * 0.1f), 0.2f, 0.4f)));
            }
        }
    }

    public CustomButton DrawAndUpdate(BitmapFont font, TouchData touchData)
    {
        tempButton = null;//Set null to default.
        if(!batch.isDrawing()) batch.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.setColor(color);
        batch.draw(DialogPic, pos.x, pos.y, size.x, size.y);

        if(boxType == BoxType.MODESELECT)
        {
            /*
                options list are used as game modes in this BoxType
             */

            font.setScale(1.5f / (DialogMessage.length() / 10f));
            font.setColor(new Color(1f, 1f, 1f, 0.6f));//Just set it to white first :P
            font.draw(batch, DialogMessage, pos.x + pwidth(50f) - (font.getBounds(DialogMessage).width / 2f),
                    pos.y + size.y - pheight(10f));

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
            }
        }
        else if (boxType == BoxType.CHECKBOX)
        {

        }

        batch.end();
        return tempButton;
    }

    public void CheckOptionClicked(TouchData touchData)
    {

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
        return ((percent / 100f) * size.x);
    }

    public float pheight(float percent)
    {
        return ((percent / 100f) * size.y);
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
