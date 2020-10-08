package com.uber.eats.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uber.eats.enums.StatusType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_user")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends CommonBaseEntity implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;

	@Column(name = "name")
	@NotBlank
	String name;

	@Column(name = "user_name", unique = true, nullable = false)
	@NotBlank
	String userName;

	@Column(name = "password")
	@NotBlank
	String password;

	@Column(name = "mobile_phone")
	String mobilePhone;

	@Column(name = "email")
	String email;

	@Column(name = "status")
	@NotNull
	@Enumerated(EnumType.STRING)
	StatusType status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	RoleEntity role;

	@Column(name = "never_expires")
	@NotNull
	Boolean neverExpire = true;

	@Column(name = "account_expired")
	@NotNull
	Boolean accountExpired = false;

	@Column(name = "account_locked")
	@NotNull
	Boolean accountLocked = false;

	@Column(name = "credentials_expired")
	@NotNull
	Boolean credentialsExpired = false;

	@Column(name = "enable")
	@NotNull
	Boolean enable = true;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tbl_user_address", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "address_id"))
	Set<AddressEntity> addressList = new HashSet<>();

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> authorityList = new ArrayList<>();

		if (role != null) {

			authorityList.add(role);

		}

		return authorityList;
	}

	@Override
	@JsonIgnore
	public String getUsername() {

		return userName;

	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {

		return !accountExpired;

	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {

		return !accountLocked;

	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {

		return !credentialsExpired;

	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {

		return enable;

	}

	@Transient
	public List<String> getStringAuthorities() {

		return getAuthorities().stream().flatMap(authority -> Stream.of(authority.getAuthority()))
				.collect(Collectors.toList());

	}

}
