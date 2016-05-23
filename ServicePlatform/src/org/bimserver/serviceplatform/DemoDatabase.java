//package org.bimserver.serviceplatform;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class DemoDatabase implements Database {
//
//	private final Map<Long, User> users = new HashMap<>();
//	private final Map<String, Application> applications = new HashMap<>();
//	private final AtomicLong counter = new AtomicLong();
//	
//	
//	public DemoDatabase() {
//		addUser(new User("user1", "user1"));
//		addUser(new User("user2", "user2"));
//		addUser(new User("user3", "user3"));
//	}
//	
//	protected void addUser(User user) {
//		user.setId(counter.incrementAndGet());
//		users.put(user.getId(), user);
//	}
//	
//	@Override
//	public User getUserById(long userId) {
//		return users.get(userId);
//	}
//
//	@Override
//	public User getUserByUserName(String username) {
//		for (User user : users.values()) {
//			if (user.getUsername().equals(username)) {
//				return user;
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public void registerApplication(String clientId, String clientSecret, String clientName, String clientUrl, String clientDescription, String redirectURI, String clientIcon) {
//		Application application = new Application(clientId, clientSecret, clientName, clientUrl, clientDescription, redirectURI, clientIcon);
//		applications.put(application.getId(), application);
//	}
//
//	@Override
//	public Application getApplicationById(String applicationId) {
//		return applications.get(applicationId);
//	}
//
//	@Override
//	public void initDemoData() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean isNew() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//}