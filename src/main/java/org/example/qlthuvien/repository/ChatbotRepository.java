package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Chatbot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatbotRepository extends JpaRepository<Chatbot, Long> {
    List<Chatbot> findChatbotByUser_Id(Long id);
}
