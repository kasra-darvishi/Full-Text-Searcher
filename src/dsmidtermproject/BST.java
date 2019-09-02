/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsmidtermproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kasra
 */
public class BST {
    
    private BST_Node root = null;
    private ArrayList<String> stopWords, filesPath;
    private int numberOfNodesInBst = 0,numberOfTotalWords = 0;
    private String folderPath = null;
    private ArrayList<String> fileNamesAndScentenceNumbers;

    public BST(ArrayList<String> filesPath, ArrayList<String> stopWords) {
	
	this.stopWords = stopWords;
	this.filesPath = new ArrayList<>(filesPath);
	fileNamesAndScentenceNumbers = new ArrayList<>();
	
	long startTime = System.currentTimeMillis();
	
	for (int i = 0; i < filesPath.size(); i++) {
	    addFile(filesPath.get(i));
	}
	
	long stopTime = System.currentTimeMillis();
	
	System.out.println("\nnumber of files : " + filesPath.size());
	System.out.println("number of nodes in tree : " + numberOfNodesInBst);
	System.out.println("number of words : " + numberOfTotalWords);
	System.out.println("execution time : " + (stopTime - startTime) + "\n");
	
    }
    
    public void addFile(String filePath) {
	
	try {
	    
	    Scanner scanner = new Scanner(new File(filePath));
	    String[] words;
	    String word;
	    int scentenceNum = 1;
	    
	    while(scanner.hasNextLine()){

		words = scanner.nextLine().split("\\W+");
		for (int i = 0; i < words.length; i++) {
		    word = words[i].toLowerCase();
		    
		    numberOfTotalWords++;
		    //do not add stop words to tree
		    if (!stopWords.contains(word) && word.length() != 0) {

			BST_Node temp = new BST_Node(word);
			temp.addFileToList(filePath,scentenceNum);
			if(root != null)
			    insert(temp, root);
			else{
			    root = temp;
			    numberOfNodesInBst++;
			}

		    }
		    
		}
		scentenceNum++;
		
	    }
	    
	} catch (FileNotFoundException ex) {
	    System.err.println("could not find file in : " + filePath);
	}
	
    }
    
    //recursive method to insert a node to bst
    //node1 is the node which is going to be inserted
    //node2 in the parent node
    private void insert(BST_Node node1, BST_Node node2) {
	
	int compareResult = node2.data.compareTo(node1.data);
	
	if (compareResult > 0) {
	    
	    if (node2.rightChild == null) {
		node2.rightChild = node1;
		numberOfNodesInBst ++;
		return;
	    }else
		insert(node1,node2.rightChild);
	    
	}else if (compareResult < 0) {
	    
	    if (node2.leftChild == null) {
		node2.leftChild = node1;
		numberOfNodesInBst ++;
		return;
	    }else
		insert(node1,node2.leftChild);

	}else{
	    //in this case the word is already added to bst
	    node2.repeatedWord(node1);
	}
	
    }
    
    public void addFileWithName(String fileName) {
	
	String s = folderPath + "\\" + fileName + ".txt";
	
	for (int i = 0; i < filesPath.size(); i++) {
	    String get = filesPath.get(i);
	    if(get.equals(s)){
		System.out.println("its already added!");
		return;
	    }
	}
	
	filesPath.add(s);
	addFile(s);
	
    }
    
