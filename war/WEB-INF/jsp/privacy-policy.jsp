<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>The World's Most Interesting Infographic Generator</title>
	
	<!-- Google Analytics -->
		<script>
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
			})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
			ga('create', 'UA-60525977-1', 'auto');
			ga('send', 'pageview');
		</script>
	<link media="all" rel="stylesheet" href="css/main.css">
	<link href='https://fonts.googleapis.com/css?family=Roboto:500,400,300,300italic,100%7CLato:400,300italic,300' rel='stylesheet' type='text/css'>
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
			<section class="error" style="background-image:url(images/bg-error.jpg);">
				<span class="overlay"></span>
				<div class="text-wrap">
					<div class="text-holder">
						<h1>Privacy Policy</h1>
						<p>We don't store anything other than logs.  And we don't log any personally identifiable data.
						Anything that we do log is for performance and reliability purposes.  Any data gathered for a person
						during a session will expire once the session expires.</p>
						<a href="/" class="btn">Back <span class="thin">to</span> Homepage</a>
					</div>
				</div>
			</section>
		</main>
	</div>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script type="text/javascript">window.jQuery || document.write('<script src="js/jquery-1.11.2.min.js"><\/script>')</script>
	<script type="text/javascript" src="js/d3.min.js"></script>
	<script type="text/javascript" src="js/jquery.main.js"></script>
</body>
</html>
