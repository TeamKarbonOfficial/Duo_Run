package com.teamkarbon.android.duo_run;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;

//import com.badlogic.gdx.graphics.Texture;

/*
    #readme

    IMPORTANT: USE scale(float f) FUNCTION FOR ALL POSITIONS OF BODIES, AND SIZES
    NOTE: All shapes and virtual object fixtures have its x and y coordinates based on its origin.
        For example, if the shape's coordinates are (15, 30), its relative origin (0, 0), is at (15, 30),
        not necessarily (15, 30) being the corner of the shape.
        Origin for circles and boxes are at the centre, and origin for polygonshapes are always (0, 0) in the
        PolygonShape.set() function.

        Use BMFont program to create .fnt files to use in android/assets as good fonts!
        Set font size to 150, then scale down when using in the game!
        This prevents lousy text appearance!
        AngelCode/BMFont.exe
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


    TODO: READ THIS!!!!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    GAME DEVELOPMENT TERMS:
        Graphics:
            Translation:
                Global Translation (most common): The change of position of any in-game object based on the [x, y] axes of
                                                  the game world
                Local Translation               : The change of position of any in-game object based on the [x, y] axes of
                                                  the object itself. If the object is rotated 90 degrees right, moving "up"
                                                  is equivalent to moving "right" in the game world.
                Rotational Translation          : The change of quaternion of the object about its origin.
                                                  AKA: Rotation with the origin being the pivot :P
            Lateral Positioning:
                Vertex (plural Vertices)        : A Vector2 used to define a point in the game world along multiple axes
                Polygon                         : A set of Vertices in counter clockwise (CCW) order defining the corners
                                                : of a polygon.
            Game Clock:
                Delta Time                      : The amount of time taken between the current frame and the previous frame.


        #hashtags:
            readme, hashtags, start, init, game, render main, render triangle main, render text main,
            make obstacles, score, main menu, render menu, buttons, options, about

     */
    public class derptest extends ApplicationAdapter {

    //Everything Game Services
    public static AndroidMethods androidMethods;
    private final String GAMESERVICE = "Game Services";
    private final String APP_ID = "444744436262";
    private final String ACHIEVEMENT_GETTING_STARTED = "CgkIppTW5vgMEAIQAA";
    private final String LEADERBOARD_NORMAL = "CgkIppTW5vgMEAIQAQ";
    private final String LEADERBOARD_INSTADEATH = "CgkIppTW5vgMEAIQAg";

    gameMode mode; //A custom enum to manage multiple screens. (Game, main menu etc)

    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    World world;
    Body theFloor;
    Body theCeiling;
    Ball ball;
    Ball ball2;
    boolean overrideBallAutoPos, overrideBall2AutoPos;
    //Texture balltexture;
    //Texture ball2texture;
    Texture helpbuttontexture;
    BitmapFont smallfont;
    BitmapFont bigfont;
    Texture smallfonttexture;//These textures allow alpha filtering, which will then make the text look smoother :P
    Texture bigfonttexture;
    Skin skin;
    Stage stage;
    Label labelScore;

    Boolean Force = false;
    Boolean Force2 = false;
    int level;
    int score = 0;
    int pad;
    float lerp;//Linear interpolation (Cool animation when shifting between main menu -> game init -> game ;)
    boolean lerpFlag;
    float rawscore = 0;
    boolean instaDeathMode;
    boolean gameOver = false;
    //final String ballfile;
    //final String ball2file;

    ArrayList<Obstacle> obstacles;
    float obstaclesTimer;//in Seconds...
    float obstaclesRemovalTimer;
    boolean obstaclesRemoveFlag;

    public final float PixelsPerMeter = 50f;
    public float OUT_OF_BOUNDS_THRESHOLD;

    CustomGUIBox customGUIBox;
    Texture dialogBoxTexture;
    TouchData touchData;
    boolean backFlag = false;//A flag where set true within gameMode.GAME_INIT when the back button is clicked...
    boolean gameFlag = false;//A flag where set true within gameMode.GAME_INIT when the selected game mode is clicked...
    CustomButton tempButton;

    Texture splashScreen;//TODO: Make a logo/splash screen to display on init and perhaps other places when needed...

    //And here are all the psuedorandom variables concerning performance improvements so the new keyword doesn't have to be
    //called so many times..
    Polygon obs;
    Polygon playerLeft;
    Polygon playerRight;

    //Constructor for game services interface
    public derptest(AndroidMethods androidMethods) {
        super();
        derptest.androidMethods = androidMethods;
    }

    public derptest() {

    }

    @Override
    public void create() {

        androidMethods.startsignIn();

        //Load in ball's texture
        //Download textures from "http://teamkarbon.com/cloud/public.php?service=files&t=69d7aca788e27f04971fad1bd79a314c"
        //ballfile = "ball.png";
        //ball2file = "ball.png";
        //balltexture = new Texture(Gdx.files.internal(ballfile));
        //ball2texture = new Texture(Gdx.files.internal(ball2file));
        helpbuttontexture = new Texture(Gdx.files.internal("helpbutton.png"));
        dialogBoxTexture = new Texture(Gdx.files.internal("DialogBoxTexture200.png"));

        //Setting up the camera
        camera = new OrthographicCamera(pwidth(100), pheight(100));//Sets its rendering area to fill the whole screen.
        //Set camera position such that (0, 0) is the centre of the screen.
        camera.position.set(scale(camera.viewportWidth / 2f), scale(camera.viewportHeight / 2f), 0f);
        camera.update();//Make sure everything's ok :P

        //Create world
        world = new World(new Vector2(0, -11f), true);//Set gravity to 11 m/s^2 downwards
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        //Create text
        batch = new SpriteBatch();
        smallfonttexture = new Texture(Gdx.files.internal("rockwellsmall_0.png"), true);    //Get png to use in .fnt package
        smallfonttexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);//Set a midmap and linear alpha filter.
        bigfonttexture = new Texture(Gdx.files.internal("agencyFBbig_0.png"), true);        //which allows for descaling as well.
        bigfonttexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);

        //Creates the font using the .fnt position data and the filtered .png in _fonttexture.png
        smallfont = new BitmapFont(Gdx.files.internal("rockwellsmall.fnt"), new TextureRegion(smallfonttexture), false);
        bigfont = new BitmapFont(Gdx.files.internal("agencyFBbig.fnt"), new TextureRegion(bigfonttexture), false);

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
        ball = new Ball(pwidth(0), pheight(30), world, pheight(10));
        ball.setFixture(1f, 0.55f, 0.3f);
        ball2 = new Ball(scale(0), pheight(20), world, pheight(10));
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
        tempBD.position.set(0, pheight(50));
        theCeiling = world.createBody(tempBD);

        PolygonShape ceiling = new PolygonShape();
        ceiling.setAsBox(camera.viewportWidth, pheight(1f));
        Fixture ceilingFixture = theCeiling.createFixture(ceiling, 0f);

        //Ceil collision data
        tempFilter.maskBits = 3;
        tempFilter.maskBits = 3;
        ceilingFixture.setFilterData(tempFilter);

        debugRenderer = new Box2DDebugRenderer();

        touchData = new TouchData();

        Gdx.input.setInputProcessor(new InputAdapter() {
            //IMPORTANT: The Y - axis is 0 at the TOP by default.
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (x < descalepercent(50, 0).x)  Force = true;
                if (x >= descalepercent(50, 0).x) Force2 = true;

                touchData.set(x, Gdx.graphics.getHeight() - y);
                return true;
            }

            public boolean touchUp(int x, int y, int pointer, int button) {
                /* This might not be the best way though...*/
                if (x < descalepercent(50, 0).x)  Force = false;
                if (x >= descalepercent(50, 0).x) Force2 = false;

                touchData.deactivate();
                return true;
            }
        });

        lerp = 0;
        lerpFlag = false;

        //Init the obstacles ArrayList
        obstacles = new ArrayList<Obstacle>();
        obstaclesTimer = 0;//This makes sure that the obstacles are not too close to other obstacles
        obstaclesRemovalTimer = 0;
        obstaclesRemoveFlag = false;

        OUT_OF_BOUNDS_THRESHOLD = pwidth(-120f);

        obs = new Polygon();
        playerLeft = new Polygon();
        playerRight = new Polygon();

        overrideBallAutoPos = false;
        overrideBall2AutoPos = false;

        Gdx.gl.glClearColor(0, 0.06f, 0.13f, 0.8f);

        //TODO: INFO: Debug!!! Remove when game functionality complete!
        //#debug init
        mode = gameMode.MAIN_MENU_INIT;
        //mode = gameMode.SCORE_DISPLAY;
        //mode = gameMode.GAME;
        level = 1;
        instaDeathMode = true;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    @Override
    public void render() {
        //#prerender
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        camera.update();//Duh

        ProcessInput();

        debugRenderer.render(world, camera.combined);//View all colliders and stuff

        //Random graphics init to allow alpha blending...

        //This mode is for the init of "buttons" in the main menu.

        //Hashtags are for easier code jumping
        //#init
        if (mode == gameMode.MAIN_MENU_INIT) {

            obstacles.clear();//Makin' sure

            //Create a new obstacle with id "play"
            obstacles.add(new Obstacle(asBox(percent(15, 15)), world, pwidth(60f), pheight(33f), false, "play"));//Play the game
            obstacles.add(new Obstacle(asBox(percent(15, 15)), world, pwidth(100f), pheight(39f), true, "options"));//Go to options
            obstacles.add(new Obstacle(asBox(percent(15, 15)), world, pwidth(140f), pheight(30f), true, "stats"));//See all game service - related stuff
            obstacles.add(new Obstacle(asBox(percent(15, 15)), world, pwidth(96f), pheight(23f), false, "customize"));//Just an idea...
            mode = gameMode.MAIN_MENU;
        }

        //#game
        else if (mode == gameMode.GAME) {

            if(lerpFlag && gameOver)
            {
                lerp += Gdx.graphics.getDeltaTime() * 5f;
                Gdx.gl.glClearColor((float) Math.sin(lerp * Math.PI) / 2f, 0.06f, 0.13f, 0.8f);
            }
            //#render main
            DrawBall();

            DrawFloorsAndCeiling();

            ArrayList<RenderTriangle> triangles = new ArrayList<RenderTriangle>();

            //NOTE: Don't use for(object : array) type for loop as concurrent manipulations (deletions, in this case) to
            //      the array is taking place while iterating.
            for (int x = 0; x < obstacles.size(); x++) {

                Obstacle o = obstacles.get(x);

                if(!gameOver) o.translate(percent(-(7 + level) * Gdx.graphics.getDeltaTime(), 0f));//Move left (7 + level) % of screen per second..
                else o.translate(percent(-(7 + lerp) * Gdx.graphics.getDeltaTime(), 0f));

                //Remove at least 4 obstacles at a time
                if (!gameOver && o.getPos().x < OUT_OF_BOUNDS_THRESHOLD && obstaclesRemovalTimer > 13) {
                    obstacles.remove(o);
                    x--;
                    o.dispose(world);
                    obstaclesRemoveFlag = true;//This shows that a bulk removal has taken place.
                    continue;
                }
                else if (o.getPos().x < OUT_OF_BOUNDS_THRESHOLD) {
                    obstacles.remove(o);
                    x--;
                    o.dispose(world);
                    continue;
                }

                if((!o.type && !o.passed && o.getPos().x < ball.getPos().x) ||
                        (o.type && !o.passed && o.getPos().x < ball2.getPos().x) && !gameOver)
                {
                    o.passed = true;
                    Polygon temp = new Polygon();
                    temp.setVertices(o.getVerticesAsFloatArray());
                    rawscore += temp.area();
                }

                CreateRenderTriangles(o, triangles);
            }

            //Reset the timer knowing that a removal has taken place.
            if(!gameOver && obstaclesRemoveFlag) obstaclesRemovalTimer = 0;

            //#Score
            score = (int) rawscore;

            DrawAndUpdateRenderTriangles(triangles);

            //#render text main
            smallfont.setColor(Color.LIGHT_GRAY);
            smallfont.setScale(3);
            //debug!!!!
            batch.begin();
            smallfont.draw(batch, "dLevel: " + level, 300, 200);
            smallfont.setColor(Color.MAGENTA);
            smallfont.setScale(4);
            smallfont.draw(batch, "Score: " + score, 100, 100);
            batch.end();

            //Make dem obstacles

            //#make obstacles
            if (!gameOver && obstaclesTimer > 3.5f - (level / 2f) && Math.random() >= 0.5) {
                PolygonShape temp = new PolygonShape();

                float tempfloat = (float) Math.random();
                boolean derp = (Math.random() < 0.5f);
                if (tempfloat < (1f / 4f)) {

                    if (Math.random() < 0.5) {
                        temp.set(new Vector2[]{
                                percent(0, 0),
                                percent(13f, 0f),
                                percent(13f, 34f)
                        });

                        obstacles.add(Math.random() < 0.7 ? new Obstacle(temp, world, pwidth(70), pheight(-48f), derp)
                                :
                                new Obstacle(temp, world, pwidth(70), pheight(+49f - 34f), derp));
                    } else {
                        temp.set(new Vector2[]{
                                percent(0, 0),
                                percent(13f, -34f),
                                percent(13f, 0f)
                        });

                        obstacles.add(Math.random() < 0.7 ? new Obstacle(temp, world, pwidth(70), pheight(+49f), derp)
                                :
                                new Obstacle(temp, world, pwidth(70), pheight(-48f + 34f), derp));
                    }
                } else if (tempfloat < (2f / 4f)) {
                    //This makes a simple rectangle..(on the ceiling or the ground)
                    float x = pwidth(10 + (float) Math.random() * 18);//10% - 28% width
                    float y = pheight(8 + (float) Math.random() * 24);//8% - 32% height

                    temp.set(new Vector2[]{
                            new Vector2(0, 0),
                            new Vector2(x, 0),
                            new Vector2(x, y),
                            new Vector2(0, y)});

                    obstacles.add(Math.random() < 0.5 ? new Obstacle(temp, world, pwidth(70), pheight(-48f), derp)
                            :
                            new Obstacle(temp, world, pwidth(70), pheight(+49f) - y, derp));
                } else if (tempfloat < (3f / 4f)) {
                    //This makes a trapezium. The base is always bigger than the cap
                    float val1 = pwidth(18 + (float) Math.random() * 10);//From 18% - 28% width
                    float val2 = val1 + pwidth(5 + (float) Math.random() * 16);//From 23% - 49% width
                    float tempheight = pheight(15 + (float) Math.random() * 20);//From 15% - 35% height

                    if (Math.random() < 0.5) {
                        //Origin at bottom left, trapezium spawned at the bottom
                        temp.set(new Vector2[]{
                                new Vector2(0, 0),//Origin (Bottom left)
                                new Vector2(val2, 0),//Bottom right
                                new Vector2(val1 + (val2 - val1) / 2f, tempheight),//Top right
                                new Vector2((val2 - val1) / 2f, tempheight)//Top left
                        });

                        obstacles.add(new Obstacle(temp, world, pwidth(70), pheight(-48f), derp));
                    } else {
                        //Origin at top left, trapezium spawned at the top.
                        temp.set(new Vector2[]{
                                new Vector2(0, 0),//Origin (Top left)
                                new Vector2((val2 - val1) / 2f, -tempheight),//Bottom left
                                new Vector2(val1 + (val2 - val1) / 2f, -tempheight),//Bottom right
                                new Vector2(val2, 0)//Top right
                        });

                        obstacles.add(new Obstacle(temp, world, pwidth(70), pheight(49f), derp));

                    }
                } else {
                    //Make a random circle. Randomly
                    //Uses CircleShape cuz PolygonShape's circle function looks horrible and is super glitchy.
                    float ypos = pheight(((float) Math.random() * 99f) - 49f);
                    float rad = pheight(5 + (float) Math.random() * 12);//5% - 17% height

                    CircleShape cs = new CircleShape();
                    cs.setPosition(percent(0, 0));//The position is already stored in obstacle class
                    cs.setRadius(rad);

                    obstacles.add(new Obstacle(cs, world, pwidth(70), ypos, derp, rad, 25));
                }

                //Reset the obstacleTimer.
                obstaclesTimer = 0;
            }

            //#Game over :P
            //NOTE: This is not the last block of code in this gameMode. This is a flag setter to
            //      cue in the lerping of the obstacles out of the screen, and for the obstacles to
            //      stop spawning.
            if(!gameOver) {
                if (instaDeathMode) {
                    if (!inRange(ball.body.getPosition().x, pwidth(-1), pwidth(1), rangeMode.WITHIN) ||
                            !inRange(ball2.body.getPosition().x, pwidth(-1), pwidth(1), rangeMode.WITHIN)) {
                        Gdx.app.debug("instaDeathMode", "Game Over!");
                        gameOver = true;
                        lerp = 0;
                        if (androidMethods.isSignedIn()) {
                            androidMethods.submitScore(LEADERBOARD_INSTADEATH, Long.valueOf(score));
                        }
                    }
                } else if (!inRange(ball.body.getPosition().x, pwidth(-55), pwidth(55), rangeMode.WITHIN) ||
                        !inRange(ball2.body.getPosition().x, pwidth(-55), pwidth(55), rangeMode.WITHIN)) {
                    Gdx.app.debug("Normal mode", "Game over!");
                    gameOver = true;
                    lerp = 0;
                    if (androidMethods.isSignedIn()) {
                        androidMethods.submitScore(LEADERBOARD_NORMAL, Long.valueOf(score));
                    }
                }
            }

            //Check if all obs are out of screen alr, then move to score display
            //NOTE: This is the final block of code before switching to score display.
            //      as this is when there are no more obstacles on screen and all have been disposed.

            if(gameOver && obstacles.size() == 0)
            {
                lerp = 0;
                lerpFlag = true;
                gameOver = false;
                obstaclesRemovalTimer = 0f;
                obstaclesRemoveFlag = false;
                obstaclesTimer = 0f;
                mode = gameMode.SCORE_DISPLAY;

                customGUIBox = new CustomGUIBox(batch, String.valueOf(score), descalepercent(120f, 10f), descalepercent(70f, 70f), dialogBoxTexture, new String[]{
                        "Achievements", "Leaderboard", "Main Menu", "Play Again"
                }, new Color(0.1f, 0.4f, 0.1f, 0.5f), CustomGUIBox.BoxType.MODESELECT);

                if(!inRange(ball.getPos().x, pwidth(-60), pwidth(60), rangeMode.WITHIN_OR_EQUIVALENT))
                {
                    ball.setPos(pwidth(-60), pheight(0));
                }

                mode = gameMode.SCORE_DISPLAY;

                Gdx.gl.glClearColor(0, 0.06f, 0.13f, 0.8f);//Set back to normal clear color...
            }

            if(!gameOver) {
                obstaclesTimer += Gdx.graphics.getDeltaTime();
                obstaclesRemovalTimer += Gdx.graphics.getDeltaTime();
            }
        }

        //#score display
        else if (mode == gameMode.SCORE_DISPLAY) {

            DrawBall();

            DrawFloorsAndCeiling();


            if(lerpFlag){
                customGUIBox.Translate(descalepercent( -(10 + lerp) * Gdx.graphics.getDeltaTime(), 0));//Animate while lerping
                if(tempButton != null && tempButton.animateFlag)//If that button has been clicked.
                    tempButton.color.a = (float) (0.2f + (Math.sin((double) lerp * 2f) * 0.7f));
                lerp += Gdx.graphics.getDeltaTime() * 14f;

                if (tempButton != null && lerp > 14 && tempButton.animateFlag)//exit lerp; switch to game init.
                {
                    tempButton = null;//Clear this.

                    //Prep the Game Mode Select box.
                    customGUIBox = new CustomGUIBox(batch, "Game Mode", descalepercent(150, 30), descalepercent(80, 60),
                            dialogBoxTexture, new String[]{"Normal", "Insta-Death", "Back"},
                            new Color(0.5f, 0.3f, 0.3f, 1), CustomGUIBox.BoxType.MODESELECT);

                    //Switch~!
                    mode = gameMode.GAME_INIT;
                }
            }
            if(lerp > 31f || !lerpFlag)//Stop lerping
            {
                if(lerpFlag) {
                    androidMethods.showToastMessage("Score submitted!");//Show the score submitted notif here.
                }

                lerpFlag = false;

                if(lerp > -10)
                {
                    lerp -= Gdx.graphics.getDeltaTime() * 19f;
                    customGUIBox.Translate(descalepercent( -(10 + lerp) * Gdx.graphics.getDeltaTime(), 0));//Animate while lerping
                }
                else lerp = -10;
            }

            batch.begin();
            if(tempButton == null) tempButton = customGUIBox.DrawAndUpdate(bigfont, touchData);
            else customGUIBox.DrawAndUpdate(bigfont, touchData);

            bigfont.draw(batch, "touchpos: " + touchData.x + ", " + touchData.y, 190, 190);
            batch.end();

            //Once a CustomButton has been clicked, it will be stored as tempButton so that color
            //lerping can take place upon the clicked button
            if(tempButton != null) {
                if (tempButton.text.equals("Achievements")) {
                    androidMethods.showAchievements();//???
                    tempButton = null;
                }
                else if (tempButton.text.equals("Leaderboard")) {
                    if(instaDeathMode) androidMethods.showScores(LEADERBOARD_INSTADEATH);
                    else androidMethods.showScores(LEADERBOARD_NORMAL);//???
                }
                else if (tempButton.text.equals("Main Menu")) {
                    mode = gameMode.MAIN_MENU_INIT;//Ermmm.... I guess that's all?
                }
                else if (tempButton.text.equals("Play Again")) {
                    tempButton.setColor(new Color(0.8f, 0.8f, 0.8f, 0.9f));
                    tempButton.animateFlag = true;
                    lerp = 0f;
                    lerpFlag = true;
                }
            }
        }

        //#main menu
        else if (mode == gameMode.MAIN_MENU) {
            //#render menu
            DrawBall();

            DrawFloorsAndCeiling();

            ArrayList<RenderTriangle> triangles = new ArrayList<RenderTriangle>();


            //NOTE: Don't use for(object : array) type for loop as concurrent manipulations (deletions, in this case) to
            //      the array is taking place while iterating.

            for (int x = 0; x < obstacles.size(); x++) {

                Obstacle o = obstacles.get(x);

                o.translate(percent(-(7) * Gdx.graphics.getDeltaTime(), 0f));

                if (o.getPos().x < pwidth(-50f - 24f)) {
                    o.setPos(pwidth(50f + 24f), o.getPos().y);
                }

                CreateRenderTriangles(o, triangles);

                //#buttons

                //Get those Polygons ready..
                obs.setVertices(o.getVerticesAsFloatArray());
                playerLeft.setVertices(ball.getVerticesAsFloatArray());
                playerRight.setVertices(ball2.getVerticesAsFloatArray());

                //play button


                batch.begin();

                if (o.id.equals("play")) {

                    bigfont.setScale(3f);
                    bigfont.setColor(new Color(1, 1, 1, 1));
                    bigfont.draw(batch, "GO!", descale(o.getPos().x) + (Gdx.graphics.getWidth() / 2f) - (bigfont.getBounds("GO!").width / 2f),
                            descale(o.getPos().y) + (Gdx.graphics.getHeight() / 2f) + (bigfont.getBounds("GO!").height / 2f));

                    /*smallfont.setScale(1.5f);
                    smallfont.setColor(new Color(1, 1, 1, 1));//TODO: Remove this debug in the near future :P
                    smallfont.draw(batch, obs.getVertices()[0] + ", " + obs.getVertices()[1], 100, 400);
                    smallfont.draw(batch, playerLeft.getVertices()[0] + ", " + playerLeft.getVertices()[1], 100, 300);*/

                    if ((Intersector.overlapConvexPolygons(obs, playerLeft) && !o.type) ||
                            (Intersector.overlapConvexPolygons(obs, playerRight) && o.type)) {
                        mode = gameMode.GAME_INIT;
                        o.setColor(new Color(0.8f, 0.8f, 0.8f, 0.9f));
                        o.isClicked = true;
                        lerp = 0f;
                        lerpFlag = true;
                        customGUIBox = new CustomGUIBox(batch, "Game Mode", descalepercent(260, 30), descalepercent(80, 60),
                                dialogBoxTexture, new String[]{"Normal", "Insta-Death", "Back"},
                                new Color(0.5f, 0.3f, 0.3f, 1), CustomGUIBox.BoxType.MODESELECT);

                    }
                }
                else if(o.id == "options")
                {
                    bigfont.setScale(3f);

                    bigfont.setColor(new Color(1, 1, 1, 1));
                    bigfont.draw(batch, "Options", descale(o.getPos().x) + (Gdx.graphics.getWidth() / 2f) - (bigfont.getBounds("Options ").width / 2f),
                            descale(o.getPos().y) + (Gdx.graphics.getHeight() / 2f) + (bigfont.getBounds("Options").height / 2f));

                    if ((Intersector.overlapConvexPolygons(obs, playerLeft) && !o.type) ||
                            (Intersector.overlapConvexPolygons(obs, playerRight) && o.type)) {
                        mode = gameMode.OPTIONS;
                        Color c = new Color();
                        c.set(0.8f, 0.8f, 0.8f, 0.9f);
                        o.setColor(c);
                        o.isClicked = true;
                        lerp = 0f;
                        lerpFlag = true;
                        //TODO: Make a proper options GUIBox
                        String[] tempOptions = new String[]{"Vibrate"};
                        customGUIBox = new CustomGUIBox(batch, "Options", descalepercent(150, 30), descalepercent(80, 60),
                                dialogBoxTexture, tempOptions, new Color(0.5f, 0.3f, 0.3f, 1), CustomGUIBox.BoxType.CHECKBOX);
                    }
                }
                else if(o.id == "stats")
                {

                }
                else if(o.id == "customize")
                {

                }
                batch.end();
            }
            DrawAndUpdateRenderTriangles(triangles);

            triangles.clear();
        }

        //#game init
        else if (mode == gameMode.GAME_INIT) {

            if(lerp < 40.5f && lerpFlag) {
                lerp += Gdx.graphics.getDeltaTime() * 7f;//Increase speed of obstacles to make a "zooming" effect
                if(gameFlag && instaDeathMode) {//Make sure balls go back to the original spot
                    if(!inRange(ball.getPos().x, pwidth(-3), pwidth(3), rangeMode.WITHIN_OR_EQUIVALENT))
                    {
                        /*F = ma
                        * F = 1/2mv^2
                        * D = 1/2at^2
                        * T = 1s
                        * Given T, m and D and V, Find F such that when ball.x = 0, ball.velocity.x = 0. [5 marks]
                        * V = D/T
                        * D = 1/2a * 1
                        * F = 2d * m ~~ (1)
                        * F = 1/2mv^2 ~ (2)
                        * F = 2d * m - 1/2mv^2 (YAY!)
                       *                              2 *       d               *       m            -   1/2    m*/
                        ball.body.applyForceToCenter(2 * (0 - ball.getPos().x) * ball.body.getMass() - (1/2 * ball.body.getMass() *
                                //                         v                ^      2
                                ((float) Math.pow(ball.body.getLinearVelocity().x, 2))), 0, true);

                        overrideBallAutoPos = true;
                    }
                    else overrideBallAutoPos = false;

                    if(!inRange(ball2.getPos().x, pwidth(-3), pwidth(3), rangeMode.WITHIN_OR_EQUIVALENT))
                    {
                        ball2.body.applyForceToCenter(2 * (0 - ball.getPos().x) * ball.body.getMass() - (1/2 * ball.body.getMass() *
                                ((float) Math.pow(ball.body.getLinearVelocity().x, 2))), 0, true);

                        overrideBall2AutoPos = true;
                    }
                    else overrideBall2AutoPos = false;
                }
            }
            else {
                lerpFlag = false;

                if(backFlag && lerp < -1f)//Reaches same speed as obs moving in main menu, 7 pwidth / s leftwards
                {
                    backFlag = false;//Reset dem flags.
                    lerpFlag = true;
                    mode = gameMode.MAIN_MENU;//Transit to main menu!
                }
                if(gameFlag && lerp < -2f)//Same speed as obs moving in game, 6 pwidth / s
                {
                    gameFlag = false;
                    lerpFlag = true;
                    mode = gameMode.GAME;//Go to game!
                }

                if(lerp > -8) lerp -= Gdx.graphics.getDeltaTime() * 14f;//Decelerate until
                else          lerp = -8;//Stop...
            }
            //Accel: 2% width/s^2

            //Epic rendering

            //#render game init

            DrawBall();

            DrawFloorsAndCeiling();

            ArrayList<RenderTriangle> triangles = new ArrayList<RenderTriangle>();

            for (int x = 0; x < obstacles.size(); x++) {

                Obstacle o = obstacles.get(x);

                //either lerping or clicked on the back button
                if(lerpFlag || backFlag || gameFlag) o.translate(percent(-(8 + lerp) * Gdx.graphics.getDeltaTime(), 0f));

                else {
                    ClearAllObstacles(obstacles, world);
                    obstacles.remove(o);
                    x--;
                    continue;
                }

                CreateRenderTriangles(o, triangles);

                if(o.id.equals("play")) {
                    bigfont.setScale(3f);
                    bigfont.setColor(new Color(1, 1, 1, 1));
                    batch.begin();
                    bigfont.draw(batch, "GO!", descale(o.getPos().x) + (Gdx.graphics.getWidth() / 2f) - (bigfont.getBounds("GO!").width / 2f),
                            descale(o.getPos().y) + (Gdx.graphics.getHeight() / 2f) + (bigfont.getBounds("GO!").height / 2f));
                    batch.end();
                }

                //#render button
                //play button
                if (o.isClicked) {
                    //Pulsating alpha from 30% to 80%
                    o.color.a = (float) Math.sin((double) lerp * 4) / 4f + 0.4f;
                }
            }

            customGUIBox.Translate(new Vector2(descalepercent(-(8 + lerp) * Gdx.graphics.getDeltaTime(), 0f)));

            batch.begin();

            tempButton = customGUIBox.DrawAndUpdate(bigfont, touchData);//This function returns button clicked
            bigfont.draw(batch, "GUI pos: " + customGUIBox.pos.x + ", " + customGUIBox.pos.y, 60, 200);

            batch.end();
            for(CustomButton c : customGUIBox.buttons)
            {
                if(backFlag && c.text.equals("Back"))
                {
                    //Do that pulsating alpha thingy XD
                    c.color.a = (float) Math.sin((double) lerp * 4) / 4f + 0.4f;
                }
                else if(gameFlag && instaDeathMode && c.text.equals("Insta-Death"))
                {
                    c.color.a = (float) Math.sin((double) lerp * 4) / 4f + 0.4f;
                }
                else if(gameFlag && !instaDeathMode && c.text.equals("Normal"))
                {
                    c.color.a = (float) Math.sin((double) lerp * 4) / 4f + 0.4f;
                }
            }
            if(tempButton != null && !backFlag && !gameFlag)
            {
                Gdx.app.debug("gameModeSelect", tempButton.text);
                if(tempButton.text.equals("Normal"))
                {
                    //TODO: Fill up
                    gameFlag = true;
                    instaDeathMode = false;
                    lerpFlag = true;

                    ClearAllObstacles(obstacles, world);
                    obstacles.clear();

                    lerp += 10;
                }
                else if(tempButton.text.equals("Insta-Death"))
                {
                    gameFlag = true;
                    instaDeathMode = true;
                    lerpFlag = true;

                    ClearAllObstacles(obstacles, world);
                    obstacles.clear();

                    lerp += 10;
                }
                else if(tempButton.text.equals("Back"))
                {
                    ClearAllObstacles(obstacles, world);
                    obstacles.clear();

                    lerp += 0;//Reset lerp to 0 so that it will move gui out of screen and move obstacle into screen
                    PolygonShape temp = new PolygonShape();
                    temp.setAsBox(pwidth(20), pheight(20));

                    //Create a new obstacle with id "play"
                    Obstacle o = new Obstacle(temp, world, pwidth(150), pheight(48), false, "play");
                    obstacles.add(o);

                    backFlag = true;
                    lerpFlag = true;//Continue lerping again...
                }
            }

            DrawAndUpdateRenderTriangles(triangles);

        }
        //#options
        else if (mode == gameMode.OPTIONS) {

        }
        //#about
        else if (mode == gameMode.ABOUT) {

        }
        //#postrender

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.end();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    public PolygonShape asBox(Vector2 size) {
        PolygonShape temp = new PolygonShape();
        temp.setAsBox(size.x, size.y);
        return temp;
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
        temp.x = scale((percentofscreen.x / 100f) * Gdx.graphics.getWidth());
        temp.y = scale((percentofscreen.y / 100f) * Gdx.graphics.getHeight());
        return temp;
    }

    public Vector2 percent(float x, float y) {
        Vector2 temp = new Vector2();
        temp.x = scale((x / 100f) * Gdx.graphics.getWidth());
        temp.y = scale((y / 100f) * Gdx.graphics.getHeight());
        return temp;
    }

    public Vector2 descalepercent(float x, float y)
    {
        Vector2 temp = new Vector2();
        temp.x = (x / 100f) * Gdx.graphics.getWidth();
        temp.y = (y / 100f) * Gdx.graphics.getHeight();
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

    public boolean inRange(float val, float lower, float upper, rangeMode mode) {
        if (mode == rangeMode.WITHIN) {
            return (val > lower && val < upper);
        }
        //else if (mode == rangeMode.WITHIN_OR_EQUIVALENT)
        return (val >= lower && val <= upper);
    }

    public enum rangeMode {
        WITHIN, WITHIN_OR_EQUIVALENT
    }

    //A selection of current active states.
    public enum gameMode {
        MAIN_MENU, OPTIONS, ABOUT, GAME, MAIN_MENU_INIT, GAME_OVER, GAME_INIT, SCORE_DISPLAY
    }

    //#class rendertriangle
    public class RenderTriangle {
        float x1, y1, x2, y2, x3, y3;
        Color c;

        public RenderTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color _c) {
            this.x1 = x1;
            this.x2 = x2;
            this.x3 = x3;
            this.y1 = y1;
            this.y2 = y2;
            this.y3 = y3;

            c = _c;
        }

        public RenderTriangle(Vector2 _1, Vector2 _2, Vector2 _3, Color _c) {
            this.x1 = _1.x;
            this.x2 = _2.x;
            this.x3 = _3.x;
            this.y1 = _1.y;
            this.y2 = _2.y;
            this.y3 = _3.y;

            c = _c;
        }

        public RenderTriangle(Vector2[] x, Color _c) {
            if (x.length == 3) {
                x1 = x[0].x;
                x2 = x[1].x;
                x3 = x[2].x;
                y1 = x[0].y;
                y2 = x[1].y;
                y3 = x[2].y;
            }

            c = _c;
        }

        public void setColor(Color _c) {
            this.c = _c;
        }
    }


    @Override
    public void dispose() {
        // dispose of all the native resources
        //balltexture.dispose();
        //ball2texture.dispose();
        stage.dispose();
        batch.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    //#process input
    public void ProcessInput()
    {
        if (Force)
            ball.body.applyForceToCenter(0, 50, true);
        if (Force2)
            ball2.body.applyForceToCenter(0, 50, true);

        //Constantly increase the balls' speed until a certain velocity
        if (ball.body.getPosition().x < 0 && !overrideBallAutoPos)
            ball.body.applyForceToCenter(10, 0, true);
        if (ball2.body.getPosition().x < 0 && !overrideBall2AutoPos)
            ball2.body.applyForceToCenter(10, 0, true);

        if (ball.body.getPosition().x > 0 && !overrideBallAutoPos)
            ball.body.applyForceToCenter(-10, 0, true);
        if (ball2.body.getPosition().x > 0 && !overrideBall2AutoPos)
            ball2.body.applyForceToCenter(-10, 0, true);
    }

    //#draw ball
    public void DrawBall()
    {
        shapeRenderer.setColor(0.5f, 0.5f, 0f, 0.4f);
        shapeRenderer.circle(ball.body.getPosition().x, ball.body.getPosition().y, pheight(10), 45);
        shapeRenderer.setColor(0f, 0f, 1f, 0.4f);
        shapeRenderer.circle(ball2.body.getPosition().x, ball2.body.getPosition().y, pheight(10), 45);
    }

    //#draw floors and ceiling
    public void DrawFloorsAndCeiling()
    {
        //DrawAndUpdate the floors and the ceiling
        shapeRenderer.setColor(0.15f, 0.4f, 0.15f, 0.7f);

        //NOTE: The origin of physical boxes are at the centre, but the origins of graphical boxes are at the bottom left corner.
        //Which is why the differing values are needed for pheight and pwidth.
        shapeRenderer.rect(theFloor.getPosition().x - camera.viewportWidth / 2f, theFloor.getPosition().y - pheight(1),
                camera.viewportWidth + scale(50), pheight(2));
        shapeRenderer.rect(theCeiling.getPosition().x - camera.viewportWidth / 2f, theCeiling.getPosition().y - pheight(1),
                camera.viewportWidth + scale(50), pheight(2));
    }

    //#create RenderTriangles
    public void CreateRenderTriangles(Obstacle o, ArrayList<RenderTriangle> triangles)
    {
        if (o.cshape == null && o.shape.getVertexCount() == 3)//Triangle
        {
            Vector2[] vects = new Vector2[]{new Vector2(), new Vector2(), new Vector2()};
            o.shape.getVertex(0, vects[0]);
            o.shape.getVertex(1, vects[1]);
            o.shape.getVertex(2, vects[2]);

            for (Vector2 v : vects) {
                v.x += o.getPos().x;
                v.y += o.getPos().y;
            }

            triangles.add(new RenderTriangle(vects, o.color));
        } else if (o.cshape == null && o.shape.getVertexCount() == 4)//Trapezium/Box
        {
            Vector2[] vects = new Vector2[]{new Vector2(), new Vector2(), new Vector2()};
            Vector2[] vects2 = new Vector2[]{new Vector2(), new Vector2(), new Vector2()};

            o.shape.getVertex(0, vects[0]);
            o.shape.getVertex(1, vects[1]);
            o.shape.getVertex(2, vects[2]);

            o.shape.getVertex(0, vects2[0]);
            o.shape.getVertex(2, vects2[1]);
            o.shape.getVertex(3, vects2[2]);

            for (Vector2 v : vects) {
                v.x += o.getPos().x;
                v.y += o.getPos().y;
            }
            for (Vector2 v : vects2) {
                v.x += o.getPos().x;
                v.y += o.getPos().y;
            }

            triangles.add(new RenderTriangle(vects, o.color));
            triangles.add(new RenderTriangle(vects2, o.color));


        }
        else //circles
        {
            shapeRenderer.setColor(o.color);
            shapeRenderer.circle(o.getPos().x, o.getPos().y, o.radius, 25);
        }
    }

    //#DrawAndUpdate RenderTriangles
    public void DrawAndUpdateRenderTriangles(ArrayList<RenderTriangle> triangles)
    {
        for (int i = 0; i < triangles.size(); i++)
        {
            RenderTriangle r = triangles.get(i);
            shapeRenderer.setColor(r.c);
            shapeRenderer.triangle(r.x1, r.y1, r.x2, r.y2, r.x3, r.y3);
            //remove out of screen render triangles
            if (r.x1 < OUT_OF_BOUNDS_THRESHOLD) {
                triangles.remove(r);
                i--;
            }
        }
    }

    public void ClearAllObstacles(ArrayList<Obstacle> obs, World world)
    {
        for(Obstacle o : obs)
        {
            o.dispose(world);
        }
        obs.clear();
    }

    public void onBackPressed() {
        if(mode == gameMode.GAME_INIT) {
            
        } else if (mode == gameMode.GAME) {
            
        } else if (mode == gameMode.ABOUT) {

        } else if (mode == gameMode.GAME_OVER) {

        } else if (mode == gameMode.MAIN_MENU) {

        } else if (mode == gameMode.MAIN_MENU_INIT) {

        } else if (mode == gameMode.OPTIONS) {

        } else if (mode == gameMode.SCORE_DISPLAY) {

        } else {

        }
    }

    //For debug purposes.
    public String floatArrayToString(float[] floats)
    {
        String s = "";
        for(float f : floats)
        {
            s += String.valueOf(f);
            s += ", ";
        }
        s.trim();
        return s;
    }

    //Game Services Interface
    /* Usage: googleServies.<Method Below>;
    * Eg. googleServices.submitScore(score, LEADERBOARD_NORMAL);
    */
    public interface AndroidMethods {
        //Game Services
        public void startsignIn();
        public void signIn();
        public void signOut();
        public void submitScore(String id, long score);
        public void showScores(String id);
        public void showAchievements();
        //NOTE! This is for NORMAL Achievements
        public void submitNorAchievements(String id);
        //NOTE! This is for Incremental Achievements
        public void submitInAchievements(String id, int number);
        public boolean isSignedIn();
        public void onSignInSucceeded();

        //Ads
        public void showflAds();

        //Toast notifs
        public void showToastMessage(String txt);

        // Shared Preferences
        /**
         * Directions:
         * Once you have made changes to the values from prefputxxx,
         * Make sure you prefCommit(); or else it wouldn't save
         *
         * KeyName is the value you want to use to be recalled at prefgetxxx
         *
         * Also, you might want to preset some of the settings in AndroidLauncher,
         * because if nothing is set, it will return null, which might cause errors :P
         *
         * prefClear(); is for a setting reset.
         */
        public void prefputBoolean(String KeyName, boolean value);
        public void prefputString(String KeyName, String value);
        public void prefputInt(String KeyName, int value);
        public void prefputFloat(String KeyName, float value);
        public void prefputLong(String KeyName, long value);
        public boolean prefgetBoolean(String KeyName);
        public String prefgetString(String KeyName);
        public int prefgetInt(String KeyName);
        public float prefgetFloat(String KeyName);
        public long prefgetLong(String KeyName);
        public void prefClear();
        public void prefCommit();
    }
}