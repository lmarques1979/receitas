package br.com.marquesapps.receitas.test

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import br.com.marquesapps.receitas.Main;

@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = Main)
@WebAppConfiguration
class ReceitasApplicationTests {

	@Test
	void contextLoads() {
	}

}
