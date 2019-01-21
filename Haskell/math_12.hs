factors n = [ i | i <- [1..n]; (mod n i) == 0 ]

triangle :: Integer -> Integer
triangle n | n < 0 = error "input to triangle is negative"
           | n > 0 = n


main        :: IO ()
main        = print (findNum)