    public boolean deleteFile(String fileName) {
	//removing the file from list of files
	boolean fileWasFound = false;
	for (int i = 0; i < filesPath.size(); i++) {
	    
	    String get = filesPath.get(i);

	    if (fileName.equals(findFileNameFromPath(get))) {
		filesPath.remove(get);
		fileWasFound = true;
	    }
	    
	}
	//checking if the file is found or not
	if (!fileWasFound) {
	    System.out.println("file was not found");
	    return false;
	}else
	    System.out.println(fileName + "successfully deleted");
	//removing the file from all nodes
	if(filesPath.size() != 0)
	    deleteFileFromNode(root, fileName, null, false);
	//if the tree is going to get empty
	else
	    root = null;
	
	return true;
	
    }
    //recursive method to remove all nodes that have the given file name
    private void deleteFileFromNode(BST_Node node, String fileName, BST_Node fatherNode, boolean isRightChild){
	
	if (node == null)
	    return;
	node.removeFileFromList(fileName);
	//checking if the node should be deleted from tree or not
	if(node.isEmpty()) {
	    
	    //if node is a leaf
	    if(node.leftChild == null && node.rightChild == null){
		
		//the node witch is going to be removed is root
		if(fatherNode == null)
		    root = null;
		//
		else
		    if(isRightChild)
			fatherNode.rightChild = null;
		    else
			fatherNode.leftChild = null;
		return;
		
	    }
	    
	    //if node has one child
	    else if(node.leftChild == null || node.rightChild == null){
		
		//the node witch is going to be removed is root
		if(fatherNode == null)
		    if(node.leftChild != null)
			root = node.leftChild;
		    else
			root = node.rightChild;
		//
		else
		    if(isRightChild && node.leftChild != null)
			fatherNode.rightChild = node.leftChild;
		    else if(isRightChild && node.rightChild != null)
			fatherNode.rightChild = node.rightChild;
		    else if(!isRightChild && node.leftChild != null)
			fatherNode.leftChild = node.leftChild;
		    else
			fatherNode.leftChild = node.rightChild;
		
	    }
	    
	    //if node has two childs
	    else {
		
		BST_Node bigLeftSideChild = getBiggestNodeInLeftSide(node);
		//copy the node
		node.filesList = bigLeftSideChild.filesList;
		
		node.data = bigLeftSideChild.data;
		
		node.filesList.delete(fileName);
		//new node should be checked again
		if(node.isEmpty()){
		    deleteFileFromNode(node, fileName, fatherNode, isRightChild);
		    return;
		}
		
	    }
	    
	}
	if(node.rightChild != null)
	    deleteFileFromNode(node.rightChild, fileName, node, true);
	if(node.leftChild != null)
	    deleteFileFromNode(node.leftChild, fileName, node, false);
	
    }
    //finds the biggest node in left side and removes it
    private BST_Node getBiggestNodeInLeftSide(BST_Node node){
	
	BST_Node temp = node.leftChild;
	BST_Node father = null;
	
	while(temp.rightChild != null){
	    father = temp;
	    temp = temp.rightChild;
	}
	//the chosen node is the left child of the node given to method
	if(temp.leftChild != null && father == null)
	    node.leftChild = temp.leftChild;
	
	//the node choosed to be the replace has a left child and its father is not null
	else if(temp.leftChild != null && father != null)
	    father.rightChild = temp.leftChild;
	
	return temp;
	
    }
    
    public void updateFile(String fileName){
	
	if(deleteFile(fileName)){
	    addFile(folderPath + "\\" + fileName + ".txt");
	    System.out.println(fileName + "successfully updated");
	}
	    
	
    }
    
    //calls the recursive method to show all the words with details
    public void listOfWords() {
	showNodeDetail(root);
    }
    
    private void showNodeDetail(BST_Node node){
	
	if (node == null) 
	    return;
	node.showDetails();
	showNodeDetail(node.leftChild);
	showNodeDetail(node.rightChild);
	
    }
    
    public void searchScentence (String scentence) {
	
	fileNamesAndScentenceNumbers = new ArrayList<String>();
	String[] words = scentence.toLowerCase().split("\\W+");
	for (int i = 0; i < words.length; i++) {
	    String word = words[i];
	    if (!stopWords.contains(word) && word.length() != 0)
		recursiveSearch(root, word);
	}
	
	System.err.println(fileNamesAndScentenceNumbers.size());
	
	for (int i = 0; i < fileNamesAndScentenceNumbers.size(); i++) {
	    
	    String get = fileNamesAndScentenceNumbers.get(i);
	    String[] s = get.split("\\s+");
	    printScentence(folderPath + "\\" + s[0] + ".txt", Integer.parseInt(s[1]));
	    
	}
	System.out.println("");
	
    }
    
    public void search(String word){

	BST_Node node = recursiveSearch(root, word.toLowerCase());
	if (node != null)
	    node.showDetails();
	else
	    System.out.println("word was not found!");
	
    }
    //recursive method to find a word
    private BST_Node recursiveSearch(BST_Node node, String word){
	
	if (node == null || node.data.equals(word)) {
	    
	    if(node != null)
		node.filesList.someMethod(fileNamesAndScentenceNumbers);
	    return node;
	}else{
	
	    int compareResult = node.data.compareTo(word);

	    if (compareResult > 0) {
		    return recursiveSearch(node.rightChild, word);
	    }else{
		    return recursiveSearch(node.leftChild, word);
	    }
	    
	}
	
    }
    
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
	    chars[k] = filePath.charAt(i);
	    k++;
	}
	
	String fileName = new String(chars);
	
	return fileName;
    }
    
    public void setFolder(String folderName){
	this.folderPath = folderName;
    }
    
    public void listOfFiles(){
	
	for (int i = 0; i < filesPath.size(); i++) {
	    
	    System.out.print(findFileNameFromPath(filesPath.get(i)) + ", ");
	    
	}
	System.out.println("\n" + "number of listed docs : " + filesPath.size());
	
    }
    
    private void printScentence(String filePath,int scentenceNumber) {
	
	Scanner scanner;
	try {
	    scanner = new Scanner(new File(filePath));
	    for (int i = 1; i < scentenceNumber; i++)
		scanner.nextLine();

	    System.out.println(findFileNameFromPath(filePath) + " :");
	    System.out.println("| - > " + scanner.nextLine() + "\n");
	} catch (FileNotFoundException ex) {
	    System.out.println("wtf error 1");
	}
	
    }
    
}
