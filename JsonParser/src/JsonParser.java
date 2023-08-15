import java.util.*;


public class JsonParser {

    // Create String data type to Object
    public static Object parseDatatype(String data) {

        if (data.startsWith("\"") && data.endsWith("\"")) {
            return  unicodeStringValue(data.substring(1, data.length()-1));
        } else if (data.equalsIgnoreCase("true") || data.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(data);
        } else if (data.equalsIgnoreCase("null")) {
            return null;

        } else if (data.startsWith("[") && data.endsWith("]")) {

            // parse Array elements here
            String[] elements = data.substring(1, data.length() - 1).split(",");
            List<Object> array = new ArrayList<>();
            for (String element : elements) {
                array.add(parseDatatype(element.trim()));
            }
            return array;

        } else if (data.matches("^-?\\d+(\\.\\d+)?([eE][-+]?\\d+)?$")) {
            if (data.contains(".") || data.toLowerCase().contains("e")) {
                return Double.parseDouble(data);
            } else {
                try {

                    if (data.matches("^-?\\d+$")) {
                        long longValue = Long.parseLong(data);

                        // Check if it's within the valid Integer range
                        if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                            return Integer.parseInt(data);
                        } else {
                            return longValue;

                        }
                    } else {
                        return Long.parseLong(data);
                    }
                }
                catch (NumberFormatException e){

                    return Long.parseLong(data);
                }
            }
        }

        return data;

    }

    // Method to convert JSON escape sequences to corresponding characters
    private static String unicodeStringValue(String value) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                char nextChar = value.charAt(i + 1);
                switch (nextChar) {
                    case '"':

                    case '\\':

                    case '/':
                        result.append(nextChar);
                        i++; // Skip the escaped character
                        break;
                    case 'b':
                        result.append('\b');
                        i++;
                        break;
                    case 'f':
                        result.append('\f');
                        i++;
                        break;
                    case 'n':
                        result.append('\n');
                        i++;
                        break;
                    case 'r':
                        result.append('\r');
                        i++;
                        break;
                    case 't':
                        result.append('\t');
                        i++;
                        break;
                    case 'u':
                        if (i + 5 < value.length()) {
                            String fourHexDigits = value.substring(i + 2, i + 6);
                            try {
                                int unicode = Integer.parseInt(fourHexDigits, 16);
                                result.append((char) unicode);
                                i += 5; // Skip the Unicode sequence
                            } catch (NumberFormatException e) {
                                // Invalid Unicode escape, treat it as a regular character
                                result.append(c);
                            }
                        } else {
                            // Not enough characters for a valid Unicode escape, treat it as a regular character
                            result.append(c);
                        }
                        break;
                    default:
                        // Invalid escape sequence, treat the backslash as a regular character
                        result.append(c);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // Json Parser to parse Json string in to Map<String, Object> type.
    public static Map<String, Object> parse(String json) {

        Stack<Map<String, Object>> objectStack = new Stack<>();
        Map<String, Object> parsedObject = new HashMap<>();
        boolean parsingKey = false , parsingValue = false,  flagValue = false;
        StringBuilder currentKey = new StringBuilder();
        StringBuilder currentValue = new StringBuilder();

        char stringQuote = '"';

        // Loop through each character in the JSON string
        for (int i=0; i < json.length() ;i++) {

            // Get the current character
            char character = json.charAt(i);
            /*
            if (Character.isWhitespace(character)) {
                continue; // Skip whitespace characters
            }*/


            // Check for opening curly brace '{' and handle object parsing
            if (character == '{' && !flagValue) {
                if (parsingValue) {

                    // Store the current object and start a new one
                    objectStack.push(parsedObject);
                    Map<String, Object> newObject = new HashMap<>();
                    parsedObject.put(currentKey.toString(), newObject);

                    currentKey.setLength(0);
                    parsedObject = newObject;

                    parsingValue = false;
                } else {
                    objectStack.push(parsedObject);
                }

                // Check for closing curly brace '}' and finalize object parsing
            } else if (character == '}' && !flagValue) {
                if (parsingValue) {

                    // Store the current value and reset for next parsing
                    parsedObject.put(currentKey.toString(), parseDatatype(currentValue.toString()));

                    currentKey.setLength(0);
                    currentValue.setLength(0);
                    parsingValue = false;
                }
                if (!objectStack.isEmpty()) {
                    parsedObject = objectStack.pop();
                }

                // Check for colon ':' to switch from key to value parsing
            } else if (character == ':' && !flagValue) {
                parsingKey = false;
                parsingValue = true;

                // Check for comma ',' to finalize value parsing
            } else if (character == ',' && !flagValue) {
                if (parsingValue) {
                    parsedObject.put(currentKey.toString(), parseDatatype(currentValue.toString()));
                    currentKey.setLength(0);
                    currentValue.setLength(0);
                    parsingValue = false;
                }

                // Check for string quotation marks '"' or '\''
            } else if (character == '"' || character == '\'') {
                if (flagValue && character == stringQuote) {
                    if (parsingValue) {

                        // Store the current value and reset for next parsing
                        parsedObject.put(currentKey.toString(), currentValue.toString());
                        currentKey.setLength(0);
                        currentValue.setLength(0);
                        parsingValue = false;
                    }
                    flagValue = false;
                } else {
                    flagValue = true;
                    stringQuote = character;

                    // Append the character to the current value
                    if (parsingValue) {
                        currentValue.append(character);
                    }
                }

                // Handle parsing of values
            } else if (parsingValue) {

                // Append the character to the current value
                currentValue.append(character);

                // Handle parsing of keys
            } else if (parsingKey) {
                currentKey.append(character);
            } else {
                // Start parsing the key
                parsingKey = true;
                currentKey.append(character);
            }
        }

        // return the parsed JSON Object
        return parsedObject;
    }

    public static void main(String[] args) {
        String json = "{\"user\":{\"name\":\"John Doe\",\"age\":30,\"address\":{\"street\":\"123 Main Street\",\"city\":\"Los Angeles\",\"state\":\"CA\",\"zipcode\":90815}},\"orders\":[{\"order_id\":123456,\"product_name\":\"iPhone 15 Pro\",\"quantity\":1,\"price\":1199.99},{\"order_id\":789012,\"product_name\":\"MacBook Pro\",\"quantity\":2,\"price\":1999.99},{\"order_id\":null,\"product_name\":null,\"quantity\":null,\"price\":null},{\"order_id\":\"unused character\",\"product_name\":\"unused character\",\"quantity\":\"unused character\",\"price\":\"unused character\"}]}";
        Map<String, Object> output = parse(json);

        System.out.println("@@output :: "+output);

        Object university =  output.get("user");

        if (university instanceof Map) {
            System.out.println("@@user Name: " + ((Map<String, Object>) university).get("name"));
        }

    }
}

