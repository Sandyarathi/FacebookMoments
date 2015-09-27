package facebookFriendPhotos;

import java.util.*;

import org.springframework.stereotype.Service;

import FacebookUser.UPost;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.*;
import com.restfb.types.Photo.Tag;
import com.restfb.types.Post.MessageTag;

@Service
public class FacebookPhotoFinder {

	List<Photo> userPhotos;

	public FacebookPhotoFinder() {
		this.userPhotos = new ArrayList<Photo>();
	}

	public List<Photo> findPhotoMoments(List<UPost> posts,
			FacebookClient fbClient) {
		userPhotos = getUserPhotos(fbClient);
		Set<Photo> photoMoments = new HashSet<Photo>();

		for (UPost post : posts) {
			photoMoments.addAll(findCommonPics(post, fbClient));
		}
		return new ArrayList<Photo>(photoMoments);
	}

	private Set<Photo> findCommonPics(UPost userPost, FacebookClient fbClient) {
		String postID = userPost.getPostId();
		/** Sample postID which returns photos, use for testing **/
		// String postID = "454981124675279_332015523638507";
		Post post = fbClient.fetchObject(postID, Post.class);
		List<String> friendIDs = getFriendIDs(post);
		Set<Photo> photoMoments = getPhotoSet(friendIDs, userPhotos);
		return photoMoments;
	}

	private List<String> getFriendIDs(Post post) {
		Set<String> friendIDs = new HashSet<String>();

		/** get tags from message **/
		Map<String, List<MessageTag>> messageTags = post.getMessageTags();
		Set<String> messageTagKeySet = messageTags.keySet();
		List<MessageTag> messageTagList = new ArrayList<MessageTag>();
		for (String key : messageTagKeySet)
			messageTagList.addAll(messageTags.get(key));
		for (MessageTag messageTag : messageTagList) {
			friendIDs.add(messageTag.getId());
		}

		/** get tags from all other sources **/
		List<NamedFacebookType> otherTags = post.getWithTags();
		for (NamedFacebookType tag : otherTags) {
			friendIDs.add(tag.getId());
		}

		return new ArrayList<String>(friendIDs);
	}

	private List<Photo> getUserPhotos(FacebookClient fbClient) {
		List<Photo> userPhotos = new ArrayList<Photo>();
		Date oneYearAgo = new Date(System.currentTimeMillis() - 1000L * 60L
				* 60L * 24L * 365L);
		Connection<Photo> photoCollection = fbClient.fetchConnection(
				"me/photos", Photo.class, Parameter.with("until", "yesterday"),
				Parameter.with("since", oneYearAgo));

		for (List<Photo> photoList : photoCollection) {
			userPhotos.addAll(photoList);
		}
		photoCollection = fbClient.fetchConnection("me/photos/uploaded",
				Photo.class, Parameter.with("until", "yesterday"),
				Parameter.with("since", oneYearAgo));

		for (List<Photo> photoList : photoCollection) {
			userPhotos.addAll(photoList);
		}
		//for(Photo photo: userPhotos){
			//System.out.println(photo.getSource());
		//}
		return userPhotos;
	}

	private Set<Photo> getPhotoSet(List<String> friendIDs,
			List<Photo> userPhotos) {
		Set<Photo> photoMoments = new HashSet<Photo>();
		for (Photo photo : userPhotos) {
			List<Tag> photoTags = photo.getTags();
			for (Tag tag : photoTags) {
				// System.out.println("Tag: " + tag.getName());
				String friendID = tag.getId();
				if (friendIDs.contains(friendID)) {
					photoMoments.add(photo);
				}
			}
		}
		return photoMoments;

	}

}
