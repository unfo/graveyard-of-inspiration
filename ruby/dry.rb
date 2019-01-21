def smallestDifference (filename, regexp, label_column, column_one, column_two)
	spread = spreadLabel = nil
	unless filename.nil?
		File::open(filename).each do |line|
			if line =~ regexp
				team = (eval "$" + label_column.to_s)
				max = (eval "$" + column_one.to_s).to_i
				min = (eval "$" + column_two.to_s).to_i
			
				if spread.nil? || (max-min).abs < spread
					spread = (max-min).abs
					spreadLabel = team
				end
			end
		end
		return spreadLabel + ": " + spread.to_s
	else
		return "Dimwit"
	end
end
