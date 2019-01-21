
#!/usr/bin/ruby

require 'rubygems'
require 'gruff'

g = Gruff::Line.new
g.title = "Blimey! Dungeons!" 


dungeons = IO.readlines('dungeon.log')

dungeon_values = []
dungeon_dates = []
dungeons.each do |line|
	if line =~ /^I, \[(\d\d\d\d-\d\d-\d\d).*?INFO -- : (-?\d+);(-?\d+);(-?\d+).*$/
		dungeon_values << $2.to_i
		dungeon_dates << $1
	end
end

g.data("Dungeons", dungeon_values)

dungeon_labels = {}
dungeon_dates.each_with_index {|date,index| dungeon_labels[index] = date if index % 50 == 0}
g.labels = dungeon_labels

g.write('dungeons.png')
