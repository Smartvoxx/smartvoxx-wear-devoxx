package net.noratek.smartvoxx.common.model;

import java.io.Serializable;
import java.util.List;

public class ConferenceApiModel implements Serializable {

	public String id;
	public String confType;
	public String confDescription;
	public String confIcon;
	public String venue;
	public String address;
	public String country;
	public String latitude;
	public String longitude;
	public List<FloorApiModel> floors;
	public String capacity;
	public String sessions;
	public String splashImgURL;
	public String fromDate;
	public String toDate;
	public String wwwURL;
	public String regURL;
	public String cfpURL;
	public String talkURL;
	public String votingURL;
	public String votingEnabled;
	public String cfpEndpoint;
	public String cfpVersion;
	public String youTubeId;
	public String hashtag;
	public String integration_id;

	@Override
	public String toString() {
		return "com.devoxx.connection.cfp.model.ConferenceApiModel{" +
				"address='" + address + '\'' +
				", cfpEndpoint='" + cfpEndpoint + '\'' +
				", cfpURL='" + cfpURL + '\'' +
				", cfpVersion='" + cfpVersion + '\'' +
				", confType='" + confType + '\'' +
				", country='" + country + '\'' +
				", confDescription='" + confDescription + '\'' +
				", confIcon='" + confIcon + '\'' +
				", fromDate='" + fromDate + '\'' +
				", id='" + id + '\'' +
				", latitude='" + latitude + '\'' +
				", longitude='" + longitude + '\'' +
				", splashImgURL='" + splashImgURL + '\'' +
				", regURL='" + regURL + '\'' +
				", toDate='" + toDate + '\'' +
				", venue='" + venue + '\'' +
				", votingEnabled='" + votingEnabled + '\'' +
				", votingURL='" + votingURL + '\'' +
				", wwwURL='" + wwwURL + '\'' +
				", youTubeId='" + youTubeId + '\'' +
				", hashtag='" + hashtag + '\'' +
				", integration_id='" + integration_id + '\'' +
				'}';
	}
}
