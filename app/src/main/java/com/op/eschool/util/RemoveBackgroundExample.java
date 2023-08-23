package com.op.eschool.util;

import okhttp3.*;
import okio.Okio;

import java.io.File;
import java.io.IOException;

public class RemoveBackgroundExample {
    public static void main(String[] args) {
        // Replace with your Remove.bg API key
        String apiKey = "YOUR_API_KEY";

        // Replace with the path to the input image file
        String inputImagePath = "path_to_input_image.jpg";

        // Replace with the path where you want to save the output image
        String outputImagePath = "path_to_output_image.png";

        OkHttpClient client = new OkHttpClient();

        File inputFile = new File(inputImagePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image_file", inputFile.getName(),
                        RequestBody.create(MediaType.parse("image/*"), inputFile))
                .build();

        Request request = new Request.Builder()
                .url("https://api.remove.bg/v1.0/removebg")
                .addHeader("X-Api-Key", apiKey)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    File outputFile = new File(outputImagePath);
                    if (outputFile.createNewFile()) {
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            responseBody.source().readAll(Okio.sink(outputFile));
                            responseBody.close();
                            System.out.println("Background removed image saved to: " + outputImagePath);
                        }
                    }
                } else {
                    System.out.println("Background removal request failed. Response code: " + response.code());
                }
            }
        });
    }
}
