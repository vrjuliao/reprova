package br.ufmg.engsoft.reprova.model.difficulty;

import java.util.List;

public interface IDifficultyGroup {
    int getDifficultyGroup(double avg);
    
    List<String> getDifficulties();
}