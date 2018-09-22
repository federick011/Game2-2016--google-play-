package com.gamescol.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class Acciones 
{

	//==funciones para mover cosas con respecto a la camara :D=============//
			public static float movercosasx(float numero, Camera cam)
			{
				Vector3 touchPos = new Vector3();
			      touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			      cam.unproject(touchPos);
			      
			      return touchPos.x-numero/2;
			}
			
			public static float movercosasy(float numero, Camera cam)
			{
				Vector3 touchPos = new Vector3();
			      touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			      cam.unproject(touchPos);
			      
			      return touchPos.y-numero/2;
			}
}
