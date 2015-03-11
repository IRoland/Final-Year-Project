package com.example.finalproject;

public class QuizQuestion {
	
	private String Question, CorrectAnswer, Choice2, Choice3, Choice4;

	public QuizQuestion(String question, String correctAnswer, String choice2,
			String choice3, String choice4) {
		super();
		Question = question;
		CorrectAnswer = correctAnswer;
		Choice2 = choice2;
		Choice3 = choice3;
		Choice4 = choice4;
	}

	public String getQuestion() {
		return Question;
	}

	public String getCorrectAnswer() {
		return CorrectAnswer;
	}

	public void setQuestion(String question) {
		Question = question;
	}

	public void setCorrectAnswer(String correctAnswer) {
		CorrectAnswer = correctAnswer;
	}

	public void setChoice2(String choice2) {
		Choice2 = choice2;
	}

	public void setChoice3(String choice3) {
		Choice3 = choice3;
	}

	public void setChoice4(String choice4) {
		Choice4 = choice4;
	}

	public String getChoice2() {
		return Choice2;
	}

	public String getChoice3() {
		return Choice3;
	}

	public String getChoice4() {
		return Choice4;
	}

}
