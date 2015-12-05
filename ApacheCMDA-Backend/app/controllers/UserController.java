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
package controllers;

import java.io.File;
import java.util.*;

import com.google.gson.GsonBuilder;
import models.Post;
import models.PostRepository;
import models.User;
import models.UserRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import util.CustomExclusionStrategy;

/**
 * The main set of web services.
 */
@Named
@Singleton
@Transactional
@EnableTransactionManagement
public class UserController extends Controller {

	private final UserRepository userRepository;
	private final PostRepository postRepository;

	// We are using constructor injection to receive a repository to support our
	// desire for immutability.
	@Inject
	public UserController(final UserRepository userRepository, final PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	public Result addUser() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("User not created, expecting Json data");
			return badRequest("User not created, expecting Json data");
		}

		// Parse JSON file
		String userName = json.path("userName").asText();
		String password = json.path("password").asText();
		String firstName = json.path("firstName").asText();
		String lastName = json.path("lastName").asText();
		String middleInitial = json.path("middleInitial").asText();
	    String affiliation = json.path("affiliation").asText();
	    String title = json.path("title").asText();
	    String email = json.path("email").asText();
	    String mailingAddress = json.path("mailingAddress").asText();
	    String phoneNumber = json.path("phoneNumber").asText();
	    String faxNumber = json.path("faxNumber").asText();
	    String researchInterests = json.path("researchInterests").asText();
	    String highestDegree = json.path("highestDegree").asText();

