package br.ufmg.engsoft.reprova.model.generator;

import br.ufmg.engsoft.reprova.model.Questionnaire;
import br.ufmg.engsoft.reprova.model.Environments;
import br.ufmg.engsoft.reprova.database.QuestionsDAO;

public class QuestionnaireGeneration extends AbstractChainQuestionnaireGeneration {
  public QuestionnaireGeneration() {
    Environments environments = Environments.getInstance();
    int valueDifficultyGroup = environments.getDifficultyGroup();

    this.generator = new GeneratorFactory().getGenerator(valueDifficultyGroup);
  }
  
  public Questionnaire generate(QuestionsDAO questionsDAO, String averageDifficulty, int questionsCount, int totalEstimatedTime) {
    Questionnaire questionnaire = this.generator.generate(questionsDAO, averageDifficulty, questionsCount, totalEstimatedTime);
    return handleGeneration(questionnaire);
  }
}