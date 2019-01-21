require 'poker'
require 'test/unit'

class TestPoker < Test::Unit::TestCase
  def test_card
    assert_equal("2H", Poker::Card.new("2H").to_s)
    assert_raise("perkele") { Poker::Card.new("0J") }
  end
end
