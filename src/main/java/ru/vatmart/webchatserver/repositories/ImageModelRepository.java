package ru.vatmart.webchatserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vatmart.webchatserver.entities.ImageModel;

@Repository
public interface ImageModelRepository extends JpaRepository<ImageModel, Long> {
}
