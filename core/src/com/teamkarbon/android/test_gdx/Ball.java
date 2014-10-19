package com.teamkarbon.android.test_gdx;

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
}
