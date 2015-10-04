package com.worldsmostinterestinginfographic.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

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

public class Test extends HttpServlet {

	private static final Logger log = Logger.getLogger(Test.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		long tick = System.currentTimeMillis();
		
		User user = (User)request.getSession().getAttribute("user");
		String accessToken = Objects.toString(Model.cache.get(user.getId() + ".token"));
		
		String postsJson = requestFeedData(accessToken);
		
		List<Post> posts = convertPostsJsonToObject(postsJson);
		if (posts.size() <= 0) {
			out.println("[]");
			return;
		}
		
		log.info("Received " + posts.size() + " stories for user " + LoggingUtil.anonymize(Objects.toString(user.getId())) 
				+ ". Collecting statistics... (" + (System.currentTimeMillis() - tick) + "ms)");
		tick = System.currentTimeMillis();
		
		// Collect statistics
		TopFriendsResult topFriendsResult = StatisticsCollector.collectTopFriends(posts, user);
		Map<Post.Type, Integer> postTypesCount = StatisticsCollector.collectPostTypes(posts);
		int[] postsByDayOfWeek = StatisticsCollector.collectPostFrequencyByDayOfWeek(posts);
		int[] postsByMonthOfYear = StatisticsCollector.collectPostFrequencyByMonthOfYear(posts);
		TopWordsResult topWordsResult = StatisticsCollector.collectWordFrequency(posts, user);
		
		String topFourFriendsJson = buildTopFourFriendsJson(topFriendsResult);
		String postTypesJson = buildPostTypesJson(postTypesCount);
		String postsByDayOfWeekJson = buildPostsByDayOfWeekJson(postsByDayOfWeek);
		String postsByMonthOfYearJson = buildPostsByMonthOfYearJson(postsByMonthOfYear);
		
		String mostFrequentPostTypeJson = buildMostFrequentPostTypeJson(postTypesCount);
		String topWordsJson = buildTopWordsJson(topWordsResult);
//		String topWordsHtml = buildTopWordsHtml(topWordsResult);
		
//		log.info("Statistics have been collected for " + LoggingUtil.anonymize(Objects.toString(user.getId())) + ". (" + (System.currentTimeMillis() - tick) + "ms)");
//		tick = System.currentTimeMillis();
		
//		System.out.println(postTypesJson);
		String result = "";
		try {
			JSONObject obj0 = new JSONObject(topFourFriendsJson);
			JSONObject obj1 = new JSONObject(postTypesJson);
//			JSONObject obj2 = new JSONObject(mostFrequentPostTypeJson);
			JSONObject obj2 = new JSONObject(postsByDayOfWeekJson);
			JSONObject obj3 = new JSONObject(postsByMonthOfYearJson);
//			System.out.println(mostFrequentPostTypeJson);
			JSONObject obj4 = new JSONObject(mostFrequentPostTypeJson);
			JSONObject obj5 = new JSONObject(topWordsJson);

			
			
			JSONArray arr = new JSONArray();
			arr.put(obj0);
			arr.put(obj1);
//			arr.put(obj2);
			arr.put(obj2);
			arr.put(obj3);
			arr.put(obj4);
			arr.put(obj5);
			result = arr.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		System.out.println(result);
		
		
		// Francis' data
//		try {
//			result = new JSONArray("[{\"friends\":[{\"color\":\"#3b5998\",\"imgSrc\":\"https://graph.facebook.com/10155520384595322/picture?width=85&height=85\",\"name\":\"Emily To\",\"likes\":19},{\"color\":\"#5bc0bd\",\"imgSrc\":\"https://graph.facebook.com/896464570395145/picture?width=85&height=85\",\"name\":\"Kenny Wong\",\"likes\":11},{\"color\":\"#f08a4b\",\"imgSrc\":\"https://graph.facebook.com/10155500478635702/picture?width=85&height=85\",\"name\":\"Arrin Linch\",\"likes\":10},{\"color\":\"#1c2541\",\"imgSrc\":\"https://graph.facebook.com/10105035261926668/picture?width=85&height=85\",\"name\":\"Krystle Fu\",\"likes\":9}]},{\"types\":[{\"color\":\"#3b5998\",\"description\":\"Status Update\",\"value\":9,\"type\":\"9\"},{\"color\":\"#5bc0bd\",\"description\":\"Image Post\",\"value\":26,\"type\":\"26\"},{\"color\":\"#2ebaeb\",\"description\":\"Shared Link\",\"value\":10,\"type\":\"10\"},{\"color\":\"#f08a4b\",\"description\":\"Video Post\",\"value\":5,\"type\":\"5\"}]},{\"frequency\":[{\"highest\":2,\"month\":\"Mon\",\"lowest\":2},{\"highest\":9,\"month\":\"Tue\",\"lowest\":9},{\"highest\":6,\"month\":\"Wed\",\"lowest\":6},{\"highest\":10,\"month\":\"Thu\",\"lowest\":10},{\"highest\":8,\"month\":\"Fri\",\"lowest\":8},{\"highest\":4,\"month\":\"Sat\",\"lowest\":4},{\"highest\":11,\"month\":\"Sun\",\"lowest\":11}]},{\"color\":\"#3a5897\",\"private\":[{\"percent\":2,\"value\":0},{\"percent\":11,\"value\":0},{\"percent\":20,\"value\":0},{\"percent\":30,\"value\":0},{\"percent\":51,\"value\":11},{\"percent\":57,\"value\":7},{\"percent\":70,\"value\":9},{\"percent\":75,\"value\":11},{\"percent\":88,\"value\":12},{\"percent\":100,\"value\":0},{\"percent\":110,\"value\":0},{\"percent\":120,\"value\":0}]}]").toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		
//		System.out.println(result);
		out.println(result);
		
		
		
//		System.out.println("Here.");
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
	
	private String buildTopWordsJson(TopWordsResult result) {
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
			
			wordsHtml.add("<li class=\\\"" + giveMeVees(emphasis) + (emphasis > 0 ? "-" : "") + "popular\\\"><a href=\\\"#\\\"/>" + topWords.get(i).getWord() + "</a></li>");
		}
		
		Collections.shuffle(wordsHtml);
		
		String html = "";
		for (String wordHtml : wordsHtml) {
			html += wordHtml;
		}
		
//		html = StringEscapeUtils.escapeHtml4(html);
		String json = "{" + 
				"	\"html\": \"" + html + "\"," + 
				"	\"topword\": \"" + topWords.get(0).getWord() + "\"" +
				"}";
		
//		json = StringEscapeUtils.escapeHtml4(json);
//		System.out.println(json);
		return json;
	}
	
	private String giveMeVees(int numVees) {
		String vees = "";
		
		for (int i = 0; i < numVees; i++) {
			vees += "v";
		}
		
		return vees;
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
			
			Calendar createdDate = null;
			if (createdDateString != null) {
				createdDate = Calendar.getInstance();
				// get the date using Java Calendar
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
				try {
//					if (createdDateString == null) {
//						System.out.println("asdf");
//					}
//					System.out.println(createdDateString);
					createdDate.setTime(sdf.parse(createdDateString));// all done
//					System.out.println(new Date(createdDate.getTimeInMillis()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
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
