require 'primes.rb'

sum = 0
known_primes = [2,3,5]

3.upto(1000000) do |number|
  next if number % 2 == 0
  divisable = false
  next if known_primes.include? number
  known_primes.each do |prime|
    next if (divisable = (number % prime == 0))
  end
  known_primes << number if not divisable
end

known_primes.each {|prime| sum += prime }
puts sum
