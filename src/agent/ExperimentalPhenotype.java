package agent;

import java.util.ArrayList;
import java.util.List;

import control.Constants;

public class ExperimentalPhenotype extends Phenotype{
	
	
	
	public ArrayList<ExperimentalPhenotype> map;
	public ArrayList<ExperimentalPhenotype> segment1;
	public ArrayList<ExperimentalPhenotype> segment2;
	public ArrayList<ExperimentalPhenotype> segment3;
	public ArrayList<ExperimentalPhenotype> segment4;
	public ExperimentalPhenotype pheno;
	
	public ExperimentalPhenotype ExperimentalPhenotype1(String phen) {
		this.pheno = phen;
		return this.pheno;
	}
	
	public ExperimentalPhenotype() {
		this("");
	}
	public void ExperimentalMaps() {
		this.map = new ArrayList<ExperimentalPhenotype>();
		this.segment1 = new ArrayList<ExperimentalPhenotype>();
		this.segment2 = new ArrayList<ExperimentalPhenotype>();
		this.segment3 = new ArrayList<ExperimentalPhenotype>();
		this.segment4 = new ArrayList<ExperimentalPhenotype>();
		this.segment1.add(ExperimentalPhenotype1("1a"));
		this.segment1.add(ExperimentalPhenotype1("3a"));
		this.segment1.add(ExperimentalPhenotype1("4a"));
		this.segment1.add(ExperimentalPhenotype1("5ab"));
		this.segment2.add(ExperimentalPhenotype1("4b"));
		this.segment2.add(ExperimentalPhenotype1("2bc"));
		this.segment3.add(ExperimentalPhenotype1("5c"));
		this.segment3.add(ExperimentalPhenotype1("10c"));
		this.segment4.add(ExperimentalPhenotype1("6d"));
		this.segment4.add(ExperimentalPhenotype1("7d"));
		this.segment4.add(ExperimentalPhenotype1("8d"));
		int a = this.segment1.size();
		int b = this.segment2.size();
		int c = this.segment3.size();
		int d = this.segment4.size();
		for(int k = 0; k < a; k++) {
			this.map.add(this.segment1.get(k));
		}
		for(int h = 0; h < b; h++) {
			this.map.add(this.segment2.get(h));
		}
		for(int g = 0; g < c; g++) {
			this.map.add(this.segment3.get(g));
		}
		for(int f = 0; f < c; f++) {
			this.map.add(this.segment4.get(f));
		}
		
		
		
}
	private ExperimentalPhenotype ExperimentalPhenotype(String string) {
		return this.pheno = ExperimentalPhenotype1(string);
	}

	public ArrayList<ExperimentalPhenotype> getSegment1(){
		return this.segment1;
	}
	public ArrayList<ExperimentalPhenotype> getSegment2(){
		return this.segment2;
	}
	public ArrayList<ExperimentalPhenotype> getSegment3(){
		return this.segment3;
	}
	public ArrayList<ExperimentalPhenotype> getSegment4(){
		return this.segment4;
	}
	public ArrayList<ExperimentalPhenotype> getMap(){
		return this.map;
	}
	
	
	@Override
	public ArrayList<Phenotype> getNeighbors() {
		ArrayList<Phenotype> neighbors1 = new ArrayList<Phenotype>();
		int j = this.map.size();
		Phenotype r = new ExperimentalPhenotype();
		for(int i = 0; i < j; i++) {
			if(ExperimentalPhenotype1(this.pheno) == this.map.get(i)) {
				neighbors1.add(r);
			}
			if(ExperimentalPhenotype1(this.pheno) == r) {
				neighbors1.add(r);
			}
			//Should return a list of neighbors for the specified this.pheno 
			
			
			r = this.map.get(i);
		}
		return neighbors1;
	}

	@Override
	public Phenotype getIdenticalCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}

}
