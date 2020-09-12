package br.ufmg.engsoft.reprova.routes.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	   * Install the endpoint in Spark.
	   * Methods:
	   * - get
	   * - post
	   * - delete
	   */
	  public void setup() {
	    Spark.get("/api/answers", (req, res) -> "Sample answers");

	    logger.info("Setup /api/answers.");
	  }
}
