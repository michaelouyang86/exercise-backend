package com.michael.exercise;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	@Disabled
	void getEncoded() {
		PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 1 << 14, 2);
		System.out.println(passwordEncoder.encode("admin"));
	}
}
