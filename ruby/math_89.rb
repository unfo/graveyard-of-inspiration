=begin
Problem 89

The rules for writing Roman numerals allow for many ways of writing each number
(see FAQ: Roman Numerals). However, there is always a "best" way of writing a
particular number.

For example, the following represent all of the legitimate ways of writing the
number sixteen:

IIIIIIIIIIIIIIII VIIIIIIIIIII VVIIIIII XIIIIII VVVI XVI

The last example being considered the most efficient, as it uses the least
number of numerals.

The 11K text file, roman.txt, contains one
thousand numbers written in valid, but not necessarily minimal, Roman numerals;
that is, they are arranged in descending units and obey the subtractive pair
rule (see FAQ).

Find the number of characters saved by writing each of these in their minimal
form.

Note: You can assume that all the Roman numerals in the file contain no more
than four consecutive identical units.

From FAQ:
I = 1
V = 5
X = 10
L = 50
C = 100
D = 500
M = 1000

   1. Only I, X, and C can be used as the leading numeral in part of a subtractive pair.
   2. I can only be placed before V and X.
   3. X can only be placed before L and C.
   4. C can only be placed before D and M.

=end

I = 1
V = 5
X = 10
L = 50
C = 100
D = 500
M = 1000

romans = IO.readlines('roman.txt')
numbers = Array.new
romans.each do |roman|
	value = 0
	roman.strip.split(//).each do |digit|
		value += eval(digit)
	end
	numbers << value
end
puts numbers

