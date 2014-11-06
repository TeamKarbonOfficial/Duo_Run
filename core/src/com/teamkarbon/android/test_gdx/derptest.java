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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/*
    IMPORTANT: USE scale(float f) FUNCTION FOR ALL POSITIONS OF BODIES, AND SIZES
    FIXME: IMPORTANT !!!~~~UTILIZE SCREEN PERCENTAGE FUNCTIONS IN ORDER TO FIT ALL SCREEN SIZES~~~!!!
 */

/*
    Game Flow: Main Menu -> Options -> difficulty: <rate of difficulty increase> easy, medium, hard
                                    -> sound: on, off
                                    -> vibrate: on, off
                                    -> colours: blue, yellow, green, red, grey
                         -> About: <Show the about screen>
                         -> Game <Endless mode> -> {level} will increase as the game proceeds
                                                -> spawning rate of obstacles will increase
                                                -> more challenging obstacles
                                                -> Game over immediately once a ball gets out of the screen...
                         -> Game <Story mode> -> Perhaps in a new update

    Check out newsapps/beeswithmachineguns on GitHub! A cool DDoS python app!

 */
public class derptest extends ApplicationAdapter{
    gameMode mode;

    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    World world;
    Body theFloor;
    Body theCeiling;
    Ball ball;
    Ball ball2;
    BitmapFont font;

    Boolean Force = false;
    Boolean Force2 = false;

    ArrayList obstacles;
    float obstaclesTimer;//in Seconds...

    int level;

    public final float PixelsPerMeter = 50f;
	
