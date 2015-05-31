package com.worldsmostinterestinginfographic.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public enum Model {
	INSTANCE;
	
	public static final String CLIENT_ID;
	public static final String CLIENT_SECRET;
	public static final String REDIRECT_URI;
	public static final String AUTH_ENDPOINT;
	public static final String TOKEN_ENDPOINT;
	public static final String FACEBOOK_REQUESTED_PROFILE_FIELDS;
	
	static {
		// Read important properties from file
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("WEB-INF/project.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		CLIENT_ID = properties.getProperty("clientId");
		CLIENT_SECRET = properties.getProperty("clientSecret");
		REDIRECT_URI = properties.getProperty("redirectUri");
		AUTH_ENDPOINT = properties.getProperty("authEndpoint");
		TOKEN_ENDPOINT = properties.getProperty("tokenEndpoint");
		FACEBOOK_REQUESTED_PROFILE_FIELDS = "id,about,address,age_range,bio,birthday,context,currency,devices,education,email,favorite_athletes,favorite_teams,first_name,gender,hometown,inspirational_people,install_type,installed,interested_in,is_shared_login,is_verified,languages,last_name,link,location,locale,meeting_for,middle_name,name,name_format,payment_pricepoints,test_group,political,relationship_status,religion,security_settings,significant_other,sports,quotes,third_party_id,timezone,updated_time,shared_login_upgrade_required_by,verified,video_upload_limits,viewer_can_send_gift,website,work,cover";
	}
}
