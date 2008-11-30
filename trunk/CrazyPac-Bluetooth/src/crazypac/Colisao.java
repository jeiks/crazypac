package crazypac;

import javax.microedition.lcdui.game.Sprite;

public class Colisao {
	
	Colisao() {};
	
	/**
	 * verifica a colisao de dois personagens
	 * eles devem ter a mesma largura e a mesma altura
	 * @param per1
	 * @param per2
	 */
	public static boolean colisao(Sprite per1, Sprite per2) 
	{
		return colisao(
				per1.getX(),per1.getY(),
				per1.getX()+per1.getWidth(),per1.getY()+per1.getHeight(),
				per2.getX(),per2.getY(),
				per2.getX()+per2.getWidth(),per2.getY()+per2.getHeight()
				);
	}
	

	/**
	 * Verifica a colisão entre duas retas
	 * @param pX
	 * @param pY
	 * @param pX2
	 * @param pY2
	 * @param poX
	 * @param poY
	 * @param poX2
	 * @param poY2
	 * @return
	 */
	public static boolean colisao(int pX,  int pY, 
			               int pX2, int pY2,
			               int poX,  int poY,
			               int poX2, int poY2) 
	{
		Linha aux = new Linha(pX,pY,pX2,pY2, Math.max(pX,pX2)+1, Math.max(pY,pY2)+1);
		return aux.colideComLinha(poX, poY, poX2, poY2);
	}
	
	/**
	 * verifica a colisao entre um desenho e as linhas que
	 * compõem o vetor
	 * @param nextX
	 * @param nextY
	 * @param width
	 * @param height
	 * @param linhas
	 * @return
	 */
	public static boolean colisao(int nextX, int nextY, int width, int height, Linha[] linhas)
	{
		for (Linha linha: linhas){
			if (linha.colideComLinha(nextX, nextY,
					                 nextX+width,
					                 nextY+height
					                )
				)
				return true;
		}
		return false;	
	}
}
