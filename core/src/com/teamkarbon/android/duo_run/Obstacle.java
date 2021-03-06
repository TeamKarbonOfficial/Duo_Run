package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Polygon shape, x, y positions, type true/type false
 */
public class Obstacle {
    PolygonShape shape;
    CircleShape cshape;
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    Body body;
    Fixture fixture;
    float [] localvertices;

    ObstacleColorType colorCollisionType;
    ObstacleType type;
    float radius;
    String id;
    float sides;//The number of sides the circleshape, if non-null, has.

    //Vars used when the obstacle is a button instead
    Color color;
    boolean isClicked = false;

    boolean passed = false;//When the score for this obs has been counted, this value will be true.

    public Obstacle(PolygonShape _shape, World world, float x, float y, ObstacleColorType _colorCollisionType)
    {
        //How to set shape: shape.set(new Vector2[]{new Vector2(3,4), new Vector2(0, 1)});
        //It is assumed that along the axis, 0 is the centre and the lateral
        //angle of the polygon is of the initialised shape along the same axes.
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        shape = _shape;
        colorCollisionType = _colorCollisionType;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);

        //To make sure the obstacles collide with the right objects
        Filter tempFilter = new Filter();

        if(colorCollisionType == ObstacleColorType.YELLOW)//collides with ball Yellow
        {
            tempFilter.maskBits = 1;
            tempFilter.categoryBits = 1;
            this.fixture.setFilterData(tempFilter);
        }
        else if(colorCollisionType == ObstacleColorType.BLUE)//collides with ball2 Blue
        {
            tempFilter.maskBits = 1;
            tempFilter.categoryBits = 2;
            this.fixture.setFilterData(tempFilter);
        }
        else if(colorCollisionType == ObstacleColorType.BOTH)
        {
            //TODO: ADD THIS COLLISION CLASS (Use White Colour)
        }

        //Set default fixture
        this.setFixture(0.4f, 1f, 0.8f);

        //Set color auto:
        //The one in render triangle is deprecated
        if (colorCollisionType == ObstacleColorType.YELLOW) this.color = new Color(0.4f, 0.4f, 0.2f, 0.45f);
        else if (colorCollisionType == ObstacleColorType.BLUE)  this.color = new Color(0, 0.3f, 1f, 0.45f);
        else this.color = new Color(1f, 1f, 1f, 0.45f);

