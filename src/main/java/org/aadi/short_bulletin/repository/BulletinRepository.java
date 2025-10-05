package org.aadi.short_bulletin.repository;

import org.aadi.short_bulletin.entity.Bulletin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BulletinRepository extends JpaRepository<Bulletin, Long> {
    Optional<Bulletin> findByDate(LocalDate date);

    @Query("SELECT b.date FROM Bulletin b")
    List<LocalDate> findAllDates();
}