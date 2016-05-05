<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Engine - Restaurant</title>
<link href="bootstrap.min.css" rel="stylesheet">
<link href="style1.css" rel="stylesheet" type="text/css">
<link href="style2.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script src="jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link rel="shortcut icon" href="favicon.png" >
<style>
.nav-tabs>li>a {
	color : #cb202d;
}
.final{
	margin: 0px;
    background-color: white;
    padding: 30px 0px 10px;
}
.tab-content {
    background-color: white;
}
.center{
	text-align:center;
}
</style>
<script type="text/javascript"> 
 $(function() {
	 $("#keywords_input").focus();
	    $("form input").keypress(function (e) {
	        if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
	        	var search = $("#keywords_input").val();	        	
	        	if (search == "") {
	        		alert("Search Query is Missing");
	        		return false;
	        		} 	           
	            $('#my_form').submit();
	            //return false;
	        } 
	    });
	    
	    
	     $("#search_button").click(function (e) {
	    	 var search = $("#keywords_input").val();	        	
	        	if (search == "") {
	        		alert("Search Query is Missing");
	        		return false;
	        		} 	        
	            $('#my_form').submit();
	    	
	    }); 
	    
	    
	    
	});
</script>


<style type="text/css">
.imageStyle {
	background: url(restaurant.jpg) no-repeat center center fixed;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	-o-background-size: cover;
	background-size: cover;
}
</style>
</head>
<body style="background-color:#F7F7F7;">
	<header class="header wrapper" id="header">
	<!-- end col-s-16 --> </header>
	<div id="resp-search-container" class="search-box-area"></div>

	<div class="wrapper bd-txt-bg">

		<div class="h-city-main-en p-relative row"
			style="background-position: 0 0;">
			<h1 class="h-city-home-title"></h1>
			<div class="dark-mask-absolute"></div>


			<div class="logged-in-home-search" style="opacity: 0.9;">


				<div id="search_main_container" class="full_search wrapper plr20 "
					style="margin-left: 200px">
					<div id="search_bar_wrapper" class="search_bar" role="form">

						<form id="my_form" method="POST" action="MainController"
							method="POST">
							<div class=" col-l-10 col-s-16 col-m-12 plr0i" style="border:1px solid black;">
								<div id="keywords_container" class="col-s-16">
									<div id="keywords_pretext">
										<div class="k-pre-1 hidden"
											style="height: 100%; overflow: hidden; display: none;">
											<span class="search-bar-icon mr10" data-icon="ƹ"></span>

											<div class="keyword_placeholder">
												<div class="keyword_div"></div>
											</div>
										</div>
										<% String queryStr = (String) session.getAttribute("queryStr"); %>
										<div class="k-pre-2  w100" style="display: inline-block;">
											<span class="glyphicon glyphicon-search"></span> <label
												id="label_search_res" class="hdn"></label> <input role="combobox"
												aria-expanded="true" aria-autocomplete="list"
												aria-owns="keywords-by" aria-labelledby="label_search_res"
												id="keywords_input" class="discover-search" name="query"
												value="<%=queryStr%>">
										</div>
									</div>

								</div>
							</div>
							<div class=" col-l-2 col-s-16 col-m-2 plr0i">
								<div role="button" aria-flowto="search-start" tabindex="0"
									id="search_button" class="left ttupper btn btn--red">
									Search</div>
								<div class="hidden search-button-loading left">
									<img width="20" alt=""
										src="https://c.zmtcdn.com/images/loading-transparent.gif">
								</div>
							</div>



						</form>




						<div class="clear"></div>
					</div>

				</div>
				<div class="clear"></div>
			</div>

		</div>
	</div>
	