        //Set local (size-only) vertices so that polygonize function doens't need to be called so many times...
        localvertices = this.getLocalVerticesAsFloatArray();
        sides = localvertices.length / 2f;//MUST SET!!!
    }
    public Obstacle(PolygonShape _shape, World world, float x, float y, ObstacleColorType _colorCollisionType, String _id)
    {
        //How to set shape: shape.set(new Vector2[]{new Vector2(3,4), new Vector2(0, 1)});
        //It is assumed that along the axis, 0 is the centre and the lateral
        //angle of the polygon is of the initialised shape along the same axes.
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        shape = _shape;
        colorCollisionType = _colorCollisionType;
        id = _id;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);

        //To make sure the obstacles collide with the right objects
        Filter tempFilter = new Filter();

        if(colorCollisionType == ObstacleColorType.YELLOW)//collides with ball
        {
            tempFilter.maskBits = 1;
            tempFilter.categoryBits = 1;
            this.fixture.setFilterData(tempFilter);
        }
        else if(colorCollisionType == ObstacleColorType.BLUE)//collides with ball2
        {
            tempFilter.maskBits = 1;
            tempFilter.categoryBits = 2;
            this.fixture.setFilterData(tempFilter);
        }
        else if(colorCollisionType == ObstacleColorType.BOTH)
        {
            //TODO:
        }

        //Set default fixture
        this.setFixture(0.4f, 1f, 0.8f);

        //Set color auto:
        //The one in render triangle is deprecated
        if (colorCollisionType == ObstacleColorType.YELLOW) this.color = new Color(0.4f, 0.4f, 0.2f, 0.45f);
        else if (colorCollisionType == ObstacleColorType.BLUE) this.color = new Color(0, 0.3f, 1f, 0.45f);
        else this.color = new Color(1f, 1f, 1f, 0.45f);

        //Set local (size-only) vertices so that polygonize function doens't need to be called so many times...
        localvertices = this.getLocalVerticesAsFloatArray();
        sides = localvertices.length / 2f;
    }
    public Obstacle(CircleShape _shape, World world, float x, float y, ObstacleColorType _colorCollisionType, float _radius, int _sides)
    {
        //How to set shape: shape.set(new Vector2[]{new Vector2(3,4), new Vector2(0, 1)});
        //It is assumed that along the axis, 0 is the centre and the lateral
        //angle of the polygon is of the initialised shape along the same axes.
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        cshape = _shape;
        colorCollisionType = _colorCollisionType;
        radius = _radius;
        sides = _sides;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        fixtureDef.shape = cshape;
        fixture = body.createFixture(fixtureDef);

        //To make sure the obstacles collide with the right objects
        Filter tempFilter = new Filter();

        if(colorCollisionType == ObstacleColorType.YELLOW)//collides with ball
        {
            tempFilter.maskBits = 1;
            tempFilter.categoryBits = 1;
        }
        else if(colorCollisionType == ObstacleColorType.BLUE)//collides with ball2
        {
            tempFilter.maskBits = 1;
            tempFilter.categoryBits = 2;
        }
        else
        {
            tempFilter.maskBits = 3;//11 & 01 && 11 = true.
            tempFilter.categoryBits = 3;//(01|10) & 11 && 11 = true.
        }
        this.fixture.setFilterData(tempFilter);

        //Set default fixture
        this.setFixture(0.4f, 1f, 0.8f);

        //Set color auto:
        //The one in render triangle is deprecated
        if (colorCollisionType == ObstacleColorType.YELLOW) this.color = new Color(0.4f, 0.4f, 0.2f, 0.45f);
        else if (colorCollisionType == ObstacleColorType.BLUE) this.color = new Color(0, 0.3f, 1f, 0.45f);
        else this.color = new Color(1f, 1f, 1f, 0.45f);

        //Set local (size-only) vertices so that polygonize function doens't need to be called so many times...
        localvertices = this.getLocalVerticesAsFloatArray();
    }

    public void setPos(float x, float y) { body.setTransform(x, y, 0); }

    public Vector2 getPos() { return body.getTransform().getPosition(); }//Make typing easier :P

    public void setColor(Color _color) {this.color = _color;}

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

    public void dispose(World world)
    {
        if(shape != null) shape.dispose();
        if(cshape != null) cshape.dispose();
        world.destroyBody(body);
    }

    public float[] getVerticesAsFloatArray()//Simplified... Yay!
    {
        float[] temp = new float[localvertices.length];
        for(int _sides = 0; _sides < sides; _sides ++)//FIXME: sides variable is 0!
        {
            temp[_sides * 2] = localvertices[_sides * 2] + this.getPos().x;
            temp[_sides * 2 + 1] = localvertices[_sides * 2 + 1] + this.getPos().y - 0.6f;
        }
        return temp;
    }

    public float[] getLocalVerticesAsFloatArray()
    {
        Vector2 v = new Vector2();
        if(this.shape != null) {//If its a polygon shape
            float[] temp = new float[this.shape.getVertexCount() * 2];

            for (int i = 0; i < this.shape.getVertexCount(); i++) {
                this.shape.getVertex(i, v);
                temp[i * 2] = v.x;
                temp[i * 2 + 1] = v.y;
            }

            return temp;
        }

        //If its a circle shape :D
        float[] temp = new float[(int)sides * 2];
        for(int _sides = 0; _sides < sides; _sides ++)
        {
            v = polygonize((_sides / sides) * 360f, radius);
            temp[_sides * 2] = v.x;
            temp[_sides * 2 + 1] = v.y;
        }
        return temp;
    }

    public Vector2[] getVerticesAsVectors()
    {
        if(this.shape != null) {
            Vector2[] v = new Vector2[this.shape.getVertexCount()];

            for (int i = 0; i < this.shape.getVertexCount(); i++) {
                this.shape.getVertex(i, v[i]);
                v[i].x += this.getPos().x;
                v[i].y += this.getPos().y;
            }

            return v;
        }

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

    public enum ObstacleColorType
    {
        YELLOW, BLUE, BOTH
    }

    public enum ObstacleType
    {
        NORMAL, BOUNCY, MOVING, BOOST, INSTA_DEATH
    }
}
