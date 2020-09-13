package br.ufmg.engsoft.reprova.routes.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufmg.engsoft.reprova.database.AnswersDAO;
import br.ufmg.engsoft.reprova.mime.json.Json;
import br.ufmg.engsoft.reprova.model.ReprovaRoute;
import spark.Request;
import spark.Response;
import spark.Spark;

public class Answers extends ReprovaRoute {
	/**
	   * Logger instance.
	   */
	  protected static final Logger logger = LoggerFactory.getLogger(Answers.class);
	  
	  /**
	   * Json formatter.
	   */
	  protected final Json json;
	  
	  private final AnswersDAO answersDAO;
	  
	  /**
	   * Instantiate the answers endpoint.
	   * The setup method must be called to install the endpoint.
	   * @param json          the json formatter
	   * @param answersDAO  the DAO for Answer
	   * @throws IllegalArgumentException  if any parameter is null
	   */
	  public Answers(Json json, AnswersDAO answersDAO) {
	    if (json == null) {
	      throw new IllegalArgumentException("json mustn't be null");
	    }

	    if (answersDAO == null) {
	      throw new IllegalArgumentException("answersDAO mustn't be null");
	    }

	    this.json = json;
	    this.answersDAO = answersDAO;
	  }
	
	/**
	   * Install the endpoint in Spark.
	   * Methods:
	   * - get
	   * - post
	   * - delete
	   */
	  public void setup() {
	    Spark.get("/api/questions/:questionId/answers", this::getAllAnswers);
	    Spark.get("/api/questions/:questionId/answers/:answerId", (req, res) -> "Specific answer");

	    logger.info("Setup /api/answers.");
	  }
	  
	  /**
       * Get endpoint: lists all answers for a given question, or a single answer 
       * if an 'id' query parameter is provided.
       */
      protected Object getAllAnswers(Request request, Response response) {
        logger.info("Received answers get:");

        String id = request.params(":questionId");
        boolean auth = authorized(request.queryParams("token"));
        
        // TODO check how to use auth here
        var answers = answersDAO.list(id, auth ? null : false);
        
        logger.info("Done. Responding...");
        response.status(200);
        return json.render(answers);
      }
}
