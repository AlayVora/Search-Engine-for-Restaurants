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
   <link rel="shortcut icon" href="favicon.png" >
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="jquery.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<style>
html{
height:100%;
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
<body style="background-color:#F7F7F7;height:100%">
	<header class="header wrapper" id="header">

	<div >

		<!--<a class="logo logo--header brl" href="#"> <img
			aria-label="Yelp Search Home" src="yelp_logo.jpg">
		</a> 
		 <section class="header-navigation hom"> <section
			class="login-navigation" id="login-navigation">
		<h2 style="text-align:center;text-transform:uppercase;">Search Engine - Restaurant</h2>-->

		<img style="display: block;margin-left: auto;height:300px;width:400px;margin-right: auto;" alt="logo" src="logo.png"></img>
		<h2 style="font-size: 27px;text-align: center;text-transform: capitalize;margin-top: -25px;
    	margin-bottom: -15px;">Search Engine for Restaurant</h2>
	</div>
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
										<div class="k-pre-2  w100" style="display: inline-block;">
											<span class="glyphicon glyphicon-search"></span> <label
												id="label_search_res" class="hdn"></label> <input role="combobox"
												aria-expanded="true" aria-autocomplete="list"
												aria-owns="keywords-by" aria-labelledby="label_search_res"
												id="keywords_input" class="discover-search" name="query"
												value="">
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

					</div>

				</div>
			</div>

		</div>
	</div>
</body>
</html>