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
public class LinkList {
    
    Link first = null;

    public LinkList() {

    }
    
    public void insert(String filePath, int scentenceNum) {
	
	Link newLink = new Link(findFileNameFromPath(filePath), filePath);
	newLink.NumOfScentences.add(scentenceNum);
	
	if (first == null) {
	    first = newLink;
	    return;
	}
	
	Link temp = first;
	Link temp2 = null;
	//going to the last link
	while (temp != null) {	    
	    
	    temp2 = temp;
	    //check if file is already added into the list
	    if (temp.filePath.equals(filePath)) {
		temp.NumOfScentences.add(scentenceNum);
		return;
	    }
	    
	    temp = temp.next;
	    
	}
	temp2.next = newLink;

    }
    
    public void delete(String fileName){
	//node is empty from any files
	if (first == null) {
	    return;
	}
	
	if (first.fileName.equals(fileName)) {
	    first = first.next;
	    return;
	}
	
	Link temp = first;
	
	while (temp.next != null) {
	    
	    if (temp.next.fileName.equals(fileName)) {
		
		temp.next = temp.next.next;
		return;
		
	    }
	    temp = temp.next;
	    
	}
	
    }
    
    //finds the file name from file path
    private String findFileNameFromPath(String filePath) {
	
	int i = filePath.length() - 4;
	int j = i;
	while (true) {	    
	    
	    if (filePath.charAt(i) == '\\')
		break;
	    else
		i--;
	    
	}
	i++;
	
	char[] chars = new char[j-i];
	
	int k = 0;
	for ( ; i < j; i++) {
	    chars[k] += filePath.charAt(i);
	    k++;
	}
	
	String fileName = String.valueOf(chars);
	
	return fileName;
    }
    
    public void print(){
	
	Link temp = first;
	while(temp != null){
	    System.out.print(temp.fileName + ", ");
	    temp = temp.next;
	}
	System.out.println("");
	
    }
    
    public boolean isEmpty() {
	return first == null;
    }
    //adds info about the files that the node exists in and the number of scentence in string format
    public void someMethod (ArrayList<String> fileNamesAndScentenceNumbers) {
	
	Link temp = first;
	while(temp != null){
	    
	    for(Integer i : temp.NumOfScentences){
		
		String s = temp.fileName + " " + i;
		if(!fileNamesAndScentenceNumbers.contains(s))
		    fileNamesAndScentenceNumbers.add(s);
		
	    }
	    
	    temp = temp.next;
	}
	
	
    }
    
}
