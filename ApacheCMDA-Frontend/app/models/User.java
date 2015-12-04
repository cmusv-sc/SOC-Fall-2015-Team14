/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.data.validation.Constraints;
import play.libs.Json;
import util.APICall;
import util.Constants;

import java.util.ArrayList;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String userName;
	@Constraints.Required
	private String password;
	@Constraints.Required
	private String firstName;
	@Constraints.Required
	private String lastName;
	private String middleInitial;
	private String affiliation;
	private String title;
	@Constraints.Required
	private String email;
	private String mailingAddress;
	private String phoneNumber;
	private String faxNumber;
	private String researchInterests;
	private String highestDegree;
    private String photoContentType;

	// @OneToMany(mappedBy = "user", cascade={CascadeType.ALL})
	// private Set<ClimateService> climateServices = new
	// HashSet<ClimateService>();

	public User() {
	}

	public User(String userName, String password, String firstName,
			String lastName, String middleInitial, String affiliation,
			String title, String email, String mailingAddress,
			String phoneNumber, String faxNumber, String researchInterests,
			String highestDegree, String photoContentType) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleInitial = middleInitial;
		this.affiliation = affiliation;
		this.title = title;
		this.email = email;
		this.mailingAddress = mailingAddress;
		this.phoneNumber = phoneNumber;
		this.faxNumber = faxNumber;
		this.researchInterests = researchInterests;
		this.highestDegree = highestDegree;
        this.photoContentType = photoContentType;
	}



	public long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public String getTitle() {
		return title;
	}

	public String getEmail() {
		return email;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public String getResearchInterests() {
		return researchInterests;
	}

	public String getHighestDegree() {
		return highestDegree;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public void setResearchInterests(String researchInterests) {
		this.researchInterests = researchInterests;
	}

	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}


    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id=").append(id);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", middleInitial='").append(middleInitial).append('\'');
        sb.append(", affiliation='").append(affiliation).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", mailingAddress='").append(mailingAddress).append('\'');
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", faxNumber='").append(faxNumber).append('\'');
        sb.append(", researchInterests='").append(researchInterests).append('\'');
        sb.append(", highestDegree='").append(highestDegree).append('\'');
        sb.append(", photoContentType='").append(photoContentType).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static User getUserById(String id) {
        JsonNode response = APICall.callAPI(Constants.NEW_BACKEND + "/users/" + id);
        ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        try {
            user = mapper.treeToValue(response, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUserByUserName(String userName) {
        JsonNode response = APICall.callAPI(Constants.NEW_BACKEND + Constants.GET_USER_BY_USERNAME + userName);
        ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        try {
            user = mapper.treeToValue(response, User.class);
            System.out.println(user.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static ArrayList<User> getFollowingUsers(String userId) {
        ArrayList<User> users = new ArrayList<>();
        JsonNode userNodes = APICall.callAPI(Constants.NEW_BACKEND + Constants.GET_FOLLOWING_USERS + userId);

        for (int i = 0; i < userNodes.size(); i++) {
            JsonNode json = userNodes.path(i);

            User newUser = Json.fromJson(json, User.class);
            users.add(newUser);
        }

        return users;
    }

    public static ArrayList<User> getFollowedUsers(String userId) {
        ArrayList<User> users = new ArrayList<>();
        JsonNode userNodes = APICall.callAPI(Constants.NEW_BACKEND + Constants.GET_FOLLOWED_USERS + userId);

        for (int i = 0; i < userNodes.size(); i++) {
            JsonNode json = userNodes.path(i);
            User newUser = Json.fromJson(json, User.class);
            users.add(newUser);
        }

        return users;
    }

}