	@Override
	public void create () {

        //Random graphics init to allow alpha blending...
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        //Setting up the camera
        camera = new OrthographicCamera(pwidth(100), pheight(100));
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
        ball = new Ball(pwidth(0), pheight(50), world, scale(90));
        ball.setFixture(1f, 0.55f, 0.3f);
        ball2 = new Ball(scale(0), scale(370), world, scale(90));
        ball2.setFixture(1f, 0.55f, 0.3f);

        //Set collision filtering (so the two balls don't collide with each other
        //It's a complex thing, read up here: http://www.box2d.org/manual.html under
        //chapter 6.2 "Fixtures -> Filtering"
        Filter tempFilter = new Filter();
        tempFilter.categoryBits = 1;//Binary 0001
        tempFilter.maskBits = 1;//Binary 0001
        ball.fixture.setFilterData(tempFilter);

        tempFilter.maskBits = 2;//Binary 0010
        tempFilter.categoryBits = 1;//Binary 0001
        ball2.fixture.setFilterData(tempFilter);

        //catBits1 & maskBits2 = 0001 & 0010 = 0. Hence ball and ball2 won't collide
        //if ( (catBits1 & maskBits2) && (catBits2 & maskBits1) ) collision = true;

        //Make the floor exist
        BodyDef tempBD;
        tempBD = new BodyDef();
        tempBD.type = BodyDef.BodyType.StaticBody;
        tempBD.position.set(scale(0), pheight(-40));
        theFloor = world.createBody(tempBD);

        //Floor bounds
        PolygonShape floor = new PolygonShape();
        floor.setAsBox(camera.viewportWidth, pheight(1f));
        Fixture floorFixture = theFloor.createFixture(floor, 0f);

        //Set Floor collision data
        tempFilter.maskBits = 3;//Binary 0011
        tempFilter.categoryBits = 3;//Binary 0011
        floorFixture.setFilterData(tempFilter);

        //The ceiling
        tempBD = new BodyDef();
        tempBD.type = BodyDef.BodyType.StaticBody;
        tempBD.position.set(scale(0), pheight(40));
        theCeiling = world.createBody(tempBD);

        //Ceiling bounds
        PolygonShape ceiling = new PolygonShape();
        ceiling.setAsBox(camera.viewportWidth, pheight(1f));
        Fixture ceilingFixture = theCeiling.createFixture(ceiling, 0f);

        //Ceil collision data
        tempFilter.maskBits = 3;
        tempFilter.maskBits = 3;
        ceilingFixture.setFilterData(tempFilter);

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

                Force2 = false;

                return true;
            }
        });

        //Init the obstacles arraylist
        obstacles = new ArrayList();
        obstaclesTimer = 0;

        //TODO: INFO: Debug!!!
        mode = gameMode.GAME;
        level = 1;
	}

	@Override
	public void render () {
        if(mode == gameMode.GAME) {
            if (Force)
                ball.body.applyForceToCenter(0, 50, true);
            if (Force2)
                ball2.body.applyForceToCenter(0, 50, true);

            //Constantly increase the balls' speed until a certain velocity
            if(ball.body.getPosition().x < 0)
                ball.body.applyForceToCenter(10, 0, true);
            if(ball2.body.getPosition().x < 0)
                ball2.body.applyForceToCenter(10, 0, true);
            if(ball.body.getPosition().x > 0)
                ball.body.applyForceToCenter(-10, 0, true);
            if(ball2.body.getPosition().x > 0)
                ball2.body.applyForceToCenter(-10, 0, true);


            theFloor.setTransform((ball.body.getPosition().x + ball2.body.getPosition().x) / 2, pheight(-40), 0);

            Gdx.gl.glClearColor(0, 0.06f, 0.13f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            camera.update();

            debugRenderer.render(world, camera.combined);

            Gdx.gl.glEnable(GL20.GL_BLEND);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.5f, 0.5f, 0f, 0.4f);
            shapeRenderer.circle(ball.body.getPosition().x, ball.body.getPosition().y, scale(90), 45);
            shapeRenderer.setColor(0f, 0f, 1f, 0.4f);
            shapeRenderer.circle(ball2.body.getPosition().x, ball2.body.getPosition().y, scale(90), 45);
            shapeRenderer.end();

            //Draw the floors and the ceiling
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1f);
            shapeRenderer.rect(theFloor.getPosition().x - camera.viewportWidth / 2f, pheight(-41), camera.viewportWidth, pheight(2));
            shapeRenderer.rect(theCeiling.getPosition().x - camera.viewportWidth / 2f, pheight(39), camera.viewportWidth, pheight(2));
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);


            batch.begin();
            font.draw(batch, "coord: " + descale(ball.body.getPosition().x) + ", " + descale(ball.body.getPosition().y) + ", Force: " + Force, 300, 200);
            batch.end();

            //Make dem obstacles
            if(obstaclesTimer > 3.5f / level && Math.random() >= 0.5)
            {
                PolygonShape temp = new PolygonShape();
                //the .set function assumes the vectors are in CCW direction
                //and also assumes that the origin of the object (it's relative [0, 0]) is actually (0, 0)

                float tempfloat = (float) Math.random();

                if(tempfloat < (1f / 4f)) {
                    //Vectors are in CCW direction!!!
                    //This makes either a sharp right angled triangle like a ramp on the floor,
                    //or a hook from the ceiling
                    temp.set(new Vector2[]{new Vector2(0, 0),
                            new Vector2(scale(80), 0),
                            new Vector2(scale(80), pheight(30))});//This makes a triangle like thingy
                                                             //origins of objects for box2d are at the centre.

                    boolean derp = (Math.random() < 0.5);//To spawn a blue collider or a yellow collider.

                    if(Math.random() < 0.5)
                        obstacles.add(new Obstacle(temp, world, pwidth(50) + scale(40), pheight(-39f), derp));
                    else
                        obstacles.add(new Obstacle(temp, world, pwidth(50) + scale(40), pheight(+39f - 30f), derp));
                }
                else if(tempfloat < (2f / 4f))
                {
                    //This makes a simple rectangle..(on the ceiling or the ground)
                    float rndval = scale(60 + (float) Math.random() * 40);
                    temp.set(new Vector2[]{new Vector2(0, 0),
                            new Vector2(rndval, 0),
                            new Vector2(rndval, pheight(28)),
                            new Vector2(0, pheight(28))});
                    if(Math.random() < 0.5)
                        obstacles.add(new Obstacle(temp, world, pwidth(50) + scale(40), pheight(- 39f), false));
                    else
                        obstacles.add(new Obstacle(temp, world, pwidth(50) + scale(40), pheight(+ 39f - 28), false));
                }
                else if(tempfloat < (3f / 4f))
                {
                    //This makes a trapezium. The base is always bigger than the cap
                    float val1 = scale(70 + (float) Math.random() * 40);//From 70 - 110 px
                    float val2 = val1 + scale(120 + (float) Math.random() * 50);//From 190 - 280 px
                    float tempheight = scale(30 + (float) Math.random() * 60);
                    temp.set(new Vector2[]{
                            new Vector2(0, 0),//Origin (Bottom left)
                            new Vector2(val2, 0),//Bottom right
                            new Vector2(val1 + (val2 - val1) / 2f, tempheight),//Top right
                            new Vector2((val2 - val1) / 2f, tempheight)//Top left
                    });
                }
                else
                {
                    //Make sth else... I guess?
                    ;
                }
            }

            obstaclesTimer += Gdx.graphics.getDeltaTime();
            world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        }
	}

    public float scale(float pixels)
    {
        return pixels / PixelsPerMeter;
    }

    public float descale(float meters)
    {
        return meters * PixelsPerMeter;
    }

    //Exact value rep for percent of screen
    public Vector2 percent(Vector2 percentofscreen)//This value is from 0 to 100
    {
        Vector2 temp = new Vector2();
        float screenSizeX = Gdx.graphics.getWidth();
        float screenSizeY = Gdx.graphics.getHeight();
        temp.x = scale((percentofscreen.x / 100f) * Gdx.graphics.getWidth());
        temp.y = scale((percentofscreen.y / 100f) * Gdx.graphics.getHeight());
        return temp;
    }

    public float pwidth(float widthpercent)
    {
        return scale((widthpercent / 100f) * Gdx.graphics.getWidth());
    }

    public float pheight(float heightpercent)
    {
        return scale((heightpercent / 100f) * Gdx.graphics.getHeight());
    }

    public enum gameMode
    {
        MAIN_MENU, OPTIONS, ABOUT, GAME
    }
}