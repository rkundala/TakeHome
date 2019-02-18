package com.mortgage;



import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mortgage.domain.Address;
import com.mortgage.domain.TakeHome;
 

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= {Mortgage1Application.class})
@RunWith(SpringRunner.class)
@WebFluxTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test1 {
	
	@Autowired
    private WebTestClient webClient;
	
	private static Map<String, TakeHome> customerMap = new HashMap<>();
	
	@BeforeClass
    public static void setup() throws Exception {
		Address add1 = new Address("A Ronaldborough", "VA");
		Address add2 = new Address("A Ronaldborough", "VA");
		TakeHome t1 = new TakeHome("home_482", "AAA Gould", "305372", add1);
		TakeHome t2 = new TakeHome("home_d50", "BBB Gould", "305373",  add2);
		customerMap.put("home_482",t1 );
		customerMap.put("home_d50",t1 );
			
		
	}
	
    @Test
    public void getProperty() throws Exception {
        
        webClient.get().uri("http://localhost:8080/property?property_id=home_482").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TakeHome.class)
                .hasSize(2)
                .contains(customerMap.get("home_482"), customerMap.get("home_d50"));
    }
    
	@Test
    public void getPropertyValue() throws Exception {
    	
        webClient.get().uri("http://localhost:8080/property?property_id=home_482").accept(MediaType.APPLICATION_JSON)
        		.exchange()
		        .expectStatus().isOk()
		        .expectBody(TakeHome.class)
		        .isEqualTo(customerMap.get("home_d50"));
    }
	
}