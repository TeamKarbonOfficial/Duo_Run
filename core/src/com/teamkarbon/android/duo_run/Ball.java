package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

//Simplifies the creation of ball
public class Ball {
    float radius;

    World world;
    FixtureDef fixtureDef;
    BodyDef bodyDef;
    CircleShape shape;
    Body body;
    Fixture fixture;
    final float sides = 45;

    public Ball(float _x, float _y, World _world, float _radius){
        //Initialise classes
        bodyDef = new BodyDef();
        shape = new CircleShape();
        fixtureDef = new FixtureDef();

        world = _world;
        radius = _radius;

        //Init bodyDef
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(_x, _y);

        //Create the ball
        body = world.createBody(bodyDef);

        //init shape
        shape.setRadius(radius);

        //Init fixture
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
    }

    public void setFixture(float _d, float _r, float _f)//density, restitution, friction
    {
        fixture.setDensity(_d);
        fixture.setRestitution(_r);
        fixture.setFriction(_f);
    }

    public void setPos(float xpos, float ypos)
    {
        body.setTransform(xpos, ypos, 0);
    }

    public Vector2 getPos() { return body.getTransform().getPosition(); }//Make typing easier :P

    public float[] getVerticesAsFloatArray()
    {
        Vector2 v = new Vector2();
        float[] temp = new float[(int)sides * 2];
        for(int _sides = 0; _sides < sides; _sides ++)
        {
            v = polygonize((_sides / sides) * 360f, radius);
            temp[_sides * 2] = v.x + this.getPos().x;
            temp[_sides * 2 + 1] = v.y + this.getPos().y;
        }
        return temp;
    }

    public Vector2[] getVerticesAsVectors()
    {
        Vector2[] v = new Vector2[(int)sides];
        for(int _sides = 0; _sides < sides; _sides ++)
        {
            v[_sides] = polygonize((_sides / sides) * 360f, radius);
            v[_sides].x += this.getPos().x;
            v[_sides].y += this.getPos().y;
        }
        return v;
    }

    //The random function used to 'polygonize' a circle :P
    //Note _theta is in degrees :P
    public Vector2 polygonize(float _theta, float _radius)
    {
        Vector2 v = new Vector2();
        double theta = ((double) _theta) * Math.PI / 180.0;//Convert to radians
        double radius = (double) _radius;
        v.x = (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(radius * Math.sin(theta), 2));
        v.y = (float) (radius * Math.sin(theta));

        //Invert x when theta is 90 deg <-> 270 deg
        //Reason: ASTC rule. Because theta is anti-clockwise, -sin t = cos t.
        //Hence the S and T quadrants are negated :P
        //A-Math for the win.
        if (_theta > 90f && _theta < 270f)
            v.x *= -1;
        return v;
    }
}
