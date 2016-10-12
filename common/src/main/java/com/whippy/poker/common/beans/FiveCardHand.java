//J-
package com.whippy.poker.common.beans;

import java.util.List;

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
