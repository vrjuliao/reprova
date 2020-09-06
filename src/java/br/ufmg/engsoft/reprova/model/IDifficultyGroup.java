package br.ufmg.engsoft.reprova.model;

import java.util.List;

public interface IDifficultyGroup{
  public int getDifficultyGroup(double avg);
  public List<String> getDifficulties();
}