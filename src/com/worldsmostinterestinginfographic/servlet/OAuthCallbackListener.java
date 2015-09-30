package com.worldsmostinterestinginfographic.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.worldsmostinterestinginfographic.collect.result.UserLikeCountPair;
import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;

public class OAuthCallbackListener extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(OAuthCallbackListener.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check for the presence of an authorization code
		String authorizationCode = request.getParameter("code");
		if (authorizationCode != null && authorizationCode.length() > 0) {

			String accessToken = requestAccessToken(authorizationCode, request);
			
			String userJson = requestProfileData(accessToken);
			
			User user = convertUserJsonToObject(userJson);
			
			String postsJson = requestFeedData(accessToken);
			
			List<Post> posts = convertPostsJsonToObject(postsJson);
			
			// Collect statistics
			TopFriendsResult topFriendsResult = StatisticsCollector.collectTopFriends(posts, user);
			Map<Post.Type, Integer> postTypesCount = StatisticsCollector.collectPostTypes(posts);
			
			// Generate output data
			String topFourFriendsJson = buildTopFourFriendsJson(topFriendsResult);
			String postTypesJson = buildPostTypesJson(postTypesCount);
			
			// Send to success page with received profile data
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("posts", posts);
			
			// Include chart data
			request.getSession().setAttribute("topFriendsData", topFourFriendsJson);
			
			// do word count here because it fails in jsp for some reason - didn't investigate too long
			Map<String, Integer> wordMap = new HashMap<String, Integer>();
			for (int i = 0; i < posts.size(); i++) {
				Post post = posts.get(i);
				// String[] messageWordMap = post.getMessage().split(" ");
				String regex = "\\b[A-Za-z]+\\b";

//System.out.println("MESSAGE: " + post.getMessage());
				
				Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(post.getMessage());
				List<String> messageWordList = new ArrayList<String>();
				while (matcher.find()) {
					messageWordList.add(matcher.group());
				}

				String[] messageWordMap = messageWordList.toArray(new String[0]);

				for (int j = 0; j < messageWordMap.length; j++) {
					if (!wordMap.containsKey(messageWordMap[j])) {
						wordMap.put(messageWordMap[j], 0);
					}

					wordMap.put(messageWordMap[j], wordMap.get(messageWordMap[j]) + 1);
				}
			}
			// end
			
			request.getSession().setAttribute("wordMap", wordMap);
			response.sendRedirect("/you-rock");
			
		} else {
			// An error happened during authorization code request. Report it.
			request.getSession().setAttribute("errorMessage", "THIS IS SOME MESSAGE!");
			response.sendRedirect("/uh-oh");
		}

	}
	
	private String buildTopFourFriendsJson(TopFriendsResult topFriendsResult) {
		List<UserLikeCountPair> topFriends = topFriendsResult.getTopFriends();
		return "{" +
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
	}
	
	private String buildPostTypesJson(Map<Post.Type, Integer> postTypesCount) {
		return "[{" +
				"	\"types\": [" +
				"		{" +
				"			\"type\": \"A\"," +
				"			\"value\": 40," +
				"			\"description\": \"Status messages\"," +
				"			\"color\": \"#3b5998\"" +
				"		}," +
				"		{" +
				"			\"type\": \"B\"," +
				"			\"value\": 45," +
				"			\"description\": \"Image Post\"," +
				"			\"color\": \"#5bc0bd\"" +
				"		}," +
				"		{" +
				"			\"type\": \"C\"," +
				"			\"value\": 10," +
				"			\"description\": \"Shared link\"," +
				"			\"color\": \"#2ebaeb\"" +
				"		}," +
				"		{" +
				"			\"type\": \"D\"," +
				"			\"value\": 17," +
				"			\"description\": \"Video Post\"," +
				"			\"color\": \"#f08a4b\"" +
				"		}" +
				"	]" +
				"}]";
	}
	
	private String requestAccessToken(String authorizationCode, HttpServletRequest request) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// Exchange authorization code for access token
			HttpPost httpPost = new HttpPost(Model.TOKEN_ENDPOINT + "?grant_type=authorization_code&code=" + authorizationCode + "&redirect_uri=" + URLEncoder.encode((request.getScheme() + "://" + request.getServerName() + Model.REDIRECT_URI), StandardCharsets.UTF_8.name()) + "&client_id=" + Model.CLIENT_ID + "&client_secret=" + Model.CLIENT_SECRET);
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
			String requestUrl = "https://graph.facebook.com/v2.2/me/feed?limit=500&access_token=" + accessToken;
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
			System.out.println(userObject.getString("id"));
			user = new User(Long.valueOf(userObject.getString("id")),
					userObject.getString("first_name"), 
					userObject.getString("last_name"), 
					userObject.getString("name"),
					userObject.getString("link"),
					userObject.getString("gender"));
			
			System.out.println("XX: " + user.getId());
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
			
			
			post = new Post(id, type, from, message, statusType, likes);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return post;
	}
}
