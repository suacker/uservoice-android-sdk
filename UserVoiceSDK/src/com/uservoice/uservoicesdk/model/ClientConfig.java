package com.uservoice.uservoicesdk.model;

import java.util.List;

import com.uservoice.uservoicesdk.Session;
import org.json.JSONException;
import org.json.JSONObject;

import com.uservoice.uservoicesdk.rest.Callback;
import com.uservoice.uservoicesdk.rest.RestTaskCallback;

public class ClientConfig extends BaseModel {
	private boolean ticketsEnabled;
	private boolean feedbackEnabled;
	private boolean whiteLabel;
	private int defaultForumId;
	private List<CustomField> customFields;
	private String defaultSort;
	private String subdomainId;
    private String key;
    private String secret;
    private String accountName;
	
	public static void loadClientConfig(final Callback<ClientConfig> callback) {
        String path = Session.getInstance().getConfig().getKey() == null ? "/clients/default.json" : "/client.json";
		doGet(apiPath(path), new RestTaskCallback(callback) {
			@Override
			public void onComplete(JSONObject result) throws JSONException {
				callback.onModel(deserializeObject(result, "client", ClientConfig.class));
			}
		});
	}
	
	@Override
	public void load(JSONObject object) throws JSONException {
		super.load(object);
		
		ticketsEnabled = object.getBoolean("tickets_enabled");
		feedbackEnabled = object.getBoolean("feedback_enabled");
		whiteLabel = object.getBoolean("white_label");
		defaultForumId = object.getJSONObject("forum").getInt("id");
		customFields = deserializeList(object, "custom_fields", CustomField.class);
		defaultSort = getString(object.getJSONObject("subdomain"), "default_sort");
		subdomainId = getString(object.getJSONObject("subdomain"), "id");
		accountName = getString(object.getJSONObject("subdomain"), "name");
        key = object.getString("key");
        // secret will only be available for the default client
        secret = object.has("secret") ? object.getString("secret") : null;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public boolean isTicketSystemEnabled() {
		return ticketsEnabled;
	}
	
	public boolean isFeedbackEnabled() {
		return feedbackEnabled;
	}
	
	public boolean isWhiteLabel() {
		return whiteLabel;
	}
	
	public int getDefaultForumId() {
		return defaultForumId;
	}
	
	public List<CustomField> getCustomFields() {
		return customFields;
	}
	
	public String getSuggestionSort() {
		return defaultSort.equals("new") ? "newest" : (defaultSort.equals("hot") ? "hot" : "votes");
	}
	
	public String getSubdomainId() {
		return subdomainId;
	}

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }
}
