package br.ufmg.engsoft.reprova.routes.api;

import spark.Spark;
import spark.Request;
import spark.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import br.ufmg.engsoft.reprova.database.QuestionnairesDAO;
import br.ufmg.engsoft.reprova.database.QuestionsDAO;
import br.ufmg.engsoft.reprova.model.Environments;
import br.ufmg.engsoft.reprova.model.Question;
import br.ufmg.engsoft.reprova.model.Questionnaire;
import br.ufmg.engsoft.reprova.mime.json.Json;

/**
 * Questionnaires route.
 */
public class Questionnaires {
    /**
     * Logger instance.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(Questionnaires.class);

    /**
     * Access token.
     */
    protected static final String TOKEN = System.getenv("REPROVA_TOKEN");

    /**
     * Messages.
     */
    protected static final String UNAUTHORIZED = "\"Unauthorized\"";
    protected static final String INVALID = "\"Invalid request\"";
    protected static final String OK = "\"Ok\"";

    /**
     * Json formatter.
     */
    protected final Json json;
    /**
     * DAO for Questionnaire.
     */
    protected final QuestionnairesDAO questionnairesDAO;
    /**
     * DAO for Question.
     */
    protected final QuestionsDAO questionsDAO;

    /**
     * Instantiate the questionnaires endpoint.
     * The setup method must be called to install the endpoint.
     * 
     * @param json              the json formatter
     * @param questionnairesDAO the DAO for Questionnaire
     * @param questionsDAO      the DAO for Question
     * @throws IllegalArgumentException if any parameter is null
     */
    public Questionnaires(Json json, QuestionnairesDAO questionnairesDAO, QuestionsDAO questionsDAO) {
        if (json == null) {
            throw new IllegalArgumentException("json mustn't be null");
        }

        if (questionnairesDAO == null) {
            throw new IllegalArgumentException("questionnairesDAO mustn't be null");
        }

        if (questionsDAO == null) {
            throw new IllegalArgumentException("questionsDAO mustn't be null");
        }

        this.json = json;
        this.questionnairesDAO = questionnairesDAO;
        this.questionsDAO = questionsDAO;
    }

    /**
     * Install the endpoint in Spark.
     * Methods:
     * - get
     * - post
     * - generate
     * - delete
     */
    public void setup() {
        Spark.get("/api/questionnaires", this::get);
        Spark.post("/api/questionnaires", this::post);
        Spark.post("/api/questionnaires/generate", this::generate);
        Spark.delete("/api/questionnaires", this::delete);
        Spark.delete("/api/questionnaires/deleteAll", this::deleteAll);

        LOGGER.info("Setup /api/questionnaires.");
    }

    /**
     * Check if the given token is authorized.
     */
    protected static boolean authorized(String token) {
        return Questionnaires.TOKEN.equals(token);
    }

    /**
     * Get endpoint: lists all questionnaires, or a single questionnaire if a 'id'
     * query parameter is
     * provided.
     */
    protected Object get(Request request, Response response) {
        LOGGER.info("Received questionnaires get:");

        var questionnaireId = request.queryParams("id");
        var auth = authorized(request.queryParams("token"));

        if (questionnaireId == null) {
            return this.get(request, response, auth);
        }

        return this.get(request, response, questionnaireId, auth);
    }

    /**
     * Get id endpoint: fetch the specified questionnaire from the database.
     * If not authorised, and the given questionnaire is private, returns an error
     * message.
     */
    protected Object get(Request request, Response response, String questionnaireId, boolean auth) {
        if (questionnaireId == null) {
            throw new IllegalArgumentException("id mustn't be null");
        }

        response.type("application/json");

        LOGGER.info("Fetching questionnaire {}", questionnaireId);

        var questionnaire = questionnairesDAO.get(questionnaireId);

        if (questionnaire == null) {
            LOGGER.error("Invalid request!");
            response.status(400);
            return INVALID;
        }

        LOGGER.info("Done. Responding...");

        response.status(200);

        return json.render(questionnaire);
    }

    /**
     * Get all endpoint: fetch all questionnaires from the database.
     * If not authorized, fetches only public questionnaires.
     */
    protected Object get(Request request, Response response, boolean auth) {
        response.type("application/json");

        LOGGER.info("Fetching questionnaires.");

        var questionnaires = questionnairesDAO.list();

        LOGGER.info("Done. Responding...");

        response.status(200);

        return json.render(questionnaires);
    }

    /**
     * Helper function to build Question
     *
     */
    private Question buildQuestion(Question question) {
        if (Environments.getInstance().getEnableEstimatedTime()) {
            return new Question.Builder()
                .theme(question.theme)
                .description(question.description)
                .statement(question.statement)
                .estimatedTime(question.estimatedTime)
                .record(question.record)
                .pvt(question.pvt)
                .build();
        }

        return new Question.Builder()
            .theme(question.theme)
            .description(question.description)
            .statement(question.statement)
            .record(question.record)
            .pvt(question.pvt)
            .build();
    }

