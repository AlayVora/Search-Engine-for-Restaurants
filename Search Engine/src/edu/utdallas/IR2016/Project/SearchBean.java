package edu.utdallas.IR2016.Project;

import java.util.ArrayList;
import java.util.TreeMap;

public class SearchBean {

	public ArrayList<String> titleString;
	public TreeMap<String, String> treeData;
	public ArrayList<ArrayList<String>> clusterData;
	public ArrayList<String> hitResults;
	

	public SearchBean(ArrayList<String> titleString, TreeMap<String, String> treeData)
	{
		this.titleString= titleString;
		this.treeData= treeData;
	}
	public SearchBean(ArrayList<String> answer, TreeMap<String, String> tree, ArrayList<String> hitResults) {
		// TODO Auto-generated constructor stub
		
		this.titleString= answer;
		this.treeData= tree;
		this.hitResults= hitResults;
	}
	
	public SearchBean() {
		// TODO Auto-generated constructor stub
	}
}
