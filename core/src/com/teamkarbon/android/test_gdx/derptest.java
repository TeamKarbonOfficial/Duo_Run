package com.teamkarbon.android.test_gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/*
    IMPORTANT: USE scale(float f) FUNCTION FOR ALL POSITIONS OF BODIES, AND SIZES
 */
public class derptest extends ApplicationAdapter{
    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    World world;
    Body theFloor;
    Ball ball;
    Ball ball2;
    BitmapFont font;

    Boolean Force = false;
    Boolean Force2 = false;

    public final float PixelsPerMeter = 50f;
	
	@Override
	public void create () {

        camera = new OrthographicCamera(scale(Gdx.graphics.getWidth()), scale(Gdx.graphics.getHeight()));
        camera.position.set(scale(camera.viewportWidth / 2f), scale(camera.viewportHeight / 2f), 0f);
        camera.update();

        //Create world

        world = new World(new Vector2(0, -9.8f), true);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        //Create text
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(com.badlogic.gdx.graphics.Color.LIGHT_GRAY);
        font.setScale(4);

        //Init ball classes
        ball = new Ball(scale(130), scale(370), world, scale(90));
        ball.setFixture(1f, 0.55f, 0.3f);
        ball2 = new Ball(scale(130), scale(370), world, scale(90));
        ball.setFixture(1f, 0.55f, 0.3f);


        //Make the floor exist
        BodyDef tempBD;
        tempBD = new BodyDef();
        tempBD.type = BodyDef.BodyType.StaticBody;
        tempBD.position.set(scale(0), scale(-300));
        theFloor = world.createBody(tempBD);

        //Floor bounds
        PolygonShape floor = new PolygonShape();
        floor.setAsBox(camera.viewportWidth * 2, scale(10));
        theFloor.createFixture(floor, 0f);

        debugRenderer = new Box2DDebugRenderer();

        //Set input processor
        /*
        inputProcessor = new CustomInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
        */
        //OR
        Gdx.input.setInputProcessor(new InputAdapter(){
            public boolean touchDown (int x, int y, int pointer, int button) {
                // your touch down code here
                if(x < Gdx.graphics.getWidth() / 2f)
                    Force = true;
                if(x >= Gdx.graphics.getWidth() / 2f)
                    Force2 = true;

                return true;
            }

            public boolean touchUp (int x, int y, int pointer, int button) {
                // your touch up code here

                Force = false;

                Force2 = false; //Does this work?? o.O

                return true;
            }
        });
	}

	@Override
	public void render () {

        if(Force)
            ball.body.applyForceToCenter(0, 50, true);
        if(Force2)
            ball2.body.applyForceToCenter(0, 50, true);

		Gdx.gl.glClearColor(0, 0.06f, 0.13f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        debugRenderer.render(world, camera.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.5f, 0.5f, 0f, 0.4f);
        shapeRenderer.circle(ball.body.getPosition().x, ball.body.getPosition().y, scale(90), 45);
        shapeRenderer.setColor(0f, 0f, 1f, 0.4f);
        shapeRenderer.circle(ball2.body.getPosition().x, ball2.body.getPosition().y, scale(90), 45);
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "coord: " + descale(ball.body.getPosition().x) + ", " + descale(ball.body.getPosition().y) + ", Force: " + Force, 300, 200);
        batch.end();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
	}

    public float scale(float pixels)
    {
        return pixels / PixelsPerMeter;
    }

    public float descale(float meters)
    {
        return meters * PixelsPerMeter;
    }
}
