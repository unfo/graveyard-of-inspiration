a = IO.readlines('code.txt')
a.each do |line|
	numbers = line.strip.split(' ')
	numbers.each do |n|
		n[1..2].to_i.times { printf("%s", (n[0].chr == '1') ? '#' : ' ') }
	end
  puts
end
