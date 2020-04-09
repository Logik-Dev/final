package project.utils;

import java.time.DayOfWeek;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import project.models.BookingStatus;
import project.models.Role;
import project.models.entities.Address;
import project.models.entities.Booking;
import project.models.entities.Equipment;
import project.models.entities.Room;
import project.models.entities.RoomType;
import project.models.entities.User;
import project.repositories.BookingRepository;
import project.repositories.EquipmentRepository;
import project.repositories.RoomRepository;
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
	private RoomRepository roomRepository;
	
	@Autowired
	private BookingRepository bookingRepository;
		
	@Autowired
	private PasswordEncoder encoder;
	
	public void initData() {
		
		// Création de l'admin
		User admin = new User();
		admin.setFirstname("Cédric");
		admin.setLastname("Maunier");
		admin.setEmail("admin@gmail.com");
		admin.setPassword(encoder.encode("root"));
		admin.setRoles(Set.of(Role.ADMIN));
		userRepository.save(admin);
		
		// Creation de Charlotte
		User charlotte = new User();
		charlotte.setFirstname("Charlotte");
		charlotte.setLastname("Carré");
		charlotte.setEmail("boulet1310@gmail.com");
		charlotte.setPassword(encoder.encode("changeme"));
		userRepository.save(charlotte);
				
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
		
		// Création de l'adresse dancing
		Address address = new Address();
		address.setCity("Achères");
		address.setLabel("2 Allée andré Chénier");
		address.setLatitude(48.505015);
		address.setLongitude(2.5646454);
		address.setZipCode(78260);
		
		// Création de la salle dancing
		Room dancing = new Room();
		dancing.setName("Le dancing");
		dancing.setAvailableDays(Set.of(DayOfWeek.FRIDAY, DayOfWeek.MONDAY, DayOfWeek.SUNDAY, DayOfWeek.SATURDAY));
		dancing.setEquipments(Set.of(parquet, mirrors));
		dancing.setMaxCapacity(50);
		dancing.setPrice(25);
		dancing.setOwner(charlotte);
		dancing.setType(dancehall);
		dancing.setAddress(address);
		roomRepository.save(dancing);
		
		// Créer une réservation passé
		Booking booking = new Booking();
		booking.setClient(admin);
		booking.setRoom(dancing);
		booking.setBegin(DateUtils.parseDateTime("01/01/2020 10:00"));
		booking.setEnd(DateUtils.parseDateTime("01/01/2020 11:00"));
		booking.setWeekRepetition(0);
		booking.calculatePrice();
		booking.setStatus(BookingStatus.FINISHED);

		// Reservation en cours confirmé
		Booking booking2 = new Booking();
		booking2.setClient(admin);
		booking2.setRoom(dancing);
		booking2.setBegin(DateUtils.parseDateTime("01/07/2020 16:00"));
		booking2.setEnd(DateUtils.parseDateTime("09/07/2020 17:00"));
		booking2.setWeekRepetition(1);
		booking2.calculatePrice();
		booking2.setStatus(BookingStatus.CONFIRMED);
		
		// Non confirmé
		Booking booking3 = new Booking();
		booking3.setClient(admin);
		booking3.setRoom(dancing);
		booking3.setBegin(DateUtils.parseDateTime("01/08/2020 18:00"));
		booking3.setEnd(DateUtils.parseDateTime("01/08/2020 19:00"));
		booking3.setWeekRepetition(0);
		booking3.calculatePrice();
		booking3.setStatus(BookingStatus.PENDING);
		
		bookingRepository.saveAll(Set.of(booking, booking2, booking3));
	}
}
