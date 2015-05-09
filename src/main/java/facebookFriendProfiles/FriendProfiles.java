package facebookFriendProfiles;

import java.util.ArrayList;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.WebRequestor;
import com.restfb.types.Photo;
import com.restfb.types.User;
import com.restfb.types.User.Picture;

public class FriendProfiles {
	//collects all the profiles of logged in users friends and returns a list of profile pictures.
	public List<Picture> getProfilePhotos(FacebookClient fbClient) {
		List<Picture> friendProfilePhotos= new ArrayList<Picture>();
		User user = fbClient.fetchObject("me", User.class);
		Connection<User> myFriends = fbClient.fetchConnection("me/friends", User.class,Parameter.with("fields", "id, name,picture"));
		List<User> myFriendsList = myFriends.getData();
		for (User friend: myFriendsList){
			System.out.println(friend.getPicture());
			friendProfilePhotos.add( friend.getPicture());
			
			
		}
		
		return friendProfilePhotos ;
	}

	

}
