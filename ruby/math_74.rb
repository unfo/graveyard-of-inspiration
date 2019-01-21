#Problem 74
#
#The number 145 is well known for the property that 
#the sum of the factorial of its digits is equal to 145:
#
#1! + 4! + 5! = 1 + 24 + 120 = 145
#
#Perhaps less well known is 169, in that it produces 
#the longest chain of numbers that link back to 169; 
#it turns out that there are only three such loops that exist:
#
#169 ? 363601 ? 1454 ? 169
#871 ? 45361 ? 871
#872 ? 45362 ? 872
#
#It is not difficult to prove that EVERY starting 
#number will eventually get stuck in a loop. For example,
#
#69 ? 363600 ? 1454 ? 169 ? 363601 (? 1454)
#78 ? 45360 ? 871 ? 45361 (? 871)
#540 ? 145 (? 145)
#
#Starting with 69 produces a chain of five non-repeating terms, 
#but the longest non-repeating chain with a starting number 
#below one million is sixty terms.
#
#How many chains, with a starting number below one million, 
#contain exactly sixty non-repeating terms?

def factorial(number)
  if number < 2 
    return 1
  else
    result = 1
    number.downto(2) { |num| result *= num }
    return result
  end
end

def next_link(number)
  result = 0
  number.to_s.split(//).each do |i|
    result += factorial(i.to_i)
  end
  return result
end


chain_count = 0

1.upto(999999) do |number|
  print "%d," % number
  steps = Array.new
  steps << number
  catch (:duplicate) do
    loop do
      nextone = next_link(steps.last)
      if steps.include?(nextone)
        throw (:duplicate)
      else
        steps << nextone
      end
    end
  end
  if steps.size == 60
    chain_count += 1
    puts "\n%d -> %d steps | Total of %d chains" % [number, steps.size, chain_count]
  end
end

puts chain_count