		try {
			if (userRepository.findByUserName(userName).size()>0) {
				System.out.println("UserName has been used: " + userName);
				return badRequest("UserName has been used");
			}

			User user = new User(userName, password, firstName, lastName, middleInitial, affiliation,
					title, email, mailingAddress, phoneNumber, faxNumber, researchInterests, highestDegree);
			userRepository.save(user);
			System.out.println("User saved: " + user.getId());
			return created(new Gson().toJson(user.getId()));
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			System.out.println("User not saved: " + firstName + " " + lastName);
			return badRequest("User not saved: " + firstName + " " + lastName);
		}
	}

	public Result uploadUserPhoto(Long id) {
		MultipartFormData body = request().body().asMultipartFormData();

		FilePart photo = body.getFile("photo");
		if (photo == null) {
			System.out.println("User photo not saved, expecting binary data");
			return notFound("User photon not saved, expecting binary data");
		}

		User user = userRepository.findOne(id);
		if (user == null) {
			System.out.println("User not found with id: " + id);
			return notFound("User not found with id: " + id);
		}

		File dir = new File("images");
		if (!dir.exists()) {
			dir.mkdir();
		}

		File oldFile = new File("images/" + user.getId());
		if (oldFile.exists() && oldFile.isFile()) {
			oldFile.delete();
		}

		File newFile = photo.getFile();
		String filename = newFile.getName();
		/*System.out.println("****************************");
		Map<String, String[]> headerMap = request().headers();
		for(Map.Entry<String, String[]> entry: headerMap.entrySet()) {
			System.out.println(entry.getKey());
			for(String s: entry.getValue()) {
				System.out.println("\t" + s);
			}
		}
		System.out.print(filename);*/
		user.setPhotoContentType(photo.getContentType());

		newFile.renameTo(new File("images/" + user.getId()));

		try {
			User savedUser = userRepository.save(user);
			System.out.println("User photo updated: " + savedUser.getFirstName()
					+ " " + savedUser.getLastName());
			return ok("User photo updated: " + savedUser.getFirstName() + " "
					+ savedUser.getLastName());
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			System.out.println("User not updated: " + user.getFirstName() + " "
					+ user.getLastName());
			return badRequest("User not updated: " + user.getFirstName() + " " +
					user.getLastName());
		}
	}

	public Result getUserPhoto(Long id) {
		User user = userRepository.findOne(id);
		if (user == null) {
			System.out.println("User not found with id: " + id);
			return notFound("User not found with id: " + id);
		}

		File photo = new File("images/" + user.getId());
		if (!photo.exists() || !photo.isFile()) {
			System.out.println("User photo not found with id: " + id);
			return notFound("User photo not found with id: " + id);
		}

		response().setContentType(user.getPhotoContentType());
		return ok(photo);
	}

	public Result getFrontLayerPhoto(String type) {
		if (type.equals("France")) {
			File photo = new File("images/front-layer/france_flag.jpg");
			if (!photo.exists() || !photo.isFile()) {
				return notFound("Front layer photo not found with type: " + type);
			}
			response().setContentType("image/jpeg");
			return ok(photo);
		}
		return badRequest("Don't support layer for " + type);
	}

	public Result deleteUser(Long id) {
		User deleteUser = userRepository.findOne(id);
		if (deleteUser == null) {
			System.out.println("User not found with id: " + id);
			return notFound("User not found with id: " + id);
		}

		File photo = new File("images/" + deleteUser.getId());
		if (photo.exists() && photo.isFile()) {
			photo.delete();
		}

		userRepository.delete(deleteUser);
		System.out.println("User is deleted: " + id);
		return ok("User is deleted: " + id);
	}

	public Result updateUser(long id) {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("User not saved, expecting Json data");
			return badRequest("User not saved, expecting Json data");
		}

		// Parse JSON file
		String firstName = json.path("firstName").asText();
		String lastName = json.path("lastName").asText();
		String middleInitial = json.path("middleInitial").asText();
	    String affiliation = json.path("affiliation").asText();
	    String title = json.path("title").asText();
	    String email = json.path("email").asText();
	    String mailingAddress = json.path("mailingAddress").asText();
	    String phoneNumber = json.path("phoneNumber").asText();
	    String faxNumber = json.path("faxNumber").asText();
	    String researchInterests = json.path("researchInterests").asText();
	    String highestDegree = json.path("highestDegree").asText();
		try {
			User updateUser = userRepository.findOne(id);

			updateUser.setFirstName(firstName);
			updateUser.setLastName(lastName);
			updateUser.setAffiliation(affiliation);
			updateUser.setEmail(email);
			updateUser.setFaxNumber(faxNumber);
			updateUser.setHighestDegree(highestDegree);
			updateUser.setMailingAddress(mailingAddress);
			updateUser.setMiddleInitial(middleInitial);
			updateUser.setPhoneNumber(phoneNumber);
			updateUser.setResearchInterests(researchInterests);
			updateUser.setTitle(title);
			
			User savedUser = userRepository.save(updateUser);
			System.out.println("User updated: " + savedUser.getFirstName()
					+ " " + savedUser.getLastName());
			return created("User updated: " + savedUser.getFirstName() + " "
					+ savedUser.getLastName());
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			System.out.println("User not updated: " + firstName + " "
					+ lastName);
			return badRequest("User not updated: " + firstName + " " + lastName);
		}
	}

	public Result getFollowers(Long id, String format) {
		if (id == null) {
			System.out.println("User id is null or empty!");
			return badRequest("User id is null or empty!");
		}

		User user = userRepository.findOne(id);

		if (user == null) {
			System.out.println("User not found with with id: " + id);
			return notFound("User not found with with id: " + id);
		}

		String result = new String();
		if (format.equals("json")) {
			Gson gson = new GsonBuilder().serializeNulls()
					.setExclusionStrategies(new CustomExclusionStrategy(User.class))
					.excludeFieldsWithoutExposeAnnotation().create();

			result = gson.toJson(new ArrayList<User>(user.getFollowers()));
		}

		return ok(result);
	}

	public Result getFollowingUsers(Long id, String format) {
		if (id == null) {
			System.out.println("User id is null or empty!");
			return badRequest("User id is null or empty!");
		}

		User user = userRepository.findOne(id);

		if (user == null) {
			System.out.println("User not found with with id: " + id);
			return notFound("User not found with with id: " + id);
		}

		String result = new String();
		if (format.equals("json")) {
			Gson gson = new GsonBuilder().serializeNulls()
					.setExclusionStrategies(new CustomExclusionStrategy(User.class))
					.excludeFieldsWithoutExposeAnnotation().create();

			result = gson.toJson(new ArrayList<User>(user.getFollowedUsers()));

		}

		return ok(result);
	}

	public Result getSharedPosts(Long id, String format) {
		if (id == null) {
			System.out.println("User id is null or empty!");
			return badRequest("User id is null or empty!");
		}

		User user = userRepository.findOne(id);

		if (user == null) {
			System.out.println("User not found with with id: " + id);
			return notFound("User not found with with id: " + id);
		}

		String result = new String();
		if (format.equals("json")) {
			result = new Gson().toJson(new ArrayList<Post>(user.getSharedPosts()));
		}

		return ok(result);
	}

	public Result getUser(Long id, String format) {
		if (id == null) {
			System.out.println("User id is null or empty!");
			return badRequest("User id is null or empty!");
		}

		User user = userRepository.findOne(id);

		if (user == null) {
			System.out.println("User not found with with id: " + id);
			return notFound("User not found with with id: " + id);
		}
		String result = new String();
		if (format.equals("json")) {
			//result = new Gson().toJson(user);
			Gson gson = new GsonBuilder().serializeNulls()
					.setExclusionStrategies(new CustomExclusionStrategy(User.class))
					.excludeFieldsWithoutExposeAnnotation().create();

			result = gson.toJson(user);
		}

		return ok(result);
	}

	public Result getUserByUserName(String userName, String format) {
		if (userName == null) {
			System.out.println("Username is null or empty!");
			return badRequest("Username is null or empty!");
		}

		List<User> users = userRepository.findByUserName(userName);

		if (users.size() ==0) {
			System.out.println("User not found with with Username: " + userName);
			return notFound("User not found with with Username: " + userName);
		}

		User user = users.get(0);
		String result = new String();
		if (format.equals("json")) {
			Gson gson = new GsonBuilder().serializeNulls()
					.setExclusionStrategies(new CustomExclusionStrategy(User.class))
					.excludeFieldsWithoutExposeAnnotation().create();

			result = gson.toJson(user);
		}

		return ok(result);
	}
	
	public Result getAllUsers(String format) {
		Iterable<User> userIterable = userRepository.findAll();
		List<User> userList = new ArrayList<User>();
		for (User user : userIterable) {
			userList.add(user);
		}
		String result = new String();
		if (format.equals("json")) {
			//result = new Gson().toJson(userList);
			Gson gson = new GsonBuilder().serializeNulls()
					.setExclusionStrategies(new CustomExclusionStrategy(User.class))
					.excludeFieldsWithoutExposeAnnotation().create();

			result = gson.toJson(userList);
		}
		return ok(result);
	}

	public Result isUserNameExisted() {

		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("Cannot check userName, expecting Json data");
			return badRequest("Cannot check userName, expecting Json data");
		}
		String userName = json.path("userName").asText();
		//if (userRepository.findByUserName(userName) != null) {
		List<User> userList = userRepository.findByUserName(userName);
		if(userList.size() >0) {
			return badRequest("UserName already existed");
		}
		return ok("UserName is valid");
