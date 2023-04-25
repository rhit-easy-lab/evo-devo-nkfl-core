package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import agent.NKPhenotype;
import control.PropParser;
import landscape.NKLandscape;

public class NKLandscapeTest {
	
	private boolean isLocalMax(NKLandscape l, NKPhenotype p) {
		double fit = l.getFitness(p);
		NKPhenotype n = p.getIdenticalCopy();
		for(int i = 0; i<l.n; i++) {
			n.flipBit(i);
			if(fit<l.getFitness(n)) {
				return false;
			}
			n.flipBit(i);
		}
		return true;
	}
	private boolean isLocalMin(NKLandscape l, NKPhenotype p) {
		double fit = l.getFitness(p);
		NKPhenotype n = p.getIdenticalCopy();
		for(int i = 0; i<l.n; i++) {
			n.flipBit(i);
			if(fit>l.getFitness(n)) {
				return false;
			}
			n.flipBit(i);
		}
		return true;
	}
	
	void init()
	{
		PropParser.load("src/test/testConfig.properties");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {10,15})
	void testProbExtremaKN(int n) {
		int num_landscapes = 100;
		double sample_multiplier = 10.0;
		init();
		Random rand = new Random(n);
		int countMax = 0;
		int countMin = 0;
		/**
		 *  With K of N-1 there should be no relation between adjacent phenotypes
		 *  as such the probability of that point being the largest of N+1 sample 
		 *  points is 1/(N+1) http://www.tcs.hut.fi/Studies/T-79.250/tekstit/lecnotes_07.pdf
		 *  
		 *  By sampling (n+1)*n*10 points on 100 landscapes we hope to reduce variation 
		 *  enough to make the test reliable and reduce reliance on one seed
		 *  
		 *  This also avoids testing 2^n points per landscape if we were to sample the whole landscape
		 *  
		 *  This gives us a binomial distribution B((n+1)*n*1000,1/(n+1)) https://en.wikipedia.org/wiki/Binomial_distribution
		 *  This can then be estimated using a normal distribution as the number of samples is large https://en.wikipedia.org/wiki/Binomial_distribution#Normal_approximation
		 *  N(n*1000,n*n*1000/(n+1)) https://en.wikipedia.org/wiki/Normal_distribution
		 *  
		 *  We want the test to pass if we are reasonably close to the mean at 1 std we have a %68.2 chance of passing
		 *  
		 *  Any Duplicate phenotypes are relatively unlikely for large n as there are 2^n phenotypes
		 *  Even sampled with duplication the distribution should be similar due to random sampling
		 */
		for(int l = 0; l<num_landscapes; l++) {
			NKLandscape nk = new NKLandscape(rand.nextInt(),n,n-1);
			for(int i = 0; i<(n+1)*n*sample_multiplier; i++) {
				NKPhenotype p = new NKPhenotype(n);
				if(isLocalMax(nk,p)) {
					countMax++;
				}else if(isLocalMin(nk,p)) {
					countMin++;
				}
			}
		}
		
		double mean = num_landscapes*sample_multiplier*n;
		double std = (num_landscapes*sample_multiplier*n*n)/(n+1.0);
		System.out.println("For Test Maxima "+n+", count is "+countMax+", "+(countMax-mean)/std+" standard deviations off the expeted mean");
		System.out.println("For Test Minima "+n+", count is "+countMin+", "+(countMin-mean)/std+" standard deviations off the expeted mean");
		
		assertTrue((countMax)>(mean-std),"too few maxima got: "+countMax+" was expecting at least: "+(mean-std));
		assertTrue((countMax)<(mean+std),"too many maxima got: "+countMax+" was expecting no more than: "+(mean+std));
		assertTrue((countMin)>(mean-std),"too few minima got: "+countMin+" was expecting at least: "+(mean-std));
		assertTrue((countMin)<(mean+std),"too many minima got: "+countMin+" was expecting no more than: "+(mean+std));
	}
	
	@ParameterizedTest
	@ValueSource(ints = {10,15})
	void testNumExtremaK0(int n) {
		int num_landscapes = 100;
		init();
		Random rand = new Random(n);
		int countMax = 0;
		int countMin = 0;
		
		for(int i = 0; i<num_landscapes;i++) {
			NKLandscape nk = new NKLandscape(rand.nextInt(),n,0);
			int[] bitstring = new int[n];
			for(int j = 0; j < 1<<n; j++) {
				for(int k = 0; k<n; k++) {
					if(((j>>k)&1)==0) {
						bitstring[k] = 0;
					}else {
						bitstring[k] = 1;
					}
				}
				NKPhenotype p = new NKPhenotype(bitstring);
				if(isLocalMax(nk,p)) {
					countMax++;
				}else if(isLocalMin(nk,p)) {
					countMin++;
				}
			}
		}
		
		assertEquals(num_landscapes,countMax,"incorrect number of maxima");
		assertEquals(num_landscapes,countMin,"incorrect number of maxima");
	}
}
