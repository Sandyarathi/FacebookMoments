package Facebook;

import FacebookUser.UPost;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.WebRequestor;
import com.restfb.types.FacebookType;
import com.restfb.types.Photo;
import com.restfb.types.User.Picture;

import facebookFriendPhotos.FacebookPhotoFinder;
import facebookFriendProfiles.FriendProfiles;
import facebookPostStory.PostStory;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@RestController
public class FController {
    private static final Logger logger = Logger.getLogger(FController.class);
    
    
    FacebookPhotoFinder facebookPhotoFinder = new FacebookPhotoFinder();
    PostStory postStory= new PostStory();
    FriendProfiles friends= new FriendProfiles();
    
    
    
    
    FacebookDesign fb = new FacebookDesign();
    
    private static final String REDIRECT_URL = "http://localhost:8080/welcome.jsp/action";

    /*--------------------------Welcome Page ---------------------------------*/
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "welcome.jsp/action", method = RequestMethod.GET)
    public ResponseEntity fbConnect(@RequestParam(value = "code") String code) throws IOException {
        FacebookClient.AccessToken token = getFacebookUserToken(code, REDIRECT_URL);
        String accessToken = token.getAccessToken();
        //FacebookClient fbClient = new DefaultFacebookClient("CAACEdEose0cBABGQOiuGfQsZBZB7XF8sQH85urgODrwNEsddksU7C09nJ8XSbv52Kah8wzlG4eJfzmEjFQZAicvrEvZAIj8n3oihbuNfNZA6fWD1gU0agIkQSqFIhZCcj1DZC0EQafgNnZBJ1GoZA6UcPBOz9JQPh173ZCXJDqMZB4rD2VAkz9bXiupvZBG0mSSi5yZCQ0dg6zIglIlFFEKhTvEs4");
        FacebookClient fbClient = new DefaultFacebookClient(accessToken);
        
        
        //to get the top posts
        TreeMap<String, ArrayList<UPost>> allPosts = fb.getHighlights(fbClient);
        
        
        //to post a story to logged in users wall
        String story="Version 8 WallCheck ";
        FacebookType publishMessageResponse =postStory.PostOnWall(fbClient, story);
        System.out.println("Published message ID: " + publishMessageResponse.getId());
        
        //to get profile photos of friends of logged in user
        List<Picture> friendProfilePhotos=friends.getProfilePhotos(fbClient);
        
        logger.info(String.format("Found %s profiles", friendProfilePhotos.size()));
        for(Picture profilePicture: friendProfilePhotos){
        	System.out.println(profilePicture.getUrl());
        }		  
        
        ArrayList<UPost> topPosts =  allPosts.get(allPosts.firstKey());
        List<Photo> photoMoments = facebookPhotoFinder.findPhotoMoments(topPosts, fbClient);
        logger.info(String.format("Found %s photo moments", photoMoments.size()));
        for(Photo photo: photoMoments){
        	System.out.println(photo.getSource());
        }
        if (!allPosts.isEmpty())
            return new ResponseEntity<>(allPosts, HttpStatus.OK);
        else
            return new ResponseEntity<>("There are no Highlights to display currently", HttpStatus.BAD_REQUEST);
    }

   /* public TreeMap<String, ArrayList<UPost>> getResults() {
        FacebookDesign fb = new FacebookDesign(this.fbClient);
        return fb.getHighlights();
    }
*/

    /*---------------------------------Generate User Token --------------------------------------------------*/
    private FacebookClient.AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {
        String appId = "403024159903643";
        String appSecretId = "15b0bf950c65802d807eb71ac932820a";
        WebRequestor wr = new DefaultWebRequestor();
        WebRequestor.Response accessTokenResponse = wr.executeGet("https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUrl + "&client_secret=" + appSecretId + "&code=" + code + "&scope=user_about_me%2Cuser_friends%2Cuser_photos%2Cpublish_stream");
        
        //"CAAFujFZCjD5sBALnCNZCmrzvcosCs0PvNdga9GDA8qMZCAT1ZATCVTJUcxXmkgYsLxN2Veyuu5iC3cIz13juPH26TQJKWwAKRZCK2vWB1WXn6oZC5NMzAWDlCPZCCcYjQZCLZC7QDXapmaqabMNdDGKGpS83nryo6QZBjtCME8IT3gXVDlDMiE32NBDun4PIya0ua1KPW4Cqdml4QNVBOtVik6";
        //https://graph.facebook.com/​oauth/authorize?​client_id=MY_API_KEY&​redirect_uri=http://www.facebook.com/​connect/login_success.html&​scope=publish_stream,​create_event
        return DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
    }
}