package sermaye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Multithreading {

	private static int NUM_OF_THREADS = 5;
	private static final Map<Integer, ThreadWorker> objectMap = new HashMap<>(); // Map or storing Threadworker objects
	private static List<String> sentences = new ArrayList<String>();
	public static LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
	public static int sentenceCount = 0;
	public static int wordCount = 0;

	public static String getFilePath() {
		System.out.println("Lütfen dosya pathi giriniz : ");
		return new Scanner(System.in).next();
	}

	public static void findThreadCount() {
		System.out.println("Lütfen çalışacak thread adedi giriniz : ");
		try {
			NUM_OF_THREADS = new Scanner(System.in).nextInt();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void readFile(String path) {
		try {
			String content = new String(Files.readAllBytes(Paths.get(path)));
			String[] sentencesArr = content.split("[\\.\\!\\?]");
			for (String s : sentencesArr) {
				s = s.trim();
				sentences.add(s);
				sentenceCount++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String filePath = getFilePath();
		findThreadCount();
		readFile(filePath);

		System.out.println("Thread counts:");
		ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREADS);
		// Creating threads
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			ThreadWorker thread = new ThreadWorker(i);
			objectMap.put(i, thread); // Add objects to map
		}
		for (int i = 0; i < sentences.size(); i++) {
			objectMap.get(i % NUM_OF_THREADS).addToQueue(sentences.get(i));
		}

		for (int i = 0; i < NUM_OF_THREADS; i++) {
			executor.execute(objectMap.get(i));
		}

		boolean running = true;
		boolean isFinished = false;
		while (running) {
			int i = 0;
			isFinished = false;
			for (i = 0; i < objectMap.size(); i++) {
				if (objectMap.get(i).isFinished) {
					isFinished = true;
				} else {
					isFinished = false;
					break;
				}
			}
			running = !isFinished;

		}
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			// Comparing two entries by value
			public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {

				// Subtracting the entries
				return entry2.getValue() - entry1.getValue();
			}
		});

		for (Map.Entry<String, Integer> l : list) {

			// Printing the sorted map
			// using getKey() and getValue() methods
			System.out.println(l.getKey() + " " + l.getValue());
		}
	}
}