/*
		List<User> userList = userRepository.findByUserName(userName);

		String responseStr = null;
		if(userList.size() >0 ) {
			responseStr = "true";

		} else {
			responseStr = "false";
		}

		System.out.println("UserName existed: " + responseStr);
		return ok(responseStr);*/
	}

	public Result isEmailExisted(){
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("Cannot check email, expecting Json data");
			return badRequest("Cannot check email, expecting Json data");
		}
		String email = json.path("email").asText();
		if (userRepository.findByEmail(email) != null) {
			return badRequest("Email already existed");
		}
		return ok("Email is valid");
	}

	public Result isUserValid() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("Cannot check user, expecting Json data");
			return badRequest("Cannot check user, expecting Json data");
		}
		//String email = json.path("email").asText();
		String userName = json.path("userName").asText();
		String password = json.path("password").asText();
		//User user = userRepository.findByEmail(email);
		List<User> userList = userRepository.findByUserName(userName);

		if(userList.size() != 1) {
			System.out.println("User not found with with userName: " + userName);
			return notFound("User not found with with userName: " + userName);
		}

		User user = userList.get(0);
		if (user.getPassword().equals(password)) {
			System.out.println("User is valid");
			return ok("User is valid");
		} else {
			System.out.println("User is not valid");
			return badRequest("User is not valid");
		}
	}
	
	public Result deleteUserByUserNameandPassword(String userName, String password) {
		try {
			List<User> users = userRepository.findByUserName(userName);
			if (users.size()==0) {
				System.out.println("User is not existed");
				return badRequest("User is not existed");
			}
			User user = users.get(0);
			if (user.getPassword().equals(password)) {
				System.out.println("User is deleted: "+user.getId());
				File photo = new File("images/" + user.getId());
				if (photo.exists() && photo.isFile()) {
					photo.delete();
				}

				/*Set<User> followers = user.getFollowers();
				for(User f : followers) {
					f.removeFollowedUser(user);
					userRepository.save(f);
				}

				Set<User> followedUsers = user.getFollowedUsers();
				for(User f : followedUsers) {
					f.removeFollower(user);
					userRepository.save(f);
				}
*/
				user.cleanUpBeforeDelete();
				userRepository.save(user);
				userRepository.delete(user);
				return ok("User is deleted");
			}
			else {
				System.out.println("User is not deleted for wrong password");
				return badRequest("User is not deleted for wrong password");
			}
		}
		catch (PersistenceException pe) {
			pe.printStackTrace();
			System.out.println("User is not deleted");
			return badRequest("User is not deleted");
		}
		
	}

	public Result addFollowee() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("User not saved, expecting Json data");
			return badRequest("User not saved, expecting Json data");
		}

		long followerID = json.path("followerID").asLong();
		long followeeID = json.path("followeeID").asLong();

		if(followerID == followeeID) {
			String response = "Cannot follow yourself!";
			return badRequest(response);
		}
		User follower = userRepository.findOne(followerID);
		if (follower == null) {
			System.out.println("Follower not found with id: " + followerID);
			return notFound("Follower not found with id: " + followerID);
		}
		User followee = userRepository.findOne(followeeID);
		if (followee == null) {
			System.out.println("Followee not found with id: " + followeeID);
			return notFound("Followee not found with id: " + followeeID);
		}
		follower.addFollowedUser(followee);
		followee.addFollower(follower);
		userRepository.save(follower);
		userRepository.save(followee);

		return ok("Followed");

	}


	public Result removeFollowee(long followerID, long followeeID) {
		if(followerID == followeeID) {
			String response = "Cannot follow yourself!";
			return badRequest(response);
		}
		User follower = userRepository.findOne(followerID);
		if (follower == null) {
			System.out.println("Follower not found with id: " + followerID);
			return notFound("Follower not found with id: " + followerID);
		}
		User followee = userRepository.findOne(followeeID);
		if (follower == null) {
			System.out.println("Followee not found with id: " + followeeID);
			return notFound("Followee not found with id: " + followeeID);
		}
		follower.removeFollowedUser(followee);
		followee.removeFollower(follower);
		userRepository.save(follower);
		userRepository.save(followee);

		return ok("Unfollowed");

	}

    /*
    * Define any extra CORS headers needed for option requests (see http://enable-cors.org/server.html for more info)
    */
    public Result preflight(String all) {
        response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response().setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept");
        return ok();
    }

	public Result getFollowingPosts(Long id, String format) {
		if (id == null) {
			System.out.println("User id is null or empty!");
			return badRequest("User id is null or empty!");
		}

		User user = userRepository.findOne(id);

		if (user == null) {
			System.out.println("User not found with with id: " + id);
			return notFound("User not found with with id: " + id);
		}

		Set<User> followedUsers = user.getFollowedUsers();
		List<Post> posts = new ArrayList<>();
		for (User followedUser : followedUsers) {
			posts.addAll(postRepository.findByUserOrderByTimeDesc(followedUser));
		}

		Collections.sort(posts, new PostComparator());

		String result = new String();
		if (format.equals("json")) {
			Gson gson = new GsonBuilder().serializeNulls()
					.setExclusionStrategies(new CustomExclusionStrategy(User.class))
					.excludeFieldsWithoutExposeAnnotation().create();

			result = gson.toJson(posts);
		}
		return ok(result);
	}

	public Result fuzzySearchUsers(String key, String format) {
		List<User> users = new ArrayList<>();
		//users.addAll(userRepository.findByFirstNameContainsOrLastNameContains(key));
		//users.addAll(userRepository.findByAffiliationContainsOrResearchInterestsContains(key));

		users.addAll(userRepository.findByFirstNameContainingOrLastNameContainingOrAffiliationContainingOrResearchInterestsContainingOrUserNameContaining(key, key, key, key, key));

		String result = new String();
		if (format.equals("json")) {
			Gson gson = new GsonBuilder().serializeNulls()
					.setExclusionStrategies(new CustomExclusionStrategy(User.class))
					.excludeFieldsWithoutExposeAnnotation().create();

			result = gson.toJson(users);
		}
		return ok(result);
	}


