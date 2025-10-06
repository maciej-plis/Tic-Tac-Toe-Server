package matthias.tictactoe.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import matthias.tictactoe.IntegrationTest;
import org.junit.platform.suite.api.*;
import org.springframework.security.test.context.support.WithMockUser;

import static com.decathlon.tzatziki.steps.ObjectSteps.handlebars;
import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;
import static java.util.UUID.randomUUID;

@WithMockUser
@CucumberContextConfiguration
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "junit, html:target/build/cucumber-reports/cucumber.html")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "matthias.tictactoe.cucumber, com.decathlon.tzatziki.steps")
@ExcludeTags("Ignore")
@SelectClasspathResource("features")
@IncludeEngines("cucumber")
@Suite
class CucumberTest extends IntegrationTest {

    static {
        handlebars.registerHelper("randomUUID", (ctx, options) -> randomUUID());
    }
}
