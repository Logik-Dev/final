package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.models.entities.User;

@Service
public class AdminService {
	
	@Autowired
	private UserService userService;
	
	public User lockUserAccount(Long userId) {
		User user = userService.findById(userId);
		user.setAccountNonLocked(false);
		return userService.update(user);
	}
}
