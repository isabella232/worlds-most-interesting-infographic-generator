<%@ page import="com.worldsmostinterestinginfographic.model.object.User" %>
<%@ page import="com.worldsmostinterestinginfographic.model.Model" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User)Model.cache.get(request.getSession().getId() + ".profile");
%>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/jsp/inc/html-head.jsp" %>
<body class="infographic">
<div id="wrapper">
<%@include file="/WEB-INF/jsp/inc/header.jsp" %>
    <main id="main" role="main">
        <article class="intro">
            <div class="container">
                <h1>Hello, <%= user.getName() %>!</h1>
                <img src="https://graph.facebook.com/<%= user.getId() %>/picture?width=268&height=268" alt="profile picture" width="268" height="268">
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
            <section class="block-friends fade-block">
                <h2 class="blue">Top friends who like your posts</h2>
                <div class="holder">
                    <div class="total">
                        <p><span class="number blue" id="friends-likes">703</span>Total likes received from <span class="blue">“Top <span id="friends-amount">4</span> friends”</span></p>
                    </div>
                    <div id="friend-chart" data-json="inc/friends-likes.json">
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
                    <p><span id="max-value" class="number"><span>0</span>%</span> of your post types are <span id="key-word-max" class="green">images</span></p>
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
            <section class="block-popular">
                <h2 class="fade-block">Your most popular posts</h2>
                <div class="box fade-block">
                    <div class="box-holder">
                        <img src="images/image-4.jpg" alt="image description">
                        <div class="text-holder">
                            <p>There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable.</p>
                        </div>
                        <ul class="link-holder">
                            <li><a href="#"><i class="icon-thumbs-o-up"></i>185 likes</a></li>
                            <li><a class="orange" href="#"><i class="icon-comment-o"></i>85 comments</a></li>
                        </ul>
                    </div>
                </div>
                <div class="box fade-block">
                    <div class="box-holder full">
                        <img src="images/image-5.jpg" alt="image description">
                        <ul class="link-holder">
                            <li><a href="#"><i class="icon-thumbs-o-up"></i>185 likes</a></li>
                            <li><a class="orange" href="#"><i class="icon-comment-o"></i>85 comments</a></li>
                        </ul>
                    </div>
                </div>
                <div class="box fade-block">
                    <div class="box-holder">
                        <img src="images/image-4.jpg" alt="image description">
                        <div class="text-holder">
                            <p>There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable.</p>
                        </div>
                        <ul class="link-holder">
                            <li><a href="#"><i class="icon-thumbs-o-up"></i>185 likes</a></li>
                            <li><a class="orange" href="#"><i class="icon-comment-o"></i>85 comments</a></li>
                        </ul>
                    </div>
                </div>
            </section>
            <section class="block-privacy fade-block">
                <div class="text-holder">
                    <h2 class="blue-light">Your post privacy</h2>
                    <p><span id="highest-average-public" class="number blue-light">3.1</span>highest average <span class="mark">public posts/week</span></p>
                    <p><span id="highest-average-private" class="number blue">2.75</span>highest average <br><span class="mark">private posts/week</span></p>
                </div>
                <div id="post-privacy-line-chart" data-public="inc/post-public.json"  data-private="inc/post-private.json">
                    <!-- Here insert chart -->
                </div>
            </section>
            <section class="block-words fade-block">
                <div class="text-holder">
                    <h2 class="black">Your most used words</h2>
                    <p><span class="large">“Damn”</span>is your most used word</p>
                </div>
                <div class="htagcloud">
                    <ul class="popularity">
                        <li class="vv-popular"><a href="#">Excited</a></li>
                        <li class="vvv-popular"><a href="#">Bucharest</a></li>
                        <li class="popular"><a href="#">Honesty</a></li>
                        <li class="vvvvv-popular"><a href="#">Design</a></li>
                        <li class="v-popular"><a href="#">FIFA</a></li>
                        <li class="vvvvvv-popular"><a href="#">Damn</a></li>
                        <li class="v-popular"><a href="#">Deftones</a></li>
                        <li class="vv-popular"><a href="#">Girlfriend</a></li>
                        <li class="v-popular"><a href="#">Creative</a></li>
                        <li class="popular"><a href="#">Games</a></li>
                        <li class="vvvv-popular"><a href="#">F*ck</a></li>
                        <li class="vvvv-popular"><a href="#">Tennis</a></li>
                        <li class="vvv-popular"><a href="#">Tech</a></li>
                    </ul>
                </div>
            </section>
        </div>
    </main>
<%@include file="/WEB-INF/jsp/inc/footer.jsp" %>
</div>
<%@include file="/WEB-INF/jsp/inc/scripts.jsp" %>
<script>
    $.getJSON( "/stats", function( data ) {
        alert(data.data);
        blah = data;
    });
</script>
</body>
</html>
