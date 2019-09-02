/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsmidtermproject;

/**
 *
 * @author kasra
 */
public class TST_Node {
    
    TST_Node leftChild = null, rightChild = null, midChild = null;
    char data;
    boolean isEnd = false;
    LinkList filesList = null;

    public TST_Node(char data) {
	
	this.data = data;
	
    }
    
    public void addFileToList (String filePath, int scentenceNum) {
	filesList = new LinkList();
	filesList.insert(filePath, scentenceNum);
    }
    
    public void addFileToListForRepeatedWord (String filePath, int scentenceNum) {
	filesList.insert(filePath, scentenceNum);
    }
    
    public void removeFileFromList(String fileName){
	
	if (filesList != null)
	    filesList.delete(fileName);
	
    }
    
    public boolean isEmpty() {
	
	if (filesList == null)
	    return true;
	else
	    return filesList.isEmpty();
		    
    }
    
    public void showDetails() {
	
	if(filesList != null)
	    filesList.print();
	
    }
    
}
