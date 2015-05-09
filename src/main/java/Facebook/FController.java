package Facebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import FacebookUser.UPost;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.WebRequestor;
import com.restfb.types.FacebookType;
import com.restfb.types.Photo;
import com.restfb.types.User.Picture;

import facebookFriendPhotos.FacebookPhotoFinder;
import facebookFriendProfiles.FriendProfiles;
import facebookPostStory.PostStory;

@RestController
public class FController {
	private static final Logger logger = Logger.getLogger(FController.class);

	FacebookPhotoFinder facebookPhotoFinder = new FacebookPhotoFinder();

	PostStory postStory = new PostStory();

	FriendProfiles friends = new FriendProfiles();

	FacebookDesign fb = new FacebookDesign();

	private static final String REDIRECT_URL = "http://localhost:8080/welcome.jsp/action";

	private final static String STORY = "Version 8 WallCheck ";

	/*--------------------------Welcome Page ---------------------------------*/
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "welcome.jsp/action", method = RequestMethod.GET)
	public ResponseEntity fbConnect(@RequestParam(value = "code") String code)
			throws IOException {
		FacebookClient.AccessToken token = getFacebookUserToken(code,
				REDIRECT_URL);
		String accessToken = token.getAccessToken();
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		getUserPermissions(accessToken);

		/** Publish story to FB wall Example **/
		FacebookType publishMessageResponse = publishStory(STORY, fbClient);

		/** Get Profile photos of Friends Example **/
		List<Picture> friendProfilePhotos = getProfilePhotos(fbClient);

		/** Get all posts of user Example **/
		TreeMap<String, ArrayList<UPost>> allPosts = fb.getHighlights(fbClient);

		/** Get common photos of user Example **/
		List<Photo> photoMoments = getPhotoMoments(allPosts, fbClient);

		if (!allPosts.isEmpty())
			return new ResponseEntity<>(allPosts, HttpStatus.OK);
		else
			return new ResponseEntity<>(
					"There are no Highlights to display currently",
					HttpStatus.BAD_REQUEST);
	}

	public FacebookType publishStory(String story, FacebookClient fbClient) {

		// to post a story to logged in users wall
		FacebookType publishMessageResponse = postStory.PostOnWall(fbClient,
				story);
		System.out.println("Published message ID: "
				+ publishMessageResponse.getId());
		return publishMessageResponse;

	}

	public List<Picture> getProfilePhotos(FacebookClient fbClient) {
		// to get profile photos of friends of logged in user
		List<Picture> friendProfilePhotos = friends.getProfilePhotos(fbClient);

		logger.info(String.format("Found %s profiles",
				friendProfilePhotos.size()));
		for (Picture profilePicture : friendProfilePhotos) {
			System.out.println(profilePicture.getUrl());
		}
		return friendProfilePhotos;
	}

	public List<Photo> getPhotoMoments(
			TreeMap<String, ArrayList<UPost>> allPosts, FacebookClient fbClient) {

		if (!allPosts.isEmpty()) {
			List<UPost> topPosts = fb.getTopPosts(allPosts, fbClient);
			List<Photo> photoMoments = facebookPhotoFinder.findPhotoMoments(
					topPosts, fbClient);
			logger.info(String.format("Found %s photo moments",
					photoMoments.size()));
			for (Photo photo : photoMoments) {
				System.out.println(photo.getSource());
			}
			return photoMoments;
		}
		return new ArrayList<Photo>();

	}

	public void getUserPermissions(String accessToken) throws IOException {
		WebRequestor wr = new DefaultWebRequestor();
		WebRequestor.Response permissions = wr
				.executeGet("https://graph.facebook.com/me/permissions?access_token="
						+ accessToken);
		logger.debug(permissions.toString());
	}

	/*---------------------------------Generate User Token --------------------------------------------------*/
	private FacebookClient.AccessToken getFacebookUserToken(String code,
			String redirectUrl) throws IOException {
		String appId = "403024159903643";
		String appSecretId = "15b0bf950c65802d807eb71ac932820a";
		WebRequestor wr = new DefaultWebRequestor();
		WebRequestor.Response accessTokenResponse = wr
				.executeGet("https://graph.facebook.com/oauth/access_token?client_id="
						+ appId
						+ "&redirect_uri="
						+ redirectUrl
						+ "&client_secret="
						+ appSecretId
						+ "&code="
						+ code
						+ "&scope=user_posts%2Cuser_photos%2Cpublish_stream");

		return DefaultFacebookClient.AccessToken
				.fromQueryString(accessTokenResponse.getBody());
	}
}