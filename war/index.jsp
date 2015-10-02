<%@page import="com.worldsmostinterestinginfographic.model.Model"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="java.net.URLEncoder"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%><!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="The World's Most Interesting Infographic Generator" />
	<meta name="keywords" content="OAuth, OAuth2, WMIIG, worlds most interesting infographic generator, Facebook, token, client id, client secret" />
	<title>The World's Most Interesting Infographic Generator</title>
	<link media="all" rel="stylesheet" href="css/main.css">
	<link rel="icon" type="image/png" href="images/favicon.png" />
	<link href='https://fonts.googleapis.com/css?family=Roboto:500,400,300,300italic,100%7CLato:400,300italic,300' rel='stylesheet' type='text/css'>
	
	<!-- Google Analytics -->
	<script>
		(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
		ga('create', 'UA-60525977-1', 'auto');
		ga('send', 'pageview');
	</script>
</head>
<body>
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
			<div class="promo bg-holder">
				<div class="bg-frame"><img src="images/bg.jpg" width="1920" height="658" alt=""></div>
				<article class="text-holder fade-block">
					<h1>Hello!</h1>
					<p>Log in with your Facebook account to see the world's <span class="mark">most interesting infographic</span></p>
					<a href="<%=Model.AUTHORIZATION_ENDPOINT%>?response_type=code&scope=user_posts&client_id=<%= Model.CLIENT_ID %>&redirect_uri=<%=URLEncoder.encode(request.getScheme() + "://" + request.getServerName() + Model.REDIRECTION_ENDPOINT, StandardCharsets.UTF_8.name())%>" class="btn"><i class="icon-facebook"></i>Login <span class="thin">with</span> Facebook</a>
				</article>
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
	<script type="text/javascript" src="js/jquery.main.js"></script>
</body>
</html>
