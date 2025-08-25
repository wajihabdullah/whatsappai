package com.wajih.whatsappai.Repository;

import com.wajih.whatsappai.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
