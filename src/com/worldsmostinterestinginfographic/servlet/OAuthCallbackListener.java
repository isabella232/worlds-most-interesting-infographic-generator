package com.worldsmostinterestinginfographic.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.worldsmostinterestinginfographic.collect.StatisticsCollector;
import com.worldsmostinterestinginfographic.collect.result.TopFriendsResult;
import com.worldsmostinterestinginfographic.collect.result.TopWordsResult;
import com.worldsmostinterestinginfographic.collect.result.UserLikeCountPair;
import com.worldsmostinterestinginfographic.collect.result.WordCountPair;
import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.util.LoggingUtil;
import com.worldsmostinterestinginfographic.util.Minify;

public class OAuthCallbackListener extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(OAuthCallbackListener.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Our clock
		long tick = Long.MIN_VALUE;
		
		// Check for the presence of an authorization code
		String authorizationCode = request.getParameter("code");
		if (authorizationCode != null && authorizationCode.length() > 0) {

			log.info("Starting session.  Requesting access token with authorization code " + LoggingUtil.anonymize(authorizationCode));
			tick = System.currentTimeMillis();
			
			String accessToken = requestAccessToken(authorizationCode, request);
			
			log.info("Access token " + LoggingUtil.anonymize(accessToken) + " received.  Requesting profile data. (" + (System.currentTimeMillis() - tick) + "ms)");
			tick = System.currentTimeMillis();
			
			String userJson = requestProfileData(accessToken);
			User user = convertUserJsonToObject(userJson);
			
			log.info("Hello, " + LoggingUtil.anonymize(Objects.toString(user.getName() + ":" + user.getId())) + "!  Requesting " + Model.MAX_NUMBER_OF_FACEBOOK_POSTS_TO_REQUEST
					+ " stories for you. (" + (System.currentTimeMillis() - tick) + "ms)");
			tick = System.currentTimeMillis();
			
			// Send to success page with received profile data
			request.getSession().setAttribute("user", user);
			Model.cache.put(user.getId() + ".profile", user);
			Model.cache.put(user.getId() + ".token", accessToken);
			
			response.sendRedirect("/you-rock");
			
			
			/*
			System.out.println("Beginning of last stuff");
			String postsJson = requestFeedData(accessToken);
			List<Post> posts = convertPostsJsonToObject(postsJson);
			
			log.info("Received " + posts.size() + " stories for user " + LoggingUtil.anonymize(Objects.toString(user.getId())) 
					+ ". Collecting statistics... (" + (System.currentTimeMillis() - tick) + "ms)");
			tick = System.currentTimeMillis();
			
			// Collect statistics
			TopFriendsResult topFriendsResult = StatisticsCollector.collectTopFriends(posts, user);
			Map<Post.Type, Integer> postTypesCount = StatisticsCollector.collectPostTypes(posts);
			int[] postsByDayOfWeek = StatisticsCollector.collectPostFrequencyByDayOfWeek(posts);
			int[] postsByMonthOfYear = StatisticsCollector.collectPostFrequencyByMonthOfYear(posts);
			TopWordsResult topWordsResult = StatisticsCollector.collectWordFrequency(posts, user);
			
			log.info("Statistics have been collected for " + LoggingUtil.anonymize(Objects.toString(user.getId())) + ". (" + (System.currentTimeMillis() - tick) + "ms)");
			tick = System.currentTimeMillis();
			
			// Generate output data
			String topFourFriendsJson = buildTopFourFriendsJson(topFriendsResult);
			String postTypesJson = buildPostTypesJson(postTypesCount);
			String mostFrequentPostTypeJson = buildMostFrequentPostTypeJson(postTypesCount);
			String postsByDayOfWeekJson = buildPostsByDayOfWeekJson(postsByDayOfWeek);
			String postsByMonthOfYearJson = buildPostsByMonthOfYearJson(postsByMonthOfYear);
			String topWordsHtml = buildTopWordsHtml(topWordsResult);
			
			// Send to success page with received profile data
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("posts", posts);
			
			// Include chart data
			request.getSession().setAttribute("topFriendsJson", topFourFriendsJson);
			request.getSession().setAttribute("postTypesJson", postTypesJson);
			request.getSession().setAttribute("mostFrequentPostTypeJson", mostFrequentPostTypeJson);
			request.getSession().setAttribute("postsByDayOfWeekJson", postsByDayOfWeekJson);
			request.getSession().setAttribute("postsByMonthOfYearJson", postsByMonthOfYearJson);
			request.getSession().setAttribute("topWordsHtml", topWordsHtml);
			request.getSession().setAttribute("topWord", topWordsResult.getTopWords(1).get(0).getWord());
			
			System.out.println("End of last stuff");
//			response.sendRedirect("/you-rock");
			//*/
		} else if (request.getParameter("error") != null) {
			
			String error = request.getParameter("error");
			String errorDescription = request.getParameter("error_description");
			
			// An error happened during authorization code request
			log.severe("Error encountered during authorization code request: " + error + " - " + errorDescription);

			request.getSession().setAttribute("error", error);
			request.getSession().setAttribute("errorDescription", errorDescription);
			response.sendRedirect("/uh-oh");
		} else {
			log.warning("An unknown error encountered at redirection endpoint");
			response.sendRedirect("/uh-oh");
		}

	}
	
	private String buildTopFourFriendsJson(TopFriendsResult topFriendsResult) {
		List<UserLikeCountPair> topFriends = topFriendsResult.getTopFriends();
		String json = "{" +
				"	\"friends\": [" +
				"		{" +
				"			\"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(0).getUser().getId() + "/picture?width=85&height=85\"," +
				"			\"likes\": " + topFriends.get(0).getCount() + "," +
				"			\"name\": \"" + topFriends.get(0).getUser().getName() + "\"," +
				"			\"color\": \"#3b5998\"" +
				"		}," +
				"		{" +
				"			\"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(1).getUser().getId() + "/picture?width=85&height=85\"," +
				"			\"likes\": " + topFriends.get(1).getCount() + "," +
				"			\"name\": \"" + topFriends.get(1).getUser().getName() + "\"," +
				"			\"color\": \"#5bc0bd\"" +
				"		}," +
				"		{" +
				"			\"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(2).getUser().getId() + "/picture?width=85&height=85\"," +
				"			\"likes\": " + topFriends.get(2).getCount() + "," +
				"			\"name\": \"" + topFriends.get(2).getUser().getName() + "\"," +
				"			\"color\": \"#f08a4b\"" +
				"		}," +
				"		{" +
				"			\"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(3).getUser().getId() + "/picture?width=85&height=85\"," +
				"			\"likes\": " + topFriends.get(3).getCount() + "," +
				"			\"name\": \"" + topFriends.get(3).getUser().getName() + "\"," +
				"			\"color\": \"#1c2541\"" +
				"		}" +
				"	]" +
				"}";
		
		return new Minify().minify(json);
	}
	
	private String buildPostTypesJson(Map<Post.Type, Integer> postTypesCount) {
		String json = "{" +
				"	\"types\": [" +
				"		{" +
				"			\"type\": \"" + postTypesCount.get(Post.Type.STATUS) + "\"," +
				"			\"value\": " + postTypesCount.get(Post.Type.STATUS) + "," + 
				"			\"description\": \"Status Update\"," +
				"			\"color\": \"#3b5998\"" +
				"		}," +
				"		{" +
				"			\"type\": \"" + postTypesCount.get(Post.Type.PHOTO) + "\"," +
				"			\"value\": " + postTypesCount.get(Post.Type.PHOTO) + "," + 
				"			\"description\": \"Image Post\"," +
				"			\"color\": \"#5bc0bd\"" +
				"		}," +
				"		{" +
				"			\"type\": \"" + postTypesCount.get(Post.Type.LINK) + "\"," +
				"			\"value\": " + postTypesCount.get(Post.Type.LINK) + "," + 
				"			\"description\": \"Shared Link\"," +
				"			\"color\": \"#2ebaeb\"" +
				"		}," +
				"		{" +
				"			\"type\": \"" + postTypesCount.get(Post.Type.VIDEO) + "\"," +
				"			\"value\": " + postTypesCount.get(Post.Type.VIDEO) + "," + 
				"			\"description\": \"Video Post\"," +
				"			\"color\": \"#f08a4b\"" +
				"		}" +
				"	]" +
				"}";
		
		return new Minify().minify(json);
	}
	
	private String buildMostFrequentPostTypeJson(Map<Post.Type, Integer> postTypesCount) {

		Post.Type mostFrequentPostType = null;
		int numPosts = 0;
		for (Map.Entry<Post.Type, Integer> entry : postTypesCount.entrySet()) {
			if (mostFrequentPostType == null ||postTypesCount.get(entry.getKey()) > postTypesCount.get(mostFrequentPostType)) {
				mostFrequentPostType = entry.getKey();
			}
			
			numPosts += entry.getValue();
			
//		    System.out.println(entry.getKey() + "/" + entry.getValue());
		}

		String postTypesCountJson = "{" +
				"	\"type\": \"" + (mostFrequentPostType.equals(Post.Type.STATUS) ? "status updates" : 
					mostFrequentPostType.equals(Post.Type.PHOTO) ? "photos" : 
						mostFrequentPostType.equals(Post.Type.LINK) ? "shared links" : "videos") + "\"," +
				"	\"percentage\": " + (mostFrequentPostType.equals(Post.Type.STATUS) ? postTypesCount.get(Post.Type.STATUS) * 100 / numPosts : 
					mostFrequentPostType.equals(Post.Type.PHOTO) ? postTypesCount.get(Post.Type.PHOTO) * 100 / numPosts : 
						mostFrequentPostType.equals(Post.Type.LINK) ? postTypesCount.get(Post.Type.LINK) * 100 / numPosts : postTypesCount.get(Post.Type.VIDEO) * 100 / numPosts) + "," +
				"	\"color\": \"" + (mostFrequentPostType.equals(Post.Type.STATUS) ? "blue" : 
					mostFrequentPostType.equals(Post.Type.PHOTO) ? "green" : 
						mostFrequentPostType.equals(Post.Type.LINK) ? "blue-light" : "orange") + "\"" +
				"}";
		
		String result = new Minify().minify(postTypesCountJson);
//		System.out.println("AA: " + result);
		
		return result;
	}
	
	private String buildPostsByDayOfWeekJson(int[] postsByDayOfWeek) {
		String json = "{" +
				"	\"frequency\": [" +
				"		{" +
				"			\"lowest\": " + postsByDayOfWeek[1] + "," +
				"			\"highest\": " + postsByDayOfWeek[1] + "," +
				"			\"month\": \"Mon\"" +
				"		}," +
				"		{" +
				"			\"lowest\": " + postsByDayOfWeek[2] + "," +
				"			\"highest\": " + postsByDayOfWeek[2] + "," +
				"			\"month\": \"Tue\"" +
				"		}," +
				"		{" +
				"			\"lowest\": " + postsByDayOfWeek[3] + "," +
				"			\"highest\": " + postsByDayOfWeek[3] + "," +
				"			\"month\": \"Wed\"" +
				"		}," +
				"		{" +
				"			\"lowest\": " + postsByDayOfWeek[4] + "," +
				"			\"highest\": " + postsByDayOfWeek[4] + "," +
				"			\"month\": \"Thu\"" +
				"		}," +
				"		{" +
				"			\"lowest\": " + postsByDayOfWeek[5] + "," +
				"			\"highest\": " + postsByDayOfWeek[5] + "," +
				"			\"month\": \"Fri\"" +
				"		}," +
				"		{" +
				"			\"lowest\": " + postsByDayOfWeek[6] + "," +
				"			\"highest\": " + postsByDayOfWeek[6] + "," +
				"			\"month\": \"Sat\"" +
				"		}," +
				"		{" +
				"			\"lowest\": " + postsByDayOfWeek[0] + "," +
				"			\"highest\": " + postsByDayOfWeek[0] + "," +
				"			\"month\": \"Sun\"" +
				"		}" +
				"	]" +
				"}";
		
		return new Minify().minify(json);
	}
	
	private String buildPostsByMonthOfYearJson(int[] postsByMonthOfYear) {
		String json = "{" +
				"	\"private\": [" +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[0] + "," +
				"			\"percent\": 2" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[1] + "," +
				"			\"percent\": 11" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[2] + "," +
				"			\"percent\": 20" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[3] + "," +
				"			\"percent\": 30" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[4] + "," +
				"			\"percent\": 51" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[5] + "," +
				"			\"percent\": 57" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[6] + "," +
				"			\"percent\": 70" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[7] + "," +
				"			\"percent\": 75" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[8] + "," +
				"			\"percent\": 88" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[9] + "," +
				"			\"percent\": 100" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[10] + "," +
				"			\"percent\": 110" +
				"		}," +
				"		{" +
				"			\"value\": " + postsByMonthOfYear[11] + "," +
				"			\"percent\": 120" +
				"		}" +
				"	]," +
				"	\"color\": \"#3a5897\"" +
				"}";
		
		return new Minify().minify(json);
	}
	
	private String buildTopWordsHtml(TopWordsResult result) {
		List<WordCountPair> topWords = result.getTopWords(15);
		
//		System.out.println(topWords.size());
		
		
		List<String> wordsHtml = new ArrayList<String>(topWords.size());
		int emphasis = 5;
		int previousCount = topWords.get(0).getCount();
		for (int i = 0; i < topWords.size(); i++) {
//			System.out.println(wordCountPair.getWord() + ": " + wordCountPair.getCount());
			
			if (emphasis > 0 && topWords.get(i).getCount() < previousCount) {
				emphasis--;
			}
			
			wordsHtml.add("<li class=\"" + giveMeVees(emphasis) + (emphasis > 0 ? "-" : "") + "popular\"><a href=\"#\">" + topWords.get(i).getWord() + "</a></li>");
		}
		
		Collections.shuffle(wordsHtml);
		
		String html = "";
		for (String wordHtml : wordsHtml) {
			html += wordHtml;
		}
		
//		String html = "<li class=\"vv-popular\"><a href=\"#\">Excited</a></li>" +
//				"<li class=\"vvv-popular\"><a href=\"#\">Bucharest</a></li>" +
//				"<li class=\"popular\"><a href=\"#\">Honesty</a></li>" +
//				"<li class=\"vvvvv-popular\"><a href=\"#\">Design</a></li>" +
//				"<li class=\"v-popular\"><a href=\"#\">FIFA</a></li>" +
//				"<li class=\"vvvvvv-popular\"><a href=\"#\">Damn</a></li>" +
//				"<li class=\"v-popular\"><a href=\"#\">Deftones</a></li>" +
//				"<li class=\"vv-popular\"><a href=\"#\">Girlfriend</a></li>" +
//				"<li class=\"v-popular\"><a href=\"#\">Creative</a></li>" +
//				"<li class=\"popular\"><a href=\"#\">Games</a></li>" +
//				"<li class=\"vvvv-popular\"><a href=\"#\">F*ck</a></li>" +
//				"<li class=\"vvvv-popular\"><a href=\"#\">Tennis</a></li>" +
//				"<li class=\"vvv-popular\"><a href=\"#\">Tech</a></li>";

		return html;
	}
	
	private String giveMeVees(int numVees) {
		String vees = "";
		
		for (int i = 0; i < numVees; i++) {
			vees += "v";
		}
		
		return vees;
	}
	
	private String requestAccessToken(String authorizationCode, HttpServletRequest request) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// Exchange authorization code for access token
			HttpPost httpPost = new HttpPost(Model.TOKEN_ENDPOINT + "?grant_type=authorization_code&code=" + authorizationCode + "&redirect_uri=" + URLEncoder.encode((request.getScheme() + "://" + request.getServerName() + Model.REDIRECTION_ENDPOINT), StandardCharsets.UTF_8.name()) + "&client_id=" + Model.CLIENT_ID + "&client_secret=" + Model.CLIENT_SECRET);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String line = bufferedReader.readLine();
			String accessToken = line.split("&")[0].split("=")[1];
			return accessToken;
		} finally {
			httpClient.close();
		}
	}
	
	private String requestProfileData(String accessToken) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// Use access token to request profile data
			String requestUrl = "https://graph.facebook.com/v2.2/me?fields=" + Model.FACEBOOK_REQUESTED_PROFILE_FIELDS;
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(requestUrl);
			httpPost.addHeader("Authorization", "Bearer " + accessToken);
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("method", "get"));
			httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String userJson = bufferedReader.readLine();
			return userJson;
		} finally {
			httpClient.close();
		}
	}
	
	private String requestFeedData(String accessToken) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// User access token to request posts
			String requestUrl = "https://graph.facebook.com/v2.2/me/feed?limit=" + Model.MAX_NUMBER_OF_FACEBOOK_POSTS_TO_REQUEST + "&access_token=" + accessToken;
			httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(requestUrl);
			HttpResponse httpResponse = httpClient.execute(get);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String allPostsJson = bufferedReader.readLine();
