package com.pocket.naturalist;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.pocket.naturalist.repository.AnimalRepository;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.SightingsRepository;
import com.pocket.naturalist.repository.UserRepository;

@SpringBootTest
class NaturalistApplicationTests {
	 @Mock
    private AnimalRepository animalRepository;

    @Mock
    private ParkRepository parkRepository;

    @Mock
    private SightingsRepository sightingsRepository;

	@Mock
	private UserRepository userRepository;
	

	@Test
	void contextLoads() {
	}

}
