package br.ufmg.engsoft.reprova.model;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;

import br.ufmg.engsoft.reprova.database.QuestionsDAO;

/**
 * The Questionnaire type
 */
public class Questionnaire{
  /**
   * The id of the questionnaire.
   * When null, the id will be automatically generated by the database.
   */
  public final String id;
  /**
   * The list of Questions in the Questionnaire
   */
  public final ArrayList<Question> questions;
  /**
   * The Questionnaire's average difficulty.
   */
  public final String averageDifficulty;
  /**
   * The Questionnaire's total estimated time.
   */
  public final int totalEstimatedTime;

  public static class Generator{
    protected String id;
    protected String averageDifficulty;
    protected int totalEstimatedTime;
    protected int questionsCount;
    private List<Question> allQuestions;
    private List<String> difficultyGroup;

    public Generator id(String id){
      this.id = id;
      return this;
    }

    public Generator averageDifficulty(String averageDifficulty){
      this.averageDifficulty = averageDifficulty;
      return this;
    }

    public Generator totalEstimatedTime(int totalEstimatedTime){
      this.totalEstimatedTime = totalEstimatedTime;
      return this;
    }

    public Generator questionsCount(int questionsCount){
      this.questionsCount = questionsCount;
      return this;
    }

    /**
     * Auxiliary function that returns a list of questions with the given difficulty.
     * Attempts to fill the returned list with the given count of questions.
     */
    private List<Question> getQuestionsOfDifficulty(int count, String difficulty){
      List<Question> questions = new ArrayList<Question>();
      List<Question> questionsOfDifficulty = this.allQuestions.stream()
                                          .filter(q -> q.difficulty.equals(difficulty))
                                          .collect(Collectors.toList());

      Collections.shuffle(questionsOfDifficulty);
      for (int i = 0; i < count; i++){
        if (i >= questionsOfDifficulty.size()){
          break;
        }
        questions.add(questionsOfDifficulty.get(i));
      }

      return questions;
    }

    /**
     * Generate a new Quesitonnaire based on the parameters.
     * Selects a collection of questions the best fit the parameters.
     * Calls the Questionnaire's Builder.
     */
    public Questionnaire generate(QuestionsDAO questionsDAO){
      String envDifficultyGroup = System.getenv("DIFFICULTY_GROUP");
      if (envDifficultyGroup != null){
        if (envDifficultyGroup.equals("3")){
          this.difficultyGroup = new DifficultyFactory(3).difficultyGroup.getDifficulties();
        } else {
          this.difficultyGroup = new DifficultyFactory(5).difficultyGroup.getDifficulties();
        }
      } else {
        this.difficultyGroup = null;
      }

      if (this.averageDifficulty == null){
        this.averageDifficulty = "Average";
      } else {
        if (!this.difficultyGroup.contains(this.averageDifficulty)){
          throw new IllegalArgumentException("invalid average difficulty");
        }
      }
      if (this.totalEstimatedTime == 0){
        this.totalEstimatedTime = 60;
      }
      if (this.questionsCount == 0){
        this.questionsCount = 5;
      }

      ArrayList<Question> questions = new ArrayList<Question>();
      this.allQuestions = new ArrayList<Question>(questionsDAO.list(null, null));

      if (allQuestions.size() <= this.questionsCount){
        for(Question question : allQuestions){
          questions.add(question);
        }
      } else {
        int averageQuestionsCount = (int)Math.ceil(this.questionsCount * 0.5);
        List<Question> averageQuestions = getQuestionsOfDifficulty(averageQuestionsCount, this.averageDifficulty);
        questions.addAll(averageQuestions);

        int remainingQuestionsCount = this.questionsCount - questions.size();
        int easierQuestionsCount = remainingQuestionsCount % 2 == 1 ? remainingQuestionsCount/2 + 1 : remainingQuestionsCount/2;
        int harderQuestionsCount = remainingQuestionsCount - easierQuestionsCount;

        int easierDifficultyIndex = this.difficultyGroup.indexOf(this.averageDifficulty);
        int harderDifficultyIndex = easierDifficultyIndex;
        while (remainingQuestionsCount > 0){
          if (harderDifficultyIndex == 0){
            easierQuestionsCount += harderQuestionsCount;
            harderQuestionsCount = -1;
            harderDifficultyIndex = -1;
          } else {
            harderDifficultyIndex--;
          }
          
          if (easierDifficultyIndex == this.difficultyGroup.size()-1){
            harderQuestionsCount += easierQuestionsCount;
            easierQuestionsCount = -1;
            easierDifficultyIndex = -1;
          } else {
            easierDifficultyIndex++;
          }

          if (harderQuestionsCount != -1){
            List<Question> harderQuestions = getQuestionsOfDifficulty(harderQuestionsCount, this.difficultyGroup.get(harderDifficultyIndex));
            harderQuestionsCount -= harderQuestions.size();
            remainingQuestionsCount -= harderQuestions.size();
            questions.addAll(harderQuestions);
          }

          if (easierQuestionsCount != -1){
            List<Question> easierQuestions = getQuestionsOfDifficulty(easierQuestionsCount, this.difficultyGroup.get(easierDifficultyIndex));
            easierQuestionsCount -= easierQuestions.size();
            remainingQuestionsCount -= easierQuestions.size();
            questions.addAll(easierQuestions);
          }
        }
      }

      return new Questionnaire.Builder()
                 .averageDifficulty(this.averageDifficulty)
                 .totalEstimatedTime(this.totalEstimatedTime)
                 .questions(questions)
                 .build();
    }
  }

