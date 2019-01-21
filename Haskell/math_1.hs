{- Project Euler Problem #1
 - Add all the natural numbers below 1000 that are multiples of 3 or 5.
 -}
mynums = [ x | x <- [1..999], x `mod` 3 == 0, x `mod` 5 == 0]

main        :: IO ()
main        = print (sum mynums)
