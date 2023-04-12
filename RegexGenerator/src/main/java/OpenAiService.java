import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class OpenAiService {
    private static final String API_KEY = "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";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/text-davinci-002/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client;
    private Gson gson;

    public OpenAiService() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public String generateRegex(String userInput) throws IOException {
        String prompt = "Generate an Oracle database regex function and pattern based on user input: \"" + userInput + "\"";
        String jsonInput = createJsonInput(prompt);

        RequestBody requestBody = RequestBody.create(jsonInput, JSON);
        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String jsonResponse = response.body().string();
            return parseRegexFromJson(jsonResponse);
        }
    }

    private String createJsonInput(String prompt) {
        JsonObject jsonInput = new JsonObject();
        jsonInput.addProperty("prompt", prompt);
        jsonInput.addProperty("max_tokens", 50);
        jsonInput.addProperty("n", 1);
        jsonInput.add("stop", null);
        return gson.toJson(jsonInput);
    }

    private String parseRegexFromJson(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String regex = jsonObject.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .get("text").getAsString()
                .trim();
        return regex;
    }
}
