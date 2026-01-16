package com.minor.photo_app.service;

import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.request.PhotoCreateRequest;
import com.minor.photo_app.dto.response.PhotoResponse;
import com.minor.photo_app.entity.Photo;
import com.minor.photo_app.entity.Place;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.exception.NotFoundException;
import com.minor.photo_app.exception.PhotoAppException;
import com.minor.photo_app.mapper.PhotoMapper;
import com.minor.photo_app.repository.PhotoRepository;
import com.minor.photo_app.service.fileStorage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final UserService userService;
    private final PlaceService placeService;
    private final PhotoMapper photoMapper;
    private final FileStorage fileStorage;

    @Transactional(readOnly = true)
    public Slice<PhotoResponse> getPhotosByUser(UserPrincipal userPrincipal) {
        User user = userService.getUserByPrincipal(userPrincipal);
        Slice<Photo> photos = photoRepository.getPhotosByUserId(user.getId());
        return photos.map(photoMapper::toPhotoResponse);
    }

    @Transactional
    public PhotoResponse createPhoto(MultipartFile image, PhotoCreateRequest request, UserPrincipal userPrincipal) {
        User user = userService.getUserByPrincipal(userPrincipal);
        Place place = placeService.getPlace(request.getPlaceId());

        String imageUrl = fileStorage.saveFile(image);

        Photo photo = photoMapper.toEntity(request);
        photo.setUser(user);
        photo.setPlace(place);
        photo.setImageUrl(imageUrl);
        if (request.getIsMain() == null) {
            photo.setIsMain(Boolean.FALSE);
        }

        Photo saved = photoRepository.save(photo);
        return photoMapper.toPhotoResponse(saved);
    }

    public void deletePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() ->
                new NotFoundException(String.format("Фото с id = %s не найдено", photoId)));
        fileStorage.deleteFile(photo.getImageUrl());
        photoRepository.delete(photo);
    }

    @Transactional
    public PhotoResponse updatePhotoWithPlace(Long photoId, Long placeId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() ->
                new NotFoundException(String.format("Фото с id = %s не найдено", photoId)));

        if (photo.getPlace().getId().equals(placeId)) {
            throw new PhotoAppException(String.format("Невозможно сменить место фотографии. " +
                    "Такой id = %s места уже есть у фотографии!", placeId));
        }

        Place place = placeService.getPlace(placeId);
        photo.setPlace(place);
        Photo saved = photoRepository.save(photo);
        return photoMapper.toPhotoResponse(saved);
    }
}
