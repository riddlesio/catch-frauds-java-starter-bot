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

package bot.action;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import bot.BotState;
import bot.checkpoint.AbstractCheck;

/**
 * bot.action.Assessment
 *
 * This class performs the assessment of the record against all the checks and
 * determines if the record is approved or rejected.
 *
 * This assessment is stored and can be gotten from the toString() method
 * in the right format for the game engine.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Assessment {

    private final static Logger LOGGER = Logger.getLogger(Assessment.class.getName());

    private Boolean isAuthorized;
    private ArrayList<Integer> failedChecks;

    /**
     * Used to assess the current record and store the assessment so it can
     * be outputted to the engine later.
     * Firstly stores all the checks the record failed and then stores whether
     * the record should be authorized or rejected.
     * @param checks A list of all checks
     * @param state Current state of the bot
     */
    public void assessRecord(ArrayList<AbstractCheck> checks, BotState state) {
        this.failedChecks = this.doChecks(checks, state);
        this.isAuthorized = this.authorizeRecord(checks, state);
    }

    /**
     * This method loops the current record (via state) through
     * all the checks and stores the ID of the check if it does not pass the check.
     * @param checks A list of all checks
     * @param state Current state of the bot
     * @return A list of the IDs of all failed checks
     */
    private ArrayList<Integer> doChecks(ArrayList<AbstractCheck> checks, BotState state) {
        return checks.stream()
                .filter(check -> !check.approveRecord(state))
                .map(AbstractCheck::getId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * This method should return true if the record (state.getCurrentRecord())
     * is authorized, and false if the record is rejected.
     * This example rejects a record if it fails all checks.
     * @param checks A list of all checks
     * @param state Current state of the bot
     * @return True if record is authorized, false otherwise
     */
    private boolean authorizeRecord(ArrayList<AbstractCheck> checks, BotState state) {

        // TODO: Change this to do something more intelligent
        return this.failedChecks.size() < checks.size();
    }

    /**
     * Transforms the assessment to the correct string format
     * @return Assessment as a string
     */
    @Override
    public String toString() {
        if (this.isAuthorized == null) {
            LOGGER.severe("No assessment done yet. Output set to 'pass'");
            return "pass";
        }

        String authorized = this.isAuthorized ? "authorized" : "rejected";
        String failedChecksString = this.failedChecks.stream()
                .map(check -> check + "")
                .collect(Collectors.joining(","));

        return String.format("%s %s", authorized, failedChecksString);
    }
}
