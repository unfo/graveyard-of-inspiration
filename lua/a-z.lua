local a = string.byte("a")
local z = string.byte("z")

local brain = {}
local NOWORD = "\0"


---[[
function populate_stuff(dict, filename)
	io.input(filename)
	local lines = 0
	for line in io.lines() do
		lines = lines + 1
		local prev1, prev2 = NOWORD, NOWORD
		for _word in string.gmatch(line, "%w") do
			dict[prev1 .. prev2] = dict[prev1 .. prev2] or {}
			dict[prev1 .. prev2][_word] = dict[prev1 .. prev2][c] or 0
			dict[prev1 .. prev2][_word] = dict[prev1 .. prev2][c] + 1
			prev1, prev2 = prev2, _word
		end
	end

	return dict,words
end

local files = { "ex-con.txt" }
for _,filename in pairs(files) do 
	brain, count = populate_stuff(brain, filename)
	io.write(filename, " ", count, " lines processed\n")
end
--]]
io.input(io.stdin)

print("Give me a line:")
local function bigger(a,b)
	return a > b
end
local line = io.read("*line")
while (line ~= "quit") do
	local prev1, prev2 = NOWORD, NOWORD
	local hitword = ""
	local talk = ""
	for word in string.gmatch(line, "%w") do
		
		local hits = brain[prev1 .. prev2][word] or 0
		if hits > 5 then
			hitword = word
			break
		end
		prev1, prev2 = prev2, word
	end


	if hitword ~= ""
		talk = {prev1,prev2,hitword}
		prev1, prev2 = prev2, hitword
		for w=1,3 do
			if next(brain[prev1 .. prev2]) == nil then
				break
			end
			print("Before sort: " .. brain[prev1 .. prev2][1])
			table.sort(brain[prev1 .. prev2], bigger)
			print("After sort: " .. brain[prev1 .. prev2][1])
			table.insert(talk, brain[prev1 .. prev2][1])
		end
		print("Output >> " .. table.concat(talk, " "))
	else
		print("No hits")
	end

	print("Give me a line or [quit]:")
	line = io.read("*line")
end