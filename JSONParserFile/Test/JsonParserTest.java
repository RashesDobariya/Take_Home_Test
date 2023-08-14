import org.junit.jupiter.api.Test;
import java.util.*;

public class JsonParserTest {
    @Test
    public void testParse() {

        // JSON input string for testing
        String jsonString = "{\"debug\":\"on\",\"window\":{\"title\":\"sample\",\"size\":500}}";

        // Invoke the parse method to deserialize the JSON
        Map<String, Object> output = JsonParser.parse(jsonString);

        // Extract window map from the output
        Map<String, Object> windowMap = (Map<String, Object>) output.get("window");

        assert output.get("debug").equals("on");

        assert windowMap.get("title").equals("sample");
        assert windowMap.get("size").equals(500);

        // Assert the window title value directly
        //assert (Map<String, Object>) (output.get("window")).get("title").equals("sample"));

        //assert (Map<String, Object>) (output.get("window")).get("size").equals(500));


    }

}