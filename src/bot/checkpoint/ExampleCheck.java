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

package bot.checkpoint;

import bot.BotState;
import bot.data.Record;

/**
 * bot.checkpoint.ExampleCheck
 *
 * Example check
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class ExampleCheck extends AbstractCheck {

    public ExampleCheck(int id) {
        super(id);
    }

    @Override
    public String getDescription() {
        return "Rejects French visa cards";
    }

    @Override
    public boolean approveRecord(BotState state) {
        Record record = state.getCurrentRecord();

        boolean isFrench = record.getData("issuercountrycode").equals("FR");
        boolean isVisa = record.getData("displayabletxvariantcode").equals("visa");

        return !isFrench || !isVisa;
    }
}
