package control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import agent.Agent;
import agent.Step;
import evolution.Generation;
import evolution.Simulation;

public class ExperimentWriter {
	//TODO enable file overwrite options
	private PrintWriter out;
	private int simulationNum = 0;
	private StringBuilder line;
	private List<String> params;
	private String filename;
	
	public ExperimentWriter() throws IOException {
		this(Constants.FILENAME,Constants.WRITER_PARAMS);
		
	}
	
	/**
	 * Constructor for the Experiment Writer which outputs experimental results to a CSV file at the specified filename
	 * Current implementation requires the manual construction of the given directory structure TODO
	 * 
	 * @param filename
	 * @param params what columns to include in the output
	 * @throws IOException
	 */
	public ExperimentWriter(String filename, String[] params) throws IOException {
		this(new FileWriter(outputFile(filename)),params);
	}
	
	/**
	 * Converts a given filename to a file and creates any necessary directories
	 * @param filename
	 * @return output file
	 */
	public static File outputFile(String filename) {
		File file = new File("output/" + rename(filename));
		file.getParentFile().mkdirs();
		return file;
	}
	
	/**
	 * Converts the given filename to a proper format
	 * @param filename
	 * @return new filename
	 */
	public static String rename(String filename) {
		if(filename.contains("RENAME")) {
			filename = ""+SeededRandom.getInstance().getSeed();
		}
		if(!filename.endsWith(".csv")) {
			filename = filename+".csv";
		}
		return filename;
	}
	
	/**
	 * Create an experiment writer to the given java.io.Writer
	 * @param output
	 * @param params what columns to include in the output
	 */
	public ExperimentWriter(Writer output,String[] params) {
		out = new PrintWriter(new BufferedWriter(output));
		this.params = new ArrayList<String>();
		for(String p:params) {
			this.params.add(p);
		}
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
		line.append("Agent_number,");
		if(params.contains("generation size")) {
			line.append("Generation_size,");
		}
		if(params.contains("function string")) {
			line.append("Function,");
		}
		if(params.contains("block")) {
			line.append("Block,");
		}
		if(params.contains("program")) {
			line.append("Program,");
		}
		if(params.contains("strategy")) {
			line.append("Strategy,");
		}
		if(params.contains("final fitness")) {
			line.append("Final_Fitness,");
		}
		if(params.contains("fitness history")) {
			line.append("Fitness_history,");
		}
		if(params.contains("parent")) {
			line.append("Parent_number,");
		}
		
		
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
		List<Generation> gens = sim.getGenerations();
		simulationNum++;
		for(int i = 0; i<gens.size(); i+= gen_spacing) {
			writeGen(gens.get(i),""+i);
		}
		// We want to make sure we always output the final generation
		if(requireLast&&gens.size()%gen_spacing!=0) {
			writeGen(gens.get(gens.size()-1),""+(gens.size()-1));
		}
	}
	
	/**
	 * Writes the top agents as per the Constants File
	 * @param gen
	 * @param genIndex
	 */
	public void writeGen(Generation gen, String genIndex) {
		writeGen(gen, genIndex, Constants.AGENTS_OUT);
	}
	
	/**
	 * Writes the top N agents of the generation to the csv
	 * @param gen
	 * @param genIndex
	 * @param top_n
	 */
	public void writeGen(Generation gen, String genIndex, int top_n) {
		int[] agent_numbers = new int[top_n];
		for(int i = 0; i<top_n;i++) {
			agent_numbers[i]=i;
		}
		writeGen(gen,genIndex,agent_numbers);
	}
	
	/**
	 * Writes a singular Generation to the CSV file
	 * genIndex is a string rather than integer to allow for extra-evolutionary generation runs
	 * @param gen
	 * @param genIndex
	 */
	public void writeGen(Generation gen, String genIndex, int[] agent_numbers) {
		List<Agent> agents = gen.getAgents();
		for(int num:agent_numbers) {
			
			Agent agent = agents.get(num);
			
			line = new StringBuilder();
			
			// Simulation
			line.append(simulationNum+",");

			// Generation
			line.append(toCSVDelimited(genIndex));

			// Agent Number
			line.append(num+",");
			
			if(params.contains("generation size")) {
				line.append(Constants.GENERATION_SIZE+",");
			}
			if(params.contains("function string")) {
				line.append(toCSVDelimited(Constants.FITNESS_FUNCTION_TYPE));//TODO make this better so that it can include more details
			}
			if(params.contains("block")) {
				StringBuilder sb = new StringBuilder();
				for(List<Step> block : agent.getBlocks()) {
					sb.append("{");
					for(Step s : block) {
						sb.append(s.toString());
						sb.append(",");
					}
					sb.replace(sb.length()-1, sb.length(), "},"); // replace extra comma with closing bracket and separating comma
				}
				sb.deleteCharAt(sb.length()-1); // remove extra comma
				line.append(toCSVDelimited(sb.toString()));
			}
			if(params.contains("program")) {
				StringBuilder sb = new StringBuilder();
				if(Constants.PROGRAM_LENGTH > 0)
				{
				for(Integer block : agent.getProgram()) {
					sb.append(block);
					sb.append(",");
				}
				sb.deleteCharAt(sb.length()-1); // remove extra comma
				}
				line.append(toCSVDelimited(sb.toString()));
			}
			if(params.contains("strategy")) {
				line.append(toCSVDelimited(agent.getStrategy().toString()));
			}
			if(params.contains("final fitness")) {
				line.append(""+agent.getFinalFitness()+',');
			}
			if(params.contains("fitnesses")) {
				line.append(toCSVDelimited(agent.getFitnessHistory().toString()));
			}
			if(params.contains("fitness history")) {
				line.append("PLACEHOLDER,"); // TODO replace with actual parent number
			}
			
			line.replace(line.length()-1, line.length(), "\n"); // replace the extra comma with a next line
			out.print(line);
		}
		
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
	
	public void closePrintWriter() {
		out.close();
	}
	
	public String getFileName() {
		return filename;
	}
}