<div>
  <!-- Nav tabs -->
  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#team" aria-controls="team" role="tab" data-toggle="tab">Results</a></li>
    <li role="presentation"><a href="#team1" aria-controls="team1" role="tab" data-toggle="tab">Link Analysis</a></li>
    <li role="presentation"><a href="#clustering" aria-controls="clustering" role="tab" data-toggle="tab">Clustering</a></li>
    <li role="presentation"><a href="#expansion" aria-controls="expansion" role="tab" data-toggle="tab">Query Expansion</a></li>
    <li role="presentation"><a href="#google" aria-controls="google" role="tab" data-toggle="tab">Google</a></li>
    <li role="presentation"><a href="#bing" aria-controls="bing" role="tab" data-toggle="tab">Bing</a></li>
  </ul>

  <!-- Tab panes -->
  <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="team">
    	<%String q=(String)request.getAttribute("resp");%>
		<%if(q!=null && !("".equals(q))){ %>
			<h3 class="final" align="center" style="margin-bottom:30px;color:#3f3f3f"></h3>
		<%} %>
	
		<%ArrayList<String> list = (ArrayList<String>)request.getAttribute("list"); %>
			<%if(list!=null && list.size()>0){	
				int cnt =0;%>
				<%for(int i=0;i<list.size();i++){ //list.size()
					String a[] = list.get(i).split(":::");
				if(a[1].contains("Redirecting")){
					continue;
				}
					if(cnt==10) {
				break;} else {cnt++;} 
				%>
		<!-- <%String href="http://"+ a[a.length-2];%> -->
		<div style="margin-left: 20%;margin-right: 20%;margin-bottom: 25px;">
		<h5 style="margin-bottom:0px"><%=a[1] %></h5>
		<a href="<%=a[0]%>" target="_blank"> <%=a[0] %></a>
		</div>
		<%} %>
	<%}else{%>
		<h3 class="center">Your search - <%=q %> - did not match any documents.</h3>
	<%}%>
    </div>
    
    <div role="tabpanel" class="tab-pane" id="team1">
    <%if(q!=null && !("".equals(q))){ %>
			<h3 class="final" align="center" style="margin-bottom:30px;color:#3f3f3f">PageRank & HITS</h3>
		<%} %>
		<%ArrayList<String> list1 = (ArrayList<String>)request.getAttribute("hitsResult"); %>
			<%if(list1!=null && list1.size()>0){	
				int cnt =0;%>
				<%for(int i=0;i<list1.size();i++){ //list.size()
					String a = list1.get(i);
				if(a=="" || a == null){continue;}
					if (a.contains("facebook") || a.contains("twitter") || a.contains("google")){
						continue;
					}
					
					if(cnt==10) {
				break;} else {cnt++;} 
				%>
		<div style="margin-left: 20%;margin-right: 20%;margin-bottom: 25px;">
		<h5 style="margin-bottom:0px">Result <%=cnt %></h5>
		<a href="<%=a%>" target="_blank"> <%=a %></a>
		</div>
		<%} %>
	<%}else{%>
		<h3 class="center">Your search - <%=q %> - did not match any documents.</h3>
	<%}%>
    </div>
    
    <div role="tabpanel" class="tab-pane" id="clustering">
    <%String[] color = {"#D03642","#005100","#0000cd"}; %>
	<%List<ArrayList<String>> clusterList = (List<ArrayList<String>>)request.getAttribute("cData");%> 
		<%if(clusterList!=null && clusterList.size()>0){ %>
			<h3 class="final" align="center" style="margin-bottom:30px;color:#3f3f3f">Clustered Results</h3>
			<%for(int i=0;i<clusterList.size();i++){ %>
				<%if(clusterList.get(i)!=null && clusterList.get(i).size()>0){ %>
				<%for(String x : clusterList.get(i)){ %>	
					<% String a[] = x.split(":::");
					if(a[1].contains("Redirecting")){
						continue;
					};%>
					<div style="margin-left: 20%;margin-right: 20%;margin-bottom: 25px;">
					<h5 style="margin-bottom:0px"><%=a[1] %><span class="glyphicon glyphicon-ok" style="color:<%=color[i]%>;margin-left: 10px;"></span></h5>
					<a href="<%=a[0]%>" target="_blank"> <%=a[0] %></a>
					</div>
				<%} %>
			<%} %>
		<%} %>
	<%}else{%>
		<h3 class="center">Your search - <%=q %> - did not match any documents.</h3>
	<%}%>
    </div>
    
    
    
	<div role="tabpanel" class="tab-pane" id="expansion">
	<%String qExpand = (String)request.getAttribute("queryExpanded");  %>
		<%if(qExpand !=null && !("".equals(qExpand))){ %>
			<h3 class="final" align="center" style="margin-bottom:30px;color:#3f3f3f">Expanded Query 
			<span style="color:#D03642"><%= qExpand %></span></h3>
		<%} %>
	
		<%ArrayList<String> expandedList = (ArrayList<String>)request.getAttribute("expandedList"); %>
			<%if(expandedList!=null && expandedList.size()>0){ 
			int cnt =0;%>
				<%for(int i=0;i<list.size();i++){ //expandedList.size()
					String a[] = expandedList.get(i).split(":::");
					if(a[1].contains("Redirecting")){
						continue;
					}
					if(cnt==10) {
						break;} else {cnt++;}
					%>
		<div style="margin-left: 20%;margin-right: 20%;margin-bottom: 25px;">
		<h5 style="margin-bottom:0px"><%=a[1] %></h5>
		<a href="<%=a[0]%>" target="_blank"> <%=a[0] %></a>
		</div>
		<%} %>
	<%}else{%>
		<h3 class="center">Your search - <%=q %> - did not match any documents.</h3>
	<%}%>
    
    </div>
    
<div role="tabpanel" class="tab-pane" id="google">
<%ArrayList<String> googleSearchList = (ArrayList<String>)request.getAttribute("googleSearchList"); %>
<%if(googleSearchList!=null && googleSearchList.size()>0){ int cnt=1;%>
	<h3 class="final" align="center" style="margin-bottom:30px;color:#3f3f3f">Google Results</h3>
		<%for(int i=0;i<googleSearchList.size();i++){ 		
		String a[] = googleSearchList.get(i).split(" ");
		if(cnt==10) {
			break;} else {cnt++;}
		String data="";
		for(int j=1;j<a.length;j++){
			data+=a[j]+" ";	
		}
		%>
		<div style="margin-left: 20%;margin-right: 20%;margin-bottom: 25px;">
		<h5 style="margin-bottom:0px"><%=data %></h5>
		<a href='<%=a[0] %>' target="_blank"> <%=a[0] %></a>
		</div>
		<%} %>
	<%}%>
    </div>
    
    
    <div role="tabpanel" class="tab-pane" id="bing">
    	<%ArrayList<String> bingList = (ArrayList<String>)request.getAttribute("bingList"); %>
		<%if(bingList!=null && bingList.size()>0){ int cnt =0; %>
		<h3 class="final" align="center" style="margin-bottom:30px;color:#3f3f3f">Bing Results</h3>
	
		<%for(int i=0;i<bingList.size();i++){ 	//bingList.size()	
		String a[] = bingList.get(i).split(" ");
		if(cnt==10) {
			break;} else {cnt++;}
		String data="";
		for(int j=1;j<a.length;j++){
			data+=a[j]+" ";	
		}
		%>
		
		<div style="margin-left: 20%;margin-right: 20%;margin-bottom: 25px;">
		<h5 style="margin-bottom:0px"><%=data %></h5>
		<a href='<%=a[0] %>' target="_blank"> <%=a[0] %></a>
		</div>
		<%} %>
	<%}%>
    </div>
  </div>
</div>
</body>
</html>