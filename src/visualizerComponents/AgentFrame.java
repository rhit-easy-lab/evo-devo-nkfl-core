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
import agent.Step;
import javax.swing.JPanel;

import agent.Agent;
import agent.AgentSimple;
import agent.Bitstring;
import agent.Phenotype;
import landscape.FitnessFunction;

public class AgentFrame extends JPanel {
	int xloc;
	int yloc;
	Agent a;
	Font f = new Font("TimesRoman", Font.PLAIN, 13);
	FitnessFunction fitFun;
	int width = 750;
	int height = 500;
	List<Color> blockColors = new ArrayList<Color>();
	
	public AgentFrame(Agent a, FitnessFunction fitFun, int xloc, int yloc)
	{
		this.a=a;
		this.xloc=xloc;
		this.yloc=yloc;
		this.fitFun = fitFun;
		this.setPreferredSize(new Dimension(width+20, height));
		
		blockColors.add(Color.ORANGE);
//		blockColors.add(Color.YELLOW);
		blockColors.add(Color.GREEN);
		blockColors.add(Color.BLUE);
		blockColors.add(Color.MAGENTA);
		blockColors.add(Color.PINK);
	}
	
	public AgentFrame(Agent a, FitnessFunction fitFun, int xloc, int yloc, int width, int height)
	{
		this(a,fitFun,xloc,yloc);
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width+20, height));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(xloc, yloc);
		g2.setFont(f);
		g2.setStroke(new BasicStroke(2));
		
		g2.drawRect(0, 0, width, height);
		g2.drawString("Blocks and Program", 10, 20);
		
		drawBlocks(g2);
		g2.translate(0, 200);
		
		g2.drawRect(0, 0, width, height-200);
		g2.drawLine(0, 150, width, 150);
		g2.drawString("History and Strategy", 10, 20);
		
		this.drawHistory(g2);
		
		g2.translate(0, 150);
		g2.drawString("Current View", 10, 20);
		this.drawCurrentView(g2);
		g2.translate(0, -150);
		
		g2.setStroke(new BasicStroke(1));
		
		g2.translate(0, -200);
		
		g2.translate(-xloc, -yloc);
	}
	
	int sidelen = 500/7;
	int sidewid = 500/7;
	//Written assuming that block and program lengths are <=3. Might act weird if not true.
	private void drawBlocks(Graphics2D g2)
	{
		int center = width/2;
		int boxwidth = 100;
		int boxheight = 30;
		FontMetrics metrics = g2.getFontMetrics(f);
		List<Integer> prog = a.getProgram();
		int totalWidth = prog.size()*boxwidth;
		int tl = center-totalWidth/2;
		String progName = "Program";
		int x = (width - metrics.stringWidth(progName)) / 2;
		g2.drawString(progName, x, 20);
		for(int block=0; block<prog.size(); block++)
		{
			g2.setColor(blockColors.get(prog.get(block)));
			 g2.drawRect(tl+block*boxwidth, 30, boxwidth, boxheight);
			 g2.setColor(Color.BLACK);
			 String stepName = prog.get(block).toString();
			int x1 = (boxwidth - metrics.stringWidth(stepName)) / 2;
			int y1 = ((boxheight - metrics.getHeight()) / 2) + metrics.getAscent();
			g2.drawString(stepName, tl + x1 + (boxwidth*block), y1+30);
		}
		
		
		String blockName = "Blocks";
		g2.drawString(blockName, x, 80);
		int totalBlocksLength = 0;
		for(List<Step> block : a.getBlocks())
		{
			for(Step s : block)
			{
				totalBlocksLength++;
			}
		}
		
		int topLeft = center-(totalBlocksLength*boxwidth/2)-(20*a.getBlocks().size());
		int offsetSum = topLeft;
		for(int i=0; i<a.getBlocks().size(); i++)
		{
			offsetSum+=20;
			
			List<Step> block = a.getBlocks().get(i);
			int totalBlockWidth = boxwidth*block.size();
			String name = "block:"+i;
			int x3 = (totalBlockWidth - metrics.stringWidth(name)) / 2;
			int y3 = ((boxheight - metrics.getHeight()) / 2) + metrics.getAscent();
			g2.drawString(name, x3+offsetSum, y3+80);
			
			for(Step s : block)
			{
				g2.setColor(blockColors.get(i));
				g2.drawRect(offsetSum, 110, boxwidth, boxheight);
				String stepName = s.name();
				int x1 = (boxwidth - metrics.stringWidth(stepName)) / 2;
				int y1 = ((boxheight - metrics.getHeight()) / 2) + metrics.getAscent();
				g2.setColor(Color.BLACK);
				g2.drawString(stepName, x1+offsetSum, y1+110);
				offsetSum += boxwidth;
			}
		}
		
		
	}
	
	private void drawHistory(Graphics2D g2)
	{
		List<Step> strategy = a.getStrategy();
		List<Phenotype> phenotypeHist = a.getPhenotypeHistory();
		List<Double> fitHist = a.getFitnessHistory();
		
		int boxwidth = 100;
		int boxheight = 30;
		
		if((width-(boxwidth*strategy.size())) < 0)
		{
			System.out.println("Strategy too long to visualize, please use <=10 steps");
		}
		
		FontMetrics metrics = g2.getFontMetrics(f);
		sidewid = Math.max(500/7, metrics.stringWidth(a.phenotype.toString()) + 20);
		
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
				g2.setColor(blockColors.get(stepInBlock(i)));
				g2.drawRect(boxwidth*i, 0, boxwidth, 30);
				g2.setColor(Color.BLACK);
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
	
	private int stepInBlock(int step)
	{
		if(step == 0)
		{
			return a.getProgram().get(0);
		}
		int progIndex=0;
		int index=0;
		while(step > index)
		{
			index += a.getBlocks().get(a.getProgram().get(progIndex)).size();
			progIndex++;
		}
		return a.getProgram().get(progIndex-1);
	}
	
	private void drawCurrentView(Graphics2D g2)
	{
		//Draw current phenotype
		g2.setColor(Color.RED);
		drawPhenotypeBox(g2, a.phenotype, width/2-(sidewid/2), 20);
		g2.setColor(Color.BLACK);
		
		List<Phenotype> neighbors = a.phenotype.getNeighbors();
		int ns = neighbors.size();
		
		for(int n=0; n<ns; n++)
		{
			int yoff = (height-350-20-sidelen);
			int incr = (width-40)/(ns+1);
			int xoff = 20 + ((n+1)*incr) - sidewid/2;
			drawPhenotypeBox(g2, neighbors.get(n), xoff, yoff);
//			g2.setColor(Color.RED);
			g2.drawLine(width/2, 20+sidelen, xoff+sidewid/2, yoff);
//			g2.setColor(Color.BLACK);
		}
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
	
	public void drawPhenotypeBox(Graphics2D g2, Phenotype phenotype, int xoffset, int yoffset)
	{
		g2.translate(xoffset, yoffset);
		
		
		
		FontMetrics metrics = g2.getFontMetrics(f);
		String boxName = phenotype.toString();
		if(boxName.length() > 10)
		{
			boxName = boxName.substring(0, 10) + "\n" + boxName.substring(10);
		}

		g2.drawRect(0, 0, sidewid, sidelen);
		g2.setColor(Color.BLACK);
		int x1 = (sidewid - metrics.stringWidth(boxName)) / 2;
		int y1 = ((sidelen - metrics.getHeight()) / 2) + metrics.getAscent() - metrics.getAscent();
		String boxFit = "fit:"+Math.round(fitFun.getFitness(phenotype)*1000.0)/1000.0+"000000";
		boxFit = boxFit.substring(0,9);
		int x2 = (sidewid - metrics.stringWidth(boxFit)) / 2;
		int y2 = ((sidelen - metrics.getHeight()) / 2) + metrics.getAscent() + metrics.getAscent();
		g2.drawString(boxName, x1, y1);
		g2.drawString(boxFit, x2, y2);		
		g2.translate(-xoffset, -yoffset);
	}
}
