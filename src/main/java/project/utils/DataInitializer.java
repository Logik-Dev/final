package project.utils;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import project.models.Role;
import project.models.entities.Equipment;
import project.models.entities.RoomType;
import project.models.entities.User;
import project.repositories.EquipmentRepository;
import project.repositories.RoomTypeRepository;
import project.repositories.UserRepository;

@Component

public class DataInitializer {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoomTypeRepository roomTypeRepository;
	
	@Autowired
	private EquipmentRepository equipmentRepository;
		
	@Autowired
	private PasswordEncoder encoder;
	
	public void initData() {
		
		// Création de l'admin
		User user = new User();
		user.setFirstname("Cédric");
		user.setLastname("Maunier");
		user.setEmail("admin@gmail.com");
		user.setPassword(encoder.encode("root"));
		user.setRoles(Set.of(Role.ADMIN));
		userRepository.save(user);
		
		// Création des types de salle
		RoomType show = new RoomType("Salle de spectacle");
		RoomType bar = new RoomType("Bar");
		RoomType workshop = new RoomType("Atelier");
		RoomType dancehall = new RoomType("Salle de danse");
		RoomType cityRoom = new RoomType("Salle municipale");
		RoomType studio = new RoomType("Studio d'enregistrement");
		RoomType repetition = new RoomType("Salle de répétition");
		RoomType unusual = new RoomType("Insolite");
		RoomType other = new RoomType("Autre");
		Set<RoomType> allTypes = Set.of(show, bar, workshop, dancehall, cityRoom, studio, repetition, unusual, other);
		roomTypeRepository.saveAll(allTypes);
		
		// Création des équipements
		Equipment parquet = new Equipment("Parquet");
		Equipment mirrors = new Equipment("Miroirs");
		Equipment mix = new Equipment("Console de mixage");
		Set<Equipment> equipments = Set.of(parquet, mirrors, mix);
		equipmentRepository.saveAll(equipments);
		
		
	}
}
