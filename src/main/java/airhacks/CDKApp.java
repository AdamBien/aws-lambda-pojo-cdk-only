package airhacks;

import java.util.Map;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;

public class CDKApp {
    public static void main(final String[] args) {

        var app = new App();
        var appName = "aws-lambda-pojo-cdk-only";
        Tags.of(app).add("project", "airhacks.live");
        Tags.of(app).add("environment", "workshops");
        Tags.of(app).add("application", appName);

        new LambdaStack.Builder(app, appName)
                .functionHandler("airhacks.lambda.greetings.boundary.Greetings::onEvent")
                .functionName("airhacks_POJOGreetings")
                .functionJar("../[PROJECT_NAME]/target/function.jar")
                .configuration(Map.of("message", "hello,duke"))
                .build();
        app.synth();
    }
}
