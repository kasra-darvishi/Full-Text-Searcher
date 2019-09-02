/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsmidtermproject;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author kasra
 */
public class StopWords {
    
    private ArrayList<String> stopWords;

    public StopWords() {
	
	stopWords = new ArrayList<String>();
	try{
	    
	    Scanner scanner = new Scanner(new File("StopWords.txt"));
	    
	    while(scanner.hasNext()){
		stopWords.add(scanner.next().toLowerCase());
	    }
	 
	}
	catch(Exception e){
	    System.err.println("could not find file : StopWords.txt");
	}
	
    }
    
    public ArrayList<String> getStopWords(){
	return stopWords;
    }
    
}
