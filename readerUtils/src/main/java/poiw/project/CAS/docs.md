##  Card reader

Provides a wrapper around the Java Smart Card I/O API for getting UID
of contactless cards

## # Example usage
```java
Reader reader = new Reader();
reader.initialize();

// Get list of readers and print them
System.out.println(reader.getReaders());

// Select the first reader and get UID
reader.setReader(0);
String cardUID = reader.getUID();
System.out.println("Card UID: " + cardUID);

```