    /**
     * Post endpoint: add or update a questionnaire in the database.
     * The questionnaire must be supplied in the request's body.
     * If the questionnaire has an 'id' field, the operation is an update.
     * Otherwise, the given questionnaire is added as a new questionnaire in the
     * database.
     * This endpoint is for authorized access only.
     */
    protected Object post(Request request, Response response) {
        String body = request.body();

        LOGGER.info("Received questionnaires post: {}", body);

        response.type("application/json");

        var token = request.queryParams("token");

        if (!authorized(token)) {
            LOGGER.info("Unauthorized token: {}", token);
            response.status(403);
            return UNAUTHORIZED;
        }

        Questionnaire questionnaire;
        try {
            questionnaire = json
                .parse(body, Questionnaire.Builder.class)
                .build();
        } catch (Exception e) {
            LOGGER.error("Invalid request payload!", e);
            response.status(400);
            return INVALID;
        }

        LOGGER.info("Parsed {}", questionnaire);
        LOGGER.info("Adding questionnaire.");

        var success = questionnairesDAO.add(questionnaire);

        LOGGER.info("Added questionnaire.");
        LOGGER.info("Adding questions.");

        for (var question : questionnaire.questions) {
            question = buildQuestion(question);

            questionsDAO.add(question);
        }

        response.status(
            success ? 200
                    : 400
        );

        LOGGER.info("Done. Responding...");

        return OK;
    }

    /**
     * Generate endpoint: create a questionnaire in the database.
     * The parameters for the questionnaire's generation must be supplied in the
     * request's body.
     * Such parameters must include the averageDifficulty and may include
     * totalEstimatedTime.
     * This endpoint is for authorized access only.
     */
    protected Object generate(Request request, Response response) {
        String body = request.body();

        LOGGER.info("Received questionnaires post: {}", body);

        response.type("application/json");

        var token = request.queryParams("token");

        if (!authorized(token)) {
            LOGGER.info("Unauthorized token: {}", token);
            response.status(403);
            return UNAUTHORIZED;
        }

        Questionnaire questionnaire;
        try {
            questionnaire = json
                .parse(body, Questionnaire.Generator.class)
                .generate(questionsDAO);
        } catch (Exception e) {
            LOGGER.error("Invalid request payload!", e);
            response.status(400);
            return INVALID;
        }

        LOGGER.info("Generated {}", questionnaire);
        LOGGER.info("Adding questionnaire.");

        var success = questionnairesDAO.add(questionnaire);

        LOGGER.info("Added questionnaire.");

        response.status(
            success ? 200
                    : 400
        );

        LOGGER.info("Done. Responding...");

        return OK;
    }

    /**
     * Delete endpoint: remove a questionnaire from the database.
     * The questionnaire's id must be supplied through the 'id' query parameter.
     * This endpoint is for authorized access only.
     */
    protected Object delete(Request request, Response response) {
        LOGGER.info("Received questionnaires delete:");

        response.type("application/json");

        var questionnaireId = request.queryParams("id");
        var token = request.queryParams("token");

        if (!authorized(token)) {
            LOGGER.info("Unauthorized token: {}", token);
            response.status(403);
            return UNAUTHORIZED;
        }

        if (questionnaireId == null) {
            LOGGER.error("Invalid request!");
            response.status(400);
            return INVALID;
        }

        LOGGER.info("Deleting questionnaire {}", questionnaireId);

        var success = questionnairesDAO.remove(questionnaireId);

        LOGGER.info("Done. Responding...");

        response.status(
            success ? 200
                    : 400
        );

        return OK;
    }

    /**
     * Delete All endpoint: remove all questionnaires from the database.
     * This endpoint is for authorized access only.
     */
    protected Object deleteAll(Request request, Response response) {
        LOGGER.info("Received questionnaires delete all:");

        response.type("application/json");

        var token = request.queryParams("token");

        if (!authorized(token)) {
            LOGGER.info("Unauthorized token: {}", token);
            response.status(403);
            return UNAUTHORIZED;
        }

        LOGGER.info("Deleting all questionnaires");

        boolean success = false;
        ArrayList<Questionnaire> questionnaires = new ArrayList<Questionnaire>(questionnairesDAO.list());
        for (Questionnaire questionnaire : questionnaires) {
            String questionnaireId = questionnaire.id;
            LOGGER.info("Deleting questionnaire {}", questionnaireId);

            success = questionnairesDAO.remove(questionnaireId);
            if (!success) {
                break;
            }
        }

        LOGGER.info("Done. Responding...");

        response.status(
            success ? 200
                    : 400
        );

        return OK;
    }
}