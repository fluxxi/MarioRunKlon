package com.flxi.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class MarioRunKlon extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture dizzy;
	int manState = 0;		//Zustand der Figur
	int pause = 0;			//Pausevariable um Figur zu entschleunigen
	float gravity = 0.2f;	//float für Physik Gravitation
	float velocity = 0;		//Geschwindigkeit
	int manY;				//Y-Position von der Figur
	int gameState = 0;
	BitmapFont font;
	int score = 0;
	//final int BACKGROUND_MOVE_SPEED = 100;




	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;
	Random random;


	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount;



	Rectangle manRectangle;


	
	@Override
	public void create () {

		batch = new SpriteBatch();

		background = new Texture("bg.png");


		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight()/2;	 // Position der Figur
		dizzy = new Texture("dizzy-1.png");

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}


	public void makeCoin() {

		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());

	}


	public void makeBomb() {

		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());

	}




	@Override
	public void render () {


		batch.begin();

		batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());







		if(gameState==0) {
			//Warten auf Spielstart
			if(Gdx.input.justTouched()) {
				gameState=1;

			}


		}else if(gameState == 1) {
			//Spiel läuft
			if(coinCount < 100) {
				coinCount++;

			}else {

				coinCount =0;
				makeCoin();

			}

			coinRectangles.clear();
			for (int i = 0; i <coinXs.size(); i++){

				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i)-4);

				coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));

			}

			if(bombCount < 250) {
				bombCount++;

			}else {

				bombCount = 0;
				makeBomb();

			}



			bombRectangles.clear();
			for (int i = 0; i <bombXs.size(); i++){

				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i)-4);

				bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));

			}




			if(Gdx.input.justTouched()) { //Sprung der Figur

				velocity = -10;
				Gdx.app.log("KLICK", "HIER WURDE GEKLICKT -------------------------------------------- ");

			}



			if (pause <9) { //Verlangsamen der Figur
				pause ++;

			}else {

				pause = 0;
				if (manState<3) { //Berechnung Zustand Figur - Solange manState kleiner 3 Zähle hoch, ansonsten setze manState auf 0
					manState++;

				}else {
					manState =0;

				}


			}

			// Wenn geklickt wurde wird velocity auf -10 gesetzt und in jedem Frame 0.2 hochgezählt, dementsprechent


			velocity = velocity + gravity;
			manY = (int) (manY - velocity);
			Gdx.app.log("velocity", "Zahl: ");
			System.out.println(velocity);
			Gdx.app.log("manY", "Zahl: ");
			System.out.println(manY);

			if (manY<=0) { //Sobald untere Ecke erreicht, setzt er die figur auf y=0
				manY = 0;

			}

			manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[0].getWidth()/2,manY, man[manState].getWidth(), man[manState].getHeight());

			for(int i=0; i<coinRectangles.size(); i++) {
				if(Intersector.overlaps(manRectangle, coinRectangles.get(i))) {

					Gdx.app.log("COIN", "Kollidiert");
					score++;
					coinRectangles.remove(i);
					coinXs.remove(i);
					coinYs.remove(i);
					break;

				}


			}

			for(int i=0; i<bombRectangles.size(); i++) {
				if(Intersector.overlaps(manRectangle, bombRectangles.get(i))) {

					Gdx.app.log("BOMB", "Kollidiert");

					gameState = 2;

				}


			}



		}else if (gameState==2){
			// Game Over

			if(Gdx.input.justTouched()) {
				score= 0;
				gameState = 1;
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coinCount = 0;
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount = 0;
				manY = Gdx.graphics.getHeight() /2;

			}


		}


		if (gameState == 2){
			batch.draw(dizzy, Gdx.graphics.getWidth()/2 - man[0].getWidth()/2,manY);


		}else {
			batch.draw(man[manState], Gdx.graphics.getWidth()/2 - man[0].getWidth()/2, manY); //Draw der Figur inkl. Zustand

		}





		font.draw(batch, String.valueOf(score), 100,200);


		batch.end();

	}

	@Override
	public void dispose () {
		batch.dispose();

	}
}
