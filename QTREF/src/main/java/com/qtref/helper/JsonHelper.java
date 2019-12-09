package com.qtref.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qtref.exception.INGException;
import com.qtref.model.TransactionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the Helper class to parse the JSON Response Text
 */
public class JsonHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * This method reads the JSON response
     * Iterates each transactions object
     * from each transaction construct the Response Pojo Objects
     *
     * @param jsonResponse JSON response text to be parsed.
     * @param rootKey JSON root object
     * @return list of TransactionData
     * @throws INGException
     */
    public static List<TransactionData> getTransactionsListFromJSON (String jsonResponse, String rootKey)
            throws INGException {

        List<TransactionData> transsactionsList = Collections.emptyList();
        try {
              ObjectNode jsonNode = objectMapper.readValue(jsonResponse, ObjectNode.class);
              ArrayNode parentArrayNode = (ArrayNode) jsonNode.get(rootKey);

              parentArrayNode.forEach(childNode -> {

                 TransactionData transactionData = getTransactionData(childNode);
                 if(transactionData != null) {
                     transsactionsList.add(transactionData);
                 }
            });

        }
        catch(Exception exception) {
            throw new INGException(" Parsing Failed "+ exception.getMessage());
        }
        return transsactionsList;
    }

    /*
     * This method prepares JSON object
     */
    private static TransactionData getTransactionData(JsonNode jsonNode) {

        TransactionData transactionData = new TransactionData();

        transactionData.setId(getStringValue(jsonNode.get("id")));
        transactionData.setAccountId(getStringValue(jsonNode.get("this_account").get("id")));
        transactionData.setCounterpartyAccount(getStringValue(jsonNode.get("other_account").get("number")));
        transactionData.setCounterpartyName(getStringValue(jsonNode.get("other_account").get("holder").get("name")));
        transactionData.setCounterPartyLogoPath(getStringValue(jsonNode.get("other_account").get("metadata").get("image_URL")));

        String amount = getStringValue(jsonNode.get("details").get("value").get("amount"));
        transactionData.setInstructedAmount( (amount != null) ? Double.parseDouble(amount.replace("\"","")) : 0.0d );
        transactionData.setInstructedCurrency(getStringValue(jsonNode.get("details").get("value").get("currency")));

        amount = getStringValue(jsonNode.get("details").get("value").get("amount"));
        transactionData.setTransactionAmount( (amount != null) ? Double.parseDouble(amount.replace("\"","")) : 0.0d );
        transactionData.setTransactionCurrency(getStringValue(jsonNode.get("details").get("value").get("currency")));

        transactionData.setTransactionType(getStringValue(jsonNode.get("details").get("type")));

        transactionData.setDescription(getStringValue(jsonNode.get("details").get("description")));

        return transactionData;
    }

    /*
     * Get the String value from the JSONNode text
     */
    private static String getStringValue(Object object) {
        String retValue = null ;

        //trim if any quote character is present as prefix/suffix from the parsed text
        if(object != null) {
            retValue = object.toString().replace("\"", "");
        }
        return retValue;
    }
}
