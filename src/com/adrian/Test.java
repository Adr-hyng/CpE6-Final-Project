package com.adrian;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final String originalText = "I may be an old man, but I was once a great adventurer like you!";
//		String text = originalText;
//		String newText = "";
//		int textLimit = 29;
//		int newLineCount = text.length() / textLimit;
//		for(int i = 1; i < newLineCount + 1; i++) {
//			int leftIndex = text.substring(0, (textLimit * i)).lastIndexOf(" ");
//			text = text.substring(0, (leftIndex * 1)) + "\n" + text.substring((leftIndex * 1) + 1);
//			newText = text;
//		}
		String newText = cleanText(originalText); 
		System.out.println(newText);
	}
	
	public static String cleanText(String originalText) {
		String text = originalText;
		String newText = "";
		int textLimit = 29;
		int newLineCount = text.length() / textLimit;
		for(int i = 1; i < newLineCount + 1; i++) {
			int leftIndex = text.substring(0, (textLimit * i)).lastIndexOf(" ");
			text = text.substring(0, (leftIndex * 1)) + "\n" + text.substring((leftIndex * 1) + 1);
			newText = text;
		}
		return newText;
	}

}
