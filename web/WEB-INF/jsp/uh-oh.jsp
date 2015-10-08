<%--
  Created by IntelliJ IDEA.
  User: Charles
  Date: 10/7/2015
  Time: 11:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WMIIG</title>
    <link media="all" rel="stylesheet" href="css/main.css">
    <link href='https://fonts.googleapis.com/css?family=Roboto:500,400,300,300italic,100%7CLato:400,300italic,300' rel='stylesheet' type='text/css'>
</head>
<body>
<div id="wrapper">
    <header id="header">
        <div class="container">
            <div class="logo">
                <a href="#">
                    <img src="images/logo.png" alt="WMIIG">
                </a>
            </div>
            <div class="logo logo-alt">
                <a href="#">
                    <img src="images/logo-alt.png" alt="WMIIG">
                </a>
            </div>
            <div class="github">
                <a href="#">
                    <i class="icon-github"></i>
                    See <span class="thin">me on</span> Github
                </a>
            </div>
        </div>
    </header>
    <main id="main" role="main">
        <section class="error" style="background-image:url(images/bg-error.jpg);">
            <span class="overlay"></span>
            <div class="text-wrap">
                <div class="text-holder">
                    <h1>Something is definitely wrong!</h1>
                    <p>An error occured and you’ve ended up here.<br> You’ll probably fix it somehow.</p>
                    <a href="#" class="btn">Back <span class="thin">to</span> Homepage</a>
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
