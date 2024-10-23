package com.miniplm.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.miniplm.convert.UserConverter;
import com.miniplm.entity.Role;
import com.miniplm.entity.ZAccount;
import com.miniplm.repository.UserRepository;
import com.miniplm.request.AuthRequest;
import com.miniplm.request.RegisterRequest;
import com.miniplm.utils.Md5PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements UserDetailsService{
	
//	@Autowired
	private final UserRepository userRepository;
	
//	@Autowired
//	private final PasswordEncoder passwordEncoder;
	
//	public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = new BCryptPasswordEncoder();
//    }
	@Transactional
    public ZAccount createUser(RegisterRequest registerRequest) throws NoSuchAlgorithmException {
    	registerRequest.setPassword(new Md5PasswordEncoder().encode(registerRequest.getPassword()));
    	ZAccount user = UserConverter.INSTANCT.requestToEntity(registerRequest);
        userRepository.save(user);
        return userRepository.save(user);
    }
    
	@Transactional
    public ZAccount changePassword(AuthRequest changePasswordRequest) throws NoSuchAlgorithmException {
    	Optional oUser = userRepository.findByUsernameIgnoreCase(changePasswordRequest.getUsername());
    	ZAccount user = (ZAccount) oUser.get();
    	user.setPassword(new Md5PasswordEncoder().encode(changePasswordRequest.getPassword()));
        userRepository.save(user);
        return userRepository.save(user);
    }

	public List<ZAccount> srearchUser(String username, String email) {
    	log.info("Username: {} email: {}", username, email);
    	if (username != null && email != null) {
    		return userRepository.findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(username, email);
    	}
    	else if (username != null && email == null) {
    		return userRepository.findByUsernameContainingIgnoreCase(username);
    	}
    	else if (username == null && email != null) {
    		return userRepository.findByEmailContainingIgnoreCase(email);
    	}else {
    		return userRepository.findAll();
    	}	
    }
	
    public Page<ZAccount> srearchUser(String username, String email, Pageable pageable) {
    	log.info("Username: {} email: {}", username, email);
    	log.info("Page: {}", pageable);
    	if (username != null && email != null) {
    		return userRepository.findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(username, email, pageable);
    	}
    	else if (username != null && email == null) {
    		return userRepository.findByUsernameContainingIgnoreCase(username, pageable);
    	}
    	else if (username == null && email != null) {
    		return userRepository.findByEmailContainingIgnoreCase(email, pageable);
    	}else {
    		return userRepository.findAll(pageable);
    	}	
    }

    public ZAccount getUser(String id) {
        Optional<ZAccount> instance = userRepository.findById(id);
        if (instance.isPresent()) {
            return instance.get();
        }else {
        	throw new EntityNotFoundException();
        }
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		log.info("username: {}", username);
//		System.out.println("username:"+username);
		Optional<ZAccount> instance = userRepository.findByUsernameIgnoreCase(username);
        if (instance.isPresent()) {
//        	log.info("zaccount exist!");
//        	System.out.println("zaccount exist!");
            return instance.get();
        }
//        log.info("zaccount not exist!");
//        System.out.println("zaccount not exist!");
        throw new UsernameNotFoundException("username not found");
	}
	
	public Set<Role> getRoles(String uId){
		ZAccount user = userRepository.getReferenceById(uId);
		return user.getRoles();
	}
}