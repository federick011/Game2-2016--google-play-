package com.gamescol.save;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Save extends ApplicationAdapter {
	SpriteBatch batch;
	Texture gato, obstaculo, tablero, over, play, coche, fondo, logo, explosion;
	TextureRegion animacionlol, animacionlol1;
	TextureRegion[] indice, indice1;
	TextureRegion[][]  regiongato, regionexplo;
	Animation animacion, animacion1;
	Random random;
	Acciones acciones;
	BitmapFont font;
	Preferences preferencias;
	Music musica;
	Sound sonido;
	
	Rectangle recgato, rectangleguia, recplay;
	
	Rectangle[] recobsta;
	
	OrthographicCamera camara;
	
	int direccion=0, perdio=0;
	int[] posyobsta, posxobsta, ultimapos, golpeaobsta;
	float posxgato=0, tiempo1=0, tiempo=0, posygato=120 ;
	float[] tiempo2;
	
	int cantidadobs=3, velocidad=10, puntos=0, puntosguarda=0, pantalla=1;
	
	private AdsController adsController;
     
	  public Save(AdsController adsController){
	        this.adsController = adsController;
	    }
	
	@Override
	public void create () {
		musica = Gdx.audio.newMusic(Gdx.files.getFileHandle("musica/SuperHero_original.ogg", FileType.Internal));
		sonido=Gdx.audio.newSound(Gdx.files.getFileHandle("musica/golpedemazo.wav", FileType.Internal));
		
		font=new BitmapFont(Gdx.files.internal("menus/letrasjuegos.fnt"));
		
		//datos guardados
		preferencias = Gdx.app.getPreferences("datos de feed");
		puntosguarda=preferencias.getInteger("puntos");
		
		random=new Random();
		acciones=new Acciones();
		ultimapos=new int[cantidadobs];
		posxobsta =new int[cantidadobs];
		posyobsta =new int[cantidadobs];
		posxobsta[0]=1;
		posxobsta[1]=161;
		posxobsta[2]=321;
		posyobsta[0]=840;
		posyobsta[1]=1240;
		posyobsta[2]=1650;
		
		tiempo2=new float[3];
		tiempo2[0]=0;
		tiempo2[1]=0;
		tiempo2[2]=0;
		
		golpeaobsta=new int[3];
		golpeaobsta[0]=0;
		golpeaobsta[1]=0;
		golpeaobsta[2]=0;
		camara=new OrthographicCamera();
		camara.setToOrtho(false, 480, 800);
		
		//rectangles
		recgato=new Rectangle(posxgato, 180, 160, 160);
		rectangleguia=new Rectangle(0, 0, 50, 50);
		recplay=new Rectangle(-450, -450, 200, 150);
		
		recobsta=new Rectangle[cantidadobs];
		
		for(int c=0; c<cantidadobs; c++)
		{
			recobsta[c]=new Rectangle(posxobsta[c], posyobsta[c], 150, 150);
		}
		
		
		batch = new SpriteBatch();
		//rexturas
		gato = new Texture("textura/spritegato.png");
		obstaculo= new Texture("textura/obstaculos.png");
		tablero=new Texture("menus/tablero.png");
		over=new Texture("menus/over.png");
		play=new Texture("menus/play.png");
		coche=new Texture("textura/coche.png");
		fondo=new Texture("textura/fondo.png");
		logo=new Texture("menus/logo.png");
		explosion=new Texture("textura/Explosion.png");;
		
		gato.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		obstaculo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		tablero.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		over.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		play.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		coche.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		fondo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		explosion.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		//regions 
		regiongato=TextureRegion.split(gato, gato.getWidth()/3, gato.getHeight());
		regionexplo=TextureRegion.split(explosion, explosion.getWidth()/12, explosion.getHeight());
		
	    //adsController.showBannerAd();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1026, 255, 255, 000);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camara.combined);
		camara.update();
		switch (pantalla)
		{
		case 0:
			
			if(perdio==0)
			{
				musica.play();
				
				movercosas();
				adsController.hideBannerAd();
			}
			else
			{
				adsController.showBannerAd();
			}
			
			//rectangles
			rectangles();
			coliciones();
			if(puntos>19 && puntos<29)
			{
				velocidad=12;
			}
			else if(puntos>29 && puntos<39)
			{
				velocidad=15;
			}
			else if(puntos>39 && puntos<59)
			{
				velocidad=17;
			}
			else if(puntos>59)
			{
				velocidad=19;
			}
			batch.begin();
			batch.draw(fondo, 0, 0);
			if(perdio==0)
			{
				indice=new TextureRegion[3];
				for(int cs=0; cs<3; cs++)
				{
					indice[cs]=regiongato[0][cs];
				}
				
				animacion = new Animation(1f, indice);
				tiempo1+=Gdx.graphics.getDeltaTime();
				animacionlol= animacion.getKeyFrame(tiempo1, true);
				batch.draw(animacionlol, posxgato, posygato);
				
			}
			else if(perdio==1)
			{
				musica.stop();
				
				if(posygato>-200)
				{
					
					batch.draw(regiongato[0][2], posxgato, posygato-=12);
				}
				
			}
				
			
			
			
			
			batch.draw(coche, posxgato, 66);
			batch.draw(obstaculo, posxobsta[0], posyobsta[0]);
			batch.draw(obstaculo, posxobsta[1], posyobsta[1]);
			batch.draw(obstaculo, posxobsta[2], posyobsta[2]);
			
			if(golpeaobsta[0]==1 && tiempo2[0]<1.2f)
			{
				indice1=new TextureRegion[12];
				for(int cs=0; cs<12; cs++)
				{
					indice1[cs]=regionexplo[0][cs];
				}
				
				animacion1 = new Animation(0.1f, indice1);
				tiempo2[0]+=Gdx.graphics.getDeltaTime();
				
				animacionlol1= animacion1.getKeyFrame(tiempo2[0], true);
				batch.draw(animacionlol1, posxobsta[0], ultimapos[0]);
				if(tiempo2[0]>1.2f)
				{
					golpeaobsta[0]=0;
					tiempo2[0]=0;

				}
			}
			if(golpeaobsta[1]==1 && tiempo2[1]<1.2f)
			{
				indice1=new TextureRegion[12];
				for(int cs=0; cs<12; cs++)
				{
					indice1[cs]=regionexplo[0][cs];
				}
				
				animacion1 = new Animation(0.1f, indice1);
				tiempo2[1]+=Gdx.graphics.getDeltaTime();
				animacionlol1= animacion1.getKeyFrame(tiempo2[1], true);
				batch.draw(animacionlol1, posxobsta[1], ultimapos[1]);
				if(tiempo2[1]>1.2f)
				{
					golpeaobsta[1]=0;
					tiempo2[1]=0;

				}
			}
			if(golpeaobsta[2]==1 && tiempo2[2]<1.2f)
			{
				indice1=new TextureRegion[12];
				for(int cs=0; cs<12; cs++)
				{
					indice1[cs]=regionexplo[0][cs];
				}
				
				animacion1 = new Animation(0.1f, indice1);
				tiempo2[2]+=Gdx.graphics.getDeltaTime();
				animacionlol1= animacion1.getKeyFrame(tiempo2[2], true);
				batch.draw(animacionlol1, posxobsta[2], ultimapos[2]);
				if(tiempo2[2]>1.2f)
				{
					golpeaobsta[2]=0;
					tiempo2[2]=0;

				}
			}
			
			if(perdio==1)
			{
				
				batch.draw(tablero, 0, 700);
				String punto=Integer.toString(puntos);
				font.draw(batch, punto, 10, 740);
				punto=Integer.toString(puntosguarda);
				font.draw(batch, punto, 260, 740);
				batch.draw(over, 40, 400);
				batch.draw(play, 140, 290);
				recplay.setPosition(140, 290);
			}
			else if(perdio==0)
			{
				recplay.setPosition(-450, -450);
			}
			if(perdio==0)
			{
				String punto=Integer.toString(puntos);
				punto=Integer.toString(puntos);
				font.draw(batch, punto, 220, 600);
			}
			
			
			batch.end();
			break;

		case 1:
			musica.play();
			rectangles();
			batch.begin();
			batch.draw(fondo, 0, 0);
			batch.draw(logo, 40, 400);
			batch.draw(play, 140, 290);
			recplay.setPosition(140, 290);
			if(rectangleguia.overlaps(recplay))
			{
				musica.stop();
				pantalla=0;
			}
			batch.end();
			break;
		}
	}
	
	
	public void movercosas()
	{
		
		if(posxgato<320 && direccion==0)
		{
			posxgato+=4;
			if(posxgato==320 || posxgato>320)
			{
				direccion=1;
			}
		}
		else if(direccion==1 && posxgato>0)
		{
			posxgato-=4;
			if(posxgato==0 || posxgato<0)
			{
				direccion=0;
			}
		}
		//posxgato=acciones.movercosasx(160.0f, camara);
		
		for(int c=0; c<cantidadobs; c++)
		{
			if(posyobsta[c]<0 || rectangleguia.overlaps(recobsta[c]))
			{
				/*if(c==0)
				{
					posyobsta[c]=840;
				}
				else if(c==1)
				{
					posyobsta[c]=1240;
				}
				else if(c==2)
				{
					posyobsta[c]=1650;
				}*/
				
				
				posyobsta[c]=(801+random.nextInt(800));
			}
			else
			{
				posyobsta[c]-=velocidad;
			}
			
			//rectangles
			if(perdio==0)
			{
				recobsta[c].setPosition(posxobsta[c], posyobsta[c]);
			}
			else
			{
				recobsta[c].setPosition(0, 900);
			}
			
		}
		
	}
	
	public void rectangles() 
	{
		recgato.setPosition(posxgato,  120);
		
			
		
		
		if(Gdx.input.justTouched())
		{
			rectangleguia.x=acciones.movercosasx(50f, camara);
			rectangleguia.y=acciones.movercosasy(50f, camara);
			
			
			
		}
		else
		{
			rectangleguia.x=-40;
			rectangleguia.y=-40;
		}
		
		if(Gdx.input.justTouched() && perdio==0)
		{
			if(rectangleguia.overlaps(recobsta[0]))
			{
				golpeaobsta[0]=0;
				tiempo2[0]=0;
				sonido.play();
				puntos=puntos+1;
				golpeaobsta[0]=1;
				ultimapos[0]=posyobsta[0];
				/*indice1=new TextureRegion[12];
				for(int cs=0; cs<12; cs++)
				{
					indice1[cs]=regionexplo[0][cs];
				}
				
				animacion1 = new Animation(1f, indice1);
				tiempo2+=Gdx.graphics.getDeltaTime();
				animacionlol1= animacion1.getKeyFrame(tiempo2, true);
				batch.draw(animacionlol1, posxobsta[0], posyobsta[0]);*/
			}
			if(rectangleguia.overlaps(recobsta[1]))
			{
				golpeaobsta[1]=0;
				tiempo2[1]=0;
				sonido.play();
				puntos=puntos+1;
				golpeaobsta[1]=1;
				ultimapos[1]=posyobsta[1];
			}
			    
			if(rectangleguia.overlaps(recobsta[2]))
			{
				golpeaobsta[2]=0;
				tiempo2[2]=0;
				sonido.play();
				puntos=puntos+1;
				golpeaobsta[2]=1;
				ultimapos[2]=posyobsta[2];
			}
		}	
	}
	
	public void coliciones() 
	{
		
		if(recobsta[0].overlaps(recgato))
		{
			
			if(puntos>preferencias.getInteger("puntos"))
			{
				puntosguarda=puntos;
				preferencias.putInteger("puntos", puntos);
				preferencias.flush();
			}
			
			
			recobsta[0].setPosition(0, 900);
			recobsta[1].setPosition(0, 900);
			recobsta[2].setPosition(0, 900);
			
			perdio=1;
		}
		if(recobsta[1].overlaps(recgato))
		{
			
			if(puntos>preferencias.getInteger("puntos"))
			{
				puntosguarda=puntos;
				preferencias.putInteger("puntos", puntos);
				preferencias.flush();
			}
			
			recobsta[0].setPosition(0, 900);
			recobsta[1].setPosition(0, 900);
			recobsta[2].setPosition(0, 900);
			
			perdio=1;
		}
		if(recobsta[2].overlaps(recgato))
		{
			
			if(puntos>preferencias.getInteger("puntos"))
			{
				puntosguarda=puntos;
				preferencias.putInteger("puntos", puntos);
				preferencias.flush();
			}
			
			recobsta[0].setPosition(0, 900);
			recobsta[1].setPosition(0, 900);
			recobsta[2].setPosition(0, 900);
			
			perdio=1;
		}
		if(Gdx.input.justTouched())
		{
			if(rectangleguia.overlaps(recplay) && perdio==1)
			{
				posyobsta[0]=840;
				posyobsta[1]=1240;
				posyobsta[2]=1650;
				velocidad=8;
				puntos=0;
				perdio=0;
				posygato=120;
			}
		}
		
		
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		musica.pause();
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		sonido.dispose();
		musica.dispose();
		gato.dispose();
		obstaculo.dispose();
		tablero.dispose();
		over.dispose();
		play.dispose();
		logo.dispose();
		fondo.dispose();
		explosion.dispose();
		
	}
}
