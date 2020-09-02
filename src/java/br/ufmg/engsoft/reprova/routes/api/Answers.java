package br.ufmg.engsoft.reprova.routes.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

public class Answers {
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

	    logger.info("Setup /api/questions.");
	  }
}
