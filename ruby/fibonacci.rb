class Fibonacci
  @@sequence = [1,1] # the start of the Fibonacci sequence

  def upto(number)
    raise Exception.new('Invalid argument') if (number.nil? or number < 1)
    return 1 if number < 2
    @last = @@sequence.last
    @second_to_last = @sequence[-2]
    while (@current = @last+@second_to_last) < number
      @@sequence << @current
      @last, @second_to_last = @@sequence.last, @@sequence[-2]
    end
    return @current
  end

  def initialize(last, second_to_last)
    @last = last
    @second_to_last = second_to_last
    @current = @last + second_to_last
  end

  def next!
    @current, @last, @second_to_last = (@current + @last), @current, @last
    return @current
  end

  def is_dually_pandigital?
    return false if @current.to_s.length < 18
    as_ary = @current.to_s.split(//)
    first_nine = as_ary[0..8]
    last_nine = as_ary[-9..-1]
    return (first_nine.sort.to_s == '123456789' and last_nine.sort.to_s == '123456789')
  end

  def to_s
    @current.to_s
  end
end
