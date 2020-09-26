package br.ufmg.engsoft.reprova.model.generator;

import br.ufmg.engsoft.reprova.model.Questionnaire;

public class EstimatedTimeCalculator extends ChainQuestionnaireGeneration{

  public Questionnaire generate(Questionnaire questionnaire){
    //implement total estimated time calculation
    return handleGeneration(questionnaire);
  }
}