package com.programmerinprogress.learninggame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class LearningGdxGame extends ApplicationAdapter {

    private Texture dropImage;
    private Texture paddleImage;
    private Texture background;
    private Sound dropSound;
    private Music gameMusic;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Rectangle paddle;
    private Vector3 touchPos;

    private Array<Rectangle> balls;
    private long lastDropTime;

    @Override
	public void create() {

        dropImage = new Texture(Gdx.files.internal("ball_temple.png"));
        paddleImage = new Texture(Gdx.files.internal("paddle_temple.png"));

        background = new Texture(Gdx.files.internal("backdrop_temple.png"));

        dropSound = Gdx.audio.newSound(Gdx.files.internal("blip_temple.wav"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("theme.wav"));

        gameMusic.setLooping(true);
        gameMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 480);

        batch = new SpriteBatch();

        paddle = new Rectangle();
        paddle.x = 640/2 - 96/2;
        paddle.y = 20;
        paddle.width = 96;
        paddle.height = 16;



        touchPos = new Vector3();
        balls = new Array<Rectangle>();
        spawnBall();

	}

	@Override
	public void render () {


        // INPUT
        if(Gdx.input.isTouched()){

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            paddle.x = (int)touchPos.x - 96/2;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) paddle.x -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) paddle.x += 200 * Gdx.graphics.getDeltaTime();

        if(paddle.x < 16) paddle.x = 16;
        if(paddle.x > 640 - (96 + 16)) paddle.x = 640 - (96 + 16);


         // LOGIC

        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnBall();

        Iterator<Rectangle> iter = balls.iterator();
        while (iter.hasNext()){
            Rectangle ball = iter.next();
            ball.y -= 200 * Gdx.graphics.getDeltaTime();
            if(ball.y + 16 < 0) iter.remove();

            if(ball.overlaps(paddle)) {
                dropSound.play();
                iter.remove();
            }
        }

        //RENDERING
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(background, 0, 0);

        batch.draw(paddleImage, paddle.x, paddle.y);
        for(Rectangle ball : balls){
            batch.draw(dropImage, ball.x, ball.y);
        }


        batch.end();

	}

    @Override
    public void dispose(){
        dropImage.dispose();
        paddleImage.dispose();
        dropSound.dispose();
        gameMusic.dispose();
        batch.dispose();
    }

    private void spawnBall(){
        Rectangle ball = new Rectangle();
        ball.x = MathUtils.random(16, 640-32);
        ball.y = 640;
        ball.width = 16;
        ball.height = 16;

        balls.add(ball);
        lastDropTime = TimeUtils.nanoTime();

    }
}
