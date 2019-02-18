package com.mortgage;

import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= {Mortgage1Application.class})

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TakeHomeClientServiceImplTest {

    @Autowired
    private WebTestClient webTestClient;

@Before
public void setUp() {
    webTestClient = webTestClient
            .mutate()
            .responseTimeout(Duration.ofMillis(36000))
            .build();
}
    @Test
    public void testSearchPropertyById() {
    	String id = "home_482";
        webTestClient.get()
		.uri("/property?property_id=" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> 
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }
    

}
