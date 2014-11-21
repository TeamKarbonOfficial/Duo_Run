package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew on 21/11/2014.
 */
public class CustomDialogBox {
    SpriteBatch batch;
    Vector2 pos;
    Vector2 size;
    Texture DialogPic;

    public CustomDialogBox(SpriteBatch _batch, Vector2 _pos, Vector2 _size, Texture _DialogPic)
    {
        batch = _batch;
        pos = _pos;
        size = _size;
        DialogPic = _DialogPic;
    }

    public void Draw()
    {
        if(!batch.isDrawing()) batch.begin();

        batch.draw(DialogPic, pos.x, pos.y);

        batch.end();
    }

    public void Translate(Vector2 translation)
    {
        pos.x += translation.x;
        pos.y += translation.y;
    }
}
