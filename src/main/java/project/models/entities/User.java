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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.models.Role;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DynamicUpdate
@SelectBeforeUpdate
public class User implements UserDetails {

	private static final long serialVersionUID = -3366338174920579829L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String firstname;

	private String lastname;
	
	private String phoneNumber;

	@OneToOne(cascade = CascadeType.ALL)
	private Address address;

	@JsonBackReference
	private String password;

	private boolean accountNonLocked = true;

	@Column(unique = true)
	private String email;

	@Column(length = 16, columnDefinition = "varchar(16) default 'USER'")
	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<Role> roles = Set.of(Role.USER);

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JsonIgnoreProperties("owner")
	private Set<Room> favorites = new HashSet<Room>();
	
	@JsonIgnoreProperties("owner")
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private Set<Room> rooms = new HashSet<Room>();

	@JsonIgnoreProperties("client")
	@OneToMany(mappedBy = "client")
	private Set<Booking> bookings = new HashSet<Booking>();

	@JsonIgnore
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<Comment>();

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		for (Role role : roles) {
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

	@PreRemove
	public void preRemove() {
		for(Booking booking: bookings) {
			booking.setClient(null);
		}
	}
	
	

}
