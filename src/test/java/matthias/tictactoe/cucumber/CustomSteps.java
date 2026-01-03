package matthias.tictactoe.cucumber;

import com.decathlon.tzatziki.steps.HttpSteps;
import com.decathlon.tzatziki.steps.ObjectSteps;
import com.decathlon.tzatziki.utils.Guard;
import com.decathlon.tzatziki.utils.HttpStatusCode;
import com.decathlon.tzatziki.utils.Method;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;

import static com.decathlon.tzatziki.utils.Guard.GUARD;
import static com.decathlon.tzatziki.utils.HttpStatusCode.CREATED_201;
import static com.decathlon.tzatziki.utils.Method.POST;
import static com.decathlon.tzatziki.utils.Patterns.*;

public class CustomSteps {

    @Autowired
    private ObjectSteps objectSteps;

    @Autowired
    private HttpSteps httpSteps;

    @Given(THAT + GUARD + "(" + A_USER + ")" + "creates a Game Room with id (?:" + TYPE + " )?" + VARIABLE + "$")
    public void gameRoomIsCreated(Guard guard, String user, Type type, String variable) {
        httpSteps.send(Guard.always(), user, POST, "/game-rooms", null, null);
        httpSteps.we_receive_a_status_and_we_save_the_body_as(Guard.always(), CREATED_201, type, variable);
    }
}
