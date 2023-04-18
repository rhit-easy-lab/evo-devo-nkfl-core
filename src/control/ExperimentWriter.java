package control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import agent.Agent;
import agent.Step;
import evolution.Generation;
import evolution.Simulation;

public class ExperimentWriter {
	private PrintWriter out;
//	private boolean summary_stats; TODO
	private int simulationNum = 0;
	private StringBuilder line;
	
	/**
	 * Constructor for the Experiment Writer which outputs experimental results to a CSV file at the specified filename
	 * Current implementation requires the manual construction of the given directory structure TODO
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public ExperimentWriter(String filename) throws IOException {
		this(new FileWriter(rename(filename)));
	}
	
	/**
	 * Converts the given filename to a proper format
	 * @param filename
	 * @return new filename
	 */
	public static String rename(String filename) {
		if(filename.contains("RENAME")) {
			filename = ""+SeededRandom.getInstance().nextInt();
		}
		if(!filename.endsWith(".csv")) {
			filename = filename+".csv";
		}
		return filename;
	}
	
	/**
	 * Create an experiment writer to the given java.io.Writer
	 * @param output
	 */
	public ExperimentWriter(Writer output) {
		out = new PrintWriter(new BufferedWriter(output));
		writeHeader();
	}
	
	/**
	 * Writes the header of the CSV file for readability
	 * 
	 * More logic may be added as this is parameterized TODO
	 */
	private void writeHeader() {
		line = new StringBuilder();
		line.append("Simulation,");
		line.append("Generation,");
		line.append("Generation_size,");
		line.append("Function,");
		line.append("Block_best,");
		line.append("Program_best,");
//		line.append("Plasticity_best,"); I dont think we were doing this IDK TODO
		line.append("Fitness_best,");
//		if(summary_stats) {
//			line.append("Fitness_avg,");
//			line.append("Fitness_std,");
//			line.append("Strategy_Hamming");
//		}
		line.replace(line.length()-1, line.length(), "\n"); //replace the extra comma with a next line
		out.print(line);
	}
	
	/**
	 * Override of writeSim which prints every generation
	 * @param sim
	 */
	public void writeSim(Simulation sim) {
		writeSim(sim,1,false); // require last is false to avoid a conditional check
	}
	
	/**
	 * Override of writeSim which always requires the last generation
	 * @param sim
	 * @param gen_spacing
	 */
	public void writeSim(Simulation sim, int gen_spacing) {
		writeSim(sim,gen_spacing,true);
	}
	
	/**
	 * Writes the given simulation to the output file, outputting every gen_spacing Generations
	 * Require last ensures that the last generation is output
	 * @param sim
	 * @param gen_spacing
	 * @param requireLast
	 */
	public void writeSim(Simulation sim, int gen_spacing, boolean requireLast) {
		Generation[] gens = sim.getGenerations();
		simulationNum++;
		for(int i = 0; i<gens.length; i+= gen_spacing) {
			writeGen(gens[i],""+i);
		}
		// We want to make sure we always output the final generation
		if(requireLast&&gens.length%gen_spacing!=0) {
			writeGen(gens[gens.length-1],""+(gens.length-1));
		}
	}
	
	/**
	 * Writes a singular Generation to the CSV file
	 * genIndex is a string rather than integer to allow for extra-evolutionary generation runs
	 * @param gen
	 * @param genIndex
	 */
	public void writeGen(Generation gen, String genIndex) {
		line = new StringBuilder();
		
		// Simulation
		line.append(simulationNum+",");
		
		// Generation
		line.append(toCSVDelimited(genIndex));
		
		// Generation Size
		line.append(gen.size()+",");
		
		// Function
		line.append(toCSVDelimited(gen.getFunction().toString())); //it may make more sense to pass in the landscape IDK 
		
		// Block of Best
		Agent best = gen.getBest();
		StringBuilder sb = new StringBuilder();
		for(List<Step> block : best.getBlocks()) {
			sb.append("{");
			for(Step s : block) {
				sb.append(s.toString());
				sb.append(",");
			}
			sb.replace(sb.length()-1, sb.length(), "},"); // replace extra comma with closing bracket and separating comma
		}
		sb.deleteCharAt(sb.length()-1); // remove extra comma
		line.append(toCSVDelimited(sb.toString()));
		
		// Program of Best
		sb = new StringBuilder();
		for(Integer block : best.getProgram()) {
			sb.append(block);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1); // remove extra comma
		line.append(toCSVDelimited(sb.toString()));
		
		
		// Fitness of Best
		sb = new StringBuilder();
		sb.append('"');
		for(Double fit : best.getFitnessHistory()) {
			sb.append(fit);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1); // remove extra comma
		line.append(toCSVDelimited(sb.toString()));
		
		line.replace(line.length()-1, line.length(), "\n"); // replace the extra comma with a next line
		out.print(line);
	}
	
	/**
	 * A method which ensures that the inputs to each cell of a CSV have been properly delimited
	 * @param input
	 * @return delimited cell contents
	 */
	public static String toCSVDelimited(String input) {
		//Special charters can be delimited: https://en.wikipedia.org/wiki/Comma-separated_values#Basic_rules
		if(!(input.contains("\"")||input.contains(",")||input.contains("\n"))) {
			return input+",";
		}
		String out = input.replace("\"", "\"\"");
		out = '"'+out+"\",";
		return out;
	}
}
