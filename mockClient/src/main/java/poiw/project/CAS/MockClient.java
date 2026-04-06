package poiw.project.CAS;

public class MockClient {
    public static void main(String[] args) {
        MockClient client = new MockClient();
        System.out.println(client.getMessage());
    }

    public String getMessage() {
        return "Hello, World!";
    }
}
