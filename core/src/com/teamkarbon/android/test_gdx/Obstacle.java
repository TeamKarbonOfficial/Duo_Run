package com.teamkarbon.android.test_gdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Polygon shape, world, x, y positions, type true/type false
 */
public class Obstacle {
    World world;
    PolygonShape shape;
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    Body body;
    Fixture fixture;

    boolean type;

    public Obstacle(PolygonShape _shape, World _world, float x, float y, boolean _type)
    {
        //How to set shape: shape.set(new Vector2[]{new Vector2(3,4), new Vector2(0, 1)});
        //It is assumed that along the axis, 0 is the centre and the lateral
        //angle of the polygon is of the initialised shape along the same axes.
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        world = _world;
        shape = _shape;
        type = _type;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);

        //To make sure the obstacles collide with the right objects
        Filter tempFilter = new Filter();

        if(type == false)//collides with ball
        {
            tempFilter.maskBits = 1;
            tempFilter.categoryBits = 1;
            this.fixture.setFilterData(tempFilter);
        }
        else//collides with ball2
        {
            tempFilter.maskBits = 1;
            tempFilter.categoryBits = 2;
            this.fixture.setFilterData(tempFilter);
        }
    }

    public void setPos(float x, float y)
    {
        body.setTransform(x, y, 0);
    }

    public Vector2 getPos() { return body.getTransform().getPosition(); }//Make typing easier :P

    public Vector2 translate(float x, float y)
    {
        this.setPos(this.getPos().x + x, this.getPos().y + y);
        return(this.getPos());
    }

    public Vector2 translate(Vector2 val)
    {
        this.setPos(this.getPos().x + val.x, this.getPos().y + val.y);
        return(this.getPos());
    }

    public void setFixture(float _f, float _d, float _r)
    {
        fixture.setFriction(_f);
        fixture.setDensity(_d);
        fixture.setRestitution(_r);
    }
}
