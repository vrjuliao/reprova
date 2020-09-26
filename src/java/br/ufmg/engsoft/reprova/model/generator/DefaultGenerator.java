package br.ufmg.engsoft.reprova.model.generator;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;

import br.ufmg.engsoft.reprova.model.Environments;
import br.ufmg.engsoft.reprova.model.Question;
import br.ufmg.engsoft.reprova.model.Questionnaire;
import br.ufmg.engsoft.reprova.database.QuestionsDAO;
import br.ufmg.engsoft.reprova.model.difficulty.DifficultyFactory;

public class DefaultGenerator implements IQuestionnaireGenerator{

  public Questionnaire generate(QuestionsDAO questionsDAO, String averageDifficulty, int questionsCount, int totalEstimatedTime){
    //implement default questionnaire generation
    ArrayList<Question> questions = new ArrayList<Question>();

    return new Questionnaire.Builder()
                .averageDifficulty(averageDifficulty)
                .totalEstimatedTime(totalEstimatedTime)
                .questions(questions)
                .build();
  };
}