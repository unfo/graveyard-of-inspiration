def erastothenes(n)
  a = Array.new
  a[1] = 0
  2.upto(n) {|i| a[i] = 1 }
  p = 2
  while p**2 < n
    j = p**2
    while j < n
      a[j] = 0
      j = j+p
    end
    while a[p] != 1
      p += 1
    end
  end
  return a
end

puts erastothenes(30)

require 'log4r'
include Log4r

@log = Logger.new 'log'
so = StdoutOutputter.new 'console'
@log.add(so)

def main
  i = 1
  start = 2
