package facebookFriendPhotos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.restfb.types.Photo.Tag;
import com.restfb.types.Photo;
import com.restfb.types.Post.MessageTag;

import FacebookUser.UPost;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Post;

@Service
public class FacebookPhotoFinder {

	public List<Photo> findPhotoMoments(List<UPost> posts, FacebookClient fbClient){
		Set<Photo> photoMoments = new HashSet<Photo>();
		for(UPost post: posts){
			System.out.println("Top post ids:"+post.getPostId() );
			photoMoments.addAll(findCommonPics(post, fbClient));
		}
		return new ArrayList<Photo>(photoMoments);
	}
	private Set<Photo> findCommonPics(UPost userPost, FacebookClient fbClient) {
		String postID = userPost.getPostId();
		/**Sample postID which returns photos, use for testing**/
		//String postID = "454981124675279_332015523638507";
		Post post = fbClient.fetchObject(postID, Post.class);
		List<String> friendIDs = getFriendIDs(post);
		List<Photo> userPhotos = getUserPhotos(fbClient);
		Set<Photo> photoMoments = getPhotoSet(friendIDs,userPhotos);
		return photoMoments;
	}

	private List<String> getFriendIDs(Post post) {
		Map<String, List<MessageTag>> messageTags = post.getMessageTags();
		Set<String> messageTagKeySet = messageTags.keySet();
		List<String> friendIDs = new ArrayList<String>();
		List<MessageTag> messageTagList = new ArrayList<MessageTag>();
		for (String key : messageTagKeySet)
			messageTagList.addAll(messageTags.get(key));
		for (MessageTag messageTag : messageTagList) {
			friendIDs.add(messageTag.getId());
		}
		return friendIDs;
	}

	private List<Photo> getUserPhotos(FacebookClient fbClient) {
		Connection<Photo> photoCollection = fbClient.fetchConnection(
				"me/photos", Photo.class, Parameter.with("limit", 1000));
		return photoCollection.getData();
		
	}
	
	private Set<Photo>getPhotoSet(List<String> friendIDs, List<Photo> userPhotos){
		Set<Photo> photoMoments = new HashSet<Photo>();
		for(Photo photo: userPhotos){
			List<Tag> photoTags = photo.getTags();
			for(Tag tag: photoTags){
				String friendID = tag.getId();
				if(friendIDs.contains(friendID)){
					photoMoments.add(photo);
				}
			}
		}
		return photoMoments;
		
	}

}
