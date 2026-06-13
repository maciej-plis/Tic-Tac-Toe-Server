package matthias.tictactoe;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class IntegrationTest {

}
