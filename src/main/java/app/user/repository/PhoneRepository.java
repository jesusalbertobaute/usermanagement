package app.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import app.user.model.Phone;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {
}