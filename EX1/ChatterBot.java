import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 * @author Dan Nirel
 */
class ChatterBot {
	static final String REQUEST_PREFIX = "say ";

	static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";

	static final String ILLEGAL_REQUEST_PLACEHOLDER = "<illegal request>";

	Random rand = new Random();

	String name;

	String[] repliesToLegalRequest;

	String[] repliesToIllegalRequest;


	ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
		this.name = name;
		this.repliesToLegalRequest = repliesToLegalRequest;
		for(int j = 0 ; j < repliesToLegalRequest.length ; j++) {
			this.repliesToLegalRequest[j] = repliesToLegalRequest[j];}
		this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
		for(int i = 0 ; i < repliesToIllegalRequest.length ; i = i+1) {
			this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
		}
	}

	String replacePlaceholderARandomPattern(String[] possibleReplies, String placeHolder, String replacement){
		int randomIndex = rand.nextInt(possibleReplies.length);
		String responsePattern = possibleReplies[randomIndex]; // chooses a pattern from a given array
		String reply = responsePattern.replaceAll(placeHolder, replacement);
		return reply;
	}

	String replyTo(String statement) {
		if(statement.startsWith(REQUEST_PREFIX)) {
			return respondToLegalRequest(statement);
		}
		return respondToIllegalRequest(statement);
	}

	String respondToLegalRequest(String statement) {
		//we don’t repeat the request prefix, so delete it from the reply
		String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
		String reply = replacePlaceholderARandomPattern(repliesToLegalRequest, REQUESTED_PHRASE_PLACEHOLDER, phrase);
		return reply;
	}

	String respondToIllegalRequest(String statement) {
		String reply = replacePlaceholderARandomPattern(repliesToIllegalRequest, ILLEGAL_REQUEST_PLACEHOLDER, statement);
		return reply;
	}

	String getName() {
		return name;
	}
}
