package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
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

        if(boxType == BoxType.MODESELECT)
        {
            buttons = new ArrayList<CustomButton>();
            int count = 0;
            for(String s : options)
            {
                Vector2 tempPos = pos;
                tempPos.add(pwidth(10f + (count / options.length) * 80f), pheight(40f));
                Vector2 tempSize = new Vector2(pwidth(90f / options.length), pheight(45f));

                buttons.add(new CustomButton(tempPos, tempSize, options[count], invert(color).sub(0.1f, 0.1f, 0.1f, 0f)));
                batch.setColor(invert(color).sub(0.1f, 0.1f, 0.1f, 0f));
                batch.draw(DialogPic, pos.x + pwidth(10f + (count / options.length) * 80f),
                        pos.y + pheight(40f), pwidth(90f / options.length), pheight(45f));
                count++;
            }
        }
    }

    public void Draw(BitmapFont font)
    {
        if(!batch.isDrawing()) batch.begin();

        batch.setColor(color);
        batch.draw(DialogPic, pos.x, pos.y, size.x, size.y);

        if(boxType == BoxType.MODESELECT)
        {
            /*
                options list are used as game modes in this BoxType
             */

            font.setScale(1.2f);
            font.setColor(new Color(1f, 1f, 1f, 0.6f));//Just set it to white first :P
            font.draw(batch, DialogMessage, pos.x + pwidth(50f) - (font.getBounds(DialogMessage).width / 2f), pos.y + pheight(10f));

            float count = 0;
            for(String s : options)
            {
                batch.setColor(invert(color).sub(0.1f, 0.1f, 0.1f, 0f));
                batch.draw(DialogPic, pos.x + pwidth(10f + (count / options.length) * 80f),
                        pos.y + pheight(40f), pwidth(90f / options.length), pheight(45f));
                count++;
            }
        }

        batch.end();
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
        return (percent / 100) * size.x;
    }

    private float pheight(float percent)
    {
        return (percent / 100) * size.y;
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
