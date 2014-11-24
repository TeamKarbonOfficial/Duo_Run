package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class CustomGUIBox {
    /*
        Note: Size of DialogBoxTexture200.png is 200x200
     */

    SpriteBatch batch;
    Vector2 pos;
    Vector2 size;
    Texture DialogPic;
    ArrayList<String> options;
    Color color;
    String DialogMessage;

    Vector2 MessagePosition;


    public CustomGUIBox(SpriteBatch _batch, String _DialogMessage, Vector2 _pos, Vector2 _size, Texture _DialogPic,
                        ArrayList<String> _options, Color _color)
    {
        batch = _batch;
        pos = _pos;
        size = _size;
        DialogPic = _DialogPic;
        options = _options;
        color = _color;
        DialogMessage = _DialogMessage;
    }

    public void Draw()
    {
        if(!batch.isDrawing()) batch.begin();

        batch.draw(DialogPic, pos.x, pos.y, size.x, size.y);

        batch.end();
    }

    public void Translate(Vector2 translation)
    {
        pos.x += translation.x;
        pos.y += translation.y;
    }

    public enum DialogType
    {

    }
}
