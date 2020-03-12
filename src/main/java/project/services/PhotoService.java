package project.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import project.exceptions.InternalException;
import project.exceptions.PhotoNotFoundException;
import project.models.Photo;
import project.repositories.PhotoRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhotoService {

	private final PhotoRepository photoRepository;

	private final RoomService roomService;
	
	private final String URL = "http://localhost:8080/api/photo/";

	public Photo save(MultipartFile file, int roomId) throws IOException {
		Photo photo = new Photo();
		try {
			photo.setFile(file.getBytes());
		} catch (IOException e) {
			throw new InternalException();
		}

		photo.setRoom(roomService.findById(roomId));
		photo = photoRepository.save(photo);
		photo.setUrl(URL + photo.getId());
		return photoRepository.save(photo);
	}

	public Photo findById(int id) throws PhotoNotFoundException {
		return photoRepository.findById(id)
				.orElseThrow(() -> new PhotoNotFoundException(id));

	}

	public List<Photo> findByRoom(int roomId) {
		List<Photo> photos = photoRepository.findByRoom(roomService.findById(roomId));
		if (photos.isEmpty())
			throw new PhotoNotFoundException();
		return photos;

	}
}
