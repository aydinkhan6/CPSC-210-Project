package persistence;

import org.json.JSONObject;

/* Citations: Modelled based on Sample Application, 
 * https://github.com/stleary/JSON-java.git
 */

public interface Writable {
    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
