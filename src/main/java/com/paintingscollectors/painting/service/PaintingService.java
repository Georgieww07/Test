package com.paintingscollectors.painting.service;

import com.paintingscollectors.painting.model.FavouritePainting;
import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.repository.FavouritePaintingRepository;
import com.paintingscollectors.painting.repository.PaintingRepository;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.web.dto.CreatePaintingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class PaintingService {
    private final PaintingRepository paintingRepository;
    private final FavouritePaintingRepository favouritePaintingRepository;

    @Autowired
    public PaintingService(PaintingRepository paintingRepository, FavouritePaintingRepository favouritePaintingRepository) {
        this.paintingRepository = paintingRepository;
        this.favouritePaintingRepository = favouritePaintingRepository;
    }

    public List<Painting> getAllPaintings() {
        List<Painting> paintings = paintingRepository.findAll();

        paintings.sort(Comparator.comparing(Painting::getVotes).reversed().thenComparing(Painting::getName));

        return paintings;
    }

    public void createPainting(CreatePaintingRequest createPaintingRequest, User user) {
        Painting painting = Painting.builder()
                .name(createPaintingRequest.getName())
                .author(createPaintingRequest.getAuthor())
                .style(createPaintingRequest.getStyle())
                .imageUrl(createPaintingRequest.getImageUrl())
                .owner(user)
                .votes(0)
                .build();

        paintingRepository.save(painting);
    }

    public void createFavouritePaintingById(UUID id, User user) {

        Painting painting = getPaintingById(id);

        FavouritePainting favouritePainting = FavouritePainting.builder()
                .name(painting.getName())
                .author(painting.getAuthor())
                .owner(user)
                .imageUrl(painting.getImageUrl())
                .createdOn(LocalDateTime.now())
                .build();

        favouritePaintingRepository.save(favouritePainting);

    }

    private Painting getPaintingById(UUID id) {
        return paintingRepository.findById(id).orElseThrow(() -> new RuntimeException("Painting not found"));
    }

    public void deleteById(UUID id) {
        paintingRepository.deleteById(id);
    }

    public void deleteFavouriteById(UUID id) {
        favouritePaintingRepository.deleteById(id);
    }

    public void increaseVotes(UUID id) {
        Painting painting = getPaintingById(id);

        painting.setVotes(painting.getVotes() + 1);

        paintingRepository.save(painting);
    }
}
