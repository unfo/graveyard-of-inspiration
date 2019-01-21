=begin
In the card game poker, a hand consists of five cards and are ranked, from lowest to highest, in the following way:

    * High Card: Highest value card in a hand.
    * One Pair: Two cards of the same value.
    * Two Pairs: Two different pairs.
    * Three of a Kind: Three cards of the same value.
    * Straight: All cards are consecutive values.
    * Flush: All cards of the same suit.
    * Full House: Three of a kind and a pair.
    * Four of a Kind: Four cards of the same value.
    * Straight Flush: All cards are consecutive values of same suit.
    * Royal Flush: Ten, Jack, Queen, King, Ace, in same suit

The cards are valued in the order:
2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace.

Consider the following four hands dealt to two players:
Hand	 	Player 1	 	Player 2	 	Winner
1	 	5H 5C 6S 7S KD
Pair of Fives
	 	2C 3S 8S 8D TD
Pair of Eights
	 	Player 2
2	 	5D 8C 9S JS AC
Highest card Ace
	 	2C 5C 7D 8S QH
Highest card Queen
	 	Player 1
3	 	2D 9C AS AH AC
Three Aces
	 	3D 6D 7D TD QD
Flush with Diamonds
	 	Player 2
4	 	4D 6S 9H QH QC
Pair of Queens
Highest card Nine
	 	3D 6D 7H QD QS
Pair of Queens
Highest card Seven
	 	Player 1

The file, poker.txt, contains one-thousand random hands dealt to two players. Each line of the file contains ten cards (separated by a single space): the first five are player one's cards and the last five are player two's cards. You can assume that all hands are valid (no invalid characters or repeated cards), each player's hand is in no specific order, and in each hand there is a clear winner.

How many hands does player one win?



=end

module Poker

	class Card
		attr_reader :face_value, :suit

		J = 10; Q = 11; K = 13; A = 14
		FACES = [2, 3, 4, 5, 6, 7, 8, 9, J, Q, K, A]
		SUITS = ["H", "C", "S", "D"]
		
		def initialize(value)
			@face, @suit = value.split(//)
			raise "perkele" unless (FACES.include?@face and SUITS.include?@suit)
		end
	
		def to_s
			sprintf("%s%s", @face, @suit)
		end
	end

	class Hand
		def initialize
			@hand = Array.new(5)
		end
		def initialize(*cards)
			@hand = *cards
		end
		def to_s
			sprintf("%s", @hand.join(' '))
		end
	end
end
