#Problem 53

#There are exactly ten ways of selecting three from five, 12345:
#
#123, 124, 125, 134, 135, 145, 234, 235, 245, and 345
#
#In combinatorics, we use the notation, 5C3 = 10.
#
#In general,
#nCr = 	n! / ( r!(n?r)! )
# where r <= n, n! = n×(n?1)×...×3×2×1, and 0! = 1.
#
#It is not until n = 23, that a value exceeds one-million: 23C10 = 1144066.
#
#How many values of  nCr, for 1 £ n £ 100, are greater than one-million?

def factorial(number)
  if number < 2 
    return 1
  else
    result = 1
    number.downto(2) { |num| result *= num }
    return result
  end
end

def ncr(n,r)
	factorial(n) / (factorial(r) * factorial(n-r))
end

count = 0

1.upto(100) do |n|
	1.upto(n) do |r|
		count = count.next if ncr(n,r) > 1_000_000
	end
end
puts count
