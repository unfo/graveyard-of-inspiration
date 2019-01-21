require 'RMagick'

orig = Magick::ImageList.new("original.png")
puts "width x height = %d x %d" % [orig.columns, orig.rows]
piece = Hash.new
w = orig.columns / 3
h = orig.rows / 3

puts "piece width x height = %d x %d" % [w,h]

pieces = Array.new
(0..2).each do |row|
	(0..2).each do |col|
		pieces << orig.crop(col*w, 
												row*h, 
												w, 
												h
												)
	end
end

pieces.each_with_index do |img,i|
	img.write('puzzle%d.png' % i)
end
