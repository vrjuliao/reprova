package br.ufmg.engsoft.reprova.model;

public class DifficultyFactory{
  
  public IDifficultyGroup getDifficulty(int difficultiesCount) {
	  if (difficultiesCount == 3){
		  return new DifficultyGroup3();
	  }

	  return new DifficultyGroup5();  
  }
}