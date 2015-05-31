<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.worldsmostinterestinginfographic.model.object.Post"%>
<%@page import="com.worldsmostinterestinginfographic.model.object.User"%>
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
		<p>You rock!</p>
		<p>
			This is you...
			<ul>
				<%
				User user = (User)request.getSession().getAttribute("user");
				List<Post> posts = (List<Post>)request.getSession().getAttribute("posts");
				%>
				<li>First name: <%= user.getFirstName() %></li>
				<li>Last name: <%= user.getLastName() %></li>
				<li>Name: <%= user.getName() %></li>
				<li>Link: <%= user.getLink() %></li>
				<li>Gender: <%= user.getGender() %></li>
				<li>Posts: total=<%= posts.size() %></li>
				<li><%
					// post type stats
					Map<Post.Type, Integer> postTypes = new HashMap<Post.Type, Integer>();
					for (int i = 0; i < posts.size(); i++) {
						Post post = posts.get(i);
						if (!postTypes.containsKey(post.getType())) {
							postTypes.put(post.getType(), 0);
						}
						
						postTypes.put(post.getType(), postTypes.get(post.getType()) + 1);
					}
					for (int i = 0; i < Post.Type.values().length; i++) {
						out.println(Post.Type.values()[i] + ": " + postTypes.get(Post.Type.values()[i]) + "<br>");
					}
					
					// message contents stats
					Map<String, Integer> wordMap = (Map<String, Integer>)request.getSession().getAttribute("wordMap");
					for (int i = 0; i < wordMap.keySet().size(); i++) {
						String[] usedWords = wordMap.keySet().toArray(new String[0]);
						if (wordMap.get(usedWords[i]) > 4) {
							out.println(usedWords[i] + ": " + wordMap.get(usedWords[i]) + "<br>");
						}
					}
					%>
				</li>
			</ul>
	</body>
</html>