//	public Result exactSearchUsers(String format) {
//
//		String firstName = request().getQueryString("firstName");
//		String lastName = request().getQueryString("lastName");
//		String email = request().getQueryString("email");
//		String phoneNumber = request().getQueryString("phoneNumber");
//
//
//		if((firstName == null) || (lastName == null)) {
//			System.out.println("FirstName and LastName are required!");
//			return badRequest("FirstName and LastName are required!");
//		}
//
//		List<User> users = userRepository.findByFirstNameAndLastName(firstName, lastName);
//
//		List<User> matchUsers = new ArrayList<>();
//		for (User user : users) {
//			boolean match = true;
//			if (match && (email != null) && !email.equals(user.getEmail())) {
//				match = false;
//			}
//			if (match && (phoneNumber != null) && !phoneNumber.equals(user.getPhoneNumber())) {
//				match = false;
//			}
//			if (match) {
//				matchUsers.add(user);
//			}
//		}
//
//		String result = new String();
//		if (format.equals("json")) {
//			Gson gson = new GsonBuilder().serializeNulls()
//					.setExclusionStrategies(new CustomExclusionStrategy(User.class))
//					.excludeFieldsWithoutExposeAnnotation().create();
//
//			result = gson.toJson(matchUsers);
//		}
//		return ok(result);
//	}

	public Result exactSearchUsers(String format) {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println(" expecting Json data");
			return badRequest(" expecting Json data");
		}

		// Parse JSON file
		String firstName = json.path("firstName").asText();
		String lastName = json.path("lastName").asText();
		String affiliation = json.path("affiliation").asText();
		String researchInterests = json.path("researchInterests").asText();

		List<User> users = userRepository.findByFirstNameAndLastName(firstName, lastName);
		List<User> matchUsers = new ArrayList<>();
		for (User user : users) {
			boolean match = true;
			if (match && affiliation.length() > 0 && !affiliation.equals(user.getAffiliation())) {
				match = false;
			}
			if (match && researchInterests.length() > 0 && !researchInterests.equals(user.getResearchInterests())) {
				match = false;
			}
			if (match) {
				matchUsers.add(user);
			}
		}

		String result = new String();
		if (format.equals("json")) {
			Gson gson = new GsonBuilder().serializeNulls()
					.setExclusionStrategies(new CustomExclusionStrategy(User.class))
					.excludeFieldsWithoutExposeAnnotation().create();

			result = gson.toJson(matchUsers);
		}
		return ok(result);
	}

	class PostComparator implements Comparator<Post> {
		@Override
		public int compare(Post post1, Post post2) {
			return post2.getTime().compareTo(post1.getTime());
		}
	}
}
