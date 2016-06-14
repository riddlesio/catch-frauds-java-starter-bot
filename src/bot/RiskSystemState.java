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

package bot;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import bot.action.Assessment;
import bot.data.PaymentRecord;

/**
 * bot.RiskSystemState
 * 
 * Stores all information about the current bot state, such as the bot's timebank,
 * the bot's name, records gotten so far and assessments made so far (and more).
 *
 * Also parses the input gotten from the game engine.
 *
 * This class can be editted to store even more data.
 * 
 * @author Jim van Eeden - jim@riddles.io
 */

public class RiskSystemState {

    private final static Logger LOGGER = Logger.getLogger(RiskSystemState.class.getName());

    private int MAX_TIMEBANK;
    private int TIME_PER_MOVE;
    private int MAX_CHECKPOINTS;

    private String[] recordFormat;
    private ArrayList<PaymentRecord> records;
    private ArrayList<Assessment> assessments;
    private int timebank;
    private String myName;
    
    public RiskSystemState() {
        this.records = new ArrayList<>();
        this.assessments = new ArrayList<>();
    }
    
    /**
     * Parses all the game settings given by the game engine
     * @param key Type of data given
     * @param value Value
     */
    public void parseSettings(String key, String value) {
        try {
            switch (key) {
                case "timebank":
                    int time = Integer.parseInt(value);
                    this.MAX_TIMEBANK = time;
                    this.timebank = time;
                    break;
                case "time_per_move":
                    this.TIME_PER_MOVE = Integer.parseInt(value);
                    break;
                case "player_names":
                    // ignore, only 1 player this game
                    break;
                case "your_bot":
                    this.myName = value;
                    break;
                case "max_checkpoints":
                    this.MAX_CHECKPOINTS = Integer.parseInt(value);
                    break;
                case "record_format":
                    this.recordFormat = value.split(",");
                    break;
                default:
                    LOGGER.severe(String.format("Cannot parse settings input with key '%s'", key));
            }
       } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format(
                    "Cannot parse settings value '%s' for key '%s'", value, key), e);
       }
    }
    
    /**
     * Parse a record that was sent by the game engine
     * @param value String representation of the record
     */
    public void parseRecord(String value) {
        try {
            this.records.add(new PaymentRecord(this.recordFormat, value));
        } catch (InstantiationError e) {
            LOGGER.log(Level.SEVERE, String.format("Cannot parse record '%s'", value), e);
        }
    }

    /**
     * Set the time left in the bot's time bank, i.e. time left before
     * the engine will determine the bot has timed out.
     * @param value Time left in ms
     */
    public void setTimebank(int value) {
        this.timebank = value;
    }

    /**
     * Add an assessment to the list of assessments made so far.
     * @param assessment Assessment
     */
    public void storeAssessment(Assessment assessment) {
        this.assessments.add(assessment);
    }

    /**
     * Get the current timebank.
     * @return Current timebank
     */
    public int getTimebank() {
        return this.timebank;
    }

    /**
     * Get the bot's name as determined by the game engine.
     * @return Bot's given name
     */
    public String getMyName() {
        return this.myName;
    }

    /**
     * Get the maximum amount of check points the bot can have.
     * @return Maximum amount of check points
     */
    public int getMaxCheckPoints() {
        return this.MAX_CHECKPOINTS;
    }

    /**
     * Get the record that was last given by the game engine.
     * @return Current record
     */
    public PaymentRecord getCurrentRecord() {
        return this.records.get(this.records.size() - 1);
    }

    /**
     * Get all the records the bot has received so far.
     * @return A list of all records
     */
    public ArrayList<PaymentRecord> getRecords() {
        return this.records;
    }

    /**
     * Get all the assessments made so far.
     * @return A list of all assessments
     */
    public ArrayList<Assessment> getAssessments() {
        return this.assessments;
    }
}