  public static class Builder{
    protected String id;
    protected String averageDifficulty;
    protected int totalEstimatedTime;
    protected ArrayList<Question> questions;

    public Builder id(String id){
      this.id = id;
      return this;
    }

    public Builder averageDifficulty(String averageDifficulty){
      this.averageDifficulty = averageDifficulty;
      return this;
    }

    public Builder totalEstimatedTime(int totalEstimatedTime){
      this.totalEstimatedTime = totalEstimatedTime;
      return this;
    }

    public Builder questions(ArrayList<Question> questions){
      this.questions = questions;
      return this;
    }

    /**
     * Build the Questionnaire;
     * @throws IllegalArgumentException if any parameter is invalid
     */

    public Questionnaire build() {
      if (this.questions == null){
        this.questions = new ArrayList<Question>();
      } else {
        for (var question : this.questions){
          if (question == null){
            throw new IllegalArgumentException("question mustn't be null");
          }
        }
      }

      return new Questionnaire(
        this.id,
        this.averageDifficulty,
        this.totalEstimatedTime,
        this.questions
      );
    }
  }

  /**
   * Protected constructor, should only be used by the builder.
   */
  protected Questionnaire(
    String id,
    String averageDifficulty,
    int totalEstimatedTime,
    ArrayList<Question> questions
  ){
    this.id = id;
    this.questions = questions;
    this.averageDifficulty = averageDifficulty;
    this.totalEstimatedTime = totalEstimatedTime;
  }

  /**
   * Convert a Question to String for visualization purposes.
   */
  @Override
  public String toString() {
    var builder = new StringBuilder();

    builder.append("Questionnaire:\n");
    builder.append("  id: " + this.id + "\n");
    builder.append("  averageDifficulty: " + this.averageDifficulty + "\n");
    builder.append("  totalEstimatedTime: " + this.totalEstimatedTime + "\n");
    builder.append("  questions:\n");
    for (var question : this.questions){
      builder.append("    id: " + question.id + "\n");
      builder.append("      theme: " + question.theme + "\n");
      builder.append("      desc: " + question.description + "\n");
      builder.append("      record: " + question.record + "\n");
      builder.append("      pvt: " + question.pvt + "\n");
      builder.append("      difficulty: " + question.difficulty + "\n");

      if (question.statement != null) {
        builder.append(
          "  head: " +
          question.statement.substring(
            0,
            Math.min(question.statement.length(), 50)
          ) +
          "\n"
        );
      }
    }

    return builder.toString();
  }
}