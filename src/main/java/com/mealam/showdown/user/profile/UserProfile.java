package com.mealam.showdown.user.profile;

import com.mealam.showdown.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_profiles")
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", unique = true)
	private User user;

	@Column(length = 32)
	private String displayName;

	@Column(length = 256)
	private String description;

	@Column(length = 512)
	private String imageUrl;
}
