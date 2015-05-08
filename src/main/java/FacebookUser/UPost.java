package FacebookUser;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class holds Facebook data
 * Created by Nakul Sharma on 18-04-2015.
 */
public class UPost implements Comparable<UPost> {

    private String userId;
    private String postId;

    private String postMessage = null;   // message field in Fb JSON
    private String postMonth; // created_time field in Fb JSON
    private String postYear;
    private String statusType;
    private String story = null;
    private String type = null;
    private String description = null;
    private long likesCount; // likes --> data --> id in Fb JSON
    private long commentCount; // comments field in Fb JSON
    private long rating;

    public UPost(String userId, String postId, String postMessage, String postMonth, String statusType, long likesCount, long commentCount) {
        this.setUserId(userId);
        this.setPostId(postId);
        this.setPostMessage(postMessage);
        this.setPostMonth(postMonth);
        this.setLikesCount(likesCount);
        this.setCommentCount(commentCount);
        this.setStatusType(statusType);
        this.setRating((likesCount * 5) + (commentCount * 10));
    }

    @JsonIgnore
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    @JsonIgnore
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    @JsonIgnore
    public String getPostMonth() {
        return postMonth;
    }

    public void setPostMonth(String postMonth) {
        this.postMonth = postMonth;
    }

    @JsonIgnore
    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    @JsonIgnore
    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

//    @JsonIgnore
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

  //      @JsonIgnore
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public String getPostYear() {
        return postYear;
    }

    public void setPostYear(String postYear) {
        this.postYear = postYear;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getDate() {
        return this.postMonth + "-" + this.postYear;
    }

    @Override
    public int compareTo(UPost other) {
        long r1 = this.rating;
        long r2 = other.rating;
        if (r1 < r2)
            return 1;
        else if (r1 > r2)
            return -1;
        else
            return 0;
    }

	@Override
	public String toString() {
		return "UPost [userId=" + userId + ", postId=" + postId
				+ ", postMessage=" + postMessage + ", postMonth=" + postMonth
				+ ", postYear=" + postYear + ", statusType=" + statusType
				+ ", story=" + story + ", type=" + type + ", description="
				+ description + ", likesCount=" + likesCount
				+ ", commentCount=" + commentCount + ", rating=" + rating + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (commentCount ^ (commentCount >>> 32));
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (likesCount ^ (likesCount >>> 32));
		result = prime * result + ((postId == null) ? 0 : postId.hashCode());
		result = prime * result
				+ ((postMessage == null) ? 0 : postMessage.hashCode());
		result = prime * result
				+ ((postMonth == null) ? 0 : postMonth.hashCode());
		result = prime * result
				+ ((postYear == null) ? 0 : postYear.hashCode());
		result = prime * result + (int) (rating ^ (rating >>> 32));
		result = prime * result
				+ ((statusType == null) ? 0 : statusType.hashCode());
		result = prime * result + ((story == null) ? 0 : story.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UPost other = (UPost) obj;
		if (commentCount != other.commentCount)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (likesCount != other.likesCount)
			return false;
		if (postId == null) {
			if (other.postId != null)
				return false;
		} else if (!postId.equals(other.postId))
			return false;
		if (postMessage == null) {
			if (other.postMessage != null)
				return false;
		} else if (!postMessage.equals(other.postMessage))
			return false;
		if (postMonth == null) {
			if (other.postMonth != null)
				return false;
		} else if (!postMonth.equals(other.postMonth))
			return false;
		if (postYear == null) {
			if (other.postYear != null)
				return false;
		} else if (!postYear.equals(other.postYear))
			return false;
		if (rating != other.rating)
			return false;
		if (statusType == null) {
			if (other.statusType != null)
				return false;
		} else if (!statusType.equals(other.statusType))
			return false;
		if (story == null) {
			if (other.story != null)
				return false;
		} else if (!story.equals(other.story))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

    
}
