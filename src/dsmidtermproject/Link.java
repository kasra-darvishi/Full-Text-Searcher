/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsmidtermproject;

import java.util.ArrayList;

/**
 *
 * @author kasra
 */
public class Link {
    
    String filePath;
    String fileName;
    Link next;
    ArrayList<Integer> NumOfScentences;

    public Link(String fileName,String filePath) {
	
	this.fileName = fileName;
	this.filePath = filePath;
	next = null;
	NumOfScentences = new ArrayList<Integer>();
	
    }
    
}
