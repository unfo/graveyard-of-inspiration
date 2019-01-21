local brain = {}
local NOWORD = "\0"

--[[
x: (where x is not one of the magic characters ^$()%.[]*+-?) represents the character x itself.
.: (a dot) represents all characters.
%a: represents all letters.
%c: represents all control characters.
%d: represents all digits.
%g: represents all printable characters except space.
%l: represents all lowercase letters.
%p: represents all punctuation characters.
%s: represents all space characters.
%u: represents all uppercase letters.
%w: represents all alphanumeric characters.
%x: represents all hexadecimal digits.
%x: (where x is any non-alphanumeric character) represents the character x. This is the standard way to escape the magic characters. Any punctuation character (even the non magic) can be preceded by a '%' when used to represent itself in a pattern.
[set]: represents the class which is the union of all characters in set. A range of characters can be specified by separating the end characters of the range, in ascending order, with a '-', All classes %x described above can also be used as components in set. All other characters in set represent themselves. For example, [%w_] (or [_%w]) represents all alphanumeric characters plus the underscore, [0-7] represents the octal digits, and [0-7%l%-] represents the octal digits plus the lowercase letters plus the '-' character.
The interaction between ranges and classes is not defined. Therefore, patterns like [%a-z] or [a-%%] have no meaning.

[^set]: represents the complement of set, where set is interpreted as above.

]]--

---[[

function learn(dict, line)
	local prev1, prev2 = NOWORD, NOWORD
	for _word in string.gmatch(line, "[^ ]+") do
		dict[prev1 .. prev2] = dict[prev1 .. prev2] or {}
		table.insert(dict[prev1 .. prev2], _word)
		prev1, prev2 = prev2, _word
	end
	return dict
end
function populate_stuff(dict, filename)
	io.input(filename)
	local lines = 0
	for line in io.lines() do
		lines = lines + 1
		dict = learn(dict, line)
	end

	return dict,lines
end

local files = { "~/training.txt" }
for _,filename in pairs(files) do 
	brain, count = populate_stuff(brain, filename)
	io.write(filename, " ", count, " lines processed\n")
end
--]]
io.input(io.stdin)



local function random_word(dict, w1, w2)
	if dict[w1 .. w2] == nil or next(dict[w1 .. w2]) == nil or #dict[w1 .. w2] == 0
		then return nil
	else
		words = dict[w1 .. w2]
		return words[math.random(#words)]
	end
end

while true do
	local line = io.read("*line")
	local prev1, prev2 = NOWORD, NOWORD
	local hitword = nil
	local talk
	for word in string.gmatch(line, "[^ ]+") do
		if prev1 ~= NOWORD then
			local rndword = random_word(brain, prev1, word)
			if rndword ~= nil then
				talk = {prev1,word}
				prev1, prev2 = word, rndword
				hitword = true
				break
			end
		end
		prev1, prev2 = prev2, word
	end


	if hitword then
		
		for w=1,20 do
			local new_word = random_word(brain, prev1, prev2)
			if new_word == nil then
				--io.write("-x-")
				break
			else
				--io.write(new_word, " ")
				table.insert(talk, new_word)
				prev1, prev2 = prev2, new_word
			end
		end
		--io.write("\n")
		print(table.concat(talk, " "))
	else
		--print("No hits")
	end

	brain = learn(brain, line)
end