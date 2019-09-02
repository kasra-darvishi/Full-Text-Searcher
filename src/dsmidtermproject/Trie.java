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

/**
 *
 * @author kasra
 */
public class Trie {
    
    private Trie_Node root = null;
    private ArrayList<String> stopWords, filesPath;
    private int numberOfNodesInBst = 0,numberOfTotalWords = 0;
    private String folderPath = null;
    private ArrayList<String> fileNamesAndScentenceNumbers;

    public Trie(ArrayList<String> filesPath, ArrayList<String> stopWords) {
	
	this.stopWords = stopWords;
	this.filesPath = new ArrayList<>(filesPath);
	root = new Trie_Node(' ');
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
			insert(word.toCharArray(), filePath, scentenceNum);
		    
		}
		scentenceNum++;
		
	    }
	    
	} catch (FileNotFoundException ex) {
	    System.err.println("could not find file in : " + filePath);
	}
	
    }
    
    public void insert(char[] word, String filePath, int scentenceNum){
	
	Trie_Node temp = root;
	
	for (int i = 0; i < word.length; i++) {
	    
	    char c = word[i];
	    Trie_Node child = temp.getChild(c);
	    //checking if current char exists in the tree or not
	    if (child == null) {
		
		temp.childs.add(new Trie_Node(c));
		temp = temp.getChild(c);
		
	    } else
		temp = child;
	    
	}
	//repeated word
	if(temp.isEnd == true)
	    temp.addFileToListForRepeatedWord(filePath, scentenceNum);
	else {
	    
	    temp.isEnd = true;
	    temp.addFileToList(filePath, scentenceNum);
	    numberOfNodesInBst++;
	    
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
	deleteFileFromNode(root, fileName);
	
	return true;
	
    }
    
    //recursive method to remove all words that have the given file name
    private void deleteFileFromNode(Trie_Node node, String fileName){
	
	if (node == null)
	    return;
	
	node.removeFileFromList(fileName);
	//if now node dosent exist in any file remove the word
	if (node.isEmpty())
	    node.isEnd = false;
	
	for(Trie_Node child : node.childs)
	    deleteFileFromNode(child, fileName);
	
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
		searchK2(word);
	}
	
	for (int i = 0; i < fileNamesAndScentenceNumbers.size(); i++) {
	    
	    String get = fileNamesAndScentenceNumbers.get(i);
	    String[] s = get.split("\\s+");
	    printScentence(folderPath + "\\" + s[0] + ".txt", Integer.parseInt(s[1]));
	    
	}
	System.out.println("");
	
    }
    //RelatedToScentenceSearch
    public void searchK2 (String word){
	
	char[] chars = word.toLowerCase().toCharArray();
	boolean wordWasFound = false;
	
	Trie_Node temp = root;
	for (int i = 0; i < chars.length; i++) {
	    
	    char c = chars[i];
	    
	    if (temp.getChild(c) == null)
		return;
	    else
		temp = temp.getChild(c);
	    
	}
	
	if(temp.isEnd && !temp.isEmpty())
	    temp.filesList.someMethod(fileNamesAndScentenceNumbers);

    }
    
    public void search(String word){
	
	char[] chars = word.toLowerCase().toCharArray();
	boolean wordWasFound = false;
	
	Trie_Node temp = root;
	for (int i = 0; i < chars.length; i++) {
	    
	    char c = chars[i];
	    
	    if (temp.getChild(c) == null){
		System.out.println("word was not found!");
		return;
	    }
	    else
		temp = temp.getChild(c);
	    
	}
	
	if(temp.isEnd && !temp.isEmpty())
	    temp.showDetails();
	else
	    System.out.println("word was not found!");

    }
    
    public void listOfWords() {
	showNodeDetail(root, new String());
    }
    
    private void showNodeDetail(Trie_Node node, String string){
	
	if (node == null)
	    return;
	
	string += node.data;
	if (node.isEnd && !node.isEmpty()){
	    
	    System.out.print(string + " " + "->" + " ");
	    node.showDetails();
	    
	}
	
	for(Trie_Node child : node.childs)
	    showNodeDetail(child, string);
	
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
