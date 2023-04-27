package visualizerComponents;

import java.awt.BasicStroke;
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
import agent.Step;
import javax.swing.JPanel;


import agent.AgentSimple;
import agent.Bitstring;
import agent.Phenotype;
import landscape.FitnessFunction;

public class AgentSimpleFrame extends JPanel {
	int xloc;
	int yloc;
	AgentSimple a;
	Font f = new Font("TimesRoman", Font.PLAIN, 13);
	
	int width = 750;
	int height = 500;
	
	public AgentSimpleFrame(AgentSimple a, int xloc, int yloc)
	{
		this.a=a;
		this.xloc=xloc;
		this.yloc=yloc;
		this.setPreferredSize(new Dimension(width+20, height));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(xloc, yloc);
		g2.setFont(f);
		g2.setStroke(new BasicStroke(2));
		
		g2.drawRect(0, 0, width, height);
		g2.drawLine(0, 150, width, 150);
		g2.drawString("History", 10, 20);
		
		this.drawHistory(g2);
		
		g2.translate(0, 150);
		g2.drawString("Current View", 10, 20);
		this.drawCurrentView(g2);
		g2.translate(0, -150);
		
		g2.setStroke(new BasicStroke(1));
		g2.translate(-xloc, -yloc);
	}
	
	private void drawHistory(Graphics2D g2)
	{
		List<Step> strategy = a.getStrategy();
		List<Phenotype> phenotypeHist = a.getPhenotypeHistory();
		List<Double> fitHist = a.getFitnessHistory();
		
		int boxwidth = 100;
		int boxheight = 30;
		
		if((700-(boxwidth*strategy.size())) < 0)
		{
			System.out.println("Strategy too long to visualize, please use <=10 steps");
		}
		
		FontMetrics metrics = g2.getFontMetrics(f);
		
		g2.translate((width-(boxwidth*strategy.size()+boxwidth))/2, (150-boxheight)/2);
		
		for(int i=0; i<strategy.size()+1; i++)
		{
			if(i==0)
			{
				g2.drawRect(boxwidth*i, 0, boxwidth, 30);
				String stepName = "Start";
				int x1 = (boxwidth - metrics.stringWidth(stepName)) / 2;
				int y1 = ((boxheight - metrics.getHeight()) / 2) + metrics.getAscent();
				g2.drawString(stepName, x1 + (boxwidth*i), y1);
			}
			else
			{
				g2.drawRect(boxwidth*i, 0, boxwidth, 30);
				String stepName = strategy.get(i-1).name();
				int x1 = (boxwidth - metrics.stringWidth(stepName)) / 2;
				int y1 = ((boxheight - metrics.getHeight()) / 2) + metrics.getAscent();
				g2.drawString(stepName, x1 + (boxwidth*i), y1);
			}
			
			g2.drawLine(boxwidth*i, -boxheight, boxwidth*i, boxheight*2);
			g2.drawLine(boxwidth*(i+1), -boxheight, boxwidth*(i+1), boxheight*2);
			
			if(i < phenotypeHist.size())
			{
				String phenotypeStr = phenotypeHist.get(i).toString();
				String fitString = ""+(Math.round(fitHist.get(i)*1000.0)/1000.0);
				
				int x1 = (boxwidth - metrics.stringWidth(phenotypeStr)) / 2;
				int x2 = (boxwidth - metrics.stringWidth(fitString)) / 2;
				int y = ((boxheight - metrics.getHeight()) / 2) + metrics.getAscent();
				
				g2.drawString(fitString, boxwidth*i+x2, y-30);
				g2.drawString(phenotypeStr, boxwidth*i+x1, y+30);
			}
		}
		
		g2.translate(-(width-(boxwidth*strategy.size()+boxwidth))/2, -(150-boxheight)/2);
	}
	
	private void drawCurrentView(Graphics2D g2)
	{
		
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
