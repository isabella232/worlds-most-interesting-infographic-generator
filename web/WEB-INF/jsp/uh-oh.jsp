<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/jsp/inc/html-head.jsp" %>
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
<%@include file="/WEB-INF/jsp/inc/scripts.jsp" %>
</body>
</html>
