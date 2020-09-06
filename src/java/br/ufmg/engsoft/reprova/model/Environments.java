package br.ufmg.engsoft.reprova.model;

import java.util.Optional;

public class Environments {
	private static Environments environments;
	
	private boolean enableAnswers;
	private int difficultyGroup;
	
	private Environments() {		
		Optional<String> enableAnswersEnv = Optional.ofNullable(System.getenv("ENABLE_ANSWERS"));
		enableAnswersEnv.ifPresentOrElse(
				enableAnswers -> this.enableAnswers = enableAnswers.equals("true"),
				() -> this.enableAnswers = false);
		Optional<String> envDifficultyGroup = Optional.ofNullable(System.getenv("DIFFICULTY_GROUP"));
		envDifficultyGroup.ifPresentOrElse(
		        difficultyGroup -> this.difficultyGroup = Integer.parseInt(envDifficultyGroup.get()),
		        () -> this.difficultyGroup = 0);		        		        
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
	
	public int getDifficultyGroup() {
	    return this.difficultyGroup;
	}

}
