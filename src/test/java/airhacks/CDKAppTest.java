package airhacks;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import software.amazon.awscdk.App;

public class CDKAppTest {
    private final static ObjectMapper JSON =
        new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    public void testStack() throws IOException {
        var path = Files.createTempDirectory("function");
        var functionJar = path.resolve("function.jar");
        var existingFile = Files.createFile(functionJar);        
        App app = new App();
        var stack = new LambdaStack.Builder(app, "test")  
                .functionHandler("airhacks.lambda.greetings.boundary.Greetings::onEvent")
                .functionName("airhacks_POJOGreetings")
                .functionJar(existingFile.toString())
                .configuration(Map.of("message", "hello,duke"))
                .build();

        // synthesize the stack to a CloudFormation template
        var actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());

        // Update once resources have been added to the stack
        assertThat(actual.get("Resources")).isNotEmpty();
    }
}
