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
import jdk.nashorn.internal.ir.BreakNode;

/**
 *
 * @author kasra
 */
public class TST {
    
    private TST_Node root = null;
    private ArrayList<String> stopWords, filesPath;
    private int numberOfNodesInBst = 0,numberOfTotalWords = 0;
    private String folderPath = null;
    private ArrayList<String> fileNamesAndScentenceNumbers;

    public TST(ArrayList<String> filesPath, ArrayList<String> stopWords) {
	
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
		    if (!stopWords.contains(word) && word.length() != 0)
			root = insert(root, word.toCharArray(), 0, filePath, scentenceNum);
		    
		}
		scentenceNum++;
		
	    }
	    
	} catch (FileNotFoundException ex) {
	    System.err.println("could not find file in : " + filePath);
	}
	
    }
    
    private TST_Node insert(TST_Node node, char[] word, int ptr, String filePath, int scentenceNum) {
	
	if (node == null)
	    node = new TST_Node(word[ptr]);
	
	if (node.data > word[ptr]) {
	    
	    node.leftChild = insert(node.leftChild, word, ptr, filePath,scentenceNum);

	} else if (node.data < word[ptr]) {
	    
	    node.rightChild = insert(node.rightChild, word, ptr, filePath, scentenceNum);
	    
	} else {
	    
	    if (ptr < word.length - 1) {
		node.midChild = insert(node.midChild, word, ptr + 1, filePath, scentenceNum);
	    }else{
		//check for repeated word
		if (node.isEnd == true) {
		    node.addFileToListForRepeatedWord(filePath, scentenceNum);
		}else{
		    
		    node.isEnd = true;
		    node.addFileToList(filePath, scentenceNum);
		    numberOfNodesInBst++;
		    
		}
		
	    }
	    
	}
	
	return node;
	
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
	deleteFileFromNode(root, fileName);
	
	return true;
	
    }
    
    //recursive method to remove all words that have the given file name
    private void deleteFileFromNode(TST_Node node, String fileName){
	
	if (node == null)
	    return;
	
	node.removeFileFromList(fileName);
	//if now node dosent exist in any file remove the word
	if (node.isEmpty())
	    node.isEnd = false;
	
	deleteFileFromNode(node.rightChild, fileName);
	deleteFileFromNode(node.leftChild, fileName);
	deleteFileFromNode(node.midChild, fileName);
	
    }
    
    public void updateFile(String fileName){
	
	if(deleteFile(fileName)){
	    addFile(folderPath + "\\" + fileName + ".txt");
	    System.out.println(fileName + "successfully updated");
	}
	
    }
    
        public void searchScentence (String scentence) {
	
	fileNamesAndScentenceNumbers = new ArrayList<String>();
	String[] words = scentence.toLowerCase().split("\\W+");
	for (int i = 0; i < words.length; i++) {
	    String word = words[i];
	    if (!stopWords.contains(word) && word.length() != 0)
		recursiveSearch(root, word.toCharArray(), 0, true);
	}
	
	for (int i = 0; i < fileNamesAndScentenceNumbers.size(); i++) {
	    
	    String get = fileNamesAndScentenceNumbers.get(i);
	    String[] s = get.split("\\s+");
	    printScentence(folderPath + "\\" + s[0] + ".txt", Integer.parseInt(s[1]));
	    
	}
	System.out.println("");
	
    }
    
    public void search(String word){
	
	boolean wordWasFound = recursiveSearch(root, word.toLowerCase().toCharArray(), 0, false);
	if (!wordWasFound) 
	    System.out.println("word was not found!");
	
    }
    //recursive method to find a word
    private boolean recursiveSearch(TST_Node node, char[] word, int ptr, boolean searchForScentence){
	
	if (node == null)
	    return false;
	
	if (node.data > word[ptr])
	    return recursiveSearch(node.leftChild, word, ptr, searchForScentence);
	
	else if (node.data < word[ptr])
	    return recursiveSearch(node.rightChild, word, ptr, searchForScentence);
	
	else{
	    
	    if (node.isEnd && ptr == word.length - 1 && !node.isEmpty()){
		
		if(searchForScentence)
		    node.filesList.someMethod(fileNamesAndScentenceNumbers);
		else
		    node.showDetails();
		return true;
		
	    }else if (ptr == word.length - 1)
		return false;
	    else
		return recursiveSearch(node.midChild, word, ptr + 1, searchForScentence);
	    
	}  
	
    }
    
    public void listOfWords() {
	showNodeDetail(root, new String());
    }
    
    private void showNodeDetail(TST_Node node, String string){
	
	if (node == null)
	    return;
	    
	    showNodeDetail(node.rightChild, string);
	    showNodeDetail(node.leftChild, string);
	    
	    string += node.data;
	    if (node.isEnd && !node.isEmpty()){
		
		System.out.print(string + " " + "->" + " ");
		node.showDetails();
		
	    }
	    showNodeDetail(node.midChild, string);
	
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
