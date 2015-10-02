package com.worldsmostinterestinginfographic.collect;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.worldsmostinterestinginfographic.collect.result.TopFriendsResult;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;

public class StatisticsCollector {
	public static TopFriendsResult collectTopFriends(List<Post> posts, User user) {
		
		System.out.println("# of posts: " + posts.size());
		
		Map<User, Integer> likesByFriendsMap = new HashMap<User, Integer>();
		for (Post post : posts) {
			for (User liker : post.getLikes()) {
				
				// Ignore own likes
				if (liker.equals(user)) {
					continue;
				}
				
				if (likesByFriendsMap.containsKey(liker)) {
					likesByFriendsMap.put(liker, likesByFriendsMap.get(liker) + 1);
					continue;
				}
				
				likesByFriendsMap.put(liker, 1);
			}
		}
		
		return new TopFriendsResult(likesByFriendsMap);
	}
	
	public static Map<Post.Type, Integer> collectPostTypes(List<Post> posts) {
		Map<Post.Type, Integer> postTypesCount = new HashMap<Post.Type, Integer>();
		
		for (Post post : posts) {
			if (postTypesCount.containsKey(post.getType())) {
				postTypesCount.put(post.getType(), postTypesCount.get(post.getType()) + 1);
				continue;
			}
			
			postTypesCount.put(post.getType(), 1);
		}
		
		return postTypesCount;
	}
	
	/**
	 * Returns an array where the index represents the day of the week and the value contained represents
	 * the number of posts on that day.  Since java.util.Calendar uses 1-based indexing for their days
	 * of the week (i.e. Calendar.SUNDAY = 1, Calendar.MONDAY = 2, ... , Calendar.SATURDAY = 7), I subtracted
	 * 1 from each to make them 0-indexed based so that they fit into an array without making it a size of 8.
	 * Therefore, index 0 represents the number of posts that have been made on Sundays.
	 * 
	 * @param posts
	 * @return
	 */
	public static int[] collectPostFrequencyByDayOfWeek(List<Post> posts) {
		int[] postsByDayOfWeek = new int[7];
		for (Post post : posts) {
			postsByDayOfWeek[post.getCreatedDate().get(Calendar.DAY_OF_WEEK) - 1] += 1;
		}
		
//		for (int i = 0; i < postsByDayOfWeek.length; i++) {
//			System.out.println("[" + i + "] " + postsByDayOfWeek[i]);
//		}
		
		return postsByDayOfWeek;
	}
	
	/**
	 * Returns an array where the index represents the month of the year and the value contained represents
	 * the number of posts on in that month.  The value at index 0 represents the number of posts made in 
	 * the month of January, whereas the value at index 11 represents the number of posts made in the month
	 * of December.
	 * 
	 * @param posts
	 * @return
	 * 
	 * @see http://docs.oracle.com/javase/7/docs/api/constant-values.html#java.util.Calendar.JANUARY
	 */
	public static int[] collectPostFrequencyByMonthOfYear(List<Post> posts) {
		int[] postsByMonthOfYear = new int[12];
		for (Post post : posts) {
			postsByMonthOfYear[post.getCreatedDate().get(Calendar.MONTH)] += 1;
		}
		
//		for (int i = 0; i < postsByDayOfWeek.length; i++) {
//			System.out.println("[" + i + "] " + postsByDayOfWeek[i]);
//		}
		
		return postsByMonthOfYear;
	}
}
