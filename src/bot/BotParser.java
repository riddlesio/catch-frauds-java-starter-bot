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
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

import bot.action.Assessment;

/**
 * bot.BotParser
 *
 * This class will keep reading output from the game engine.
 * Will either update the bot state or, when an action is requested, will return
 * the result the bot has calculated.
 *
 * @author Jim van Eeden - jim@riddles.io
 */

public class BotParser {

    private final static Logger LOGGER = Logger.getLogger(RiskSystemState.class.getName());

    private final Scanner scan;
    private final BotStarter bot;

    private RiskSystemState currentState;

    public BotParser(BotStarter bot) {
        this.scan = new Scanner(System.in);
        this.bot = bot;
        this.currentState = new RiskSystemState();
    }

    /**
     * Run the parser
     */
    public void run() {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            this.parseLine(line);
        }
    }

    /**
     * Parse line gotten from the game engine
     * @param line Current line
     */
    private void parseLine(String line) {
        if (line.length() <= 0) return;

        String[] parts = line.split(" ");
        switch (parts[0]) {
            case "settings": // game settings
                this.currentState.parseSettings(parts[1], parts[2]);
                break;
            case "update": // update about the game
                if (parts[1].equals(this.currentState.getMyName()) &&
                        parts[2].equals("next_record")) {

                    // record data might be separated by spaces, so combine again
                    ArrayList<String> input = new ArrayList<>(Arrays.asList(parts));
                    String record = String.join(" ", input.subList(3, input.size()));

                    this.currentState.parseRecord(record);
                } else {
                    LOGGER.severe("Unknown update input");
                }
                break;
            case "action": // action requested
                switch (parts[1]) {
                    case "checkpoints":
                        System.out.println(this.bot.checkPointsToString());
                        break;
                    case "record":
                        Assessment assessment = this.bot.getAssessment(this.currentState,
                                Integer.parseInt(parts[2]));
                        System.out.println(assessment.toString());
                        break;
                    default:
                        LOGGER.severe("Unknown action input");
                        break;
                }
                break;
            default:
                LOGGER.severe("Unknown input");
        }
    }
}