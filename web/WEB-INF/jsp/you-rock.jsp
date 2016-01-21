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
                    <p><span id="most-frequent-post-percentage" class="number"></span> of your posts are <span id="most-frequent-post-type"></span></p>
                </div>
                <div id="donut-chart-post-types" data-json="inc/post-types.json">
                    <!-- Here insert chart -->
                </div>
            </section>
            <section class="block-frequency fade-block">
                <h2 class="orange fade-block">Posts by day of the week</h2>
                <div class="fade-block" id="daily-post-frequency-bar-chart">
                    <!-- Here insert chart -->
                </div>
                <div class="text-holder fade-block">
                    <p><span id="weekly-average" class="number blue-light">0.25</span>average posts <span class="mark">per week</span></p>
                    <p><span id="highest-value" class="number orange">3.1</span>most active on <span id="highest-day" class="mark">Monday</span></p>
                </div>
            </section>
            <section class="block-privacy fade-block">
                <div class="text-holder">
                    <h2 class="blue-light">Your post frequency</h2>
                    <p><span id="highest-average-public" class="number blue-light">3.1</span>highest average <span class="mark">public posts/week</span></p>
                    <p><span id="highest-average-private" class="number blue">2.75</span>highest average <br><span class="mark">private posts/week</span></p>
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
<%@include file="/WEB-INF/jsp/inc/footer.jsp" %>
</div>
<%@include file="/WEB-INF/jsp/inc/scripts.jsp" %>
<script>
    $.getJSON( "/stats", function( data ) {

        // Get rid of the spinner
        $('#content').show();
        $('#waitscreen').hide();
        $(this).spin(false);

        // Start rendering the graphs
        initTopFriendsChart(data.TOP_FRIENDS);
        initPostTypesChart(data.POST_TYPES);
        initDailyPostFrequencyChart(data.DAILY_POST_FREQUENCY);
        initLineBar(data.MONTHLY_POST_FREQUENCY);
        initWordChart(data.TOP_WORDS);

    });
</script>
</body>
</html>
