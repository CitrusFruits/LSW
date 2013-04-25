package com.me.mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.xml.internal.stream.Entity;

public class Test implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	//private Sprite sprite;
	
	World world;
	Box2DDebugRenderer debugRenderer;
	ArrayList<Fixture> fixtures;
	
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("textures/firework_core.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);
		
		world = new World(new Vector2(0, 0), false);
		
		fixtures = new ArrayList<Fixture>();
		// Create a circle shape and set its radius to 6
		float circle_size = .01f;
		CircleShape circle = new CircleShape();
		circle.setRadius(circle_size);
		
		float size = 150;
		float angle = 0;
		float angle_inc = (float)(2*Math.PI/size);
		float radius = 0.05f;
		float speed = 0.1f;
		for(int i = 0; i < size; i++)
		{
			// First we create a body definition
			BodyDef bodyDef = new BodyDef();
			// We set our body to dynamic, for something like ground which doesnt move we would set it to StaticBody
			bodyDef.type = BodyType.DynamicBody;
			// Set our body's starting position in the world
			float x = (float)Math.sin(angle);
			float y = (float)Math.cos(angle);
			bodyDef.position.set(x*radius, y*radius);

			// Create our body in the world using our body definition
			Body body = world.createBody(bodyDef);
			body.setLinearVelocity(new Vector2(-x*speed, -y*speed));
			
			// Create a fixture definition to apply our shape to
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = circle;
			fixtureDef.density = 0.5f; 
			fixtureDef.friction = 0.4f;
			fixtureDef.isSensor = false;

			fixtureDef.restitution = 0.6f; // Make it bounce a little bit
			
			Sprite sprite = new Sprite(region);
			sprite.setSize(circle_size, circle_size * sprite.getHeight() / sprite.getWidth());
			sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
			
			Fixture fixture = body.createFixture(fixtureDef);
			fixture.setUserData(sprite);
			
			// Create our fixture and attach it to the body
			fixtures.add(fixture);
			angle += angle_inc;
			
			
		}

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
		
		//debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Fixture f: fixtures)
		{
			Sprite e = (Sprite) f.getUserData();

		    if (e != null) {
		        // Update the entities/sprites position and angle
		        e.setPosition(f.getBody().getPosition().x, f.getBody().getPosition().y);
		        // We need to convert our angle from radians to degrees
		        e.setRotation(MathUtils.radiansToDegrees * f.getBody().getAngle());
		        e.draw(batch);
		    }
		}
		//sprite.setPosition(sprite.getX()+.001f, sprite.getY());
		//sprite.draw(batch);
		batch.end();
		
		//debugRenderer.render(world, camera.combined);
		
		world.step(1/60f, 6, 2);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
