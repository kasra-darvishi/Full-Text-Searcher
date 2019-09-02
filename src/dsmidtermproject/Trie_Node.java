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
public class Trie_Node {
    
    ArrayList<Trie_Node> childs = null;
    char data;
    boolean isEnd = false;
    LinkList filesList = null;

    public Trie_Node(char data) {
	
	this.data = data;
	childs = new ArrayList<Trie_Node>();
	
    }
    
    public Trie_Node getChild(char data){
	
	if(!childs.isEmpty())
	    for(Trie_Node child : childs)
		if(child.data == data)
		    return child;
	
	return null;
	
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
