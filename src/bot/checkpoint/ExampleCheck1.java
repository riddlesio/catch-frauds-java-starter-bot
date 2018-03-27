/*
 * Copyright 2018 riddles.io (developers@riddles.io)
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

import bot.RiskSystemState;
import bot.data.PaymentRecord;

import java.util.logging.Logger;

/**
 * bot.checkpoint.ExampleCheck1
 *
 * Example check 1
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class ExampleCheck1 extends AbstractCheck {

    private static final Logger log = Logger.getLogger(ExampleCheck1.class.getSimpleName());

    private int counter = 0;

    public ExampleCheck1(int id) {
        super(id);
    }

    @Override
    public String getDescription() {
        return "Rejects odd transactions";
    }

    @Override
    public boolean rejectRecord(RiskSystemState state) {
        PaymentRecord record = state.getCurrentRecord();

        String txId = record.getData("txid");
        this.counter++;

        if (this.counter % 2 == 1) {
            log.info(String.format(
                    "Refuse every other transaction. txId: %s, counter: %d", txId, this.counter
            ));
            return true;
        }

        return false;
    }
}