<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/jsp/inc/html-head.jsp" %>
<body>
<div id="wrapper">
<%@include file="/WEB-INF/jsp/inc/header.jsp" %>
    <main id="main" role="main">
        <div class="promo bg-holder">
            <div class="bg-frame"><img src="images/bg.jpg" width="1920" height="658" alt=""></div>
            <article class="text-holder fade-block">
                <h1>Hello!</h1>

                <p>Log in with your Facebook account to see the world’s <span
                        class="mark">most interesting infographic</span></p>
                <a href="/callback" class="btn"><i class="icon-facebook"></i>Login <span class="thin">with</span>
                    Facebook</a>
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
<%@include file="/WEB-INF/jsp/inc/footer.jsp" %>
</div>
<%@include file="/WEB-INF/jsp/inc/scripts.jsp" %>
</body>
</html>
