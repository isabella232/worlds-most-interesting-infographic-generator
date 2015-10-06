<%@page import="com.worldsmostinterestinginfographic.model.object.Post"%>
<%@page import="com.worldsmostinterestinginfographic.model.object.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
	User user = (User)request.getSession().getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>The World's Most Interesting Infographic Generator</title>
	<link media="all" rel="stylesheet" href="css/main.css">
	<link href='https://fonts.googleapis.com/css?family=Roboto:500,400,300,300italic%7CLato:400,300italic,300' rel='stylesheet' type='text/css'>
</head>
<body class="infographic">
	<div id="wrapper">
		<header id="header">
			<div class="container">
				<div class="logo">
					<a href="/">
						<img src="images/logo.png" alt="WMIIG">
					</a>
				</div>
				<div class="logo logo-alt">
					<a href="/">
						<img src="images/logo-alt.png" alt="WMIIG">
					</a>
				</div>
			</div>
		</header>
		<main id="main" role="main">
			<article class="intro">
				<div class="container">
					<h1>Hello, <%= user.getName() %>!</h1>
					<img src="https://graph.facebook.com/<%= user.getId() %>/picture?width=268&height=268" alt="image description">
					<!--<p><span>26 years old</span>Web designer &amp; Developer</p>-->
				</div>
			</article>
			<div id="waitscreen" class="waitorerror">
				<h2 align="center" class="blue">Fetching world's most interesting data...</h2>
				<div id="spinner"></div>
			</div>
			<div id="error" class="waitorerror">
				<h2 align="center" class="blue">An error has occurred</h2>
				<p>Sorry, but we need access to your feed data to generate the world's most interesting infographic.  If you decide to allow us to see your feed data, go <a href="https://www.facebook.com/settings?tab=applications">here</a>, find &quot;Most Interesting Infographic&quot;, delete it, and try logging into our site again.</p>
			</div>
			<div id="content" class="container">
				<section id="topFriendsSection" class="block-friends fade-block">
					<h2 class="blue">Top friends who like your posts</h2>
					<div class="holder">
						<div class="total">
							<p><span class="number blue" id="friends-likes">703</span>Total likes received from <span class="blue">&quot;Top <span id="friends-amount">4</span> friends&quot;</span></p>
						</div>
						<div id="friend-chart" data-json="/test"> <!-- Originally was "inc/friends-likes.json" -->
							<!-- Here insert chart -->
						</div>
					</div>
				</section>
				<section class="block-types fade-block">
					<div class="text-holder">
						<h2 class="green">Your post types</h2>
						<ul id="post-types-list" class="post-types">
							<li><span class="sign">A</span> - <span class="description">Status messages</span></li>
							<li><span class="sign">A</span> - <span class="description">Status messages</span></li>
							<li><span class="sign">A</span> - <span class="description">Status messages</span></li>
							<li><span class="sign">A</span> - <span class="description">Status messages</span></li>
						</ul>
						<p><span id="most-frequent-post-percentage" class="number"></span> of your posts are <span id="most-frequent-post-type"></span></p>
					</div>
					<div id="donut-chart-post-types" data-json="inc/post-types.json">
						<!-- Here insert chart -->
					</div>
				</section>
				<section class="block-frequency fade-block">
					<h2 class="orange fade-block">Your post frequency</h2>
					<div class="fade-block" id="post-frequency-bar-chart" data-json="inc/post-frequency.json">
						<!-- Here insert chart -->
					</div>
					<div class="text-holder fade-block">
						<p><span id="highest-value" class="number orange">3.1</span>highest average <span class="mark">posts/week</span></p>
						<p><span id="lowest-value" class="number blue">0.25</span>lowest average <span class="mark">posts/week</span></p>
					</div>
				</section>
				<section class="block-privacy fade-block">
					<div class="text-holder">
						<h2 class="blue-light">Your post privacy</h2>
						<p><span class="number blue-light">3.1</span>highest average <span class="mark">public posts/week</span></p>
						<p><span class="number blue">2.75</span>highest average <br><span class="mark">private posts/week</span></p>
					</div>
					<div id="post-privacy-line-chart" data-public="inc/post-public.json"  data-private="inc/post-private.json">
						<!-- Here insert chart -->
					</div>
				</section>
				<section class="block-words fade-block">
					<div class="text-holder">
						<h2 class="black">Most used words on your feed</h2>
						<p><span id="top-word" class="large">&quot;Damn&quot;</span>is the most used word on your feed</p>
					</div>
					<div class="htagcloud">
						<ul id="top-words" class="popularity">
						<!-- Insert word cloud here -->
						</ul>
					</div>
				</section>
			</div>
		</main>
		<footer id="footer">
			<div class="container">
				<div class="col">
					<div class="logo">
						<a href="/">
							<img src="images/logo.png" alt="WMIIG">
						</a>
					</div>
					<span class="copyright">Copyright 2015 <a class="copyright-link" href="#">WMIIG</a> | All rights reserved</span>
				</div>
				<div class="col">
					<div class="box">
						<strong class="title">Contact Me</strong>
						<ul class="contact">
							<li><a href="#"><i class="icon-globe"></i> whoischarles.com</a></li>
							<li><a href="mailto:&#099;&#111;&#110;&#116;&#097;&#099;&#116;&#064;&#119;&#104;&#111;&#105;&#115;&#099;&#104;&#097;&#114;&#108;&#101;&#115;&#046;&#099;&#111;&#109;"><i class="icon-envelope-o"></i> &#099;&#111;&#110;&#116;&#097;&#099;&#116;&#064;&#119;&#104;&#111;&#105;&#115;&#099;&#104;&#097;&#114;&#108;&#101;&#115;&#046;&#099;&#111;&#109;</a></li>
						</ul>
					</div>
					<div class="box">
						<strong class="title">Follow Me</strong>
						<ul class="contact">
							<li><a href="#">Github</a></li>
							<li><a href="#">Twitter</a></li>
							<li><a href="#">Facebook</a></li>
						</ul>
					</div>
				</div>
			</div>
		</footer>
	</div>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script type="text/javascript">window.jQuery || document.write('<script src="js/jquery-1.11.2.min.js"><\/script>')</script>
	<script type="text/javascript" src="js/d3.min.js"></script>
	<script type="text/javascript" src="js/spin-2.3.2.min.js"></script>
	<script type="text/javascript" src="js/jquery.main.js"></script>
	<script type="text/javascript" src="js/jquery.spin.js"></script>
</body>
</html>
