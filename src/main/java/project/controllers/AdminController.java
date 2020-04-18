package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import project.exceptions.ForbiddenException;
import project.models.entities.User;
import project.services.AdminService;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@ResponseBody
	@PutMapping
	public User update(@RequestParam Long userId, @RequestParam(required = false) boolean locked, @AuthenticationPrincipal User admin) {
		if(admin == null || admin.getId() == userId) throw new ForbiddenException();
		return adminService.update(userId, locked);
	}
	
}
