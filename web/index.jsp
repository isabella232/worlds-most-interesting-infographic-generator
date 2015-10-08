<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WMIIG</title>
    <link media="all" rel="stylesheet" href="css/main.css">
    <link href='https://fonts.googleapis.com/css?family=Roboto:500,400,300,300italic,100%7CLato:400,300italic,300'
          rel='stylesheet' type='text/css'>
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
        <div class="promo bg-holder">
            <div class="bg-frame"><img src="images/bg.jpg" width="1920" height="658" alt=""></div>
            <article class="text-holder fade-block">
                <h1>Hello!</h1>

                <p>Log in with your Facebook account to see the world’s <span
                        class="mark">most interesting infographic</span></p>
                <a href="/callback" class="btn"><i class="icon-facebook"></i>Login <span class="thin">with</span> Facebook</a>
            </article>
        </div>
        <section class="presentation">
            <div class="container fade-block">
                <div class="img-holder">
                    <img src="images/item-1.jpg" alt="image description">
                </div>
                <div class="text-holder">
                    <h2>What is <span class="medium">this?</span></h2>

                    <p>This entire website is a working example of an application built in a book called <a class="mark"
                                                                                                            href="#">“Mastering
                        OAuth 2”</a></p>

                    <p>This book discusses building client applications that integrate with service providers like
                        Facebook, Twitter, and Instagram, using the OAuth 2 protocol.</p>
                </div>
            </div>
        </section>
        <section class="block-do bg-holder">
            <div class="bg-frame"><img src="images/bg-blue.jpg" width="1920" height="903" alt=""></div>
            <div class="container fade-block">
                <div class="text-holder">
                    <h2>What does <span class="mark">it do?</span></h2>

                    <p>Using OAuth 2, you can integrate with service providers, like Facebook, to say, make the most
                        interesting infographics.</p>

                    <p>This book does exactly this using 2 examples:</p>
                </div>
                <div class="col-holder">
                    <div class="col">
                        <div class="img-holder">
                            <img src="images/example-1.png" alt="image description">
                        </div>
                        <strong class="title">client Side example</strong>
                    </div>
                    <div class="col">
                        <div class="img-holder">
                            <img src="images/example-2.png" alt="image description">
                        </div>
                        <strong class="title">Server side example</strong>
                    </div>
                </div>
            </div>
        </section>
        <section class="block-github">
            <div class="container fade-block">
                <h2>See for yourself</h2>

                <p>It’s aaaallll open source. Embrace and extend!</p>
                <a href="#" class="btn"><i class="icon-github"></i>See <span class="thin">me on</span> Github</a>
                <span class="decor">Buy the book, grok the code, and build the next big application!</span>
            </div>
        </section>
    </main>
    <footer id="footer">
        <div class="container">
            <div class="col">
                <div class="logo">
                    <a href="#">
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
                        <li>
                            <a href="mailto:&#099;&#111;&#110;&#116;&#097;&#099;&#116;&#064;&#119;&#104;&#111;&#105;&#115;&#099;&#104;&#097;&#114;&#108;&#101;&#115;&#046;&#099;&#111;&#109;"><i
                                    class="icon-envelope-o"></i> &#099;&#111;&#110;&#116;&#097;&#099;&#116;&#064;&#119;&#104;&#111;&#105;&#115;&#099;&#104;&#097;&#114;&#108;&#101;&#115;&#046;&#099;&#111;&#109;
                            </a></li>
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
<script type="text/javascript">window.jQuery || document.write(
        '<script src="js/jquery-1.11.2.min.js"><\/script>')</script>
<script type="text/javascript" src="js/d3.min.js"></script>
<script type="text/javascript" src="js/jquery.main.js"></script>
</body>
</html>
