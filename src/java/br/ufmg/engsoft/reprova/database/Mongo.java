package br.ufmg.engsoft.reprova.database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mongodb instance.
 */
public class Mongo {
  /**
   * Logger instance.
   */
  protected static final Logger LOGGER = LoggerFactory.getLogger(Mongo.class);

  /**
   * Full connection string, obtained from 'REPROVA_MONGO' environment variable.
   */
  protected static final String ENDPOINT = System.getenv("REPROVA_MONGO");

  /**
   * The mongodb driver instance.
   */
  protected final MongoDatabase mongoDB;



  /**
   * Instantiate for access in the given database.
   * @param databaseName  the database name.
   */
  public Mongo(String databaseName) {
	System.out.println(Mongo.ENDPOINT);
	  
    this.mongoDB = MongoClients
      .create(Mongo.ENDPOINT)
      .getDatabase(databaseName);

    LOGGER.info("connected to db '" + databaseName + "'");
  }


  /**
   * Gets the given collection in the database.
   */
  public MongoCollection<Document> getCollection(String name) {
    return mongoDB.getCollection(name);
  }
}
