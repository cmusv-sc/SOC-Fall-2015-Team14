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

import com.google.gson.annotations.Expose;

import javax.persistence.*;

import java.util.Set;
import java.util.HashSet;

@Entity
public class User {

	@Expose
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Expose
	private String userName;
	@Expose
	private String password;
	@Expose
	private String firstName;
	@Expose
	private String lastName;
	@Expose
	private String middleInitial;
	@Expose
	private String affiliation;
	@Expose
	private String title;
	@Expose
	private String email;
	@Expose
	private String mailingAddress;
	@Expose
	private String phoneNumber;
	@Expose
	private String faxNumber;
	@Expose
	private String researchInterests;
	@Expose
	private String highestDegree;
	@Expose
	private String photoContentType;
	@Expose
	private boolean hasFrontLayerPhoto;

	@ManyToMany (fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinTable(name = "UserRelationship",
			joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "follower_id", referencedColumnName = "id")})
	private Set<User> followers;

	@ManyToMany (fetch = FetchType.EAGER, mappedBy = "followers" )
	private Set<User> followedUsers;

	@ManyToMany (fetch = FetchType.EAGER, mappedBy = "likeUsers" )
	private Set<Post> likePosts;

	@ManyToMany (fetch = FetchType.EAGER, mappedBy = "sharedUsers" )
	private Set<Post> sharedPosts;

	public User() {
	}

	public User(String userName, String password, String firstName,
			String lastName, String middleInitial, String affiliation,
			String title, String email, String mailingAddress,
			String phoneNumber, String faxNumber, String researchInterests,
			String highestDegree, boolean hasFrontLayerPhoto) {
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
		this.hasFrontLayerPhoto = hasFrontLayerPhoto;
		this.highestDegree = highestDegree;
		this.followers = new HashSet<>();
		this.followedUsers = new HashSet<>();
		this.likePosts = new HashSet<>()
;		this.sharedPosts = new HashSet<>();
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

	public String getPhotoContentType() { return photoContentType; }

	public Set<User> getFollowers() { return followers; }

	public Set<User> getFollowedUsers() { return followedUsers; }

	public Set<Post> getSharedPosts() { return sharedPosts; }

	public boolean isHasFrontLayerPhoto() { return hasFrontLayerPhoto; }

	public void setHasFrontLayerPhoto(boolean hasFrontLayerPhoto) { this.hasFrontLayerPhoto = hasFrontLayerPhoto; }

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

	public void setResearchInterests(String researchFields) {
		this.researchInterests = researchFields;
	}

	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}

	public void setPhotoContentType(String photoName) { this.photoContentType = photoName; }

	public void addFollower(User user) { followers.add(user); }

	public void addFollowedUser(User user) { followedUsers.add(user); };

	public void removeFollower(User user) { followers.remove(user); }

	public void removeAllFollower() { followers.clear(); }

	public void removeFollowedUser(User user) { followedUsers.remove(user); };

	public void removeAllFollowedUser() { followedUsers.clear(); };

	public void addLikePosts(Post post) { likePosts.add(post); }

	public void removeLikePosts(Post post) { likePosts.remove(post); }

	public void removeAllLikePosts() { likePosts.clear(); }

	public void addSharedPost(Post post) { sharedPosts.add(post); }

	public void removeSharedPost(Post post) { sharedPosts.remove(post); }

	public void removeAllSharedPost() { sharedPosts.clear(); }

	public void cleanUpBeforeDelete() {
		removeAllFollower();
		removeAllFollowedUser();
		removeAllLikePosts();
		removeAllSharedPost();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password="
				+ password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", middleInitial=" + middleInitial
				+ ", affiliation=" + affiliation + ", title=" + title
				+ ", email=" + email + ", mailingAddress=" + mailingAddress
				+ ", phoneNumber=" + phoneNumber + ", faxNumber=" + faxNumber
				+ ", researchInterests=" + researchInterests + ", highestDegree="
				+ highestDegree + "]";
	}

}

