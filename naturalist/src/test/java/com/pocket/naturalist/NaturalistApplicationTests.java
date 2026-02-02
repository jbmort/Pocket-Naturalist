package com.pocket.naturalist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.pocket.naturalist.repository.AnimalRepository;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.SightingsRepository;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.serviceTests.MockBean;

@SpringBootTest
class NaturalistApplicationTests {
	 @MockBean
    private AnimalRepository animalRepository;

    @MockBean
    private ParkRepository parkRepository;

    @MockBean
    private SightingsRepository sightingsRepository;

	@MockBean
	private UserRepository userRepository;
	

	@Test
	void contextLoads() {
	}

}
