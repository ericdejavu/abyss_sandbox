package com.mygdx.game;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.handler.input.InputAdapterHandler;
import com.mygdx.game.handler.input.impl.ExitGameHandler;
import com.mygdx.game.handler.input.impl.MouseDraggedHandler;

public class MyGdxGame extends ApplicationAdapter {
	private final static int RAYS_PER_BALL = 128;
	private final static float LIGHT_DISTANCE = 5f;
	private final static float RADIUS = 1f;
	private final static int VELOCITY_ITERS = 6;
	private final static int POSITION_ITERS = 2;

	private final static int MAX_FPS = 30;
	private final static int MIN_FPS = 15;

	private SpriteBatch spriteBatch;
	private Texture tex;

	private OrthographicCamera camera;

	private World world;
	private Box2DDebugRenderer renderer;

	private Body body;
	private RayHandler rayHandler;

	private float physicsTimeLeft;

	@Override
	public void create () {
		float viewportWidth = Gdx.graphics.getWidth() / 32.0f;
		float viewportHeight = Gdx.graphics.getHeight() / 32.0f;

        InputAdapterHandler.GetInstance();
        InputAdapterHandler.addInputProcessor("quite", new ExitGameHandler());

		spriteBatch = new SpriteBatch();
		tex = new Texture("badlogic.jpg");
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		camera.position.set(0, viewportHeight / 2f, 0);
		camera.update();

		world = new World(new Vector2(0,0), true);
		renderer = new Box2DDebugRenderer();

        {
            float halfWidth = viewportWidth / 2f;
            ChainShape chainShape = new ChainShape();
            chainShape.createLoop(new Vector2[]{
                    new Vector2(-halfWidth, 0f),
                    new Vector2(halfWidth, 0f),
                    new Vector2(halfWidth, viewportHeight),
                    new Vector2(-halfWidth, viewportHeight)});
            BodyDef chainBodyDef = new BodyDef();
            chainBodyDef.type = BodyDef.BodyType.StaticBody;
            Body groundBody = world.createBody(chainBodyDef);
            groundBody.createFixture(chainShape, 0);
            chainShape.dispose();

            InputAdapterHandler.addInputProcessor("body_dragged", new MouseDraggedHandler(camera, world, groundBody));
        }

		{
			PolygonShape polygonShape = new PolygonShape();
			polygonShape.setAsBox(RADIUS, RADIUS);
			FixtureDef def = new FixtureDef();
			def.shape = polygonShape;
			def.density = 1f;
			BodyDef boxBodyDef = new BodyDef();
			boxBodyDef.type = BodyDef.BodyType.DynamicBody;
			boxBodyDef.position.x = 0;
			boxBodyDef.position.y = 5;
			body = world.createBody(boxBodyDef);
			body.createFixture(def);
			polygonShape.dispose();
		}

		RayHandler.setGammaCorrection(true);
		RayHandler.useDiffuseLight(true);

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0f, 0f, 0f, 0.5f);
		rayHandler.setBlurNum(3);

		Color weakColor = new Color(0.2f,0.2f,0.2f,1f);

        PointLight pointLight = new PointLight(rayHandler, RAYS_PER_BALL, weakColor, LIGHT_DISTANCE, 0f, 0f);
		pointLight.attachToBody(body,0,0);

        ConeLight coneLight1 = new ConeLight(rayHandler, RAYS_PER_BALL, null, LIGHT_DISTANCE * 4, 0, 0, 0f, 15f);
        coneLight1.attachToBody(body,0,0);

        ConeLight coneLight2 = new ConeLight(rayHandler, RAYS_PER_BALL, weakColor, LIGHT_DISTANCE * 4, 0, 0, 0f, 60f);
        coneLight2.attachToBody(body,0,0);
	}

	@Override
	public void render () {
		camera.update();

		rayHandler.setCombinedMatrix(camera);

		boolean stepped = step(Gdx.graphics.getDeltaTime());

        ScreenUtils.clear(0.3f, 0.3f, 0.3f, 1);

		float halfWidth = Gdx.graphics.getWidth() / 2f;
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.disableBlending();
		spriteBatch.begin();
		spriteBatch.draw(tex,  -halfWidth / 32.0f, 0, Gdx.graphics.getWidth() / 32.0f, Gdx.graphics.getHeight() / 32.0f);
		spriteBatch.enableBlending();
		spriteBatch.end();

		renderer.render(world, camera.combined);

		/** BOX2D LIGHT STUFF BEGIN */
		rayHandler.setCombinedMatrix(camera);

		if (stepped) rayHandler.update();
		rayHandler.render();
		/** BOX2D LIGHT STUFF END */
	}

	public boolean step(float deltaTime) {
		float TIME_STEP = 1f / Gdx.graphics.getFramesPerSecond();
		float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
		float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;

		physicsTimeLeft += deltaTime;
		if (physicsTimeLeft > MAX_TIME_PER_FRAME)
			physicsTimeLeft = MAX_TIME_PER_FRAME;

		boolean stepped = false;
		while (physicsTimeLeft >= TIME_STEP) {
			world.step(TIME_STEP, VELOCITY_ITERS, POSITION_ITERS);
			physicsTimeLeft -= TIME_STEP;
			stepped = true;
		}
		return stepped;
	}

	@Override
	public void dispose () {

	}
}
