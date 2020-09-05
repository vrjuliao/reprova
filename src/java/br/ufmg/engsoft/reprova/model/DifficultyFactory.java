package br.ufmg.engsoft.reprova.model;

public class DifficultyFactory{
  public IDifficultyGroup difficultyGroup;

  public DifficultyFactory(int difficultiesCount){
    if (difficultiesCount == 3){
      this.difficultyGroup = new DifficultyGroup3();
    }
    this.difficultyGroup = new DifficultyGroup5();
  }
}