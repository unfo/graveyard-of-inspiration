1.upto(1000) {|a|
  (a+1).upto(1000-a) {|b|
    (b+1).upto(1000-b) {|c|
      if a+b+c == 1000
        if (a*a) + (b*b) == c*c
          puts a*b*c
        end
      end
    }
  }
}
