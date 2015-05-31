<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="java.net.URLEncoder"%>
<%@ page import="com.worldsmostinterestinginfographic.model.Model"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<title>The World's Most Interesting Infographic</title>
		
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
		
		<div id="main">
			<h2>Hello!</h2>
			<p>Log in with your Facebook account to see the world's most interesting infographic!</p>
			<a href="<%= Model.AUTH_ENDPOINT %>?response_type=code&scope=user_posts&client_id=<%= Model.CLIENT_ID %>&redirect_uri=<%= URLEncoder.encode(request.getScheme() + "://" + request.getServerName() + Model.REDIRECT_URI, StandardCharsets.UTF_8.name()) %>">Login with Facebook</a>
		</div>
		
		<div id="what-is-this">
			<p>This entire site is a working example of an application built in a book called <em>Mastering OAuth 2</em>.
			<img src="/images/mastering-oauth2-cover.png" />
			<p>This book discusses building client applications that integrate with service providers (like Facebook, Twitter, Instagram, etc.) using the OAuth 2 protocol.</p>
		</div>
		
		<div id="what-does-it-do">
			<p>Using OAuth 2, you can integrate with service providers, like Facebook, to, say, make the world's most interesting infographics!</p>
			<p>This book does this using 2 examples...</p>
		</div>
		
		<div id="see-for-yourself">
			<p>It's aaalll open source.  Embrace and extend!</p>
		</div>
		
	</body>
</html>