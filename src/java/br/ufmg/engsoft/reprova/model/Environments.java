package br.ufmg.engsoft.reprova.model;

import java.util.Optional;

public class Environments {
	private static Environments environments;
	
	private boolean enableAnswers;
	
	private Environments() {		
		Optional<String> enableAnswersEnv = Optional.ofNullable(System.getenv("ENABLE_ANSWERS"));
		enableAnswersEnv.ifPresentOrElse(
				enableAnswers -> this.enableAnswers = enableAnswers.equals("true"),
				() -> this.enableAnswers = false);
		System.out.println("ENABLE_ANSWERS: " + this.enableAnswers);
	}
	
	public static Environments getInstance() {
		if (environments == null) {
			environments = new Environments();
		}
		
		return environments;
	}
	
	public boolean getEnableAnswers() {
		return this.enableAnswers;
	}

}
