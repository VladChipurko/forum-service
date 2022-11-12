package telran.java2022;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.java2022.account.dao.UserRepository;
import telran.java2022.account.model.User;

@SpringBootApplication
public class ForumServiseApplication implements CommandLineRunner{
	@Autowired
	UserRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ForumServiseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(!repository.existsById("admin")) {
			String password = BCrypt.hashpw("admin", BCrypt.gensalt());
			User user = new User("admin", password, "", "");
			user.addRole("ADMIN");
			user.addRole("MODERATOR");
			repository.save(user);
		}
		
	}

}
