package com.mealam.showdown.user;

import com.mealam.showdown.user.profile.UserProfile;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private UserProfile profile;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, updatable = false, nullable = false, length = 36)
	private String uuid;

	@PrePersist
	public void generateUuid() {
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
		}
	}

	@Column(unique = true)
	private String username;

	private String password;
}
