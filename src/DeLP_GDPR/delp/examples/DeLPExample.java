package DeLP_GDPR.delp.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import org.tweetyproject.commons.ParserException;
import DeLP_GDPR.delp.examples.DeLPExample;
import DeLP_GDPR.delp.parser.DelpParser;
import DeLP_GDPR.delp.reasoner.DelpReasoner;
import DeLP_GDPR.delp.semantics.GeneralizedSpecificity;
import DeLP_GDPR.delp.syntax.DefeasibleLogicProgram;
import DeLP_GDPR.logics.fol.syntax.FolFormula;

/**
 * Shows how to parse and query a DeLP program.
 * 
 *
 */
public class DeLPExample {
	public static void main(String[] args) throws FileNotFoundException, ParserException, IOException{
		DelpReasoner reasoner = new DelpReasoner(new GeneralizedSpecificity());
		
		FileWriter resultWriter = new FileWriter("results.txt", true);		
	    BufferedWriter writer = new BufferedWriter(resultWriter);	    
		
		// Building test-case for different Knowledge-bases
		final File folder = new File("bin/resources/NonConsent_testcases");		
		for (final File fileEntry : folder.listFiles()) {
			//System.out.println(fileEntry.getPath());			
			String filePath = "/" + Paths.get(fileEntry.getPath()).subpath(1, 4).toString();
			
			System.out.println("Processing this Knowledge-base: " + filePath);			
			writer.write("--------");			
			writer.write("Knowledge Base test-case: " + fileEntry.getName());
			writer.newLine();
			
			DelpParser parser = new DelpParser();
			DefeasibleLogicProgram delp = parser.parseBeliefBaseFromFile(DeLPExample.class.getResource(filePath).getFile());
			// Conducting 10 tests for each case
			FolFormula query1 = (FolFormula) parser.parseFormula("ConsentCompliance(telehealthserviceserver, patient1)");
			FolFormula query2 = (FolFormula) parser.parseFormula("ConsentCompliance(telehealthserviceserver, patient2)");
			FolFormula query3 = (FolFormula) parser.parseFormula("ConsentCompliance(telehealthserviceserver, patient3)");

			for (int j = 1; j < 4; j++) {
				writer.write("Query to Knowledge base: " + "query" + String.valueOf(j));
				writer.newLine();
				
				FolFormula query;
				String result;

				if (j == 1) {
		            	query = query1;
		            } else if (j == 2) {
		            	query = query2;
		            } else // j == 3
		            	query = query3;				

				for (int i = 0; i < 10; i++) {				
		            long startTime = System.currentTimeMillis();		            
					result = reasoner.query(delp, query).toString();		            
		            long endTime = System.currentTimeMillis();
					
		            long executionTime = endTime - startTime;
		            // Write the result to file
		            writer.write(" Run: " + i + " Result: " + result + "Execution Time: " + executionTime);
		            writer.newLine();
				}
				writer.newLine();
			}
			writer.newLine();
		}
		writer.close();
		resultWriter.close();
		System.out.println("--- End of test cases ---");
	}
}