Homework 3 for CSMET622

Overview:
This project retrieves dog breed data from the Dog API and compares two ways to search for words: Java Regex and an inverted index.

Search Terms
- dog
- breed
- weight

How It Works:
The program retrieves data from the Dog API and saves a copy to breeds.json. It then counts how many times each term appears using Regex and an inverted index. The program also measures the time taken by each method using System.nanoTime().

Results:
Both methods returned the same counts:
- dog: 1,219
- breed: 185
- weight: 2

For 5 runs, the average Regex search time was approximately 30.12 milliseconds and the inverted index averaged 28.73 milliseconds.


How to Run:
1. Open the project in IntelliJ IDEA with Java 21
2. Run Main.java
3. The program retrieves the data, counts the search terms, and prints the results and timing information.

API:
https://dogapi.dog/docs/api-v2