package project.utils;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import project.models.Role;
import project.models.Volume;
import project.models.entities.Address;
import project.models.entities.Booking;
import project.models.entities.Equipment;
import project.models.entities.EventType;
import project.models.entities.Room;
import project.models.entities.RoomEquipment;
import project.models.entities.RoomType;
import project.models.entities.TimeSlot;
import project.models.entities.User;
import project.repositories.BookingRepository;
import project.repositories.EquipmentRepository;
import project.repositories.EventTypeRepository;
import project.repositories.RoomRepository;
import project.repositories.RoomTypeRepository;
import project.repositories.UserRepository;

@Slf4j
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
	private EventTypeRepository eventRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String dbMode;

	public void initData() {

		if (!dbMode.equals("update")) {
			log.info("\n ******** Initializing Data ***********");

			// Création de l'admin
			User admin = new User();
			admin.setFirstname("cédric");
			admin.setLastname("maunier");
			admin.setEmail("admin@gmail.com");
			admin.setPhoneNumber("0633462050");
			admin.setPassword(encoder.encode("root"));
			admin.setRoles(Set.of(Role.ADMIN));
			userRepository.save(admin);

			// Creation de Charlotte
			User charlotte = new User();
			charlotte.setFirstname("charlotte");
			charlotte.setLastname("carré");
			charlotte.setEmail("boulet1310@gmail.com");
			charlotte.setPhoneNumber("0689134566");
			charlotte.setPassword(encoder.encode("changeme"));
			userRepository.save(charlotte);

			// Création des types de salle
			RoomType show = new RoomType("salle de spectacle");
			RoomType bar = new RoomType("bar");
			RoomType workshop = new RoomType("atelier");
			RoomType dancehall = new RoomType("salle de danse");
			RoomType cityRoom = new RoomType("salle municipale");
			RoomType studio = new RoomType("studio d'enregistrement");
			RoomType repetition = new RoomType("salle de répétition");
			RoomType unusual = new RoomType("insolite");
			RoomType other = new RoomType("autre");
			RoomType rest = new RoomType("restaurant");
			Set<RoomType> allTypes = Set.of(show, bar, workshop, dancehall, cityRoom, studio, repetition, unusual, rest,
					other);
			roomTypeRepository.saveAll(allTypes);

			// Création des types d'évenements
			EventType anniv = new EventType("Anniversaire");
			EventType night = new EventType("Soirée");
			EventType repet = new EventType("Répétition");
			EventType afterWork = new EventType("AfterWork");
			EventType seminaire = new EventType("Séminaire");
			EventType expo = new EventType("Exposition");
			EventType concert = new EventType("Concert");
			EventType showEvent = new EventType("Show");
			EventType diner = new EventType("Diner");
			EventType artisanat = new EventType("Artisanat");
			EventType photo = new EventType("Photographie");
			EventType artistique = new EventType("Artistique");

			Set<EventType> allEvents = Set.of(anniv, night, repet, afterWork, seminaire, expo, concert, showEvent,
					diner, artisanat, photo, artistique);
			eventRepository.saveAll(allEvents);

			// Création des équipements
			Equipment proj = new Equipment("materiel de projection");
			Equipment sono = new Equipment("sono");
			Equipment vestiaire = new Equipment("vestiaire");
			Equipment piste = new Equipment("piste de danse");
			Equipment table = new Equipment("table");
			Equipment chaise = new Equipment("chaise");
			Equipment wifi = new Equipment("wifi");
			Equipment enceinte = new Equipment("enceinte");
			Equipment micro = new Equipment("micro");
			Equipment etablie = new Equipment("etabli");
			Equipment batterie = new Equipment("batterie");
			Equipment pianoQueue = new Equipment("piano à queue");
			Equipment pianoDroit = new Equipment("piano droit");
			Equipment clavier = new Equipment("synthétiseur");
			Equipment ordi = new Equipment("ordinateur");
			Equipment platine = new Equipment("platine");

			Set<Equipment> equipments = Set.of(proj, sono, vestiaire, piste, table, chaise, wifi, enceinte, micro, ordi,
					clavier, etablie, batterie, pianoDroit, pianoQueue, platine);
			equipmentRepository.saveAll(equipments);

			// Création de l'adresse dancing
			Address address = new Address();
			address.setCity("Achères");
			address.setLabel("2 Allée Andre de Chenier");
			address.setLatitude(48.956877);
			address.setLongitude(2.06249);
			address.setZipCode(78260);

			// Création de la salle dancing
			Room dancing = new Room();
			dancing.setName("le dancing");
			dancing.setAvailableDays(Set.of("vendredi", "lundi", "samedi", "dimanche"));
			dancing.setEventTypes(Set.of(repet));
			dancing.setMaxVolume(Volume.MOYEN);
			dancing.setSize(80);
			Set<RoomEquipment> dancingEquipments = Set.of(new RoomEquipment(piste, 1),
					new RoomEquipment(chaise, 6), new RoomEquipment(enceinte, 2),
					new RoomEquipment(wifi, 1));
			dancing.setEquipments(dancingEquipments);
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
			TimeSlot slot1 = new TimeSlot();
			slot1.setStart(DateUtils.parseDateTime("01/01/2020 10:00"));
			slot1.setEnd(DateUtils.parseDateTime("01/01/2020 11:00"));
			booking.setSlots(Set.of(slot1));

			Booking booking2 = new Booking();
			booking2.setClient(admin);
			booking2.setRoom(dancing);
			TimeSlot slot2 = new TimeSlot();
			slot2.setStart(DateUtils.parseDateTime("01/07/2020 16:00"));
			slot2.setEnd(DateUtils.parseDateTime("09/07/2020 17:00"));
			booking2.setSlots(Set.of(slot2));

			bookingRepository.saveAll(Set.of(booking, booking2));

			log.info("\n ******** Data initialized ***********");
		}
	}
}
