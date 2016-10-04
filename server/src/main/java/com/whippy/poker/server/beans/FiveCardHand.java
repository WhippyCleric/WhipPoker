//J-
package com.whippy.poker.server.beans;

import java.util.List;

import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.HandValue;

public class FiveCardHand {

        private List<Card> cards;
        private HandValue handValue;

        public FiveCardHand(List<Card> cards, HandValue handValue) {
                this.cards = cards;
                this.handValue = handValue;
        }

        public List<Card> getCards() {
                return cards;
        }

        public HandValue getHandValue() {
                return handValue;
        }


}
//J+
