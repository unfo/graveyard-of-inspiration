a = IO.readlines('output.txt')
lineno = 0
a.each do |line|
  #printf "\n| %2d | ", (lineno = lineno.next)
  puts
	chars = line.chomp.split('')
	bw = (chars[0] == ':') ? 1 : 0
	count = 0
	last_char = chars[0]
	chars.each do |chr|
		if chr == last_char
			count = count.next
		else
			printf("%d%02d ", bw, count)
			count = 1
			bw = (chr == ':') ? 1 : 0
		end
		last_char = chr
	end
end
puts
