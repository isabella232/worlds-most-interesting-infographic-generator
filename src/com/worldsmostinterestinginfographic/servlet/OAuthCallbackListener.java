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
import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;

public class OAuthCallbackListener extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check for the presence of an authorization code
		String code = request.getParameter("code");
		if (code != null && code.length() > 0) {

			CloseableHttpClient httpClient = HttpClients.createDefault();
			try {
				// Exchange authorization code for access token
				HttpPost httpPost = new HttpPost(Model.TOKEN_ENDPOINT + "?grant_type=authorization_code&code=" + code + "&redirect_uri=" + URLEncoder.encode((request.getScheme() + "://" + request.getServerName() + Model.REDIRECT_URI), StandardCharsets.UTF_8.name()) + "&client_id=" + Model.CLIENT_ID + "&client_secret=" + Model.CLIENT_SECRET);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String line = bufferedReader.readLine();
				String accessToken = line.split("&")[0].split("=")[1];

				/*
				// Use access token to request profile data
				String requestUrl = "https://graph.facebook.com/v2.2/me?fields=" + Model.FACEBOOK_REQUESTED_PROFILE_FIELDS + "&access_token=" + accessToken;
				httpClient = HttpClients.createDefault();
				HttpGet get = new HttpGet(requestUrl);
				httpResponse = httpClient.execute(get);
				bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String userJson = bufferedReader.readLine();
				*/
				
				// Use access token to request profile data
				String requestUrl = "https://graph.facebook.com/v2.2/me?fields=" + Model.FACEBOOK_REQUESTED_PROFILE_FIELDS;
				httpClient = HttpClients.createDefault();
				httpPost = new HttpPost(requestUrl);
				httpPost.addHeader("Authorization", "Bearer " + accessToken);
				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("method", "get"));
				httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
				httpResponse = httpClient.execute(httpPost);
				bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String userJson = bufferedReader.readLine();
				
				// Convert profile data to User object
				User user = null;
				try {
					JSONObject userObject = new JSONObject(userJson);
					user = new User(userObject.getString("first_name"), 
							userObject.getString("last_name"), 
							userObject.getString("name"),
							userObject.getString("link"),
							userObject.getString("gender"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				// User access token to request posts
				requestUrl = "https://graph.facebook.com/v2.2/me/feed?access_token=" + accessToken;
				httpClient = HttpClients.createDefault();
				HttpGet get = new HttpGet(requestUrl);
				httpResponse = httpClient.execute(get);
				bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String allPostsJson = bufferedReader.readLine();
				
				// Convert profile data to User object
				List<Post> posts = new ArrayList<Post>();
				try {
					JSONObject allPostsObject = new JSONObject(allPostsJson);
					JSONArray allPostsArray = new JSONArray(allPostsObject.getString("data"));
					
					for (int i = 0; i < allPostsArray.length(); i++) {
						
						JSONObject postObject = (JSONObject)allPostsArray.get(i);
						Post post = new Post(postObject.getString("id"),
								Post.Type.valueOf(postObject.getString("type").toUpperCase()),
								postObject.has("message") ? postObject.getString("message") : "");
						posts.add(post);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// Send to success page with received profile data
				request.getSession().setAttribute("user", user);
				request.getSession().setAttribute("posts", posts);
				
				// do word count here because it fails in jsp for some reason - didn't investigate too long
				Map<String, Integer> wordMap = new HashMap<String, Integer>();
				for (int i = 0; i < posts.size(); i++) {
					Post post = posts.get(i);
					// String[] messageWordMap = post.getMessage().split(" ");
					String regex = "\\b[A-Za-z]+\\b";

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
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				httpClient.close();
			}
			
		} else {
			// An error happened during authorization code request. Report it.
			request.getSession().setAttribute("errorMessage", "THIS IS SOME MESSAGE!");
			response.sendRedirect("/uh-oh");
		}

	}
}
