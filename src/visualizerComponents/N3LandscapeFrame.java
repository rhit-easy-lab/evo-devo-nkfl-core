package visualizerComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import agent.AgentSimple;
import agent.Bitstring;
import landscape.FitnessFunction;

/**
 * Quick visualizer for N3 landscapes
 * 
 * @author Jacob Ashworth
 *
 */
public class N3LandscapeFrame extends JPanel {
	int drawSize;
	FitnessFunction fitFunction;
	Map<Integer, Double> fitnesses = new HashMap<Integer, Double>();
	int xloc;
	int yloc;
	Font f = new Font("TimesRoman", Font.PLAIN, 13);
	
	public N3LandscapeFrame(FitnessFunction function, int drawSize, int xloc, int yloc)
	{
		this.drawSize = drawSize;
		this.fitFunction = function;
		this.xloc = xloc;
		this.yloc = yloc;
		
		for(int i=0; i<8; i++)
		{
			Bitstring b = new Bitstring(bitstringFromInt(i));
			fitnesses.put(i, Math.round(function.getFitness(b)*1000.0)/1000.0);
		}
		this.setPreferredSize(new Dimension(drawSize+xloc, drawSize+yloc));
	}
	
	AgentSimple as = null;
	public void addAgentSimple(AgentSimple as)
	{
		this.as = as;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(xloc, yloc);
		g2.setFont(f);
		int s = drawSize/7;
		
		g2.setStroke(new BasicStroke(2));
		
		drawBox(g2, 0, 0, 0);
		drawBox(g2, 1, s*6, 0);
		drawBox(g2, 2, s*2, s*2);
		drawBox(g2, 3, s*4, s*2);
		drawBox(g2, 4, 0, s*6);
		drawBox(g2, 5, s*6, s*6);
		drawBox(g2, 6, s*2, s*4);
		drawBox(g2, 7, s*4, s*4);
		
		//horizontal big
		g2.drawLine(s, s/2, s*6, s/2);
		g2.drawLine(s, 13*s/2, s*6, 13*s/2);
		//horizontal small
		g2.drawLine(s*3, 5*s/2, s*4, 5*s/2);
		g2.drawLine(s*3, 9*s/2, s*4, 9*s/2);
		//vertical small
		g2.drawLine(5*s/2, 3*s, 5*s/2, 4*s);
		g2.drawLine(9*s/2, 3*s, 9*s/2, 4*s);
		//vertical big
		g2.drawLine(s/2, s, s/2, 6*s);
		g2.drawLine(13*s/2, s, 13*s/2, 6*s);
		//crosses
		g2.drawLine(s,s,2*s,2*s);
		g2.drawLine(s,6*s,2*s,5*s);
		g2.drawLine(6*s,s,5*s,2*s);
		g2.drawLine(6*s,6*s,5*s,5*s);
		
		
		g2.setStroke(new BasicStroke(1));
		g2.translate(-xloc, -yloc);
	}
	
	
	public void drawBox(Graphics2D g2, int box, int xoffset, int yoffset)
	{
		g2.translate(xoffset, yoffset);
		int sidelen = drawSize/7;
		
		
		FontMetrics metrics = g2.getFontMetrics(f);
		String boxName = Arrays.toString(bitstringFromInt(box));
		if(as != null)
		{
			if(as.phenotype.toString().equals(boxName))
			{
				g2.setColor(Color.RED);
			}
			else
			{
				g2.setColor(Color.BLACK);
			}
		}
		g2.drawRect(0, 0, sidelen, sidelen);
		g2.setColor(Color.BLACK);
		int x1 = (sidelen - metrics.stringWidth(boxName)) / 2;
		int y1 = ((sidelen - metrics.getHeight()) / 2) + metrics.getAscent() - metrics.getAscent();
		String boxFit = "fit:"+fitnesses.get(box)+"000000";
		boxFit = boxFit.substring(0,9);
		int x2 = (sidelen - metrics.stringWidth(boxFit)) / 2;
		int y2 = ((sidelen - metrics.getHeight()) / 2) + metrics.getAscent() + metrics.getAscent();
		g2.drawString(boxName, x1, y1);
		g2.drawString(boxFit, x2, y2);		
		g2.translate(-xoffset, -yoffset);
	}
	
	private int[] bitstringFromInt(int input)
	{
		int[] thisString = new int[3];
		int icpy = input;
		if(icpy >= 4)
		{
			thisString[2]=1;
			icpy -= 4;
		}
		if(icpy >= 2)
		{
			thisString[1]=1;
			icpy -= 2;
		}
		if(icpy >= 1)
		{
			thisString[0]=1;
			icpy -= 1;
		}
		if(icpy != 0)
		{
			System.out.println("Error in 2^n decomp");
		}
		return thisString;
	}
}
