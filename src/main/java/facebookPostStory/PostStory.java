package facebookPostStory;

import java.util.Arrays;
import java.util.List;




import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.types.FacebookType;

public class PostStory {
	//publishes the story on to users wall and returns a response.
	public FacebookType PostOnWall(FacebookClient fbClient, String postMessage) throws FacebookException{
			FacebookType publishMessageResponse =
	    		   fbClient.publish("me/feed", FacebookType.class,
	    		     Parameter.with("message", postMessage));

			System.out.println("Published message ID: " + publishMessageResponse.getId());
			return publishMessageResponse;	
		
	}
}
