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
    NOTE: All shapes and virtual object fixtures have its x and y coordinates based on its origin.
        For example, if the shape's coordinates are (15, 30), its relative origin (0, 0), is at (15, 30),
        not necessarily (15, 30) being the corner of the shape.
        Origin for circles and boxes are at the centre, and origin for polygonshapes are always (0, 0) in the
        PolygonShape.set() function.
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
public class derptest extends ApplicationAdapter {
    gameMode mode; //A custom enum to manage multiple screens. (Game, main menu etc)

    ShapeRenderer shapeRenderer;//Draws basic shapes on screen based on the camera's position (Simulating a 2d infinite world)
    SpriteBatch batch;//Draws pictures, text etc
    Box2DDebugRenderer debugRenderer;//Visualises all entities and in-game physics objects (Fixtures, Bodies, forces etc)
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
    public void create() {

        //Random graphics init to allow alpha blending...
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        //Setting up the camera
        camera = new OrthographicCamera(pwidth(100), pheight(100));//Sets its rendering area to fill the whole screen.
        //Set camera position such that (0, 0) is the centre of the screen.
        camera.position.set(scale(camera.viewportWidth / 2f), scale(camera.viewportHeight / 2f), 0f);
        camera.update();//Make sure everything's ok :P

        //Create world

        world = new World(new Vector2(0, -9.8f), true);//Set gravity to 9.8 m/s^2 downwards
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        //Create text
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(com.badlogic.gdx.graphics.Color.LIGHT_GRAY);
        font.setScale(4);

        //Init ball classes
        //The meaning of each param
        //Ball: x = Centre
        //      y = + 30% above centre
        //      Exists in world
        //      radius: 10% height
        //Custom function "setFixture":
        //Density: 1kg/m^3
        //Restitution: 55% of Joules retained per collision
        //Friction: 30%
        ball = new Ball(pwidth(0), pheight(30), world, pheight(20));
        ball.setFixture(1f, 0.55f, 0.3f);
        ball2 = new Ball(scale(0), pheight(20), world, pheight(20));
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
        BodyDef tempBD;//Temporary body definition. This is similar to a Body, just without all the functions.
        //Usually used to define an actual body, although defining the actual body itself also works.
        tempBD = new BodyDef();
        tempBD.type = BodyDef.BodyType.StaticBody;//It doesn't move
        tempBD.position.set(scale(0), pheight(-50 + 1));//NOTE: The floor's origin is the centre of the box.
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
        tempBD.position.set(scale(0), pheight(50 - 1));
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


        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (x < Gdx.graphics.getWidth() / 2f)
                    Force = true;
                if (x >= Gdx.graphics.getWidth() / 2f)
                    Force2 = true;

                return true;
            }

