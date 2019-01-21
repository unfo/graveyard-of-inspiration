#Problem 66
#
#Consider quadratic Diophantine equations of the form:
#
#x**2 ? Dy**2 = 1
#
#For example, when D=13, the minimal solution in x is 6492 ? 13�1802 = 1.
#
#It can be assumed that there are no solutions in natural numbers when D is square.
#
#By finding minimal solutions in x for D = {2, 3, 5, 6, 7}, we obtain the following:
#
#32 ? 2�22 = 1
#22 ? 3�12 = 1
#92 ? 5�42 = 1
#52 ? 6�22 = 1
#82 ? 7�32 = 1
#
#Hence, by considering minimal solution in x for D ? 7, the largest x is obtained when D=5.
#
#Find the value of D ? 1000 in minimal solutions of x for which the largest value of x is obtained.
def dio (x,d,y)
	puts "x = %d | d = %d | y = %d | %d - ( %d * %d ) = %d" % [x,d,y,x**2,d,y**2,(x**2 - (d * y**2))]
	if (x**2 - (d * y**2) == 1)
		puts "x = %d | d = %d | y = %d | %d - ( %d * %d ) = %d" % [x,d,y,x**2,d,y**2,(x**2 - (d * y**2))]
		return true
	else
		return false
	end
end
largest = 0

(1..1000).each do |d|
	x = 2
	if Math.sqrt(d) % 2 != 0 #It can be assumed that there are no solutions in natural numbers when D is square.
		catch(:match_found) do
			catch(:next_x) do
				loop do
					y = 2
					loop do
            puts(x**2 < (d * y**2))
						throw(:next_x) if (x**2 < (d * y**2))
						throw(:match_found) if dio(x,d,y)
						y = y.next
					end #loop
				end # loop
			end # catch next_x
			x = x.next
		end # catch match
		largest = [largest, x].max
	end # if sqrt
	puts d
end
puts largest
