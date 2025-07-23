/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.user;

import com.mealam.showdown.user.data.UserId;
import com.mealam.showdown.user.profile.UserProfile;
import jakarta.persistence.*;
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
	private String userId;

	@PrePersist
	public void generateUuid() {
		if (userId == null) {
			userId = UserId.generate().value();
		}
	}

	@Column(unique = true)
	private String username;

	private String password;
}