            public boolean touchUp(int x, int y, int pointer, int button) {
                Force = false;
                Force2 = false;
                return true;
            }
        });

        //Init the obstacles arraylist
        obstacles = new ArrayList();
        obstaclesTimer = 0;//This makes sure that the obstacles are not too close to other obstacles

        //TODO: INFO: Debug!!! Remove when game functionality complete!
        mode = gameMode.GAME;
        level = 1;
    }

    @Override
    public void render() {
        //In game screen
        if (mode == gameMode.GAME) {
            if (Force)
                ball.body.applyForceToCenter(0, 50, true);
            if (Force2)
                ball2.body.applyForceToCenter(0, 50, true);

            //Constantly increase the balls' speed until a certain velocity
            if (ball.body.getPosition().x < 0)
                ball.body.applyForceToCenter(10, 0, true);
            if (ball2.body.getPosition().x < 0)
                ball2.body.applyForceToCenter(10, 0, true);
            if (ball.body.getPosition().x > 0)
                ball.body.applyForceToCenter(-10, 0, true);
            if (ball2.body.getPosition().x > 0)
                ball2.body.applyForceToCenter(-10, 0, true);

            //This sets the floor's position such that it follows the ball. At least it should :P
            theFloor.setTransform((ball.body.getPosition().x + ball2.body.getPosition().x) / 2, pheight(-50) + scale(1), 0);
            //Same for the ceiling
            theCeiling.setTransform(theFloor.getPosition().x, pheight(50) - scale(1), 0);

            Gdx.gl.glClearColor(0, 0.06f, 0.13f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            camera.update();//Duh

            debugRenderer.render(world, camera.combined);//View all colliders and stuff

            Gdx.gl.glEnable(GL20.GL_BLEND);//Allow for translucency (alpha blending) when shapes overlap

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.5f, 0.5f, 0f, 0.4f);
            shapeRenderer.circle(ball.body.getPosition().x, ball.body.getPosition().y, pheight(20), 45);
            shapeRenderer.setColor(0f, 0f, 1f, 0.4f);
            shapeRenderer.circle(ball2.body.getPosition().x, ball2.body.getPosition().y, pheight(20), 45);
            shapeRenderer.end();

            //Draw the floors and the ceiling
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1f);
            //NOTE: The origin of physical boxes are at the centre, but the origins of graphical boxes are at the bottom left corner.
            //Which is why the differing values are needed for pheight and pwidth.
            shapeRenderer.rect(theFloor.getPosition().x - camera.viewportWidth / 2f, theFloor.getPosition().y - pheight(1), camera.viewportWidth, pheight(2));
            shapeRenderer.rect(theCeiling.getPosition().x - camera.viewportWidth / 2f, theCeiling.getPosition().y + pheight(1), camera.viewportWidth, pheight(2));
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);


            batch.begin();
            //This is just for debug purposes. Actually its not even useful.. yet.
            font.draw(batch, "coord: " + descale(ball.body.getPosition().x) + ", " + descale(ball.body.getPosition().y) + ", Force: " + Force, 300, 200);
            batch.end();

            //Make dem obstacles
            if (obstaclesTimer > 3.5f - (level / 2f) && Math.random() >= 0.5) {
                PolygonShape temp = new PolygonShape();
                //the .set function assumes the vectors are in CCW direction
                //and also assumes that the origin of the object (it's relative [0, 0]) is actually (0, 0)

                float tempfloat = (float) Math.random();

                if (tempfloat < (1f / 4f)) {
                    //Vectors are in CCW direction!!!
                    //This makes either a sharp right angled triangle like a ramp on the floor,
                    //or a hook from the ceiling
                    temp.set(new Vector2[]{new Vector2(0, 0),
                            new Vector2(pwidth(10), 0),
                            new Vector2(pwidth(10), pheight(25))});//This makes a triangle like thingy
                    //origins of objects for box2d are at the centre.

                    boolean derp = (Math.random() < 0.5);//To spawn a blue collider or a yellow collider.

                    if (Math.random() < (1f / 3f))
                        obstacles.add(new Obstacle(temp, world, pwidth(70), pheight(-39f), derp));
                    else if (Math.random() < 0.5)
                        obstacles.add(new Obstacle(temp, world, pwidth(70), pheight(+39f - 25f), derp));
                    else {
                        temp.set(new Vector2[]{
                                new Vector2(0, 0),
                                new Vector2(pwidth(10), 0),
                                new Vector2(pwidth(10), pheight(-25))
                        });

                        obstacles.add(new Obstacle(temp, world, pwidth(70), pheight(+39f), derp));
                    }
                } else if (tempfloat < (2f / 4f)) {
                    //This makes a simple rectangle..(on the ceiling or the ground)
                    float x = pwidth(8 + (float) Math.random() * 16);//8% - 24% width
                    float y = pheight(5 + (float) Math.random() * 20);//5% - 25% height
                    temp.set(new Vector2[]{new Vector2(0, 0),
                            new Vector2(x, 0),
                            new Vector2(x, y),
                            new Vector2(0, y)});

                    boolean derp = (Math.random() < 0.5f);
                    if (Math.random() < 0.5)
                        obstacles.add(new Obstacle(temp, world, pwidth(70), pheight(-39f), derp));
                    else
                        obstacles.add(new Obstacle(temp, world, pwidth(70), pheight(+39f) - y, derp));
                } else if (tempfloat < (3f / 4f)) {
                    //This makes a trapezium. The base is always bigger than the cap
                    float val1 = pwidth(15 + (float) Math.random() * 10);//From 15% - 25% width
                    float val2 = val1 + pwidth(5 + (float) Math.random() * 16);//From 20% - 41% width
                    float tempheight = pheight(10 + (float) Math.random() * 13);//From 10% - 23% height

                    boolean derp = (Math.random() < 0.5);

                    if (Math.random() < 0.5) {
                        //Origin at bottom left, trapezium spawned at the bottom
                        temp.set(new Vector2[]{
                                new Vector2(0, 0),//Origin (Bottom left)
                                new Vector2(val2, 0),//Bottom right
                                new Vector2(val1 + (val2 - val1) / 2f, tempheight),//Top right
                                new Vector2((val2 - val1) / 2f, tempheight)//Top left
                        });

                        obstacles.add(new Obstacle(temp, world, pwidth(50) + scale(40), pheight(-39f), derp));
                    } else {
                        //Origin at top left, trapezium spawned at the top.
                        temp.set(new Vector2[]{
                                new Vector2(0, 0),//Origin (Top left)
                                new Vector2((val2 - val1) / 2f, -tempheight),//Bottom left
                                new Vector2(val1 + (val2 - val1) / 2f, -tempheight),//Bottom right
                                new Vector2(val2, 0)//Top right
                        });

                        obstacles.add(new Obstacle(temp, world, pwidth(70), pheight(39f), derp));

                    }
                } else {
                    //Make a random circle. Randomly
                    float ypos = pheight(((float) Math.random() * 78f) - 39f);
                    float rad = scale((float) Math.random() * 70);

                    temp.setRadius(rad);

                    obstacles.add(new Obstacle(temp, world, pwidth(70), ypos, false));
                }
            }

            //TODO: Render them.
            for (Object o : obstacles) {
                Obstacle temp = (Obstacle) o;

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                shapeRenderer.end();
            }

            obstaclesTimer += Gdx.graphics.getDeltaTime();
            world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        }
    }

    //So that "1" =  1 px
    //So scale(300) is 300px. Not very good tho...
    public float scale(float pixels) {
        return pixels / PixelsPerMeter;
    }

    //And the opposite. To convert pixels into meters.
    public float descale(float meters) {
        return meters * PixelsPerMeter;
    }

    //To use percentages of the screen instead of metric or pixel by pixel counting.
    public Vector2 percent(Vector2 percentofscreen)//This value is from 0 to 100
    {
        Vector2 temp = new Vector2();
        float screenSizeX = Gdx.graphics.getWidth();
        float screenSizeY = Gdx.graphics.getHeight();
        temp.x = scale((percentofscreen.x / 100f) * Gdx.graphics.getWidth());
        temp.y = scale((percentofscreen.y / 100f) * Gdx.graphics.getHeight());
        return temp;
    }

    //The percentage of screen width represented in meters.
    public float pwidth(float widthpercent) {
        return scale((widthpercent / 100f) * Gdx.graphics.getWidth());
    }

    //The percentage of screen height represented in meters.
    public float pheight(float heightpercent) {
        return scale((heightpercent / 100f) * Gdx.graphics.getHeight());
    }

    //A selection of current active states.
    public enum gameMode {
        MAIN_MENU, OPTIONS, ABOUT, GAME
    }
}