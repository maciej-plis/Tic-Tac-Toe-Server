package matthias.tictactoe.cucumber;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
class WebSocketTestConfiguration {

    @Primary
    @Bean
    ThreadPoolTaskExecutor taskExecutor() {
        return Mockito.mock(ThreadPoolTaskExecutor.class);
    }
}
