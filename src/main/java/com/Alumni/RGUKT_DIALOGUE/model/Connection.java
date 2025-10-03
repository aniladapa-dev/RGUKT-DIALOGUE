package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Connections", uniqueConstraints = @UniqueConstraint(columnNames = {"user1Id", "user2Id"}))
//uniqueConstraints : It tells the database that the combination of user1Id and user2Id must be unique in that table.
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //Many connections can include the same user.
    //Example: A may have 100 connections.
    //This field means one side of the connection.
    //In the DB, there will be a user1Id column pointing to Users.id.
    @JoinColumn(name = "user1Id", nullable = false)
    private User user1;

    @ManyToOne
    //Same as above, but for the other side of the connection.
    //Many connections can include the same user as user2.
    //In DB, this creates user2Id column pointing to Users.id.
    @JoinColumn(name = "user2Id", nullable = false)
    private User user2;

    private LocalDateTime connectedAt = LocalDateTime.now();
}

