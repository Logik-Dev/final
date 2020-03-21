package project.models.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.models.Role;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String firstname;
	
	private String lastname;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	private boolean accountNonLocked = true;
	
	@Column(unique = true)
	private String email;
	
	@Column(length = 16, columnDefinition = "varchar(16) default 'USER'")
	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<Role> roles = Set.of(Role.USER);
	
	@JsonIgnore
	@OneToMany(mappedBy = "owner")
	private Set<Room> rooms;
	
	@JsonIgnore
	@OneToMany(mappedBy = "client")
	private Set<Booking> bookings;
	
	@JsonIgnore
	@OneToMany(mappedBy = "author")
	private Set<Comment> comments;

	@ElementCollection
	private Set<Long> bookingNotifications;

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		for(Role role: roles) {
			authorities.add(new SimpleGrantedAuthority(role.toString()));
		}
		return authorities;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return email;
	}
	
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	
	
	
	
	
}
