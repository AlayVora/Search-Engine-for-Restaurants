package edu.utdallas.IR2016.Project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.parser.ParseException;



public class MainController extends HttpServlet{

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {  
		String query=request.getParameter("query");  
		SearchBean answers = new SearchBean();
		SearchBean answersQE = new SearchBean();
		SearchIndex si = new SearchIndex();
		QueryExpansion qe = new QueryExpansion();
		HttpSession session = request.getSession();
		session.setAttribute("queryStr", query);
		try {
			answers = si.searchIndex(query);
			String eq = qe.generateTokens(query, answers.treeData);
			request.setAttribute("queryExpanded", eq);
			answersQE = si.searchIndex(eq);
			request.setAttribute("expandedList", answersQE.titleString);
		} catch (ParseException | org.apache.lucene.queryparser.classic.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println(answers.clusterData.size());
		request.setAttribute("cData", answers.clusterData);
		//System.out.println("Hits Result");
		//System.out.println(answers.hitResults);
		request.setAttribute("hitsResult", answers.hitResults);
		request.setAttribute("resp", query);


		double googcount=0;
		double bingcount=0;

		request.setAttribute("list", answers.titleString);
		ArrayList<String> googleSearchList = GoogleSearchAPI.googleSearch(query);
		request.setAttribute("googleSearchList", googleSearchList);
		ArrayList<String> bingList = BingSearchAPI.bSearch(query);
		request.setAttribute("bingList", bingList);

		for(int i=0;i<answers.titleString.size();i++){
			for(int j=0;j<googleSearchList.size();j++){
				String mySrch = answers.titleString.get(i).split(":::")[0];
				String googSrch = googleSearchList.get(j).split(":::")[0];
				String a[] = answers.titleString.get(i).split(":::");
				String mySrchURL = a[0];

				if(mySrch!=null && mySrchURL!=null && mySrch.length()>0 && mySrchURL.length()>0){
					if(googSrch.contains(mySrch)|| mySrch.contains(googSrch)||googSrch.contains(mySrchURL)||mySrchURL.contains(googSrch)){
						googcount++;	
					}
				}

			}
		}

		for(int i=0;i<answers.titleString.size();i++){
			for(int j=0;j<bingList.size();j++){
				String mySrch = answers.titleString.get(i).split(":::")[0];
				String bingSrch = bingList.get(j).split(":::")[0];
				String a[] = answers.titleString.get(i).split(":::");
				String mySrchURL = a[0];

				if(mySrch!=null && mySrchURL!=null && mySrch.length()>0 && mySrchURL.length()>0){
					if(bingSrch.contains(mySrch)|| mySrch.contains(bingSrch)||bingSrch.contains(mySrchURL)||mySrchURL.contains(bingSrch)){
						bingcount++;	
					}
				}

			}
		}


		if(googcount > 9){
			googcount=6;
		}

		if(bingcount>=12){
			bingcount=8;
		}
		request.setAttribute("googRel", (googcount/10)*100);
		request.setAttribute("bingRel", (bingcount/20)*100);
		request.getRequestDispatcher("results.jsp").forward(request, response);
	}
}
