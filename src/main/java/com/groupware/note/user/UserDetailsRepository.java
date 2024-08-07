package com.groupware.note.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;



@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {
	
	Optional<UserDetails> findByEmail(String email);
<<<<<<< HEAD
	

=======
>>>>>>> c8122d41698c738aa612372cf6949461e697e6de
}
