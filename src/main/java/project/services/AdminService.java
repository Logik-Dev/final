package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.models.entities.User;

@Service
public class AdminService {
	
	@Autowired
	private UserService userService;
	
	public User update(Long userId, boolean locked) {
		User user = userService.findById(userId);
		user.setAccountNonLocked(!locked);
		return userService.update(user);
	}
}
