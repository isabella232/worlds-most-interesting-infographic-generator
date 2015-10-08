<%@ page import="com.worldsmostinterestinginfographic.model.Model" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
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
                <a href="<%=Model.AUTHORIZATION_ENDPOINT%>?response_type=code&scope=user_posts&client_id=<%= Model.CLIENT_ID %>&redirect_uri=<%=URLEncoder.encode(request.getScheme() + "://" + request.getServerName() + Model.REDIRECTION_ENDPOINT, StandardCharsets.UTF_8.name())%>" class="btn"><i class="icon-facebook"></i>Login <span class="thin">with</span> Facebook</a>
            </article>
        </div>
    </main>
<%@include file="/WEB-INF/jsp/inc/footer.jsp" %>
</div>
<%@include file="/WEB-INF/jsp/inc/scripts.jsp" %>
</body>
</html>