//			log.info(allPostsJson);
			return allPostsJson;
		} finally {
			httpClient.close();
		}
	}
	
	private User convertUserJsonToObject(String userJson) {
		User user = null;
		try {
			JSONObject userObject = new JSONObject(userJson);
			user = new User(Long.valueOf(userObject.getString("id")),
					userObject.getString("first_name"), 
					userObject.getString("last_name"), 
					userObject.getString("name"),
					userObject.getString("link"),
					userObject.getString("gender"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	private List<Post> convertPostsJsonToObject(String postsJson) {
		List<Post> posts = new ArrayList<Post>();
		try {
			JSONObject allPostsObject = new JSONObject(postsJson);
			JSONArray allPostsArray = new JSONArray(allPostsObject.getString("data"));
			
			for (int i = 0; i < allPostsArray.length(); i++) {
//System.out.println("Adding post " + i);
				JSONObject postObject = (JSONObject)allPostsArray.get(i);
				Post post = parseJsonPost(postObject);
				posts.add(post);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return posts;
	}
	
	private Post parseJsonPost(JSONObject jsonPost) {
//		System.out.println(jsonPost);
		if (jsonPost == null) {
			return null;
		}
		
		Post post = null;
		try {
			
			String id = jsonPost.getString("id");
			Post.Type type = Post.Type.valueOf(jsonPost.getString("type").toUpperCase());
//			User from = new User(jsonPost.getJSONObject("from"))
			String message = jsonPost.has("message") ? jsonPost.getString("message") : "";
			String statusType = jsonPost.has("status_type") ? jsonPost.getString("status_type") : null;
			String createdDateString = jsonPost.has("created_time") ? jsonPost.getString("created_time") : null;
			
			
			
			// get the date using Java Date
			/*
			SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			Date createdDate = null;
			try {
				createdDate = incomingFormat.parse(createdDateString);
				System.out.println(createdDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}*/
			
			
			// get the date using Java Calendar
			Calendar createdDate = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			try {
				createdDate.setTime(sdf.parse(createdDateString));// all done
//				System.out.println(new Date(createdDate.getTimeInMillis()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
//			log.info(jsonPost.getJSONObject("from").toString());
			
			
			// playing around with getting FROM
			User from = null;
			boolean hasFrom = jsonPost.has("from");
			if (hasFrom) {
//				log.info("Has from");
//				String fromString = jsonPost.getString("from");
//				JSONArray fromArray = jsonPost.getJSONArray("from");
//				log.info("XXX: " + fromString);
				JSONObject fromObject = jsonPost.getJSONObject("from");
				
//				System.out.println("String: " + fromObject.getString("id") + "\tLong: " + fromObject.getLong("id"));
				
				from = new User(Long.valueOf(fromObject.getString("id")), fromObject.getString("name"));
//				log.info("YYY: " + fromObject.getString("name"));
			} else {
				log.info("No from");
			}
//			JSONArray fromArray = jsonPost.has("from") ? jsonPost.getJSONArray("from") : null;
//			log.info(fromArray.toString());
			
			
			List<User> likes = new ArrayList<User>();
			boolean hasLikes = jsonPost.has("likes");
			if (hasLikes) {
//				log.info("Has from");
//				String fromString = jsonPost.getString("from");
//				JSONArray fromArray = jsonPost.getJSONArray("from");
//				log.info("XXX: " + fromString);
				JSONArray likesArray = jsonPost.getJSONObject("likes").getJSONArray("data");
				for (int i = 0; i < likesArray.length(); i++) {
					JSONObject likerObject = (JSONObject)likesArray.get(i);
					User liker = new User(Long.valueOf(likerObject.getString("id")), likerObject.getString("name"));
					likes.add(liker);
				}
//				from = new User(fromObject.getLong("id"), fromObject.getString("name"));
//				log.info("YYY: " + fromObject.getString("name"));
			} else {
//				log.info("No likes");
			}
			
			
			post = new Post(id, type, from, message, statusType, likes, createdDate);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return post;
	}
}
