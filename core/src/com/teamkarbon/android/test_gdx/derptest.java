package com.teamkarbon.android.test_gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class derptest extends ApplicationAdapter {
    ShapeRenderer shapeRenderer;
    Camera camera;
    World world;
    Body body;
    Body theFloor;
    Ball ball;
	
	@Override
	public void create () {
        //Create world
        world = new World(new Vector2(0, -98f), true);
        shapeRenderer.setProjectionMatrix(camera.combined);

        //Init ball class
        ball = new Ball(60, 30);

        //Define the type of body for the ball
        BodyDef tempBD = new BodyDef();
        tempBD.type = BodyDef.BodyType.DynamicBody;
        tempBD.position.set(ball.x, ball.y);

        //Create the actually body from body type
        body = world.createBody(tempBD);

        //Create a new rigidbody collider equivalent of Unity3D
        PolygonShape pshape = new PolygonShape();
        pshape.setRadius(30);

        //Define the physical properties of the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = pshape;
        fixtureDef.density = 1f;
        Fixture fixture = body.createFixture(fixtureDef);

        //Make the floor exist
        tempBD = new BodyDef();
        tempBD.type = BodyDef.BodyType.StaticBody;
        tempBD.position.set(0, 0);

        theFloor = world.createBody(tempBD);
        //Save some memory
        pshape.dispose();
	}

	@Override
	public void render () {
        world.step(Gdx.graphics.getDeltaTime(), 6, 3);

        ball.setPos(body.getPosition().x, body.getPosition().y);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 1, 1);
        shapeRenderer.circle(ball.x, ball.y, 30);
        shapeRenderer.end();
	}

    @Override
    public void dispose()
    {
        world.dispose();
    }

}
