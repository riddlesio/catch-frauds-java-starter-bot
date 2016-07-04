/*
 * Copyright 2016 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package bot.data;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Record
 *
 * This class stores all information about a record and methods can
 * be implemented here that perform calculations on the record data
 *
 * Currently only stores a map of all the string values of the record,
 * plus the parsed amount.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class PaymentRecord {

    private final static Logger LOGGER = Logger.getLogger(PaymentRecord.class.getName());

    private HashMap<String, String> stringValueMap;
    private double amount;

    public PaymentRecord(String[] recordFormat, String record) throws InstantiationError {
        this.stringValueMap  = new HashMap<>();
        String[] values = record.split(",");

        if (recordFormat.length != values.length) {
            throw new InstantiationError("Record does not match given format");
        }

        for (int i = 0; i < recordFormat.length; i++) {
            this.stringValueMap.put(recordFormat[i], values[i]);
            this.parseValues(recordFormat[i], values[i]);
        }
    }

    private void parseValues(String key, String value) {
        try {
            switch (key) {
                case "amount":
                    this.amount = Double.parseDouble(value);
                    break;
                default:
//                    LOGGER.severe(String.format("Can't parse record with key '%s'", key));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format(
                    "Cannot parse record value '%s' for key '%s'", value, key), e);
        }
    }

    public String getData(String key) {
        return this.stringValueMap.get(key);
    }

    public double getAmount() {
        return this.amount;
    }

    // TODO: Parse the record values (such as date, etc.), which are now just strings

    // TODO: Implement useful methods to perform calculations on the record data
}
