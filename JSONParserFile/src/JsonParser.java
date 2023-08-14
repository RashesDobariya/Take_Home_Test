import java.util.*;


public class JsonParser {


    // Wrapper class to hold the deserialized response
    public static class ResponseDeserializeWrapper {

        public String debug;
        public WindowDeserializerWrapper windowDeserialize = new WindowDeserializerWrapper();
    }

    // data stricture to hold window properties
    public static class WindowDeserializerWrapper {

        public String title;
        public Integer size;
    }

    // Function to parse JSON string and return a map as a response.
    public static Map<String, Object> parse(String json) {

        Map<String, Object> jsonValuesMap = new HashMap<>();
        Map<String, Object>  windowValueMap = new HashMap<>();


        // Extract the "debug" field value
        int lastValueOfDebug = json.indexOf(":");
        int windowStartValue = json.indexOf(",");
        String debugValue = json.substring(lastValueOfDebug, windowStartValue);
        debugValue = debugValue.replaceAll("[^a-zA-Z0-9]", " ").trim();

        // Extract the JSON content for the "window" field
        int windowLastValue = json.indexOf("}");
        String windowJsonFile = json.substring(windowStartValue + 1, windowLastValue + 1);

        // This function is going to parse the values of the window JSON content.
        ResponseDeserializeWrapper wrapper = windowParser(windowJsonFile);
        wrapper.debug = debugValue;

        // System.out.println("@@ debug value is :: "+ debugValue );
        // Store window properties within the map
        windowValueMap.put("title",wrapper.windowDeserialize.title);
        windowValueMap.put("size",wrapper.windowDeserialize.size);

        // Store debug and window properties in the main map
        jsonValuesMap.put("debug", wrapper.debug);
        jsonValuesMap.put("window", windowValueMap);


        return jsonValuesMap;
    }

    // Parse the JSON content related to window properties.
    public static ResponseDeserializeWrapper windowParser(String windowStr) {

        ResponseDeserializeWrapper deserializeWindow = new ResponseDeserializeWrapper();

        // Extract the index of title value
        int titleStartingIndex = windowStr.indexOf("\"title\"");
        int titleLastIndex = windowStr.indexOf(",");
        String titleValue = windowStr.substring(titleStartingIndex + 6, titleLastIndex);

        // Remove all unwanted characters from the title value.
        titleValue = titleValue.replaceAll("[^a-zA-Z0-9]", " ").trim();


        // Extract the index of size value
        int sizeStartingIndex = windowStr.indexOf("\"size\"");
        int sizeLastIndex = windowStr.indexOf("}");
        String sizeValueStr = windowStr.substring(sizeStartingIndex + 5, sizeLastIndex);

        // Remove all unwanted characters from the size value.
        sizeValueStr = sizeValueStr.replaceAll("[^a-zA-Z0-9]", " ").trim();


        int sizeOfWindow = (Integer.parseInt(sizeValueStr));

        // System.out.println("@@sizeOfWindow:: " + sizeOfWindow);

        // Populate the deserialized window properties
        deserializeWindow.windowDeserialize.title = titleValue;
        deserializeWindow.windowDeserialize.size = sizeOfWindow;

        return deserializeWindow;

    }


    public static void main(String[] args) {

        String jsonString = " { \"debug\":\"on\" ,\"window\" : {\"title\" : \"sample\",\"size\" : 500   } } ";

        parse(jsonString);

    }
}


