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
public class BST_Node {
    
    BST_Node leftChild = null;
    BST_Node rightChild = null;
    String data;
    LinkList filesList;

    public BST_Node(String data) {
	
	filesList = new LinkList();
	this.data = data;
	
    }
    
    public void addFileToList (String filePath, int scentenceNum) {
	filesList.insert(filePath, scentenceNum);
    }
    
    //only addes the file path to files list
    public void repeatedWord(BST_Node node){
	
	filesList.insert(node.filesList.first.filePath, node.filesList.first.NumOfScentences.get(0));
	
    }
    
    public void showDetails() {
	
	System.out.println(data + " " + "->" + " ");
	filesList.print();
	
    }
    
    public void removeFileFromList(String fileName){
	
	if (filesList != null)
	    filesList.delete(fileName);
	
    }
    
    public boolean isEmpty() {
	return filesList.isEmpty();
    }
    
}
