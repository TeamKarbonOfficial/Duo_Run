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

        if(boxType == BoxType.MODESELECT)
        {
            buttons = new ArrayList<CustomButton>();
            float count = 0;
            for(String s : options)
            {
                Vector2 tempPos = new Vector2();
                //tempPos is position of buttons where (0, 0) is the bottom left of the gui box
                tempPos.x = pwidth(10f + (count / (float)options.length) * 80f);
                tempPos.y = pheight(10f);
                Vector2 tempSize = new Vector2(pwidth(70f / (float)options.length), pheight(45f));

                buttons.add(new CustomButton(tempPos, tempSize, options[(int)count], invert(color).sub(0.1f, 0.1f, 0.1f, 0f)));
                Gdx.app.debug("Button Pos", buttons.get((int)count).pos.x + ", " + buttons.get((int)count).pos.y);
                count++;
            }
        }
    }

    public CustomButton DrawAndUpdate(BitmapFont font, TouchData touchData)
    {
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
                                                else tempButton = null;
            }
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
    private float pwidth(float percent)
    {
        return ((percent / 100f) * size.x);
    }

    private float pheight(float percent)
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
