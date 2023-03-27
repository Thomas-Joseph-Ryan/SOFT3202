package Tutorial.HTTP;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelloHTTP {
    public static void postRequest() {
        try {
            Post post = new Post(10, "My title", "My body text\nOf this post");
            Gson gson = new Gson();
            String postJSON = gson.toJson(post);

            HttpRequest request = HttpRequest.newBuilder(new URI("https://jsonplaceholder.typicode.com/posts"))
                    .POST(HttpRequest.BodyPublishers.ofString(postJSON))
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }
    }

    public static void getRequest() {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI("https://jsonplaceholder.typicode.com/posts/1"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());

            Gson gson = new Gson();
            Post post = gson.fromJson(response.body(), Post.class);
            System.out.println(post);

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }
    }

    public static void listAll() {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI("https://jsonplaceholder.typicode.com/posts"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());

            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(response.body(), JsonArray.class);

            for(int i=0; i<jsonArray.size(); i++) {
                // access the individual elements of the JSON array
                Post post = gson.fromJson(jsonArray.get(i), Post.class);
                System.out.println(post);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }
    }

    public static void delete() {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI("https://jsonplaceholder.typicode.com/posts/1"))
                    .DELETE()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }
    }

    public static void main(String[] args) {
        getRequest();
        postRequest();
    }
}
